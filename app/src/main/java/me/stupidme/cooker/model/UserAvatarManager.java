package me.stupidme.cooker.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * This class manage users' avatar by saving image path to shared preference file.
 * Anywhere can use this class to find image path to show.
 */

public class UserAvatarManager {

    /**
     * key of avatar in shared preference
     */
    private static final String KEY_USER_AVATAR = "user_avatar";

    /**
     * instance of UserAvatarManager
     */
    private volatile static UserAvatarManager sInstance;

    /**
     * wek reference of application context
     */
    private static WeakReference<Context> mContextRef;

    private UserAvatarManager() {

    }

    /**
     * inject application context.
     *
     * @param context application context
     */
    public static void init(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    /**
     * get instance of UserAvatarManager.
     *
     * @return instance
     */
    public static UserAvatarManager getInstance() {
        if (sInstance == null) {
            synchronized (UserAvatarManager.class) {
                if (sInstance == null)
                    sInstance = new UserAvatarManager();
            }
        }
        return sInstance;
    }

    /**
     * set avatar path.
     *
     * @param userId user's id
     * @param path   image path
     */
    public void putAvatarPath(Long userId, String path) {
        SharedPreferences preferences = findUserSharedPreference(userId);
        preferences.edit().putString(KEY_USER_AVATAR, path).apply();
    }

    /**
     * get avatar path.
     *
     * @param userId user's id
     * @return image path
     */
    public String getAvatarPath(Long userId) {
        Log.v("UserAvatarManager", "userId: " + userId);
        return findUserSharedPreference(userId).getString(KEY_USER_AVATAR, "");
    }

    /**
     * find shared preference by user's id.
     *
     * @param userId user's id
     * @return shared preference
     */
    private SharedPreferences findUserSharedPreference(Long userId) {
        if (mContextRef.get() == null)
            throw new RuntimeException("Cannot open or create shared preference file, context is null.");
        return mContextRef.get().getSharedPreferences(Math.abs(userId) + "_avatar", Context.MODE_PRIVATE);
    }
}
