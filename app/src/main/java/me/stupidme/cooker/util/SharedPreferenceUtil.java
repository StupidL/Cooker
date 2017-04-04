package me.stupidme.cooker.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.ref.WeakReference;

/**
 * Created by StupidL on 2017/3/31.
 * <p>
 * 该类负责存取用户的用户名和密码，以及设置界面的所有设置内容。
 * 配置文件SharedPreference所用的Context是ApplicationContext。
 */

public class SharedPreferenceUtil {

    private static SharedPreferences mSharedPreference;

    public static final String KEY_USER_NAME = "account_user_name";

    public static final String KEY_USER_PASSWORD = "account_user_password";

    public static final String KEY_USER_ID = "account_user_id";

    public static void init(Context context) {
        WeakReference<Context> mContextRef = new WeakReference<>(context);
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(mContextRef.get());
    }

    public static SharedPreferences getSharedPreference() {
        return mSharedPreference;
    }

    public static void setPreferenceUserName(String name) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString("account_user_name", name);
        editor.apply();
    }

    public static void setPreferenceUserPassword(String password) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString("account_user_password", password);
        editor.apply();
    }

    public static String getString(String key, String defaultValue) {
        return mSharedPreference.getString(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return mSharedPreference.getInt(key, defaultValue);
    }

    public static void putAccountUserName(String name) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public static void putAccountUserPassword(String password) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString(KEY_USER_PASSWORD, password);
        editor.apply();
    }

    public static String getAccountUserName(String defaultValue) {
        return mSharedPreference.getString(KEY_USER_NAME, defaultValue);
    }

    public static String getAccountUserPassword(String defaultValue) {
        return mSharedPreference.getString(KEY_USER_PASSWORD, defaultValue);
    }

    public static void putAccountUserId(Long id) {
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putLong(KEY_USER_ID, id);
        editor.apply();
    }

    public static Long getAccountUserId(Long defaultValue) {
        return mSharedPreference.getLong(KEY_USER_ID, defaultValue);
    }
}
