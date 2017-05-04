package me.stupidme.cooker.model;

/**
 * Created by StupidL on 2017/3/14.
 */

public interface UserModel {

    /**
     * 保存用户信息
     *
     * @param user 用户
     */
    void saveUser(UserBean user);

    /**
     * 获取用户信息
     *
     * @return 用户
     */
    UserBean getUser();
}
