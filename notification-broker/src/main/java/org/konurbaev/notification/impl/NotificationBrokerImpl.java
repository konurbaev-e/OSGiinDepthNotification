package org.konurbaev.notification.impl;

import org.konurbaev.notification.NotificationBroker;
import org.konurbaev.notification.NotificationSubscriber;

public class NotificationBrokerImpl implements NotificationBroker {

    public NotificationBrokerImpl(int port) {
        System.out.println("NotificationBrokerImpl(" + port + ")");
    }

    public void sendEvent(Object event) {
        System.out.println("sendEvent(" + event + ")");
    }

    public int subscribe(String criteria, NotificationSubscriber subscriber) {
        System.out.println("subscribe(" + criteria + ")");
        return 0;
    }

    public void unsubscribe(int susbcriberId) {
        System.out.println("unsubscribe(" + susbcriberId + ")");
    }

    public void shutdown() {
        System.out.println("shutdown()");
    }

}