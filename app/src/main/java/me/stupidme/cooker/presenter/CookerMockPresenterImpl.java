package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.mock.MockCookerService;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.db.RealmCookerManager;
import me.stupidme.cooker.model.db.RealmDbManagerImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.cooker.CookerView;

/**
 * Created by StupidL on 2017/4/30.
 */

public class CookerMockPresenterImpl implements CookerPresenter {

    private static final String TAG = "CookerMockPresenterImpl";

    private CookerView mView;

    private RealmCookerManager mRealmManager;

    private MockCookerService mMockService;

    public CookerMockPresenterImpl(CookerView view) {
        mView = view;
        mRealmManager = RealmDbManagerImpl.getInstance();
        mMockService = CookerRetrofit.getInstance().getMockService();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void deleteCooker(long cookerId) {
        mMockService.deleteCooker(SharedPreferenceUtil.getAccountUserId(0L), cookerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mRealmManager.deleteCookers(RealmCookerManager.KEY_COOKER_ID, cookerId);
                        mView.removeCooker(value.getData().get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void deleteCookers() {
        mMockService.deleteCookers(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mRealmManager.deleteCookers(RealmCookerManager.KEY_USER_ID, SharedPreferenceUtil.getAccountUserId(0L));
                        mView.removeCookers(value.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void insertCooker(CookerBean bean) {
        mMockService.insertCooker(SharedPreferenceUtil.getAccountUserId(0L), bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mRealmManager.insertCookers(value.getData());
                        mView.insertCooker(value.getData().get(0));
                        Log.v(TAG, "value resultCode: " + value.getResultCode());
                        Log.v(TAG, "value resultMessage: " + value.getResultMessage());

                        Log.v(TAG, "cooker: " + value.getData().get(0).toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void insertCookers(List<CookerBean> cookers) {

    }

    @Override
    public void queryCookerFromDB(long cookerId) {
        mView.insertCooker(mRealmManager.queryCookers(RealmCookerManager.KEY_COOKER_ID, cookerId).get(0));
    }

    @Override
    public void queryCookersFromDB() {
        mView.insertCookers(mRealmManager.queryCookers(RealmCookerManager.KEY_USER_ID,
                SharedPreferenceUtil.getAccountUserId(0L)));
    }

    @Override
    public void updateCooker(int position, CookerBean bean) {
        mMockService.updateCooker(SharedPreferenceUtil.getAccountUserId(0L), bean.getCookerId(), bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mRealmManager.updateCooker(value.getData().get(0));
                        mView.updateCooker(position, value.getData().get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void queryCookerFromServer(int position, long cookerId) {
        Log.v(TAG, "queryCookerFromServer...");
        mView.showRefreshing(true);
        mMockService.queryCooker(SharedPreferenceUtil.getAccountUserId(0L), cookerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mRealmManager.updateCooker(value.getData().get(0));
                        mView.insertCooker(value.getData().get(0));
                        mView.showRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.showRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        mView.showRefreshing(false);
                    }
                });
    }

    @Override
    public void queryCookersFromServer() {
        Log.v(TAG, "queryCookersFromServer...");
        Log.v(TAG, "userId form sp: " + SharedPreferenceUtil.getAccountUserId(0L));
        mView.showRefreshing(true);
        mMockService.queryCookers(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        Log.v(TAG, "value resultCode: " + value.getResultCode());
                        Log.v(TAG, "value resultMessage: " + value.getResultMessage());
                        Log.v(TAG, "value data size: " + value.getData().size());
                        mRealmManager.updateCookers(value.getData());
                        mView.insertCookers(value.getData());
                        mView.showRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.showRefreshing(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
