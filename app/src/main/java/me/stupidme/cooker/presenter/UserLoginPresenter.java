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
import me.stupidme.cooker.model.http.CookerService;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.login.ILoginView;

/**
 * Created by StupidL on 2017/3/14.
 */

public class UserLoginPresenter implements IUserLoginPresenter {

    private static final String TAG = "UserLoginPresenter";

    private ILoginView mView;

    private CookerService mService;

    private MockCookerService mMockService;

    public UserLoginPresenter(ILoginView view) {
        mView = view;
        mService = CookerRetrofit.getInstance().getCookerService();
        mMockService = CookerRetrofit.getInstance().getMockService();
    }

    @Override
    public void login(String name, String password, boolean remember) {
        mView.showProgress(true);
        mService.login(0L,name, password)
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
                        if (remember) {
                            UserBean user = value.getData().get(0);
                            mView.rememberUser(user);
                        }
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
        String userName = SharedPreferenceUtil.getAccountUserName("");
        String userPassword = SharedPreferenceUtil.getAccountUserPassword("");

        if (!(TextUtils.isEmpty(userName) | TextUtils.isEmpty(userPassword))) {

            mService.login(0L,userName, userPassword)
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
}
