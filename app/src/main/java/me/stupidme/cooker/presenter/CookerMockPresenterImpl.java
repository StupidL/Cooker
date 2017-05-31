package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.mock.MockCookerService;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.db.DbManager;
import me.stupidme.cooker.model.db.DbManagerImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.cooker.CookerView;

import static me.stupidme.cooker.MessageConstants.MESSAGE_DELETE_SERVER_COOKER_ERROR;
import static me.stupidme.cooker.MessageConstants.MESSAGE_DELETE_SERVER_COOKER_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_DELETE_LOCAL_COOKER_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_DELETE_SERVER_COOKER_SUCCESS_RETURN_EMPTY;
import static me.stupidme.cooker.MessageConstants.MESSAGE_INSERT_SERVER_COOKER_ERROR;
import static me.stupidme.cooker.MessageConstants.MESSAGE_INSERT_SERVER_COOKER_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_INSERT_LOCAL_COOKER_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_INSERT_SERVER_COOKER_SUCCESS_RETURN_EMPTY;
import static me.stupidme.cooker.MessageConstants.MESSAGE_QUERY_SERVER_COOKER_ERROR;
import static me.stupidme.cooker.MessageConstants.MESSAGE_QUERY_SERVER_COOKER_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_QUERY_SERVER_COOKER_SUCCESS;
import static me.stupidme.cooker.MessageConstants.MESSAGE_QUERY_SERVER_COOKER_SUCCESS_RETURN_EMPTY;
import static me.stupidme.cooker.MessageConstants.MESSAGE_UPDATE_SERVER_COOKER_ERROR;
import static me.stupidme.cooker.MessageConstants.MESSAGE_UPDATE_SERVER_COOKER_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_UPDATE_LOCAL_COOKER_FAILED;
import static me.stupidme.cooker.MessageConstants.MESSAGE_UPDATE_SERVER_COOKER_SUCCESS_RETURN_EMPTY;

/**
 * Created by StupidL on 2017/4/30.
 */

public class CookerMockPresenterImpl implements CookerPresenter {

    private static final String TAG = "CookerMockPresenterImpl";

    private CookerView mView;

    private DbManager mDbManager;

    private MockCookerService mMockService;

    public CookerMockPresenterImpl(CookerView view) {
        mView = view;
        mDbManager = DbManagerImpl.getInstance();
        mMockService = CookerRetrofit.getInstance().getMockService();
    }

