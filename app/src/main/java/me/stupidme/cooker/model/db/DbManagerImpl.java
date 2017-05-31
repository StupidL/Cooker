package me.stupidme.cooker.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/29.
 */

public class DbManagerImpl implements DbManager {

    private static final String DELETE_COOKER_TABLE = "DELETE FROM cooker";
    private static final String RESET_COOKER_SEQUENCE = "UPDATE sqlite_sequence SET seq = 0 WHERE name = 'cooker'";
    private static final String DELETE_BOOK_TABLE = "DELETE FROM book";
    private static final String RESET_BOOK_SEQUENCE = "UPDATE sqlite_sequence SET seq = 0 WHERE name = 'book'";
    private static final String DELETE_BOOK_HISTORY_TABLE = "DELETE FROM book_history";
    private static final String RESET_BOOK_HISTORY_SEQUENCE = "UPDATE sqlite_sequence SET seq = 0 WHERE name = 'book_history'";

    private volatile static DbManagerImpl sInstance;

    private SQLiteDatabase mWritableDB;

    private SQLiteDatabase mReadableDB;

    private static WeakReference<Context> mContextRef;

    public static void init(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    private DbManagerImpl() {
        if (mContextRef.get() == null)
            throw new RuntimeException("DbManagerImpl construct failed. Context is null.");
        DbHelper helper = new DbHelper(mContextRef.get());
        mWritableDB = helper.getWritableDatabase();
        mReadableDB = helper.getReadableDatabase();
    }

    public static DbManagerImpl getInstance() {
        if (sInstance == null) {
            synchronized (DbManagerImpl.class) {
                if (sInstance == null)
                    sInstance = new DbManagerImpl();
            }
        }
        return sInstance;
    }

    @Override
    public boolean insertCooker(CookerBean cooker) {
        ContentValues values = createContentValues(cooker);
        long rt = mWritableDB.replace(TABLE_NAME_COOKER, null, values);
        return rt != -1;
    }

    @Override
    public boolean insertCookers(List<CookerBean> cookers) {
        mWritableDB.beginTransaction();
        for (CookerBean cooker : cookers) {
            if (!insertCooker(cooker)) {
                mWritableDB.endTransaction();
                return false;
            }
        }
        mWritableDB.setTransactionSuccessful();
        mWritableDB.endTransaction();
        return true;
    }

    @Override
    public boolean deleteCookers(String where, Long equalTo) {
        mWritableDB.execSQL(DELETE_COOKER_TABLE);
        mWritableDB.execSQL(RESET_COOKER_SEQUENCE);
        return true;
    }

    @Override
    public CookerBean queryCooker(String where, Long equalTo) {
        return this.queryCooker(where, String.valueOf(equalTo));
    }

    @Override
    public CookerBean queryCooker(String where, String equalTo) {
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM cooker WHERE " + where + " =?",
                new String[]{equalTo});
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
    public List<CookerBean> queryCookers(String where, Long equalTo) {
        List<CookerBean> list = new ArrayList<>();
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM " + TABLE_NAME_COOKER + " WHERE " + where + " =?",
                new String[]{String.valueOf(equalTo)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return list;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(createCookerBean(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @Override
    public boolean deleteCooker(String where, Long equalTo) {
        int r = mWritableDB.delete(TABLE_NAME_COOKER, where + " =?",
                new String[]{String.valueOf(equalTo)});
        return r != 0;
    }

    @Override
    public boolean updateCooker(CookerBean cooker) {
        ContentValues values = createContentValues(cooker);
        long r = mWritableDB.replace(TABLE_NAME_COOKER, null, values);
        return r != -1;
    }

    @Override
    public boolean updateCookers(List<CookerBean> cookers) {
        mWritableDB.beginTransaction();
        for (CookerBean cooker : cookers) {
            ContentValues values = createContentValues(cooker);
            long r = mWritableDB.replace(TABLE_NAME_COOKER, null, values);
            if (r == -1) {
                mWritableDB.endTransaction();
                return false;
            }
        }
        mWritableDB.setTransactionSuccessful();
        mWritableDB.endTransaction();
        return true;
    }

    @Override
    public boolean insertBook(BookBean book) {
        ContentValues values = createContentValues(book);
        long r = mWritableDB.replace(TABLE_NAME_BOOK, null, values);
        return r != -1;
    }

    @Override
    public boolean insertBooks(List<BookBean> books) {
        mWritableDB.beginTransaction();
        for (BookBean book : books) {
            boolean r = insertBook(book);
            if (!r) {
                mWritableDB.endTransaction();
                return false;
            }
        }
        mWritableDB.setTransactionSuccessful();
        mWritableDB.endTransaction();
        return true;
    }

    @Override
    public boolean deleteBooks(String where, Long equalTo) {
        mWritableDB.execSQL(DELETE_BOOK_TABLE);
        mWritableDB.execSQL(RESET_BOOK_SEQUENCE);
        return true;
    }

    @Override
    public BookBean queryBook(String where, Long equalTo) {
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM " + TABLE_NAME_BOOK + " WHERE " + where + " =?",
                new String[]{String.valueOf(equalTo)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        BookBean bookBean = createBookBean(cursor);
        cursor.close();
        return bookBean;
    }

    @Override
    public List<BookBean> queryBooks(String where, Long equalTo) {
        List<BookBean> list = new ArrayList<>();
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM " + TABLE_NAME_BOOK + " WHERE " + where + " =?",
                new String[]{String.valueOf(equalTo)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return list;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(createBookBean(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @Override
    public boolean deleteBook(String where, Long equalTo) {
        int r = mWritableDB.delete(TABLE_NAME_BOOK, where + "=?", new String[]{String.valueOf(equalTo)});
        return r != 0;
    }

    @Override
    public boolean updateBook(BookBean book) {
        ContentValues values = createContentValues(book);
        long r = mWritableDB.replace(TABLE_NAME_BOOK, null, values);
        return r != -1;
    }

    @Override
    public boolean updateBooks(List<BookBean> books) {
        mWritableDB.beginTransaction();
        for (BookBean book : books) {
            ContentValues values = createContentValues(book);
            long r = mWritableDB.replace(TABLE_NAME_BOOK, null, values);
            if (r == -1) {
                mWritableDB.endTransaction();
                return false;
            }
        }
        mWritableDB.setTransactionSuccessful();
        mWritableDB.endTransaction();
        return true;
    }

    @Override
    public List<String> queryCookerNamesAll(String where, Long equalTo) {
        List<String> list = new ArrayList<>();
        Cursor cursor = mReadableDB.rawQuery("SELECT DISTINCT " + KEY_COOKER_NAME + " FROM " + TABLE_NAME_COOKER + " WHERE " + where + "=?",
                new String[]{String.valueOf(equalTo)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return list;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(KEY_COOKER_NAME)));
            cursor.moveToNext();
        }
        return list;
    }

    @Override
    public boolean insertBookHistory(BookBean book) {
        ContentValues values = createContentValues(book);
        long r = mWritableDB.replace(TABLE_NAME_HISTORY, null, values);
        return r != -1;
    }

    @Override
    public boolean insertBooksHistory(List<BookBean> books) {
        mWritableDB.beginTransaction();
        for (BookBean book : books) {
            boolean r = insertBookHistory(book);
            if (!r) {
                mWritableDB.endTransaction();
                return false;
            }
        }
        mWritableDB.setTransactionSuccessful();
        mWritableDB.endTransaction();
        return true;
    }

    @Override
    public boolean deleteBookHistory(String where, Long equalTo) {
        int r = mWritableDB.delete(TABLE_NAME_HISTORY, where + "=?", new String[]{String.valueOf(equalTo)});
        return r != 0;
    }

    @Override
    public boolean deleteBooksHistory(String where, Long equalTo) {
        mWritableDB.execSQL(DELETE_BOOK_HISTORY_TABLE);
        mWritableDB.execSQL(RESET_BOOK_HISTORY_SEQUENCE);
        return true;
    }

    @Override
    public BookBean queryBookHistory(String where, Long equalTo) {
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM " + TABLE_NAME_HISTORY + " WHERE " + where + " =?",
                new String[]{String.valueOf(equalTo)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        BookBean bookBean = createBookBean(cursor);
        cursor.close();
        return bookBean;
    }

    @Override
    public List<BookBean> queryBooksHistory(String where, Long equalTo) {
        List<BookBean> list = new ArrayList<>();
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM " + TABLE_NAME_HISTORY + " WHERE " + where + " =?",
                new String[]{String.valueOf(equalTo)});
        if (cursor.getCount() <= 0) {
            cursor.close();
            return list;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(createBookBean(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @Override
    public boolean updateBookHistory(BookBean book) {
        ContentValues values = createContentValues(book);
        long r = mWritableDB.replace(TABLE_NAME_HISTORY, null, values);
        return r != -1;
    }

    @Override
    public boolean updateBooksHistory(List<BookBean> books) {
        mWritableDB.beginTransaction();
        for (BookBean book : books) {
            ContentValues values = createContentValues(book);
            long r = mWritableDB.replace(TABLE_NAME_HISTORY, null, values);
            if (r == -1) {
                mWritableDB.endTransaction();
                return false;
            }
        }
        mWritableDB.setTransactionSuccessful();
        mWritableDB.endTransaction();
        return true;
    }

    private ContentValues createContentValues(CookerBean cookerBean) {
        ContentValues values = new ContentValues(8);
        values.put(KEY_USER_ID, cookerBean.getUserId());
        values.put(KEY_COOKER_ID, cookerBean.getCookerId());
        values.put(KEY_COOKER_NAME, cookerBean.getCookerName());
        values.put(KEY_COOKER_LOCATION, cookerBean.getCookerLocation());
        values.put(KEY_COOKER_STATUS, cookerBean.getCookerStatus());
        return values;
    }

    private ContentValues createContentValues(BookBean bookBean) {
        ContentValues values = new ContentValues(16);
        values.put(KEY_USER_ID, bookBean.getUserId());
        values.put(KEY_BOOK_ID, bookBean.getBookId());
        values.put(KEY_COOKER_ID, bookBean.getCookerId());
        values.put(KEY_COOKER_NAME, bookBean.getCookerName());
        values.put(KEY_COOKER_LOCATION, bookBean.getCookerLocation());
        values.put(KEY_COOKER_STATUS, bookBean.getCookerStatus());
        values.put(KEY_BOOK_PEOPLE_COUNT, bookBean.getPeopleCount());
        values.put(KEY_BOOK_RICE_WEIGHT, bookBean.getRiceWeight());
        values.put(KEY_BOOK_TASTE, bookBean.getTaste());
        values.put(KEY_BOOK_TIME, bookBean.getTime());
        return values;
    }

    private BookBean createBookBean(Cursor cursor) {
        BookBean book = new BookBean();
        cursor.moveToLast();
        book.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_USER_ID)));
        book.setBookId(cursor.getLong(cursor.getColumnIndex(KEY_BOOK_ID)));
        book.setCookerId(cursor.getLong(cursor.getColumnIndex(KEY_COOKER_ID)));
        book.setCookerName(cursor.getString(cursor.getColumnIndex(KEY_COOKER_NAME)));
        book.setCookerLocation(cursor.getString(cursor.getColumnIndex(KEY_COOKER_LOCATION)));
        book.setCookerStatus(cursor.getString(cursor.getColumnIndex(KEY_COOKER_STATUS)));
        book.setRiceWeight(cursor.getInt(cursor.getColumnIndex(KEY_BOOK_RICE_WEIGHT)));
        book.setPeopleCount(cursor.getInt(cursor.getColumnIndex(KEY_BOOK_PEOPLE_COUNT)));
        book.setTaste(cursor.getString(cursor.getColumnIndex(KEY_BOOK_TASTE)));
        book.setTime(cursor.getLong(cursor.getColumnIndex(KEY_BOOK_TIME)));
        return book;
    }

    private CookerBean createCookerBean(Cursor cursor) {
        CookerBean cookerBean = new CookerBean();
        cookerBean.setUserId(cursor.getLong(cursor.getColumnIndex(KEY_USER_ID)));
        cookerBean.setCookerId(cursor.getLong(cursor.getColumnIndex(KEY_COOKER_ID)));
        cookerBean.setCookerName(cursor.getString(cursor.getColumnIndex(KEY_COOKER_NAME)));
        cookerBean.setCookerLocation(cursor.getString(cursor.getColumnIndex(KEY_COOKER_LOCATION)));
        cookerBean.setCookerStatus(cursor.getString(cursor.getColumnIndex(KEY_COOKER_STATUS)));
        return cookerBean;
    }
}
