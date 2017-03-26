package me.stupidme.cooker.notification;

import android.app.Notification;
import android.content.Context;

/**
 * Created by StupidL on 2017/3/26.
 */

public class SimpleNotification implements ICookerNotification {

    private Notification.Builder mNotificationBuilder;

    private int id;

    public SimpleNotification(int id, Context context) {
        this.id = id;
        mNotificationBuilder = new Notification.Builder(context);
    }

    public int getId() {
        return id;
    }

    public SimpleNotification setSmallIcon(int smallIconId) {
        mNotificationBuilder.setSmallIcon(smallIconId);
        return this;
    }

    public SimpleNotification setContentTitle(CharSequence title) {
        mNotificationBuilder.setContentTitle(title);
        return this;
    }

    public SimpleNotification setContentText(CharSequence text) {
        mNotificationBuilder.setContentText(text);
        return this;
    }

    @Override
    public Notification build() {
        return mNotificationBuilder.build();
    }
}
