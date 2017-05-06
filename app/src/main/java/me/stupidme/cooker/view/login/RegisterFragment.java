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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.UserBean;
import me.stupidme.cooker.presenter.UserRegisterMockPresenterImpl;
import me.stupidme.cooker.presenter.UserRegisterPresenter;
import me.stupidme.cooker.util.SharedPreferenceUtil;

/**
 * Created by StupidL on 2017/3/14.
 */

public class RegisterFragment extends Fragment implements RegisterView {

    /**
     * A presenter to process all login logic, and transfer results to fragment through callback methods.
     */
    private UserRegisterPresenter mPresenter;

    /**
     * A view group contains two exit text for better UI affect especially when the device' screen
     * too small to show input keyboard and edit text controls together.
     */
    private ScrollView mRegisterFormView;

    /**
     * An EditText control for user input username to login's needs.
     */
    private TextInputEditText mNameEditText;

    /**
     * An EditText control for user input password to login's needs.
     */
    private TextInputEditText mPasswordEditText;

    /**
     * An EditText control for user input repeat password to login's needs.
     */
    private TextInputEditText mPasswordRepeatEditText;

    /**
     * Progressbar to show when login button clicked to make a better use experience.
     */
    private ProgressBar mProgressBar;

    public RegisterFragment() {

    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPresenter = new UserRegisterPresenter(this);
        mPresenter = new UserRegisterMockPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mRegisterFormView = (ScrollView) view.findViewById(R.id.register_form);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        mNameEditText = (TextInputEditText) view.findViewById(R.id.name);
        mPasswordEditText = (TextInputEditText) view.findViewById(R.id.password);
        mPasswordRepeatEditText = (TextInputEditText) view.findViewById(R.id.password_repeat);
        Button mRegisterButton = (Button) view.findViewById(R.id.register_finish);

        mRegisterButton.setOnClickListener(v -> {
            hideSoftInputMethod();
            String name = mNameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            String password2 = mPasswordRepeatEditText.getText().toString();
            if (isNameAndPasswordValid(name, password, password2)) {
                mPresenter.register(name, password);
            }
        });
        return view;
    }

    @Override
    public void showMessage(int what, CharSequence message) {
        switch (what) {
            case UserRegisterPresenter.MESSAGE_WHAT_REGISTER_FAILED:
                showToastShort(getString(R.string.fragment_register_failed));
                break;
            case UserRegisterPresenter.MESSAGE_WHAT_REGISTER_SUCCESS:

                break;
        }
    }

    @Override
    public void showProgress(boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

    @Override
    public void registerSuccess(UserBean user) {
        SharedPreferenceUtil.putAccountUserName(user.getUserName());
        SharedPreferenceUtil.putAccountUserPassword(user.getPassword());
        SharedPreferenceUtil.putAccountUserId(user.getUserId());

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setAction(Constants.ACTION_REGISTER_SUCCESS);
        startActivity(intent);
        getActivity().finish();
    }

    /**
     * hide soft input method when register button clicked and trying to register account.
     */
    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mRegisterFormView.getWindowToken(), 0);
    }

    /**
     * check username and password is valid or not.
     *
     * @param name     username user inputted.
     * @param password password user inputted.
     * @return true if valid.
     */
    private boolean isNameAndPasswordValid(String name, String password, String password2) {
        mNameEditText.setError(null);
        mPasswordEditText.setError(null);
        mPasswordRepeatEditText.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (!isNameValid(name)) {
            mNameEditText.setError(getString(R.string.error_invalid_name));
            cancel = true;
            focusView = mNameEditText;
        }

        if (!isPasswordValid(password)) {
            mPasswordEditText.setError(getString(R.string.error_invalid_password));
            cancel = true;
            focusView = mPasswordEditText;
        }

        if (!isPasswordsSame(password, password2)) {
            mPasswordRepeatEditText.setError(getString(R.string.error_password_different));
            cancel = true;
            focusView = mPasswordRepeatEditText;
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
        return !TextUtils.isEmpty(name) & name.length() >= 4;
    }

    /**
     * check password is valid or not. password must not be empty and length should at least 6.
     *
     * @param password password that user inputted.
     * @return true if password is valid.
     */
    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) & password.length() >= 6;
    }

    /**
     * check if the two passwords are the same or not.
     *
     * @param password  password.
     * @param password2 password repeat.
     * @return true if passwords are the same.
     */
    private boolean isPasswordsSame(String password, String password2) {
        return TextUtils.equals(password, password2);
    }

    /**
     * show a toast when received message from presenter in a short time.
     *
     * @param message the message to be toasted.
     */
    private void showToastShort(CharSequence message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
