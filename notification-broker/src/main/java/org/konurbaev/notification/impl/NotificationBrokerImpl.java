package org.konurbaev.notification.impl;

import org.konurbaev.notification.NotificationBroker;
import org.konurbaev.notification.NotificationSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationBrokerImpl implements NotificationBroker {

    private final static Logger logger = LoggerFactory.getLogger(NotificationBrokerImpl.class);

    public NotificationBrokerImpl(int port) {
        logger.debug("NotificationBrokerImpl(" + port + ")");
    }

    public void sendEvent(Object event) {
        logger.debug("sendEvent(" + event + ")");
    }

    public int subscribe(String criteria, NotificationSubscriber subscriber) {
        logger.debug("subscribe(" + criteria + ")");
        return 0;
    }

    public void unsubscribe(int susbcriberId) {
        logger.debug("unsubscribe(" + susbcriberId + ")");
    }

    public void shutdown() {
        logger.debug("shutdown()");
    }

}