package me.stupidme.cooker.presenter;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.mock.MockCookerService;
import me.stupidme.cooker.model.UserBean;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.login.ILoginView;

/**
 * Created by StupidL on 2017/4/30.
 */

public class UserLoginMockPresenter implements IUserLoginPresenter {

    private static final String TAG = "UserLoginMockPresenter";

    private ILoginView mView;

    private MockCookerService mMockService;

    public UserLoginMockPresenter(ILoginView view) {
        mView = view;
        mMockService = CookerRetrofit.getInstance().getMockService();
    }

    @Override
    public void login(String name, String password, boolean remember) {
        mView.showProgress(true);
        Long userId = SharedPreferenceUtil.getAccountUserId(0L);
        mMockService.login(userId, name, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<UserBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<UserBean>> value) {
                        if (remember) {
                            UserBean user = value.getData().get(0);
                            mView.rememberUser(user);
                        }
                        mView.loginSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showMessage(e.toString());
                        mView.showProgress(false);
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showMessage("Success!");
                        mView.loginSuccess();
                        Log.i(TAG, "onComplete");
                    }
                });
    }

    @Override
    public void attemptAutoLogin() {
        Long userId = SharedPreferenceUtil.getAccountUserId(0L);
        String userName = SharedPreferenceUtil.getAccountUserName("");
        String userPassword = SharedPreferenceUtil.getAccountUserPassword("");
        if (userId == 0L || TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword)) {
            mView.showMessage("Auto login failed. No username and password remembered.");
            return;
        }
        mMockService.login(userId, userName, userPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<UserBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(HttpResult<List<UserBean>> value) {
                        Log.i(TAG, "onNext: " + value.toString());
                        UserBean userBean = value.getData().get(0);
                        Log.v(TAG, "user: " + userBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.loginSuccess();
                        Log.i(TAG, "onComplete");
                    }
                });

    }
}
