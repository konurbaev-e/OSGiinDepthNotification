package org.konurbaev.notification.configuration;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;

public class NotificationConfiguringClient implements BundleActivator {

    public void start(BundleContext bundleContext) throws Exception {

        ServiceReference serviceReference = bundleContext.getServiceReference(ConfigurationAdmin.class.getName());

        ConfigurationAdmin configAdmin = (ConfigurationAdmin) bundleContext.getService(serviceReference);

        /**
         * Managed Service
         */
        Configuration configuration = configAdmin.getConfiguration("org.konurbaev.notification.broker", null);

        Dictionary<String, Object> configProperties = new Hashtable<>();

        configProperties.put("port", 8081);

        ServiceReference [] serviceReferences =
                bundleContext.getServiceReferences(MetaTypeProvider.class.getName(),"(" + Constants.SERVICE_PID + "=" + "org.konurbaev.notification.broker" + ")");

        MetaTypeProvider metaTypeProvider = (MetaTypeProvider) bundleContext.getService(serviceReferences[0]);

        ObjectClassDefinition ocd = metaTypeProvider.getObjectClassDefinition("org.konurbaev.notification.broker", null);

        AttributeDefinition [] attrDefs = ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);

        Arrays.stream(attrDefs).filter(attrDef -> (configProperties.get(attrDef.getName()) != null)) //check attr definition name
                .filter(attrDef -> attrDef.validate(configProperties.get(attrDef.getName()).toString()).equals("")) //check validation message against attr definition
                .findFirst() //find first empty validation message
                .ifPresent(attrDef -> new IllegalArgumentException(attrDef.validate(configProperties.get(attrDef.getName()).toString()))); //if present then exception

        configuration.update(configProperties);

    }

    public void stop(BundleContext arg0) throws Exception {
    }

}