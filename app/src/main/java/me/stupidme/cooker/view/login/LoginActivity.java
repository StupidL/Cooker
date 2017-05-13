package me.stupidme.cooker.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.stupidme.cooker.R;


/**
 * Created by StupidL on 2017/3/4.
 */
public class LoginActivity extends AppCompatActivity {

    private LoginFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        processIntentActions(getIntent());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mFragment)
                .commit();
    }

    private void processIntentActions(Intent intent) {
        switch (intent.getAction()) {
            case Constants.ACTION_REGISTER_SUCCESS:
                mFragment = LoginFragment.newInstance(true);
                break;

            case Constants.ACTION_EXIT_ACCOUNT:
                mFragment = LoginFragment.newInstance(false);
                break;

            default:
                mFragment = LoginFragment.newInstance(true);
                break;
        }
    }

}

