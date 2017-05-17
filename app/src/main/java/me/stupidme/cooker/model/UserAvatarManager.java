package me.stupidme.cooker.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by stupidl on 17-5-17.
 */

public class UserAvatarManager {

    private static final String KEY_USER_AVATAR = "user_avatar";

    private volatile static UserAvatarManager sInstance;
    private static WeakReference<Context> mContextRef;

    private UserAvatarManager() {

    }

    public static void init(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    public static UserAvatarManager getInstance() {
        if (sInstance == null) {
            synchronized (UserAvatarManager.class) {
                if (sInstance == null)
                    sInstance = new UserAvatarManager();
            }
        }
        return sInstance;
    }

    public void putAvatarPath(Long userId, String path) {
        SharedPreferences preferences = findUserSharedPreference(userId);
        preferences.edit().putString(KEY_USER_AVATAR, path).apply();
    }

    public String getAvatarPath(Long userId) {
        Log.v("UserAvatarManager", "userId: " + userId);
        return findUserSharedPreference(userId).getString(KEY_USER_AVATAR, "");
    }

    private SharedPreferences findUserSharedPreference(Long userId) {
        if (mContextRef.get() == null)
            throw new RuntimeException("Cannot open or create shared preference file, context is null.");
        return mContextRef.get().getSharedPreferences(Math.abs(userId) + "_avatar", Context.MODE_PRIVATE);
    }
}
