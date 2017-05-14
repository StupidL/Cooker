package me.stupidme.cooker.presenter;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.UserBean;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.CookerService;
import me.stupidme.cooker.model.http.HttpResult;
import me.stupidme.cooker.view.login.RegisterView;

/**
 * Created by StupidL on 2017/3/14.
 * <p>
 * A presenter to transfer response of server to UI fragment.
 */

public class UserRegisterPresenterImpl implements UserRegisterPresenter {

    //debug
    private static final String TAG = "UserRegisterPresenter";

    /**
     * A callback interface to communicate with UI fragment.
     */
    private RegisterView mView;

    /**
     * A real server.
     */
    private CookerService mService;

    public UserRegisterPresenterImpl(RegisterView view) {
        mView = view;
        mService = CookerRetrofit.getInstance().getCookerService();
    }

    @Override
    public void register(String name, String password) {
        mView.showProgress(true);
        mService.register(new UserBean(name, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<List<UserBean>>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<List<UserBean>> value) {
                        if (value == null || value.getData().size() <= 0 || value.getResultCode() != 200) {
                            mView.showProgress(false);
                            mView.showMessage(MESSAGE_WHAT_REGISTER_FAILED, null);
                            return;
                        }
                        mView.showMessage(MESSAGE_WHAT_REGISTER_SUCCESS, null);
                        mView.registerSuccess(value.getData().get(0));

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showProgress(false);
                        mView.showMessage(MESSAGE_WHAT_REGISTER_FAILED, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
