package me.stupidme.cooker.presenter;

/**
 * Created by StupidL on 2017/3/14.
 */

public interface UserLoginPresenter {

    /**
     * 登陆
     *
     * @param name     用户名
     * @param password 密码
     * @param remember 是否记住密码
     */
    void login(String name, String password, boolean remember);

    /**
     * 自动登录
     */
    void attemptAutoLogin();
}
