package me.stupidme.cooker.view.login;

import me.stupidme.cooker.model.UserBean;

/**
 * Created by StupidL on 2017/3/14.
 */

public interface ILoginView {

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
     * 记住用户名和密码
     *
     * @param user 用户
     */
    void rememberUser(UserBean user);

    /**
     * 获取用户名和密码
     *
     * @return 用户登录信息
     */
    UserBean getUserInfo();

    /**
     * 登陆成功
     */
    void loginSuccess();
}
