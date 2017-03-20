package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.IUserModel;
import me.stupidme.cooker.model.UserBean;
import me.stupidme.cooker.model.UserModel;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
import me.stupidme.cooker.retrofit.HttpResult;
import me.stupidme.cooker.view.login.ILoginView;

/**
 * Created by StupidL on 2017/3/14.
 */

public class UserLoginPresenter implements IUserLoginPresenter {

    private static final String TAG = "UserLoginPresenter";

    private IUserModel mModel;

    private ILoginView mView;

    private CookerService mService;

    public UserLoginPresenter(ILoginView view) {
        mView = view;
        mModel = UserModel.getInstance();
        mService = CookerRetrofit.getInstance().getCookerService();
    }

    @Override
    public void login(String name, String password, boolean remember) {
        mView.showProgress(true);
        mService.login(name, password)
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
                        mView.showMessage(e.toString());
                        mView.showProgress(false);
                        Log.i(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.showMessage("Success!");
                        if (remember) {
                            mView.rememberUser(new UserBean(name, password));
                        }
                        mView.loginSuccess();
                        Log.i(TAG, "onComplete");
                    }
                });
    }

    @Override
    public void attemptAutoLogin() {
        if (mView.getUserInfo() != null) {
            String name = mView.getUserInfo().getUserName();
            String password = mView.getUserInfo().getPassword();

            mService.login(name, password)
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
