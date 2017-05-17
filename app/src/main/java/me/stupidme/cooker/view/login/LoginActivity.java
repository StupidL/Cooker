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
        if (intent == null)
            return;

        String action = intent.getAction();
        if (Constants.ACTION_REGISTER_SUCCESS.equals(action)) {
            mFragment = LoginFragment.newInstance(true);
        } else if (Constants.ACTION_EXIT_ACCOUNT.equals(action)) {
            mFragment = LoginFragment.newInstance(false);
        } else {
            mFragment = LoginFragment.newInstance(true);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

}

