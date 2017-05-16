package me.stupidme.cooker.presenter;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.mock.MockCookerService;
import me.stupidme.cooker.model.UserBean;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.view.splash.SplashView;

/**
 * Created by StupidL on 2017/5/16.
 */

public class SplashMockPresenterImpl implements SplashPresenter {

    private SplashView mView;

    private MockCookerService mMockService;

    public SplashMockPresenterImpl(SplashView view) {
        mView = view;
        mMockService = CookerRetrofit.getInstance().getMockService();
    }

    @Override
    public void attemptAutoLogin(Long userId, String username, String password) {
        mMockService.login(userId, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<UserBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<UserBean>> value) {
                        List<UserBean> userBeanList = value.getData();
                        if (userBeanList == null || userBeanList.size() <= 0 || value.getResultCode() != 200) {
                            mView.onLoginFailed();
                            return;
                        }
                        UserBean userBean = userBeanList.get(0);
                        if (userBean.getUserId() != (long) userId
                                || !userBean.getUserName().equals(username)
                                || !userBean.getPassword().equals(password)) {
                            mView.onLoginFailed();
                            return;
                        }
                        mView.onLoginSuccess();
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
