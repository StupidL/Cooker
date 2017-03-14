package me.stupidme.cooker.presenter;

import me.stupidme.cooker.model.IUserModel;
import me.stupidme.cooker.model.UserModel;
import me.stupidme.cooker.view.login.IRegisterView;

/**
 * Created by StupidL on 2017/3/14.
 */

public class UserRegisterPresenter implements IUserRegisterPresenter {

    private IUserModel mModel;

    private IRegisterView mView;

    public UserRegisterPresenter(IRegisterView view){
        mView = view;
        mModel = UserModel.getInstance();
    }

    @Override
    public void register(String name, String password) {

    }
}
