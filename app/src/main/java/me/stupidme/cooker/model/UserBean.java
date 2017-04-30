package me.stupidme.cooker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 该类用来承载用户信息，包括<code>username</code>、<code>password</code>和<code>userId</code>.
 * 在发送登录请求的时候，可以将该类对象作为参数，Retrofit会将该类的所有域映射成对应的POST请求参数。
 * Created by StupidL on 2017/3/8.
 */

public class UserBean extends RealmObject {

    /**
     * 用户名。对应登录/注册POST请求参数<code>username</code>
     */
    private String username;

    /**
     * 密码。对应登录/注册POST请求参数<code>password</code>
     */
    private String password;

    /**
     * 用户ID。当用户注册成功时，服务器会返回该用户独一无二的ID。
     */
    @PrimaryKey
    private Long userId;

    /**
     * 构造器，因为不一定有<code>userId</code>值，所以没有userId参数
     *
     * @param username 用户名
     * @param password 密码
     */
    public UserBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * 默认构造器
     */
    public UserBean() {

    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     * @return 自身引用，支持链式调用
     */
    public UserBean setUserName(String username) {
        this.username = username;
        return this;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUserName() {
        return username;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     * @return 自身引用，支持链式调用
     */
    public UserBean setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置<code>userId</code>
     *
     * @param id 用户ID
     * @return 自身引用，支持链式调用
     */
    public UserBean setUserId(Long id) {
        this.userId = id;
        return this;
    }

    /**
     * 获取用户ID
     *
     * @return 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 转化成字符串，方便打印，便于调试
     *
     * @return 该类的字符串
     */
    @Override
    public String toString() {
        return "username: " + username + " \tpassword: " + password + "\tuserId: " + userId;
    }
}
