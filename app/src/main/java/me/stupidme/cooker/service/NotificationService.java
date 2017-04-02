package me.stupidme.cooker.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import me.stupidme.cooker.R;
import me.stupidme.cooker.view.StatusActivity;

public class NotificationService extends Service {

    private static final String TAG = "NotificationService";

    private static final String STOP_SERVICE_ACTION = "me.stupidme.cooker.ACTION_STOP_SERVICE";
    Notification.Builder mBuilder;

    NotificationManager mManager;

    private static final int mNotificationId = 0xaa;

    private PendingIntent mStatusIntent;

    private PendingIntent mStopIntent;

    public NotificationService() {

    }

    @Override
    public void onCreate() {

        mBuilder = new Notification.Builder(this);

        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Intent statusIntent = new Intent(this, StatusActivity.class);
        statusIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        mStatusIntent = PendingIntent.getActivity(this,
                0,
                statusIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopIntent = new Intent(this, NotificationService.class);
        stopIntent.setAction(STOP_SERVICE_ACTION);

        mStopIntent = PendingIntent.getService(this, 1, stopIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        Log.v(TAG, "onCreate()...");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendSimpleNotification(
                R.drawable.cooker,
                "Cooker Notification",
                "This is a message from server !"
        );

//        sendCustomBigPictureNotification(
//                "Cooker",
//                "No Activity..."
//        );

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return true;
    }

    @Override
    public void onDestroy() {

    }

    private void sendSimpleNotification(int smallIconId, CharSequence title, CharSequence text) {
        mBuilder.setSmallIcon(smallIconId)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(mStatusIntent);
        Notification notification = mBuilder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        mManager.notify(mNotificationId, notification);
    }

    private void sendCustomBigPictureNotification(CharSequence title, CharSequence text) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        remoteViews.setTextViewText(R.id.remote_title, title);
        remoteViews.setTextViewText(R.id.remote_describe, text);
        remoteViews.setOnClickPendingIntent(R.id.remote_detail, mStatusIntent);
        remoteViews.setOnClickPendingIntent(R.id.remote_remove, mStopIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cooker)
                .setContentTitle("Hello")
                .setContentText("World")
                .setContent(remoteViews);

        Notification notification = builder.build();

        notification.bigContentView = remoteViews;

        mManager.notify(mNotificationId, notification);

        Log.v("NotificationService", "sendCustomNotification");
    }
}
