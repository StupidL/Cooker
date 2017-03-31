package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.CookerModel;
import me.stupidme.cooker.model.ICookerModel;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
import me.stupidme.cooker.retrofit.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.cooker.ICookerView;

/**
 * Created by StupidL on 2017/3/7.
 */

public class CookerPresenter implements ICookerPresenter {

    private static final String TAG = "CookerPresenter";

    private static CookerPresenter sInstance;

    private ICookerView mView;

    private ICookerModel mModel;

    private CookerService mService;

    private CookerPresenter(ICookerView view) {
        mView = view;
        mModel = CookerModel.getInstance();
        mService = CookerRetrofit.getInstance().getCookerService();
    }

    public static CookerPresenter getInstance(ICookerView view) {
        if (sInstance == null)
            sInstance = new CookerPresenter(view);
        return sInstance;
    }

    @Override
    public void deleteCooker(long cookerId) {

        Log.v(TAG, "++++++CookerPresenter deleteCooker()++++++");
        Log.v(TAG, "args: CookerBean id = " + cookerId);

        mService.deleteCooker(SharedPreferenceUtil.getAccountUserId(0L), cookerId)
                .subscribeOn(Schedulers.io())
                .doOnNext(listHttpResult -> mModel.deleteCooker(cookerId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mView.removeCooker(value.getData().get(0));
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.setRefreshing(false);
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void deleteCookers() {

        mService.deleteCookers(SharedPreferenceUtil.getAccountUserId(0L))
                .observeOn(Schedulers.io())
                .doOnNext(listHttpResult -> mModel.deleteCookers())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        for (CookerBean cookerBean : value.getData())
                            mView.removeCooker(cookerBean);
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.setRefreshing(false);
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void insertCooker(CookerBean bean) {

        Log.v(TAG, "++++++CookerPresenter insertCooker()++++++");
        Log.v(TAG, "args: CookerBean = " + bean.toString());

        mService.insertCooker(SharedPreferenceUtil.getAccountUserId(0L), bean)
                .subscribeOn(Schedulers.io())
                .doOnNext(listHttpResult -> mModel.insertCooker(listHttpResult.getData().get(0)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mView.insertCooker(value.getData().get(0));
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.setRefreshing(false);
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void insertCookers(List<CookerBean> cookers) {

    }

    @Override
    public void queryCookerFromDB(long cookerId) {
        mView.insertCooker(mModel.queryCooker(cookerId));
    }

    @Override
    public void queryCookersFromDB() {

//        mView.insertCookersFromDB(mModel.queryCookers());

        //just a test
        List<CookerBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CookerBean cooker = new CookerBean();
            cooker.setCookerName("Cooker" + i);
            cooker.setCookerLocation("Place" + i);
            cooker.setCookerStatus(i % 2 == 0 ? "free" : "booking");
            list.add(cooker);
        }
        mView.insertCookersFromDB(list);
    }

    @Override
    public void updateCooker(int position, CookerBean bean) {

        Log.v(TAG, "++++++CookerPresenter updateCooker()++++++");
        Log.v(TAG, "args: CookerBean = " + bean.toString());

        mService.updateCooker(SharedPreferenceUtil.getAccountUserId(0L), bean.getCookerId(), bean)
                .subscribeOn(Schedulers.io())
                .doOnNext(listHttpResult -> mModel.updateCooker(listHttpResult.getData().get(0)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mView.updateCooker(position, bean);
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.setRefreshing(false);
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void queryCookerFromServer(int position, long cookerId) {

        mService.queryCooker(SharedPreferenceUtil.getAccountUserId(0L), cookerId)
                .subscribeOn(Schedulers.io())
                .doOnNext(listHttpResult -> mModel.updateCooker(listHttpResult.getData().get(0)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mView.updateCooker(position, value.getData().get(0));
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.setRefreshing(false);
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void queryCookersFromServer() {

        Log.v(TAG, "++++++CookerPresenter queryCookersFromServer()++++++");

        mView.setRefreshing(true);

        mService.queryCookers(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .doOnNext(listHttpResult -> mModel.updateCookers(listHttpResult.getData()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mView.updateCookersFromServer(value.getData());
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.setRefreshing(false);
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }
}
