package org.konurbaev.notification.configuration;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationConfiguringFactoryClient implements BundleActivator {

    private final static Logger logger = LoggerFactory.getLogger(NotificationConfiguringFactoryClient.class);

    public void start(BundleContext bundleContext) throws Exception {

        ServiceReference serviceReference = bundleContext.getServiceReference(ConfigurationAdmin.class.getName());

        ConfigurationAdmin configAdmin = (ConfigurationAdmin) bundleContext.getService(serviceReference);

        Configuration configuration = configAdmin.createFactoryConfiguration("org.konurbaev.notification.broker", null);

        Dictionary<String, Object> configProperties = new Hashtable<>();

        configProperties.put("port", 8081);

        configuration.update(configProperties);

        logger.debug("Create 1st config");

        configuration = configAdmin.createFactoryConfiguration("org.konurbaev.notification.broker", null);

        configProperties = new Hashtable<>();

        configProperties.put("port", 8082);

        configuration.update(configProperties);

        logger.debug("Create 2nd config");

        configProperties.remove("port");

        configuration.update(configProperties);

        logger.debug("Update 2nd config");

        logger.debug("Factory PID = " + configuration.getFactoryPid() + ", PID = " + configuration.getPid());
    }

    public void stop(BundleContext arg0) throws Exception {
    }
}
