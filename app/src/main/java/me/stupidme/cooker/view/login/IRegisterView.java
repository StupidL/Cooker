package me.stupidme.cooker.view.login;

import me.stupidme.cooker.model.UserBean;

/**
 * Created by StupidL on 2017/3/14.
 */

public interface IRegisterView {

    /**
     * 弹出提示信息
     *
     * @param message 要展示的信息
     */
    void showMessage(String message);

    /**
     * 显示进度条
     *
     * @param show true则显示，否则不显示
     */
    void showProgress(boolean show);

    /**
     * 登陆成功回调
     */
    void loginSuccess();

    /**
     * 保存用户信息搭配SharedPreference
     *
     * @param user 用户信息
     */
    void saveUserInfo(UserBean user);
}
