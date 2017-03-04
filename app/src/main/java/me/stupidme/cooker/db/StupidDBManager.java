package me.stupidme.cooker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.lang.ref.WeakReference;

/**
 * Created by StupidL on 2017/3/4.
 */

public class StupidDBManager {

    private static StupidDBManager sInstance;

    private static WeakReference<Context> mContextWeakRef;

    private SQLiteDatabase mDataBase;

    public static void init(Context context) {
        mContextWeakRef = new WeakReference<>(context);
    }

    private StupidDBManager() {
        if (mContextWeakRef.get() != null) {
            mDataBase = new StupidDBHelper(mContextWeakRef.get()).getWritableDatabase();
        } else {
            throw new NullPointerException("Context is null. Create Database failed.");
        }
    }

    /**
     * Singleton pattern
     *
     * @return instance of StupidDBManager
     */
    public static StupidDBManager getInstance() {
        if (sInstance == null) {
            synchronized (StupidDBManager.class) {
                if (sInstance == null)
                    sInstance = new StupidDBManager();
            }
        }
        return sInstance;
    }

}
