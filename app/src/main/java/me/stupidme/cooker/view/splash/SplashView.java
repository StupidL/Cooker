package me.stupidme.cooker.view.splash;

/**
 * Callback interface for {@link SplashActivity} to response auto login action.
 */

public interface SplashView {

    /**
     * Called when auto login failed.
     */
    void onLoginFailed();

    /**
     * Called when auto login success.
     */
    void onLoginSuccess();
}
