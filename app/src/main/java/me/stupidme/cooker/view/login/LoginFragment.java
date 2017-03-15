package me.stupidme.cooker.view.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.UserBean;
import me.stupidme.cooker.presenter.IUserLoginPresenter;
import me.stupidme.cooker.presenter.UserLoginPresenter;
import me.stupidme.cooker.view.cooker.CookerActivity;

import static me.stupidme.cooker.view.login.Constants.USER_NAME;
import static me.stupidme.cooker.view.login.Constants.USER_PASSWORD;

/**
 * Created by StupidL on 2017/3/14.
 */

public class LoginFragment extends Fragment implements ILoginView {

    private static final String TAG = "LoginFragment";

    private IUserLoginPresenter mPresenter;

    private ProgressBar mProgressBar;

    private TextInputEditText mNameEditText;

    private TextInputEditText mPasswordEditText;

    private CheckBox mCheckBox;

    private ScrollView mLoginFormView;

    private static final String AUTO_LOGIN = "autoLogin";

    public LoginFragment() {

    }

    public static LoginFragment newInstance(boolean autoLogin) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBoolean(AUTO_LOGIN, autoLogin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserLoginPresenter(this);

        Bundle bundle = getArguments();
        boolean autoLogin = bundle.getBoolean(AUTO_LOGIN);

        if (autoLogin)
            mPresenter.attemptAutoLogin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginFormView = (ScrollView) view.findViewById(R.id.login_form);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mCheckBox = (CheckBox) view.findViewById(R.id.checkBox);
        mNameEditText = (TextInputEditText) view.findViewById(R.id.name);
        mPasswordEditText = (TextInputEditText) view.findViewById(R.id.password);
        Button mLoginButton = (Button) view.findViewById(R.id.login);
        Button mRegisterButton = (Button) view.findViewById(R.id.register);

        mLoginButton.setOnClickListener(v -> {
            hideSoftInputMethod();

            String name = mNameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();

            if (isNameAndPasswordValid(name, password))
                mPresenter.login(name, password, mCheckBox.isChecked());

        });

        mRegisterButton.setOnClickListener(v -> {
            hideSoftInputMethod();

            Intent intent = new Intent(getActivity(), RegisterActivity.class);
            startActivity(intent);
        });

        return view;
    }

    /**
     * 记住密码
     *
     * @param user 用户
     */
    @Override
    public void rememberUser(UserBean user) {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.COOKER_USER_LOGIN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_NAME, user.getName());
        editor.putString(USER_PASSWORD, user.getPassword());
        editor.apply();
    }

    /**
     * 自动登录的时候调用
     *
     * @return 用户信息
     */
    @Override
    public UserBean getUserInfo() {
        SharedPreferences preferences = getActivity().getSharedPreferences(Constants.COOKER_USER_LOGIN, Context.MODE_PRIVATE);
        String name = preferences.getString(USER_NAME, "");
        String password = preferences.getString(USER_PASSWORD, "");
        if (isNameAndPasswordValid(name, password))
            return new UserBean(name, password);
        return null;
    }

    /**
     * 登陆成功时回调，进入主界面
     */
    @Override
    public void loginSuccess() {
        Intent intent = new Intent(getActivity(), CookerActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * 隐藏输入法
     */
    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLoginFormView.getWindowToken(), 0);
    }

    /**
     * 判断账号信息是否有效
     *
     * @param name     用户名
     * @param password 密码
     * @return true则有效，否则无效
     */
    private boolean isNameAndPasswordValid(String name, String password) {
        mNameEditText.setError(null);
        mPasswordEditText.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (!isNameValid(name)) {
            mNameEditText.setError(getString(R.string.error_invalid_name));
            focusView = mNameEditText;
            cancel = true;
        }

        if (!isPasswordValid(password)) {
            mPasswordEditText.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * 判断用户名是否有效
     *
     * @param name 用户名
     * @return true则有效，否则无效
     */
    private boolean isNameValid(String name) {
        return !TextUtils.isEmpty(name) && name.length() >= 4;
    }

    /**
     * 判断密码是否有效
     *
     * @param password 密码
     * @return true则有效，否则无效
     */
    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    /**
     * 弹出提示信息
     *
     * @param message 要展示的信息
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 控制进度条的展示
     *
     * @param show true则显示，否则不显示
     */
    @Override
    public void showProgress(boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressBar.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

}
