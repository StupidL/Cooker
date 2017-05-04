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
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.view.login.RegisterView;

/**
 * Created by StupidL on 2017/4/30.
 */

public class UserRegisterMockPresenterImpl implements UserRegisterPresenter {

    private static final String TAG = UserRegisterMockPresenterImpl.class.getSimpleName();

    private RegisterView mView;

    private MockCookerService mMockService;

    public UserRegisterMockPresenterImpl(RegisterView view) {
        mView = view;
        mMockService = CookerRetrofit.getInstance().getMockService();
    }

    @Override
    public void register(String name, String password) {
        mMockService.register(new UserBean(name, password))
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
