package me.stupidme.cooker.view.login;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import me.stupidme.cooker.R;
import me.stupidme.cooker.view.cooker.CookerActivity;


/**
 * Created by StupidL on 2017/3/4.
 */
public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_REGISTER = 0x01;
    public static final String COOKER_USER_LOGIN = "UserLoginInfo";
    public static final String USER_NAME = "name";
    public static final String USER_PASSWORD = "password";

    private UserLoginTask mAuthTask = null;

    private TextInputEditText mNameView;
    private TextInputEditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    /**
     * 记住密码，默认选择
     */
    private CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();

        //是否是从主界面推出app，如果是，则不自动登陆，否则，自动登录
        boolean isExitApp = intent.getBooleanExtra("isExitApp", false);

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
        mSignInButton.setOnClickListener(view -> attemptLogin(
                mNameView.getText().toString(),
                mPasswordView.getText().toString())
        );

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Button mSignUpButton = (Button) findViewById(R.id.sign_up);
        mSignUpButton.setOnClickListener(v -> attemptRegister());

        mCheckBox = (CheckBox) findViewById(R.id.checkBox);

        if (isExitApp) {
            setNameAndPassword();
        } else {
            attemptAutoLogin();
        }

    }

    /**
     * 设置用户名和密码，从SharedPreference获取数据
     */
    private void setNameAndPassword() {
        SharedPreferences preferences = getSharedPreferences(COOKER_USER_LOGIN, MODE_PRIVATE);
        String name = preferences.getString(USER_NAME, "");
        String password = preferences.getString(USER_PASSWORD, "");
        mNameView.setText(name);
        mPasswordView.setText(password);
    }

    /**
     * 跳转到注册界面
     */
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

    /**
     * 自动登录
     */
    private void attemptAutoLogin() {
        SharedPreferences preferences = getSharedPreferences(COOKER_USER_LOGIN, MODE_PRIVATE);
        String name = preferences.getString(USER_NAME, "");
        String password = preferences.getString(USER_PASSWORD, "");
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            return;
        }

        attemptLogin(name, password);
    }

    /**
     * 登陆
     *
     * @param name     用户名
     * @param password 密码
     */
    private void attemptLogin(String name, String password) {
        if (mAuthTask != null) {
            return;
        }

        mNameView.setError(null);
        mPasswordView.setError(null);

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

    /**
     * 检查用户名是否有效
     *
     * @param name 用户名
     * @return true则有效，否则无效
     */
    private boolean isNameValid(String name) {
        return !name.isEmpty();
    }

    /**
     * 检查密码是否有效
     *
     * @param password 密码
     * @return true则有效，否则无效
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * 保存用户名和密码
     *
     * @param name     用户名
     * @param password 密码
     */
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


    /**
     * 通过异步任务登陆
     */
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
                startActivity(new Intent(LoginActivity.this, CookerActivity.class));
                LoginActivity.this.finish();

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

