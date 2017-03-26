package me.stupidme.cooker.notification;

import android.app.Notification;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * Created by StupidL on 2017/3/26.
 */

public class NotificationFactory implements INotificationFactory {

    private WeakReference<Context> mContextRef;

    public NotificationFactory(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    @Override
    public Notification create(int id, NotificationType type) {
        ICookerNotification notification = null;
        switch (type) {
            case SIMPLE:
                if (mContextRef.get() != null) {
                    notification = new SimpleNotification(id, mContextRef.get());
                    //TODO setup notification
                }
                break;

            case BIG_PICTURE:
                if (mContextRef.get() != null) {
                    notification = new BigPictureNotification(id, mContextRef.get());
                    //TODO setup notification
                }
                break;

            default:
                break;
        }

        return notification != null ? notification.build() : null;
    }
}
