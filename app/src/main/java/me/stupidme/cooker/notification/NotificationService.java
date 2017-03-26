package me.stupidme.cooker.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import me.stupidme.cooker.R;
import me.stupidme.cooker.view.StatusActivity;

public class NotificationService extends Service {

    public NotificationService() {

    }

    @Override
    public void onCreate() {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, StatusActivity.class),
                0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cooker)
                .setContentTitle("Cooker")
                .setContentText("This is a test")
                .setContentIntent(pendingIntent);

        manager.notify(0xaa, builder.build());

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
}
