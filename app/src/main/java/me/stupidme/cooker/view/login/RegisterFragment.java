package me.stupidme.cooker.view.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/3/14.
 */

public class RegisterFragment extends Fragment implements ILoginAndRegisterView {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
