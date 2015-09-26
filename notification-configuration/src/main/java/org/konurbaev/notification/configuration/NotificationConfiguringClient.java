package org.konurbaev.notification.configuration;

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

        Dictionary<String, Object> configProperties = new Hashtable<String, Object>();

        configProperties.put("port", 8081);

        ServiceReference [] serviceReferences =
                bundleContext.getServiceReferences(MetaTypeProvider.class.getName(),"(" + Constants.SERVICE_PID + "=" + "org.konurbaev.notification.broker" + ")");

        MetaTypeProvider metaTypeProvider = (MetaTypeProvider) bundleContext.getService(serviceReferences[0]);

        ObjectClassDefinition ocd = metaTypeProvider.getObjectClassDefinition("org.konurbaev.notification.broker", null);

        AttributeDefinition [] attrDefs = ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);

        for (AttributeDefinition attrDef : attrDefs) {
            Object configPropertyValue = configProperties.get(attrDef.getName());

            if (configPropertyValue != null) {
                String validationMessage = attrDef.validate(configPropertyValue.toString());

                if (!validationMessage.equals("")) {
                    throw new IllegalArgumentException(validationMessage);
                }
            }
        }

        configuration.update(configProperties);

    }

    public void stop(BundleContext arg0) throws Exception {
    }

}