package me.stupidme.cooker.mock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.UserBean;

/**
 * Created by StupidL on 2017/5/1.
 */

public class ServerDbManagerImpl implements ServerDbManager {

    private SQLiteDatabase mDatabase;

    private static volatile ServerDbManagerImpl sInstance;

    private static WeakReference<Context> mContextRef;

    private ServerDbManagerImpl() {
        if (mContextRef.get() != null) {
            mDatabase = new ServerDbHelper(mContextRef.get()).getWritableDatabase();
        } else throw new RuntimeException("SQLiteServerManager construct failed. Context is null.");
    }

    public static void init(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    public static ServerDbManagerImpl getInstance() {
        if (sInstance == null) {
            synchronized (ServerDbManagerImpl.class) {
                if (sInstance == null)
                    sInstance = new ServerDbManagerImpl();
            }
        }
        return sInstance;
    }

    @Override
    public UserBean insertUser(UserBean userBean) {
        ContentValues values = createContentValues(userBean);
        long rt = mDatabase.replace(TABLE_NAME_USER_SERVER, null, values);
        if (rt == -1)
            return null;
        return userBean;
    }

    @Override
    public UserBean queryUser(String where, Long equalTo) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_USER_SERVER + " WHERE " + where + "=?",
                new String[]{String.valueOf(equalTo)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        UserBean userBean = createUserBean(cursor);
        cursor.close();
        return userBean;
    }

    @Override
    public UserBean updateUser(UserBean userBean) {
        long r = mDatabase.replace(TABLE_NAME_USER_SERVER, null, createContentValues(userBean));
        if (r == -1) {
            return null;
        }
        return userBean;
    }

    @Override
    public CookerBean insertCooker(CookerBean cooker) {
        long r = mDatabase.replace(TABLE_NAME_COOKER_SERVER, null, createContentValues(cooker));
        if (r == -1)
            return null;
        return cooker;
    }

    @Override
    public List<CookerBean> insertCookers(List<CookerBean> cookers) {
        mDatabase.beginTransaction();
        for (CookerBean cookerBean : cookers) {
            long r = mDatabase.replace(TABLE_NAME_COOKER_SERVER, null, createContentValues(cookerBean));
            if (r == -1) {
                mDatabase.endTransaction();
                return null;
            }
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return cookers;
    }

    @Override
    public CookerBean deleteCooker(Long userId, Long cookerId) {
        CookerBean cookerBean = this.queryCooker(userId, cookerId);
        if (cookerBean == null)
            return null;
        mDatabase.delete(TABLE_NAME_COOKER_SERVER, KEY_USER_ID_SERVER + "=? AND "
                        + KEY_COOKER_ID_SERVER + "=?",
                new String[]{String.valueOf(userId), String.valueOf(cookerId)});

        return cookerBean;
    }

    @Override
    public List<CookerBean> deleteCookers(Long userId) {
        List<CookerBean> cookerBeanList = this.queryCookers(userId);
        if (cookerBeanList == null || cookerBeanList.size() <= 0)
            return null;
        mDatabase.delete(TABLE_NAME_COOKER_SERVER, KEY_USER_ID_SERVER + "=?",
                new String[]{String.valueOf(userId)});
        return cookerBeanList;
    }

    @Override
    public CookerBean queryCooker(Long userId, Long cookerId) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_COOKER_SERVER + " WHERE "
                        + KEY_USER_ID_SERVER + "=? AND " + KEY_COOKER_ID_SERVER + "=?",
                new String[]{String.valueOf(userId), String.valueOf(cookerId)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        CookerBean cookerBean = createCookerBean(cursor);
        cursor.close();
        return cookerBean;
    }

    @Override
    public List<CookerBean> queryCookers(Long userId) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_COOKER_SERVER + " WHERE " + KEY_USER_ID_SERVER + "=?",
                new String[]{String.valueOf(userId)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        List<CookerBean> list = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            list.add(createCookerBean(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @Override
    public CookerBean updateCooker(CookerBean cooker) {
        long r = mDatabase.replace(TABLE_NAME_COOKER_SERVER, null, createContentValues(cooker));
        if (r == -1)
            return null;
        return cooker;
    }

    @Override
    public List<CookerBean> updateCookers(List<CookerBean> cookers) {
        mDatabase.beginTransaction();
        for (CookerBean cookerBean : cookers) {
            long r = mDatabase.replace(TABLE_NAME_COOKER_SERVER, null, createContentValues(cookerBean));
            if (r == -1) {
                mDatabase.endTransaction();
                return null;
            }
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return cookers;
    }

    @Override
    public BookBean insertBook(BookBean book) {
        long r = mDatabase.replace(TABLE_NAME_BOOK_SERVER, null, createContentValues(book));
        if (r == -1)
            return null;
        return book;
    }

    @Override
    public List<BookBean> insertBooks(List<BookBean> books) {
        mDatabase.beginTransaction();
        for (BookBean bookBean : books) {
            long r = mDatabase.replace(TABLE_NAME_BOOK_SERVER, null, createContentValues(bookBean));
            if (r == -1) {
                mDatabase.endTransaction();
                return null;
            }
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return books;
    }

    @Override
    public BookBean deleteBook(Long userId, Long bookId) {
        BookBean bookBean = this.queryBook(userId, bookId);
        if (bookBean == null)
            return null;
        mDatabase.delete(TABLE_NAME_BOOK_SERVER, KEY_USER_ID_SERVER + "=? AND " + KEY_BOOK_ID_SERVER + "=?",
                new String[]{String.valueOf(userId), String.valueOf(bookId)});
        return bookBean;
    }

    @Override
    public List<BookBean> deleteBooks(Long userId) {
        List<BookBean> list = this.queryBooks(userId);
        if (list == null || list.size() <= 0)
            return null;
        mDatabase.delete(TABLE_NAME_BOOK_SERVER, KEY_USER_ID_SERVER + "?=", new String[]{String.valueOf(userId)});
        return list;
    }

    @Override
    public BookBean queryBook(Long userId, Long bookId) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_BOOK_SERVER + " WHERE " + KEY_USER_ID_SERVER + "=? AND "
                + KEY_BOOK_ID_SERVER + "=?", new String[]{String.valueOf(userId), String.valueOf(bookId)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return null;
        }
        BookBean bookBean = createBookBean(cursor);
        cursor.close();
        return bookBean;
    }

    @Override
    public List<BookBean> queryBooks(Long userId) {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_BOOK_SERVER + " WHERE " + KEY_USER_ID_SERVER + "=?",
                new String[]{String.valueOf(userId)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        List<BookBean> list = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            list.add(createBookBean(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @Override
    public BookBean updateBook(BookBean book) {
        long r = mDatabase.replace(TABLE_NAME_BOOK_SERVER, null, createContentValues(book));
        if (r == -1)
            return null;
        return book;
    }

    @Override
    public List<BookBean> updateBooks(List<BookBean> books) {
        mDatabase.beginTransaction();
        for (BookBean bookBean : books) {
            long r = mDatabase.replace(TABLE_NAME_BOOK_SERVER, null, createContentValues(bookBean));
            if (r == -1) {
                mDatabase.endTransaction();
                return null;
            }
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
        return books;
    }

    private ContentValues createContentValues(UserBean userBean) {
        ContentValues values = new ContentValues(3);
        values.put(KEY_TABLE_USER_ID, userBean.getUserId());
        values.put(KEY_TABLE_USER_NAME, userBean.getUserName());
        values.put(KEY_TABLE_USER_PASSWORD, userBean.getPassword());
        return values;
    }

    private UserBean createUserBean(Cursor cursor) {
        UserBean userBean = new UserBean();
        userBean.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_TABLE_USER_ID)));
        userBean.setUserName(cursor.getString(cursor.getColumnIndex(KEY_TABLE_USER_NAME)));
        userBean.setPassword(cursor.getString(cursor.getColumnIndex(KEY_TABLE_USER_PASSWORD)));
        return userBean;
    }

    private ContentValues createContentValues(CookerBean cookerBean) {
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID_SERVER, cookerBean.getUserId());
        values.put(KEY_COOKER_ID_SERVER, cookerBean.getCookerId());
        values.put(KEY_COOKER_NAME_SERVER, cookerBean.getCookerId());
        values.put(KEY_COOKER_LOCATION_SERVER, cookerBean.getCookerLocation());
        values.put(KEY_COOKER_STATUS_SERVER, cookerBean.getCookerStatus());
        return values;
    }

    private CookerBean createCookerBean(Cursor cursor) {
        CookerBean cookerBean = new CookerBean();
        cookerBean.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_USER_ID_SERVER)));
        cookerBean.setCookerId(cursor.getLong(cursor.getColumnIndex(KEY_COOKER_ID_SERVER)));
        cookerBean.setCookerName(cursor.getString(cursor.getColumnIndex(KEY_COOKER_NAME_SERVER)));
        cookerBean.setCookerLocation(cursor.getString(cursor.getColumnIndex(KEY_COOKER_LOCATION_SERVER)));
        cookerBean.setCookerStatus(cursor.getString(cursor.getColumnIndex(KEY_COOKER_STATUS_SERVER)));
        return cookerBean;
    }

    private ContentValues createContentValues(BookBean bookBean) {
        ContentValues values = new ContentValues(16);
        values.put(KEY_USER_ID_SERVER, bookBean.getUserId());
        values.put(KEY_BOOK_ID_SERVER, bookBean.getBookId());
        values.put(KEY_COOKER_ID_SERVER, bookBean.getCookerId());
        values.put(KEY_COOKER_NAME_SERVER, bookBean.getCookerName());
        values.put(KEY_COOKER_LOCATION_SERVER, bookBean.getCookerLocation());
        values.put(KEY_COOKER_STATUS_SERVER, bookBean.getCookerStatus());
        values.put(KEY_BOOK_PEOPLE_COUNT_SERVER, bookBean.getPeopleCount());
        values.put(KEY_BOOK_RICE_WEIGHT_SERVER, bookBean.getRiceWeight());
        values.put(KEY_BOOK_TASTE_SERVER, bookBean.getTaste());
        values.put(KEY_BOOK_TIME_SERVER, bookBean.getTime());
        return values;
    }

    private BookBean createBookBean(Cursor cursor) {
        BookBean book = new BookBean();
        cursor.moveToFirst();
        book.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_USER_ID_SERVER)));
        book.setBookId(cursor.getLong(cursor.getColumnIndex(KEY_BOOK_ID_SERVER)));
        book.setCookerId(cursor.getLong(cursor.getColumnIndex(KEY_COOKER_ID_SERVER)));
        book.setCookerName(cursor.getString(cursor.getColumnIndex(KEY_COOKER_NAME_SERVER)));
        book.setCookerLocation(cursor.getString(cursor.getColumnIndex(KEY_COOKER_LOCATION_SERVER)));
        book.setCookerStatus(cursor.getString(cursor.getColumnIndex(KEY_COOKER_STATUS_SERVER)));
        book.setRiceWeight(cursor.getInt(cursor.getColumnIndex(KEY_BOOK_RICE_WEIGHT_SERVER)));
        book.setPeopleCount(cursor.getInt(cursor.getColumnIndex(KEY_BOOK_PEOPLE_COUNT_SERVER)));
        book.setTaste(cursor.getString(cursor.getColumnIndex(KEY_BOOK_TASTE_SERVER)));
        book.setTime(cursor.getLong(cursor.getColumnIndex(KEY_BOOK_TIME_SERVER)));
        return book;
    }

}
