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
import me.stupidme.cooker.presenter.UserRegisterPresenter;
import me.stupidme.cooker.presenter.UserRegisterMockPresenterImpl;
import me.stupidme.cooker.util.SharedPreferenceUtil;

/**
 * Created by StupidL on 2017/3/14.
 */

public class RegisterFragment extends Fragment implements RegisterView {

    private UserRegisterPresenter mPresenter;

    private ScrollView mRegisterFormView;

    private TextInputEditText mNameEditText;

    private TextInputEditText mPasswordEditText;

    private TextInputEditText mPasswordRepeatEditText;

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
            showProgress(true);
            String name = mNameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            String password2 = mPasswordRepeatEditText.getText().toString();
            if (isNameAndPasswordValid(name, password, password2)) {
                mPresenter.register(name, password);
            }
        });
        return view;
    }

    /**
     * 弹出提示消息
     *
     * @param message 要展示的信息
     */
    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 控制进度条的展示与否
     *
     * @param show true则显示，否则不显示
     */
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

    /**
     * 登陆成功回调，回到登录界面
     */
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
     * 隐藏输入法
     */
    private void hideSoftInputMethod() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mRegisterFormView.getWindowToken(), 0);
    }

    /**
     * 判断用户名和密码是否有效
     *
     * @param name      用户名
     * @param password  密码
     * @param password2 重复密码
     * @return true则有效，否则无效
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
     * 判断用户名是否有效。非空且长度大于4有效
     *
     * @param name 用户名
     * @return true则有效，否则无效
     */
    private boolean isNameValid(String name) {
        return !TextUtils.isEmpty(name) & name.length() > 4;
    }

    /**
     * 判断密码是否有效。非空且长度大于6有效
     *
     * @param password 密码
     * @return true则有效，否则无效
     */
    private boolean isPasswordValid(String password) {
        return !TextUtils.isEmpty(password) & password.length() > 6;
    }

    /**
     * 判断密码是否一致
     *
     * @param password  密码
     * @param password2 重复密码
     * @return true则一致，否则不一致
     */
    private boolean isPasswordsSame(String password, String password2) {
        return TextUtils.equals(password, password2);
    }

}
