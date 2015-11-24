package org.konurbaev.notification.impl;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;

import org.konurbaev.notification.NotificationBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationBrokerFactoryActivator implements BundleActivator, ManagedServiceFactory {

    private final static Logger logger = LoggerFactory.getLogger(NotificationBrokerFactoryActivator.class);
    private ServiceRegistration registration;
    private BundleContext context;
    private final Map<String, NotificationBrokerImpl> brokers = new HashMap<>();
    private final Map<String, ServiceRegistration> brokerRegistrations = new HashMap<>();

    public void start(BundleContext bundleContext) throws Exception {
        this.context = bundleContext;

        logger.debug("starting notification broker");

        Dictionary<String, Object> properties = new Hashtable<>();

        properties.put(Constants.SERVICE_PID, "org.konurbaev.notification.broker");

        registration = bundleContext.registerService(
                ManagedServiceFactory.class.getName(),
                this,
                properties
        );
    }

    @SuppressWarnings("rawtypes")
    public synchronized void updated(String pid, Dictionary configuration)
            throws ConfigurationException {

        logger.debug("PID " + pid + " is being updated");

        Integer port = (Integer) configuration.get("port");

        if (port == null) {
            throw new ConfigurationException("port", "Port configuration property is missing");
        }

        if (brokers.get(pid) == null) {
            startBrokerService(pid, port);
        } else {
            throw new ConfigurationException("port", "Update to existing service not allowed");
        }
    }

    public synchronized void deleted(String pid) {
        stopBrokerService(pid);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void startBrokerService(String pid, int port) {
        NotificationBrokerImpl broker = new NotificationBrokerImpl(port);

        Dictionary serviceProperties = new Hashtable();
        serviceProperties.put("port", port);

        ServiceRegistration registration = context.registerService(
                NotificationBroker.class.getName(),
                broker,
                serviceProperties
        );

        brokerRegistrations.put(pid, registration);
        brokers.put(pid, broker);
    }

    private void stopBrokerService(String pid) {
        NotificationBrokerImpl broker = brokers.remove(pid);

        if (broker != null) {
            broker.shutdown();
            brokerRegistrations.remove(pid).unregister();
        }
    }

    public String getName() {
        return "Notification Broker Managed Service Factory";
    }

    public void stop(BundleContext arg0) throws Exception {
        if (registration != null) {
            registration.unregister();
        }
    }
}