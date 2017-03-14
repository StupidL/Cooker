package me.stupidme.cooker.view.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/3/4.
 */

public class RegisterActivity extends AppCompatActivity {

    public static final int RESULT_CODE_REGISTER = 0x02;
    private View mProgressView;
    private View mRegisterFormView;
    private TextInputEditText mNameView;
    private TextInputEditText mPasswordView;
    private TextInputEditText mPasswordRepeatView;

    private RegisterTask mTask = null;

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
        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });

        mProgressView = findViewById(R.id.register_progress);
        mRegisterFormView = findViewById(R.id.register_form);
        mNameView = (TextInputEditText) findViewById(R.id.name);
        mPasswordView = (TextInputEditText) findViewById(R.id.password);
        mPasswordRepeatView = (TextInputEditText) findViewById(R.id.password_repeat);

        Button button = (Button) findViewById(R.id.register_finish);
        button.setOnClickListener(v -> attemptRegister());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setResult(RESULT_OK);
    }

    private void attemptRegister() {
        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        String password2 = mPasswordRepeatView.getText().toString();
        View focusView = null;
        boolean cancel = false;
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password2)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordRepeatView;
            cancel = true;
        }

        if (!TextUtils.equals(password, password2)) {
            mPasswordRepeatView.setError(getString(R.string.error_password_different));
            focusView = mPasswordRepeatView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            doRegister(name, password);
        }
    }

    private void doRegister(String name, String password) {
        showProgress(true);
        mTask = new RegisterTask(name, password);
        mTask.execute((Void) null);
    }

    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {

        private String mName;
        private String mPassword;

        RegisterTask(String name, String password) {
            mName = name;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mTask = null;
            showProgress(false);
            if (success) {
                Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CODE_REGISTER);
                RegisterActivity.this.finish();
            } else {
                if (!isCancelled())
                    onCancelled();
                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mTask = null;
            showProgress(false);
        }
    }

}
