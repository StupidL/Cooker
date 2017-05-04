package me.stupidme.cooker.presenter;

/**
 * Created by StupidL on 2017/3/14.
 */

public interface UserRegisterPresenter {

    /**
     * 注册
     *
     * @param name     用户名
     * @param password 密码
     */
    void register(String name, String password);
}
