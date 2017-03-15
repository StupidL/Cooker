package me.stupidme.cooker.presenter;

import android.util.Log;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.IUserModel;
import me.stupidme.cooker.model.UserBean;
import me.stupidme.cooker.model.UserModel;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
import me.stupidme.cooker.view.login.IRegisterView;

/**
 * Created by StupidL on 2017/3/14.
 */

public class UserRegisterPresenter implements IUserRegisterPresenter {

    private static final String TAG = "UserRegisterPresenter";

    private IUserModel mModel;

    private IRegisterView mView;

    private CookerService mService;

    public UserRegisterPresenter(IRegisterView view) {
        mView = view;
        mModel = UserModel.getInstance();
        mService = CookerRetrofit.getInstance().getCookerService();
    }

    @Override
    public void register(String name, String password) {
        mView.showProgress(true);
        mService.rxRegister(new UserBean(name, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(UserBean value) {
                        Log.v(TAG, "onNext: " + value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showProgress(false);
                        mView.showMessage(e.toString());
                        Log.v(TAG, "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        mView.loginSuccess();
                        Log.v(TAG, "onComplete");
                    }
                });
    }
}
