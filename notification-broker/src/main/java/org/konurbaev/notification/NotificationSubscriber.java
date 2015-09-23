package org.konurbaev.notification;

public interface NotificationSubscriber {

    void onEvent(Object event);

}
