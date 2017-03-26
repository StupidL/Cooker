package me.stupidme.cooker.notification;

import android.app.Notification;
import android.content.Context;

/**
 * Created by StupidL on 2017/3/26.
 */

public class BigPictureNotification implements ICookerNotification {

    private Notification.Builder mNotificationBuilder;

    private int id;

    public BigPictureNotification(int id, Context context) {
        this.id = id;
        mNotificationBuilder = new Notification.Builder(context);
    }

    public int getId() {
        return id;
    }

    public BigPictureNotification setContentTitle(CharSequence title){
        mNotificationBuilder.setContentTitle(title);
        return this;
    }

    public BigPictureNotification setContentText(CharSequence text){
        mNotificationBuilder.setContentText(text);
        return this;
    }

    public BigPictureNotification setBigPicture(int resId){

        return this;
    }

    public BigPictureNotification setRemoteView(int resId){

        return this;
    }

    @Override
    public Notification build() {
        return mNotificationBuilder.build();
    }
}
