package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.CookerModel;
import me.stupidme.cooker.model.ICookerModel;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
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
    public void deleteCooker(CookerBean bean) {

        Log.v(TAG, "++++++CookerPresenter deleteCooker()++++++");
        Log.v(TAG, "args: CookerBean = " + bean.toString());

        //同步至服务器
        mService.rxDeleteDevice(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CookerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(CookerBean value) {
                        mView.showMessage(value.toString());
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
                        //界面上移除该项目
                        mView.removeCooker(bean);
                        //数据库删除该设备
                        mModel.deleteCooker(bean);
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void insertCooker(CookerBean bean) {

        Log.v(TAG, "++++++CookerPresenter insertCooker()++++++");
        Log.v(TAG, "args: CookerBean = " + bean.toString());

        //同步至服务器
        mService.rxPostNewDevice(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CookerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(CookerBean value) {
                        mView.showMessage(value.toString());
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
                        mModel.insertCooker(bean);
                        mView.insertCooker(bean);
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void queryCookersFromDB() {

        //just a test
        List<CookerBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            CookerBean cooker = new CookerBean();
            cooker.setName("Cooker" + i);
            cooker.setLocation("Place" + i);
            cooker.setStatus(i % 2 == 0 ? "free" : "booking");
            list.add(cooker);
        }
        mView.insertCookers(list);

//        Log.v(TAG, "++++++CookerPresenter deleteCooker()++++++");
//        Log.v(TAG, "args: null ");
//
//        mView.insertCookers(mModel.queryCookers());
    }

    @Override
    public void updateCooker(int position, CookerBean bean) {

        Log.v(TAG, "++++++CookerPresenter updateCooker()++++++");
        Log.v(TAG, "args: CookerBean = " + bean.toString());

        //同步至服务器
        mService.rxUpdateDevice(bean)               //创建Observable对象
                .subscribeOn(Schedulers.io())       //在io线程产生事件
                .observeOn(AndroidSchedulers.mainThread())  //在主线程消费事件
                .subscribe(new Observer<CookerBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(CookerBean value) {
                        mView.showMessage(value.toString());
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
                        mView.updateCooker(position, bean);
                        mModel.updateCooker(bean);
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });

    }

    @Override
    public void queryCookersFromServer(Map<String, String> map) {

        Log.v(TAG, "++++++CookerPresenter queryCookersFromServer()++++++");
        Log.v(TAG, "args: Map = " + map.toString());

        mView.setRefreshing(true);
        mService.rxGetAllDevices(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<CookerBean>>() {

                    List<CookerBean> list = new ArrayList<>();

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(List<CookerBean> value) {
                        list.addAll(value);
                        mView.showMessage(value.toString());
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
                        mView.updateCookers(list);
                        mModel.updateCookers(list);
                        mView.setRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }
}
