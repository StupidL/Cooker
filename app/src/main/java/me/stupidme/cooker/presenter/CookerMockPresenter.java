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
import me.stupidme.cooker.model.db.RealmDbManager;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.cooker.ICookerView;

/**
 * Created by StupidL on 2017/4/30.
 */

public class CookerMockPresenter implements ICookerPresenter {

    private static final String TAG = "CookerMockPresenter";

    private ICookerView mView;

    private RealmCookerManager mRealmManager;

    private MockCookerService mMockService;

    public CookerMockPresenter(ICookerView view) {
        mView = view;
        mRealmManager = RealmDbManager.getInstance();
        mMockService = CookerRetrofit.getInstance().getMockService();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void deleteCooker(long cookerId) {
        mMockService.deleteCooker(SharedPreferenceUtil.getAccountUserId(0L), cookerId)
                .subscribeOn(Schedulers.io())
//                .doOnNext(listHttpResult -> mRealmManager.deleteCookers(RealmCookerManager.KEY_COOKER_ID, cookerId + ""))
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
//                .doOnNext(listHttpResult -> mRealmManager.deleteCookers(RealmCookerManager.KEY_USER_ID, SharedPreferenceUtil.getAccountUserId(0L) + ""))
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
//                .doOnNext(listHttpResult -> mRealmManager.insertCookers(listHttpResult.getData()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mRealmManager.insertCookers(value.getData());
                        mView.insertCooker(value.getData().get(0));
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
//                .doOnNext(listHttpResult -> mRealmManager.updateCooker(listHttpResult.getData().get(0)))
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
//                .doOnNext(listHttpResult -> mRealmManager.updateCooker(listHttpResult.getData().get(0)))
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
        mMockService.queryCookers(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
//                .doOnNext(listHttpResult -> mRealmManager.updateCookers(listHttpResult.getData()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        mRealmManager.updateCookers(value.getData());
                        mView.insertCookers(value.getData());
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
}
