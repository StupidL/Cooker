package me.stupidme.cooker.presenter;

import android.text.TextUtils;

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
import me.stupidme.cooker.view.login.LoginView;

/**
 * Created by StupidL on 2017/4/30.
 * <p>
 * A mock presenter to mock server response for test.
 */

public class UserLoginMockPresenterImpl implements UserLoginPresenter {

    //debug
    private static final String TAG = "UserLoginMockPresenter";

    /**
     * A callback interface to transfer response from server to UI fragment.
     */
    private LoginView mView;

    /**
     * A mock server.
     */
    private MockCookerService mMockService;

    public UserLoginMockPresenterImpl(LoginView view) {
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

                        List<UserBean> userBeanList = value.getData();
                        if (userBeanList.size() <= 0) {
                            mView.showProgress(false);
                            mView.showMessage(MESSAGE_WHAT_LOGIN_FAILED, null);
                            return;
                        }
                        if (remember) {
                            UserBean user = userBeanList.get(0);
                            mView.rememberUser(user);
                        }
                        mView.showMessage(MESSAGE_WHAT_LOGIN_SUCCESS, null);
                        mView.loginSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showProgress(false);
                        mView.showMessage(MESSAGE_WHAT_LOGIN_FAILED, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void attemptAutoLogin() {
        Long userId = SharedPreferenceUtil.getAccountUserId(0L);
        String userName = SharedPreferenceUtil.getAccountUserName("");
        String userPassword = SharedPreferenceUtil.getAccountUserPassword("");
        if (userId == 0L || TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPassword)) {
            mView.showMessage(MESSAGE_WHAT_LOGIN_AUTO_FAILED, null);
            mView.showProgress(false);
            return;
        }
        mMockService.login(userId, userName, userPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<UserBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<UserBean>> value) {
                        if (value.getResultCode() == 200) {
                            mView.showMessage(MESSAGE_WHAT_LOGIN_AUTO_SUCCESS, null);
                            mView.loginSuccess();
                        } else {
                            mView.showProgress(false);
                            mView.showMessage(MESSAGE_WHAT_LOGIN_AUTO_FAILED, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showProgress(false);
                        mView.showMessage(MESSAGE_WHAT_LOGIN_AUTO_FAILED, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
}
