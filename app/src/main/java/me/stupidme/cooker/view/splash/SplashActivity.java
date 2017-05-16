package me.stupidme.cooker.view.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.stupidme.cooker.presenter.SplashMockPresenterImpl;
import me.stupidme.cooker.presenter.SplashPresenter;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.cooker.CookerActivity;
import me.stupidme.cooker.view.login.LoginActivity;

public class SplashActivity extends AppCompatActivity implements SplashView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
