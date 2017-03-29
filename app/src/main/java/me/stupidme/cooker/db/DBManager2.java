package me.stupidme.cooker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/29.
 */

public class DBManager2 implements IDBManager2 {

    private static final String DELETE_COOKER_TABLE = "DELETE FROM cooker";
    private static final String RESET_COOKER_SEQUENCE = "UPDATE sqlite_sequence SET seq = 0 WHERE name = 'cooker'";
    private static final String DELETE_BOOK_TABLE = "DELETE FROM book";
    private static final String RESET_BOOK_SEQUENCE = "UPDATE sqlite_sequence SET seq = 0 WHERE name = 'book'";

    private static final String PREFIX_INSERT_BOOK = "REPLACE INTO book(bookId,cookerId," +
            "cookerName,cookerLocation,cookerStatus,riceWeight,peopleCount,taste,time) VALUES(";

    private static final String PREFIX_INSERT_COOKER = "REPLACE INTO cooker(cookerId," +
            "cookerName,cookerLocation,cookerStatus) VALUES(";
    private static DBManager2 sInstance;

    private SQLiteDatabase mWritableDB;

    private SQLiteDatabase mReadableDB;

    private static WeakReference<Context> mContextRef;

    public static void init(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    private DBManager2() {
        DBHelper helper = new DBHelper(mContextRef.get());
        mWritableDB = helper.getWritableDatabase();
        mReadableDB = helper.getReadableDatabase();
    }

    public static DBManager2 getInstance() {
        if (sInstance == null) {
            synchronized (DBManager2.class) {
                if (sInstance == null)
                    sInstance = new DBManager2();
            }
        }
        return sInstance;
    }

    @Override
    public boolean insertCooker(CookerBean cooker) {

        String sql = PREFIX_INSERT_COOKER +
                cooker.getCookerId() + ",'" + cooker.getCookerName() + "','" +
                cooker.getCookerLocation() + "','" + cooker.getCookerStatus() + "')";

        mWritableDB.execSQL(sql);

        return true;
    }

    @Override
    public boolean insertCookers(List<CookerBean> cookers) {
        mWritableDB.beginTransaction();
        for (CookerBean cooker : cookers) {
            insertCooker(cooker);
        }
        mWritableDB.setTransactionSuccessful();
        mWritableDB.endTransaction();
        return true;
    }

    @Override
    public boolean deleteCooker(long cookerId) {
        int r = mWritableDB.delete("cooker", "cookerId=?", new String[]{String.valueOf(cookerId)});
        return r != 0;
    }

    @Override
    public boolean deleteCookers() {
        mWritableDB.execSQL(DELETE_COOKER_TABLE);
        mWritableDB.execSQL(RESET_COOKER_SEQUENCE);
        return true;
    }

    @Override
    public CookerBean queryCooker(long cookerId) {
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM cooker WHERE cookerId=?",
                new String[]{String.valueOf(cookerId)});
        CookerBean cooker = new CookerBean();
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            cooker.setCookerId(cursor.getLong(cursor.getColumnIndex("cookerId")));
            cooker.setCookerName(cursor.getString(cursor.getColumnIndex("cookerName")));
            cooker.setCookerLocation(cursor.getString(cursor.getColumnIndex("cookerLocation")));
            cooker.setCookerStatus(cursor.getString(cursor.getColumnIndex("cookerStatus")));
        }
        cursor.close();
        return cooker;
    }

    @Override
    public List<CookerBean> queryCookers() {
        List<CookerBean> list = new ArrayList<>();
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM cooker", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            CookerBean cooker = new CookerBean();
            cooker.setCookerId(cursor.getLong(cursor.getColumnIndex("cookerId")));
            cooker.setCookerName(cursor.getString(cursor.getColumnIndex("cookerName")));
            cooker.setCookerLocation(cursor.getString(cursor.getColumnIndex("cookerLocation")));
            cooker.setCookerStatus(cursor.getString(cursor.getColumnIndex("cookerStatus")));
            list.add(cooker);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    @Override
    public boolean updateCooker(CookerBean cooker) {
        ContentValues values = new ContentValues(4);
        values.put("cookerId", cooker.getCookerId());
        values.put("cookerName", cooker.getCookerName());
        values.put("cookerLocation", cooker.getCookerLocation());
        values.put("cookerStatus", cooker.getCookerStatus());
        int r = mWritableDB.update("cooker", values, "cookerId=?",
                new String[]{String.valueOf(cooker.getCookerId())});
        return r > 0;
    }

    @Override
    public boolean updateCookers(List<CookerBean> cookers) {
        mWritableDB.beginTransaction();
        mWritableDB.execSQL(DELETE_COOKER_TABLE);
        mWritableDB.execSQL(RESET_COOKER_SEQUENCE);
        long r = 0;
        for (CookerBean cooker : cookers) {
            ContentValues values = new ContentValues(4);
            values.put("cookerId", cooker.getCookerId());
            values.put("cookerName", cooker.getCookerName());
            values.put("cookerLocation", cooker.getCookerLocation());
            values.put("cookerStatus", cooker.getCookerStatus());
            r = mWritableDB.insert("cooker", null, values);
            if (r == -1) {
                break;
            }
        }
        mWritableDB.endTransaction();
        return r != -1;
    }

    @Override
    public boolean insertBook(BookBean book) {

        String sql = PREFIX_INSERT_BOOK +
                book.getBookId() + ", " + book.getCookerId() + ",'" + book.getCookerName() + "','" +
                book.getCookerLocation() + "','" + book.getCookerStatus() + "'," +
                book.getRiceWeight() + "," + book.getPeopleCount() + ",'" + book.getTaste() + "','" +
                book.getTime() + "'" + ")";

        mWritableDB.execSQL(sql);

        return true;
    }

    @Override
    public boolean insertBooks(List<BookBean> books) {
        mWritableDB.beginTransaction();
        for (BookBean book : books) {
            insertBook(book);
        }
        mWritableDB.setTransactionSuccessful();
        mWritableDB.endTransaction();
        return true;
    }


    @Override
    public boolean deleteBook(long bookId) {
        int r = mWritableDB.delete("book", "bookId=?", new String[]{String.valueOf(bookId)});
        return r > 0;
    }

    @Override
    public boolean deleteBooks() {
        mWritableDB.execSQL(DELETE_BOOK_TABLE);
        mWritableDB.execSQL(RESET_BOOK_SEQUENCE);
        return true;
    }

    @Override
    public BookBean queryBook(long bookId) {
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM book WHERE bookId=?",
                new String[]{String.valueOf(bookId)});

        Log.v("DBManager2", "cursor count : " + cursor.getCount());
        BookBean book = new BookBean();
        if (cursor.getCount() > 0) {
            cursor.moveToLast();
            book.setBookId(cursor.getLong(cursor.getColumnIndex("bookId")));
            book.setCookerId(cursor.getLong(cursor.getColumnIndex("cookerId")));
            book.setCookerName(cursor.getString(cursor.getColumnIndex("cookerName")));
            book.setCookerLocation(cursor.getString(cursor.getColumnIndex("cookerLocation")));
            book.setCookerStatus(cursor.getString(cursor.getColumnIndex("cookerStatus")));
            book.setRiceWeight(cursor.getInt(cursor.getColumnIndex("riceWeight")));
            book.setPeopleCount(cursor.getInt(cursor.getColumnIndex("peopleCount")));
            book.setTaste(cursor.getString(cursor.getColumnIndex("taste")));
            book.setTime(cursor.getString(cursor.getColumnIndex("time")));
        }
        cursor.close();
        return book;
    }

    @Override
    public List<BookBean> queryBooks() {
        List<BookBean> list = new ArrayList<>();
        Cursor cursor = mReadableDB.rawQuery("SELECT * FROM book", null);
        Log.v("DBManager2", "cursor getCount = " + cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BookBean book = new BookBean();
            book.setBookId(cursor.getLong(cursor.getColumnIndex("bookId")));
            book.setCookerId(cursor.getLong(cursor.getColumnIndex("cookerId")));
            book.setCookerName(cursor.getString(cursor.getColumnIndex("cookerName")));
            book.setCookerLocation(cursor.getString(cursor.getColumnIndex("cookerLocation")));
            book.setCookerStatus(cursor.getString(cursor.getColumnIndex("cookerStatus")));
            book.setRiceWeight(cursor.getInt(cursor.getColumnIndex("riceWeight")));
            book.setPeopleCount(cursor.getInt(cursor.getColumnIndex("peopleCount")));
            book.setTaste(cursor.getString(cursor.getColumnIndex("taste")));
            book.setTime(cursor.getString(cursor.getColumnIndex("time")));
            list.add(book);
            cursor.moveToNext();
        }
        Log.v("DBManager2", "queryBooks success. List size: " + list.size());
        cursor.close();
        return list;
    }

    @Override
    public boolean updateBook(BookBean book) {
        ContentValues values = new ContentValues(9);
        values.put("bookId", book.getBookId());
        values.put("cookerId", book.getCookerId());
        values.put("cookerName", book.getCookerName());
        values.put("cookerLocation", book.getCookerLocation());
        values.put("cookerStatus", book.getCookerStatus());
        values.put("riceWeight", book.getRiceWeight());
        values.put("peopleCount", book.getPeopleCount());
        values.put("taste", book.getTaste());
        values.put("time", book.getTime());
        int r = mWritableDB.update("book", values, "bookId=?",
                new String[]{String.valueOf(book.getBookId())});

        Log.v("DBManager2", "cookerName : " + book.getCookerName());
        Log.v("DBManager2", "update return: " + r);

        return r > 0;
    }

    @Override
    public boolean updateBooks(List<BookBean> books) {
        mWritableDB.beginTransaction();
        mWritableDB.execSQL(DELETE_BOOK_TABLE);
        mWritableDB.execSQL(RESET_BOOK_SEQUENCE);
        long r = 0;
        for (BookBean book : books) {
            ContentValues values = new ContentValues(9);
            values.put("bookId", book.getBookId());
            values.put("cookerId", book.getCookerId());
            values.put("cookerName", book.getCookerName());
            values.put("cookerLocation", book.getCookerLocation());
            values.put("cookerStatus", book.getCookerStatus());
            values.put("riceWeight", book.getRiceWeight());
            values.put("peopleCount", book.getPeopleCount());
            values.put("taste", book.getTaste());
            values.put("time", book.getTime());
            r = mWritableDB.insert("cooker", null, values);
            if (r == -1) {
                break;
            }
        }
        mWritableDB.setTransactionSuccessful();
        mWritableDB.endTransaction();
        return r != -1;
    }
}
