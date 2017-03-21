package me.stupidme.cooker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/20.
 */

public class DBManager implements IDBManager {

    private static final String DELETE_COOKER_TABLE = "DELETE FROM cooker";
    private static final String RESET_COOKER_SEQUENCE = "UPDATE sqlite_sequence SET seq = 0 WHERE name = 'cooker'";
    private static final String DELETE_BOOK_TABLE = "DELETE FROM book";
    private static final String RESET_BOOK_SEQUENCE = "UPDATE sqlite_sequence SET seq = 0 WHERE name = 'book'";

    private static WeakReference<Context> mContextRef;

    private SQLiteDatabase mWritableDB;

    private SQLiteDatabase mReadableDB;

    private static DBManager sInstance;

    public static void init(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    private DBManager() {
        if (mContextRef.get() != null) {
            DBHelper helper = new DBHelper(mContextRef.get());
            mWritableDB = helper.getWritableDatabase();
            mReadableDB = helper.getReadableDatabase();
        } else {
            throw new RuntimeException("Context is null. Create Database failed.");
        }
    }

    public static DBManager getInstance() {
        if (sInstance == null) {
            synchronized (DBManager.class) {
                if (sInstance == null)
                    sInstance = new DBManager();
            }
        }
        return sInstance;
    }

    @Override
    public Observable<Boolean> insertCooker(CookerBean cooker) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                ContentValues values = new ContentValues(4);
                values.put("cookerId", cooker.getCookerId());
                values.put("cookerName", cooker.getCookerName());
                values.put("cookerLocation", cooker.getCookerLocation());
                values.put("cookerStatus", cooker.getCookerStatus());
                long r = mWritableDB.insert("cooker", null, values);
                if (r == -1) {
                    observer.onError(new Throwable("Error occurs when insert contents in table 'cooker'"));
                    observer.onComplete();
                } else {
                    observer.onNext(true);
                    observer.onComplete();
                }
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> insertCookers(List<CookerBean> cookers) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                mWritableDB.beginTransaction();
                boolean success = true;
                for (CookerBean cooker : cookers) {
                    ContentValues values = new ContentValues(4);
                    values.put("cookerId", cooker.getCookerId());
                    values.put("cookerName", cooker.getCookerName());
                    values.put("cookerLocation", cooker.getCookerLocation());
                    values.put("cookerStatus", cooker.getCookerStatus());
                    long c = mWritableDB.insertOrThrow("cooker", null, values);
                    if (c == -1) {
                        success = false;
                        break;
                    }
                }
                mWritableDB.endTransaction();
                if (success) {
                    observer.onNext(true);
                    observer.onComplete();
                } else {
                    observer.onError(new Throwable("Error occurs when insert contents into table 'cooker'"));
                    observer.onComplete();
                }
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> deleteCooker(long cookerId) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                int r = mWritableDB.delete("cooker", "cookerId=?", new String[]{String.valueOf(cookerId)});
                if (r > 0) {
                    observer.onNext(true);
                    observer.onComplete();
                } else {
                    observer.onError(new Throwable("Error occurs when delete item from tale 'cooker'"));
                    observer.onComplete();
                }
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> deleteCookers() {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                mWritableDB.execSQL(DELETE_COOKER_TABLE);
                mWritableDB.execSQL(RESET_COOKER_SEQUENCE);
                observer.onNext(true);
                observer.onComplete();
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<CookerBean> queryCooker(long cookerId) {
        return new Observable<CookerBean>() {
            @Override
            protected void subscribeActual(Observer<? super CookerBean> observer) {
                Cursor cursor = mReadableDB.rawQuery("SELECT * FROM cooker WHERE cookerId=?",
                        new String[]{String.valueOf(cookerId)});
                if (cursor.getCount() > 0) {
                    cursor.moveToLast();

                    CookerBean cooker = new CookerBean();
                    cooker.setCookerId(cursor.getLong(cursor.getColumnIndex("cookerId")));
                    cooker.setCookerName(cursor.getString(cursor.getColumnIndex("cookerName")));
                    cooker.setCookerLocation(cursor.getString(cursor.getColumnIndex("cookerLocation")));
                    cooker.setCookerStatus(cursor.getString(cursor.getColumnIndex("cookerStatus")));

                    observer.onNext(cooker);
                    observer.onComplete();
                } else {
                    observer.onError(new Throwable("Can not find cooker in table 'cooker' where cookerId = " + cookerId));
                    observer.onComplete();
                }
                cursor.close();
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<CookerBean>> queryCookers() {
        return new Observable<List<CookerBean>>() {
            @Override
            protected void subscribeActual(Observer<? super List<CookerBean>> observer) {
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
                observer.onNext(list);
                observer.onComplete();
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> updateCooker(CookerBean cooker) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                ContentValues values = new ContentValues(4);
                values.put("cookerId", cooker.getCookerId());
                values.put("cookerName", cooker.getCookerName());
                values.put("cookerLocation", cooker.getCookerLocation());
                values.put("cookerStatus", cooker.getCookerStatus());
                int r = mWritableDB.update("cooker", values, "cookerId=?",
                        new String[]{String.valueOf(cooker.getCookerId())});
                if (r > 0) {
                    observer.onNext(true);
                    observer.onComplete();
                } else {
                    observer.onError(new Throwable("There is no this item in table 'cooker', update failed."));
                    observer.onComplete();
                }
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> updateCookers(List<CookerBean> cookers) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                mWritableDB.beginTransaction();
                mWritableDB.execSQL(DELETE_COOKER_TABLE);
                mWritableDB.execSQL(RESET_COOKER_SEQUENCE);
                for (CookerBean cooker : cookers) {
                    ContentValues values = new ContentValues(4);
                    values.put("cookerId", cooker.getCookerId());
                    values.put("cookerName", cooker.getCookerName());
                    values.put("cookerLocation", cooker.getCookerLocation());
                    values.put("cookerStatus", cooker.getCookerStatus());
                    long r = mWritableDB.insert("cooker", null, values);
                    if (r == -1) {
                        observer.onError(new Throwable("Error occurs when insert content into table 'cooker'"));
                    }
                }
                mWritableDB.endTransaction();
                observer.onNext(true);
                observer.onComplete();
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> insertBook(BookBean book) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
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
                long r = mWritableDB.insert("book", null, values);
                if (r == -1) {
                    observer.onError(new Throwable("Error occurs when insert contents in table 'cooker'"));
                    observer.onComplete();
                } else {
                    observer.onNext(true);
                    observer.onComplete();
                }
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> insertBooks(List<BookBean> books) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                mWritableDB.beginTransaction();
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
                    long r = mWritableDB.insert("book", null, values);
                    if (r == -1) {
                        observer.onError(new Throwable("Error occurs when insert contents in table 'cooker'"));
                    }
                }
                mWritableDB.endTransaction();
                observer.onNext(true);
                observer.onComplete();
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> deleteBook(long bookId) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                int r = mWritableDB.delete("book", "bookId=?", new String[]{String.valueOf(bookId)});
                if (r > 0) {
                    observer.onNext(true);
                    observer.onComplete();
                } else {
                    observer.onError(new Throwable("Error occurs when delete item from tale 'book'"));
                    observer.onComplete();
                }
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> deleteBooks() {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                mWritableDB.execSQL(DELETE_BOOK_TABLE);
                mWritableDB.execSQL(RESET_BOOK_SEQUENCE);
                observer.onNext(true);
                observer.onComplete();
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<BookBean> queryBook(long bookId) {
        return new Observable<BookBean>() {
            @Override
            protected void subscribeActual(Observer<? super BookBean> observer) {
                Cursor cursor = mReadableDB.rawQuery("SELECT * FROM cooker WHERE bookId=?",
                        new String[]{String.valueOf(bookId)});
                if (cursor.getCount() > 0) {
                    cursor.moveToLast();
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
                    observer.onNext(book);
                    observer.onComplete();
                } else {
                    observer.onError(new Throwable("Can not find item in table 'cooker' where bookId = " + bookId));
                    observer.onComplete();
                }
                cursor.close();
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<BookBean>> queryBooks() {
        return new Observable<List<BookBean>>() {
            @Override
            protected void subscribeActual(Observer<? super List<BookBean>> observer) {
                List<BookBean> list = new ArrayList<>();
                Cursor cursor = mReadableDB.rawQuery("SELECT * FROM cooker", null);
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
                cursor.close();
                observer.onNext(list);
                observer.onComplete();
            }
        }.subscribeOn(Schedulers.computation());
    }

    @Override
    public Observable<Boolean> updateBook(BookBean book) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
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
                        new String[]{String.valueOf(book.getCookerId())});
                if (r > 0) {
                    observer.onNext(true);
                    observer.onComplete();
                } else {
                    observer.onError(new Throwable("There is no this item in table 'book', update failed."));
                    observer.onComplete();
                }
            }
        }.subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Boolean> updateBooks(List<BookBean> books) {
        return new Observable<Boolean>() {
            @Override
            protected void subscribeActual(Observer<? super Boolean> observer) {
                mWritableDB.beginTransaction();
                mWritableDB.execSQL(DELETE_BOOK_TABLE);
                mWritableDB.execSQL(RESET_BOOK_SEQUENCE);
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
                    long r = mWritableDB.insert("cooker", null, values);
                    if (r == -1) {
                        observer.onError(new Throwable("Error occurs when insert content into table 'cooker'"));
                    }
                }
                mWritableDB.endTransaction();
                observer.onNext(true);
                observer.onComplete();
            }
        }.subscribeOn(Schedulers.io());
    }
}
