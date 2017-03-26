package me.stupidme.cooker.notification;

import android.app.Notification;

/**
 * Created by StupidL on 2017/3/26.
 */

public interface INotificationFactory {

    Notification create(int id, NotificationType type);

    enum NotificationType{
        SIMPLE,
        BIG_PICTURE
    }
}
