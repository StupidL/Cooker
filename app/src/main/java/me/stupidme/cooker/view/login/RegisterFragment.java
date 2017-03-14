package me.stupidme.cooker.view.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.stupidme.cooker.R;
import me.stupidme.cooker.presenter.IUserRegisterPresenter;
import me.stupidme.cooker.presenter.UserRegisterPresenter;

/**
 * Created by StupidL on 2017/3/14.
 */

public class RegisterFragment extends Fragment implements IRegisterView {

    private IUserRegisterPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new UserRegisterPresenter(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        return view;
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showProgress(boolean show) {

    }
}
