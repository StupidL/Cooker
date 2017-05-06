package me.stupidme.cooker.view.login;

import me.stupidme.cooker.model.UserBean;

/**
 * Created by StupidL on 2017/3/14.
 * <p>
 * A callback interface to accept login response from server and then transfer them to UI fragment.
 */

public interface LoginView {

    /**
     * Show a message in fragment. Messages has many types, which defined by UserLoginPresenter.
     *
     * @param what    message type.
     * @param message message contents.
     * @see me.stupidme.cooker.presenter.UserLoginPresenter
     */
    void showMessage(int what, String message);

    /**
     * Show a progressbar in fragment.
     *
     * @param show show progressbar with animations if true.
     */
    void showProgress(boolean show);

    /**
     * Save user's info to a SharedPreference file.
     * This method called when login success in UserLoginPresenter.
     *
     * @param user user account info including username and password and userId.
     */
    void rememberUser(UserBean user);

    /**
     * Called in UserLoginPresenter when login success.
     */
    void loginSuccess();
}
