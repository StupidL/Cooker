package me.stupidme.cooker.mock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.ref.WeakReference;
import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.UserBean;

/**
 * Created by StupidL on 2017/5/1.
 */

public class SQLiteServerManager implements IServerDbManager {

    private SQLiteDatabase mDatabase;

    private static volatile SQLiteServerManager sInstance;

    private static WeakReference<Context> mContextRef;

    private SQLiteServerManager() {
        if (mContextRef.get() != null) {
            mDatabase = new ServerDbHelper(mContextRef.get()).getWritableDatabase();
        } else throw new RuntimeException("SQLiteServerManager construct failed. Context is null.");
    }

    public static void init(Context context) {
        mContextRef = new WeakReference<Context>(context);
    }

    public static SQLiteServerManager getInstance() {
        if (sInstance == null) {
            synchronized (SQLiteServerManager.class) {
                if (sInstance == null)
                    sInstance = new SQLiteServerManager();
            }
        }
        return sInstance;
    }

    @Override
    public UserBean insertUser(UserBean userBean) {
        ContentValues values = createContentValues(userBean);
        long rt = mDatabase.insert("cooker_user", null, values);
        if (rt == -1)
            return null;
        return userBean;
    }

    @Override
    public UserBean queryUser(Long userId) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM cooker_user WHERE userId =?", new String[]{String.valueOf(userId)});
        if (cursor.getCount() <= 0)
            return null;
        return createUserBean(cursor);
    }

    @Override
    public CookerBean insertCooker(CookerBean cooker) {
        return null;
    }

    @Override
    public List<CookerBean> insertCookers(List<CookerBean> cookers) {
        return null;
    }

    @Override
    public CookerBean deleteCooker(Long cookerId) {
        return null;
    }

    @Override
    public List<CookerBean> deleteCookers(Long userId) {
        return null;
    }

    @Override
    public CookerBean queryCooker(Long userId, Long cookerId) {
        return null;
    }

    @Override
    public List<CookerBean> queryCookers(Long userId) {
        return null;
    }

    @Override
    public CookerBean updateCooker(CookerBean cooker) {
        return null;
    }

    @Override
    public List<CookerBean> updateCookers(List<CookerBean> cookers) {
        return null;
    }

    @Override
    public BookBean insertBook(BookBean book) {
        return null;
    }

    @Override
    public List<BookBean> insertBooks(List<BookBean> books) {
        return null;
    }

    @Override
    public BookBean deleteBook(Long bookId) {
        return null;
    }

    @Override
    public List<BookBean> deleteBooks(Long userId) {
        return null;
    }

    @Override
    public BookBean queryBook(Long bookId) {
        return null;
    }

    @Override
    public List<BookBean> queryBooks(Long userId) {
        return null;
    }

    @Override
    public BookBean updateBook(BookBean book) {
        return null;
    }

    @Override
    public List<BookBean> updateBooks(List<BookBean> books) {
        return null;
    }

    private ContentValues createContentValues(UserBean userBean) {
        ContentValues values = new ContentValues(3);
        values.put("userId", userBean.getUserId());
        values.put("userName", userBean.getUserName());
        values.put("userPassword", userBean.getPassword());
        return values;
    }

    private UserBean createUserBean(Cursor cursor) {
        UserBean userBean = new UserBean();
        userBean.setUserId(cursor.getLong(cursor.getColumnIndex("userId")));
        userBean.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
        userBean.setPassword(cursor.getString(cursor.getColumnIndex("userPassword")));
        return userBean;
    }
}
