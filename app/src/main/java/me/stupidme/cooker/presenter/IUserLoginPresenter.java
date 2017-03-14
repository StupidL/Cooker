package me.stupidme.cooker.presenter;

/**
 * Created by StupidL on 2017/3/14.
 */

public interface IUserLoginPresenter {

    /**
     * 登陆
     *
     * @param name     用户名
     * @param password 密码
     */
    void login(String name, String password);
}
