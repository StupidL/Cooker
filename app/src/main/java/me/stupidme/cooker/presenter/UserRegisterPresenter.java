package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.mock.MockCookerService;
import me.stupidme.cooker.model.UserBean;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.CookerService;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.view.login.IRegisterView;

/**
 * Created by StupidL on 2017/3/14.
 */

public class UserRegisterPresenter implements IUserRegisterPresenter {

    private static final String TAG = "UserRegisterPresenter";

    private IRegisterView mView;

    private CookerService mService;

    private MockCookerService mMockService;

    public UserRegisterPresenter(IRegisterView view) {
        mView = view;
        mService = CookerRetrofit.getInstance().getCookerService();
        mMockService = CookerRetrofit.getInstance().getMockService();
    }

    @Override
    public void register(String name, String password) {
        mView.showProgress(true);
//        mService.register(new UserBean(name, password))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<HttpResult<List<UserBean>>>() {
//
//                    UserBean userBean;
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        Log.v(TAG, "onSubscribe: " + d.toString());
//                    }
//
//                    @Override
//                    public void onNext(HttpResult<List<UserBean>> value) {
//                        userBean = value.getData().get(0);
//                        mView.registerSuccess(userBean);
//                        Log.v(TAG, "onNext: " + value.toString());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mView.showProgress(false);
//                        mView.showMessage(e.toString());
//                        Log.v(TAG, "onError: " + e.toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.v(TAG, "onComplete");
//                    }
//                });
        mMockService.register(new UserBean("allen", "abc123"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<UserBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<UserBean>> value) {
                        List<UserBean> list = value.getData();
                        Log.v(TAG, "code: " + value.getResultCode());
                        Log.v(TAG, "message: " + value.getResultMessage());
                        Log.v(TAG, "data: ");
                        for (UserBean userBean : list) {
                            Log.v(TAG, "user: " + userBean.toString());
                        }
                        mView.registerSuccess(list.get(0));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