    @Override
    public void deleteCooker(long cookerId) {
        mView.showDialog(true);
        mMockService.deleteCooker(SharedPreferenceUtil.getAccountUserId(0L), cookerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_DELETE_SERVER_COOKER_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_DELETE_SERVER_COOKER_SUCCESS_RETURN_EMPTY, null);
                            return;
                        }
                        boolean success = mDbManager.deleteCooker(DbManager.KEY_COOKER_ID, cookerId);
                        if (!success) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_DELETE_LOCAL_COOKER_FAILED, null);
                            return;
                        }
                        mView.removeCooker(value.getData().get(0));
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showDialog(false);
                        mView.showMessage(MESSAGE_DELETE_SERVER_COOKER_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showDialog(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void deleteCookers() {
        mView.showDialog(true);
        mMockService.deleteCookers(SharedPreferenceUtil.getAccountUserId(0L))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_DELETE_SERVER_COOKER_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_DELETE_SERVER_COOKER_SUCCESS_RETURN_EMPTY, null);
                            return;
                        }
                        boolean success = mDbManager.deleteCookers(DbManager.KEY_USER_ID,
                                SharedPreferenceUtil.getAccountUserId(0L));
                        if (!success) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_DELETE_LOCAL_COOKER_FAILED, null);
                            return;
                        }
                        for (CookerBean cookerBean : value.getData())
                            mView.removeCooker(cookerBean.getCookerId());
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showDialog(false);
                        mView.showMessage(MESSAGE_DELETE_SERVER_COOKER_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showDialog(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void insertCooker(CookerBean bean) {
        mView.showDialog(true);
        Long userId = SharedPreferenceUtil.getAccountUserId(0L);
        bean.setUserId(userId);
        Log.v(TAG, "CookerBean: " + bean.toString());
        mMockService.insertCooker(userId, bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_SERVER_COOKER_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_SERVER_COOKER_SUCCESS_RETURN_EMPTY, null);
                            return;
                        }
                        boolean success = mDbManager.insertCooker(value.getData().get(0));
                        if (!success) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_INSERT_LOCAL_COOKER_FAILED, null);
                            return;
                        }
                        mView.insertCooker(value.getData().get(0));
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showDialog(false);
                        mView.showMessage(MESSAGE_INSERT_SERVER_COOKER_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showDialog(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void queryCookerFromDB(long cookerId) {
        mView.insertCooker(mDbManager.queryCookers(DbManager.KEY_COOKER_ID, cookerId).get(0));
    }

    @Override
    public void queryCookersFromDB() {
        mView.insertCookers(mDbManager.queryCookers(DbManager.KEY_USER_ID,
                SharedPreferenceUtil.getAccountUserId(0L)));
    }

    @Override
    public void updateCooker(int position, CookerBean bean) {
        mView.showDialog(true);
        mMockService.updateCooker(SharedPreferenceUtil.getAccountUserId(0L), bean.getCookerId(), bean)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<CookerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<CookerBean>> value) {
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_UPDATE_SERVER_COOKER_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_UPDATE_SERVER_COOKER_SUCCESS_RETURN_EMPTY, null);
                            return;
                        }
                        boolean success = mDbManager.updateCooker(value.getData().get(0));
                        if (!success) {
                            mView.showDialog(false);
                            mView.showMessage(MESSAGE_UPDATE_LOCAL_COOKER_FAILED, null);
                            return;
                        }
                        mView.updateCooker(position, bean);
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showDialog(false);
                        mView.showMessage(MESSAGE_UPDATE_SERVER_COOKER_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showDialog(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void queryCookerFromServer(int position, long cookerId) {
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
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.showRefreshing(false);
                            mView.showMessage(MESSAGE_QUERY_SERVER_COOKER_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.showRefreshing(false);
                            mView.showMessage(MESSAGE_QUERY_SERVER_COOKER_SUCCESS_RETURN_EMPTY, null);
                            return;
                        }
                        boolean success = mDbManager.updateCooker(value.getData().get(0));
                        if (!success) {
                            mView.showRefreshing(false);
                            mView.showMessage(MESSAGE_UPDATE_LOCAL_COOKER_FAILED, null);
                            return;
                        }
                        mView.updateCooker(position, value.getData().get(0));
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showRefreshing(false);
                        mView.showMessage(MESSAGE_QUERY_SERVER_COOKER_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }

    @Override
    public void queryCookersFromServer() {
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
                        if (value == null || value.getData() == null || value.getResultCode() != 200) {
                            mView.showRefreshing(false);
                            mView.showMessage(MESSAGE_QUERY_SERVER_COOKER_FAILED, null);
                            return;
                        }
                        if (value.getData().size() <= 0) {
                            mView.showRefreshing(false);
                            mView.showMessage(MESSAGE_QUERY_SERVER_COOKER_SUCCESS_RETURN_EMPTY, null);
                            return;
                        }
                        boolean success = mDbManager.updateCookers(value.getData());
                        if (!success) {
                            mView.showRefreshing(false);
                            mView.showMessage(MESSAGE_UPDATE_LOCAL_COOKER_FAILED, null);
                            return;
                        }
                        mView.insertCookers(value.getData());
                        Log.i(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showRefreshing(false);
                        mView.showMessage(MESSAGE_QUERY_SERVER_COOKER_ERROR, e.toString());
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showRefreshing(false);
                        Log.i(TAG, "onComplete: ");
                    }
                });
    }
}
