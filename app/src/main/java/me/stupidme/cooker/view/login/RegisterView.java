package me.stupidme.cooker.view.login;

import me.stupidme.cooker.model.UserBean;

/**
 * Created by StupidL on 2017/3/14.
 * <p>
 * A callback interface to accept response from server and then decides actions of UI fragment.
 */

public interface RegisterView {

    /**
     * Show message in fragment.
     *
     * @param what    message type
     * @param message message contents.
     */
    void showMessage(int what, CharSequence message);

    /**
     * Show progressbar when necessary.
     *
     * @param show show progressbar with animations if true.
     */
    void showProgress(boolean show);

    /**
     * Called bu UserRegisterPresenter only if register success.
     *
     * @param user user account info including username, password and userId.
     */
    void registerSuccess(UserBean user);

}
