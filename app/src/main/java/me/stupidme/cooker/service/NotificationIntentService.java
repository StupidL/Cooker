package me.stupidme.cooker.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;

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

/**
 * Created by stupidl on 17-5-17.
 */

public class NotificationIntentService extends IntentService {

    private static final String ACTION_NOTIFY = "me.stupidme.cooker.action.NOTIFY";

    private CountDownTimer mCountDownTimer;

    private CookerService mService;

    private DbManager mDbManager;

    private NotifyBroadcastReceiver mReceiver;

    private Notification mNotification;

    public NotificationIntentService(String name) {
        super(name);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        mService = CookerRetrofit.getInstance().getCookerService();
        mDbManager = DbManagerImpl.getInstance();

        mReceiver = new NotifyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NOTIFY);
        registerReceiver(mReceiver, filter);

        Long frequency = Long.valueOf(SharedPreferenceUtil.getSyncFrequency("3"));
        mCountDownTimer = new CountDownTimer(Long.MAX_VALUE, frequency * 60 * 1000) {
            @Override
            public void onTick(long l) {
                syncDataFromServerAndNotify();
            }

            @Override
            public void onFinish() {

            }
        };


    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mCountDownTimer.start();
    }

    @Override
    public void onDestroy() {
        mCountDownTimer.cancel();
        unregisterReceiver(mReceiver);
    }

    private void syncDataFromServerAndNotify() {
        syncCookers();
        syncBooks();
        checkAndNotify();
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

        books.parallelStream()
                .filter(b -> "Booking".toUpperCase().equals(b.getCookerStatus().toUpperCase()))
                .forEach(bookBean -> {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_NOTIFY);
                    sendBroadcast(intent);
                });
    }

    private class NotifyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_NOTIFY.equals(intent.getAction())) {
                sendSimpleNotification(context);
            }
        }

        private void sendSimpleNotification(Context context) {
            Intent statusIntent = new Intent(context, StatusActivity.class);
            statusIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (mNotification == null) {
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
            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0xaa, mNotification);
        }
    }
}
