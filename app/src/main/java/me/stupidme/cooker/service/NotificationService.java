package me.stupidme.cooker.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Iterator;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.db.DbManager;
import me.stupidme.cooker.model.db.DbManagerImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.CookerService;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.status.StatusActivity;

public class NotificationService extends Service {

    private static final String TAG = "NotificationService";
    public static final String ACTION_NOTIFY = "me.stupidme.cooker.action.NOTIFY";
    private static final String STOP_SERVICE_ACTION = "me.stupidme.cooker.ACTION_STOP_SERVICE";
    Notification.Builder mBuilder;
    NotificationManager mManager;
    private static final int mNotificationId = 0xaa;
    private PendingIntent mStatusIntent;
    private PendingIntent mStopIntent;
    private CookerService mService;
    private DbManager mDbManager;
    private Thread mThread;

    public NotificationService() {

    }

    @Override
    public void onCreate() {
        mService = CookerRetrofit.getInstance().getMockService();
        mDbManager = DbManagerImpl.getInstance();

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
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (mThread == null) {
            mThread = new Thread(() -> {
                syncCookers();
                syncBooks();
                checkAndNotify();
            }, "NotifyThread");
        }
        mThread.start();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void syncCookers() {
        mService.queryCookers(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .doOnNext(listHttpResult -> {
                    if (listHttpResult != null && listHttpResult.getResultCode() == 200) {
                        if (listHttpResult.getData() == null || listHttpResult.getData().size() <= 0)
                            return;

                        List<CookerBean> cookers = listHttpResult.getData();
                        mDbManager.updateCookers(cookers);
                    }
                })
                .subscribe();
    }

    private void syncBooks() {
        mService.queryBooks(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .doOnNext(listHttpResult -> {
                    if (listHttpResult != null && listHttpResult.getResultCode() == 200) {
                        if (listHttpResult.getData() == null || listHttpResult.getData().size() <= 0)
                            return;
                        List<BookBean> books = listHttpResult.getData();
                        mDbManager.updateBooks(books);
                    }
                })
                .subscribe();

    }

    private void checkAndNotify() {
        List<BookBean> books = mDbManager.queryBooks(DbManager.KEY_USER_ID,
                SharedPreferenceUtil.getAccountUserId(0L));
        for (BookBean bookBean : books) {
            if ("BOOKING".equals(bookBean.getCookerStatus().toUpperCase())) {
                Intent intent = new Intent();
                intent.setAction(ACTION_NOTIFY);
                sendBroadcast(intent);
            }
        }
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

}
