package me.stupidme.cooker.view.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/3/4.
 */

public class RegisterActivity extends AppCompatActivity {

    public static final int RESULT_CODE_REGISTER = 0x02;

    @Override
    public void onCreate(Bundle savedInstanceSate) {
        super.onCreate(savedInstanceSate);

        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_register));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, RegisterFragment.newInstance())
                .commit();

    }

}
