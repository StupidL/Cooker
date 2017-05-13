package me.stupidme.cooker.presenter;

/**
 * Created by StupidL on 2017/3/14.
 * <p>
 * A interface to determine what register presenter should do.
 */

public interface UserRegisterPresenter {

    /**
     * A message type that means register success.
     */
    int MESSAGE_WHAT_REGISTER_SUCCESS = 0x01;

    /**
     * A message type that means register failed.
     */
    int MESSAGE_WHAT_REGISTER_FAILED = 0x02;

    /**
     * Register action. Called in register fragment when register button clicked.
     *
     * @param name     username that user inputted.
     * @param password password that user inputted.
     */
    void register(String name, String password);
}
