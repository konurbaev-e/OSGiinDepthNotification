package org.konurbaev.notification;

public interface NotificationBroker {

    void sendEvent(Object event);

    int subscribe(String criteria, NotificationSubscriber subscriber);

    void unsubscribe(int susbcriberId);

}