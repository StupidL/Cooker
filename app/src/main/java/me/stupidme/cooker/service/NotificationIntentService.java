package me.stupidme.cooker.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.db.DbManager;
import me.stupidme.cooker.model.db.DbManagerImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.CookerService;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;

/**
 * Created by stupidl on 17-5-17.
 */

public class NotificationIntentService extends IntentService {

    private static final String ACTION_NOTIFY = "me.stupidme.cooker.action.NOTIFY";
    private CountDownTimer mCountDownTimer;
    private CookerService mService;
    private DbManager mDbManager;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mService = CookerRetrofit.getInstance().getMockService();
        mDbManager = DbManagerImpl.getInstance();

        Long frequency = Long.valueOf(SharedPreferenceUtil.getSyncFrequency("1"));
        Log.v("NotifyService", "====fre: " + frequency);
        mCountDownTimer = new CountDownTimer(6 * 100 * 1000, frequency * 60 * 1000) {
            @Override
            public void onTick(long l) {
                syncCookers();
                syncBooks();
                checkAndNotify();
                Log.v("NotifyService", "==========onTick()....");
                Log.v("NotifyService", l / 1000 + "");
            }

            @Override
            public void onFinish() {

            }
        };
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mCountDownTimer.start();

//        for(;;) {
//            Long frequency = Long.valueOf(SharedPreferenceUtil.getSyncFrequency("1"));
//            syncCookers();
//            syncBooks();
//            checkAndNotify();
//            try {
//                Thread.sleep(frequency * 60 * 1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void onDestroy() {
        mCountDownTimer.cancel();
    }

    private void syncCookers() {
        mService.queryCookers(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        if (value != null && value.getResultCode() == 200) {
                            if (value.getData() == null || value.getData().size() <= 0)
                                return;

                            List<CookerBean> cookers = value.getData();
                            mDbManager.updateCookers(cookers);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void syncBooks() {
        mService.queryBooks(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<HttpResult<List<BookBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<BookBean>> value) {
                        if (value != null && value.getResultCode() == 200) {
                            if (value.getData() == null || value.getData().size() <= 0)
                                return;
                            List<BookBean> books = value.getData();
                            mDbManager.updateBooks(books);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

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

}
