package me.stupidme.cooker.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import me.stupidme.cooker.R;


/**
 * Created by StupidL on 2017/3/4.
 */
public class LoginActivity extends AppCompatActivity {


    private static final int REQUEST_REGISTER = 0x01;
    private static final String COOKER_USER_LOGIN = "UserLoginInfo";
    private static final String USER_NAME = "name";
    private static final String USER_PASSWORD = "password";

    private UserLoginTask mAuthTask = null;

    private TextInputEditText mNameView;
    private TextInputEditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private CheckBox mCheckBox;

    private boolean isExitApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        isExitApp = intent.getBooleanExtra("isExitApp", false);

        mNameView = (TextInputEditText) findViewById(R.id.name);
        mPasswordView = (TextInputEditText) findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(mNameView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button mSignUpButton = (Button) findViewById(R.id.sign_up);
        mSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        mCheckBox = (CheckBox) findViewById(R.id.checkBox);

        if (!isExitApp)
            attemptAutoLogin();
    }

    private void attemptRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(intent, REQUEST_REGISTER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REGISTER) {
            if (resultCode == RegisterActivity.RESULT_CODE_REGISTER) {
                Toast.makeText(this, getString(R.string.toast_register_success), Toast.LENGTH_LONG).show();
            }
        }
        Log.i("LoginActivity", "result code: " + resultCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAuthTask != null) {
            mAuthTask.onCancelled();
        }
    }

    private void attemptAutoLogin() {
        SharedPreferences preferences = getSharedPreferences(COOKER_USER_LOGIN, MODE_PRIVATE);
        String name = preferences.getString(USER_NAME, "");
        String password = preferences.getString(USER_PASSWORD, "");
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            return;
        }

        attemptLogin(name, password);
    }

    private void attemptLogin(String name, String password) {
        if (mAuthTask != null) {
            return;
        }

        mNameView.setError(null);
        mPasswordView.setError(null);
//
//        String name = mNameView.getText().toString();
//        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

//        if (TextUtils.isEmpty(name)) {
//            mNameView.setError(getString(R.string.error_field_required));
//            focusView = mNameView;
//            cancel = true;
//        } else
        if (!isNameValid(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            if (mCheckBox.isChecked())
                savePassword(name, password);
            mAuthTask = new UserLoginTask(name, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isNameValid(String name) {
        return !name.isEmpty();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void savePassword(String name, String password) {
        SharedPreferences preferences = getSharedPreferences(COOKER_USER_LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_NAME, name);
        editor.putString(USER_PASSWORD, password);
        editor.apply();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mName;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mName = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();

            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

