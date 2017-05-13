package me.stupidme.cooker.view.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
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
import me.stupidme.cooker.presenter.UserLoginMockPresenterImpl;
import me.stupidme.cooker.presenter.UserLoginPresenter;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.cooker.CookerActivity;

/**
 * Created by StupidL on 2017/3/14.
 * <p>
 * UI fragment to react with users to perform login or register actions.
 */

public class LoginFragment extends Fragment implements LoginView {
    //debug
    private static final String TAG = "LoginFragment";

    /**
     * A presenter to process all login logic, and transfer results to fragment through callback methods.
     */
    private UserLoginPresenter mPresenter;

    /**
     * Progressbar to show when login button clicked to make a better use experience.
     */
    private ProgressBar mProgressBar;

    /**
     * An EditText control for user input username to login's needs.
     */
    private TextInputEditText mNameEditText;

    /**
     * An EditText control for user input password to login's needs.
     */
    private TextInputEditText mPasswordEditText;

    /**
     * A checkbox that decides remember username and password or not when login.
     */
    private CheckBox mCheckBox;

    /**
     * A view group contains two exit text for better UI affect especially when the device' screen
     * too small to show input keyboard and edit text controls together.
     */
    private ScrollView mLoginFormView;

    /**
     * A remark to decide auto login or not when login activity lunched.
     */
    private boolean autoLogin = false;

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
//        mPresenter = new UserLoginPresenter(this);
        mPresenter = new UserLoginMockPresenterImpl(this);
        Bundle bundle = getArguments();
        autoLogin = bundle.getBoolean(AUTO_LOGIN);

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

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        if (autoLogin) {
            showProgress(true);
            mPresenter.attemptAutoLogin();
        }
    }

    @Override
    public void rememberUser(UserBean user) {
        SharedPreferenceUtil.putAccountUserName(user.getUserName());
        SharedPreferenceUtil.putAccountUserPassword(user.getPassword());
        SharedPreferenceUtil.putAccountUserId(user.getUserId());
    }

    @Override
    public void loginSuccess() {
        Intent intent = new Intent(getActivity(), CookerActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * hide the soft input method when login button clicked.
     */
    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mLoginFormView.getWindowToken(), 0);
    }

    /**
     * check username and password is valid or not.
     *
     * @param name     username user inputted.
     * @param password password user inputted.
     * @return true if valid.
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
     * check username is valid or not. username must not be empty and length should at least 4.
     *
     * @param name username that user inputted.
     * @return true if username is valid.
     */
    private boolean isNameValid(String name) {
        return !TextUtils.isEmpty(name) && name.length() >= 4;
    }

    /**
     * check password is valid or not. password must not be empty and length should at least 6.
     *
     * @param password password that user inputted.
     * @return true if password is valid.
     */
    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    @Override
    public void showMessage(int what, String message) {
        switch (what) {
            case UserLoginPresenter.MESSAGE_WHAT_LOGIN_FAILED:
                showToastShort(getActivity().getString(R.string.fragment_login_failed));
                break;
            case UserLoginPresenter.MESSAGE_WHAT_LOGIN_AUTO_FAILED:

                break;
            case UserLoginPresenter.MESSAGE_WHAT_LOGIN_SUCCESS:

                break;
            case UserLoginPresenter.MESSAGE_WHAT_LOGIN_AUTO_SUCCESS:

                break;
        }
    }

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

    /**
     * show a toast_backgroud when received message from presenter in a short time.
     *
     * @param message the message to be toasted.
     */
    private void showToastShort(CharSequence message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
