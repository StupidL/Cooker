package me.stupidme.cooker.presenter;

/**
 * Created by StupidL on 2017/3/14.
 * <p>
 * A interface to determine what a login presenter should do.
 */

public interface UserLoginPresenter {

    /**
     * A message type that means login success.
     */
    int MESSAGE_WHAT_LOGIN_SUCCESS = 0x01;

    /**
     * A message type that means login failed.
     */
    int MESSAGE_WHAT_LOGIN_FAILED = 0x02;

    /**
     * A message type that means auto login success.
     */
    int MESSAGE_WHAT_LOGIN_AUTO_SUCCESS = 0x03;

    /**
     * A message type that means auto login failed.
     */
    int MESSAGE_WHAT_LOGIN_AUTO_FAILED = 0x04;

    /**
     * login action called when login button clicked on login fragment.
     *
     * @param name     username of account.
     * @param password password of account.
     * @param remember remember account or not.
     */
    void login(String name, String password, boolean remember);

    /**
     * auto login action called when login activity lunched.
     */
    void attemptAutoLogin();
}
