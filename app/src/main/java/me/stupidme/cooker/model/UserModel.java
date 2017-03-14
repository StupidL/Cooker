package me.stupidme.cooker.model;

import android.content.SharedPreferences;

/**
 * Created by StupidL on 2017/3/14.
 */

public class UserModel implements IUserModel {

    private static UserModel sInstance;

    private UserModel() {

    }

    public static UserModel getInstance() {
        if (sInstance == null) {
            synchronized (UserModel.class) {
                if (sInstance == null)
                    sInstance = new UserModel();
            }
        }
        return sInstance;
    }

    @Override
    public void saveUser(UserBean user) {

    }

    @Override
    public UserBean getUser() {
        return null;
    }
}
