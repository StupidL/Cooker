package me.stupidme.cooker.view.login;

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
}
