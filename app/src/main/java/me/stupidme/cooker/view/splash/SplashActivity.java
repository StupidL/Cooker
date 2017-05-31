package me.stupidme.cooker.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.stupidme.cooker.presenter.SplashMockPresenterImpl;
import me.stupidme.cooker.presenter.SplashPresenter;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.cooker.CookerActivity;
import me.stupidme.cooker.view.login.LoginActivity;

/**
 * A splash screen to show when first launch application.
 * {@link SplashPresenter} will attempt to auto login when splashing.
 * If auto login failed, this activity intents to {@link LoginActivity},
 * else intents to the main page {@link CookerActivity}.
 */
public class SplashActivity extends AppCompatActivity implements SplashView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get login info from shared preference file and attempt to login
        Long userId = SharedPreferenceUtil.getAccountUserId(0L);
        String username = SharedPreferenceUtil.getAccountUserName("");
        String password = SharedPreferenceUtil.getAccountUserPassword("");
        if (userId == 0 || username.equals("") || password.equals("")) {
            this.onLoginFailed();
            return;
        }

        SplashPresenter presenter = new SplashMockPresenterImpl(this);
        presenter.attemptAutoLogin(userId, username, password);

    }

    @Override
    public void onLoginFailed() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onLoginSuccess() {
        startActivity(new Intent(SplashActivity.this, CookerActivity.class));
        finish();
    }
}
