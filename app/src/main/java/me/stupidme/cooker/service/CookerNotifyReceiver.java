package me.stupidme.cooker.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import me.stupidme.cooker.R;
import me.stupidme.cooker.view.status.StatusActivity;

public class CookerNotifyReceiver extends BroadcastReceiver {

    private Notification mNotification;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("Receiver", "===========onReceive========");

        if (!NotificationService.ACTION_NOTIFY.equals(intent.getAction()))
            return;
        if (mNotification == null) {
            Intent statusIntent = new Intent(context, StatusActivity.class);
            statusIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,
                    0,
                    statusIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            mNotification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.cooker)
                    .setContentTitle("Cooker Tips")
                    .setContentText("You have a book running...")
                    .setContentIntent(pendingIntent)
                    .build();
            mNotification.defaults |= Notification.DEFAULT_SOUND;
            mNotification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0xaa, mNotification);
    }
}
