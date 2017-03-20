package me.stupidme.cooker.db;

import android.content.Context;
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

    private static WeakReference<Context> mContextRef;

    private SQLiteDatabase mDataBase;

    private static DBManager sInstance;

    public static void init(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    private DBManager() {
        if (mContextRef.get() != null) {
            mDataBase = new StupidDBHelper(mContextRef.get()).getWritableDatabase();
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
        return null;
    }

    @Override
    public Observable<Boolean> insertCookers(List<CookerBean> cookers) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteCooker(long cookerId) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteCookers() {
        return null;
    }

    @Override
    public Observable<CookerBean> queryCooker(long cookerId) {
        return new Observable<CookerBean>() {
            @Override
            protected void subscribeActual(Observer<? super CookerBean> observer) {
                CookerBean cooker = new CookerBean();

                //TODO query operations

                observer.onNext(cooker);
                observer.onComplete();
            }
        }.subscribeOn(Schedulers.computation());
    }

    @Override
    public Observable<List<CookerBean>> queryCookers() {
        return new Observable<List<CookerBean>>() {
            @Override
            protected void subscribeActual(Observer<? super List<CookerBean>> observer) {
                List<CookerBean> list = new ArrayList<>();

                //TODO query operations


                observer.onNext(list);
                observer.onComplete();
            }
        }.subscribeOn(Schedulers.computation());
    }

    @Override
    public Observable<Boolean> updateCooker(CookerBean cooker) {
        return null;
    }

    @Override
    public Observable<Boolean> updateCookers() {
        return null;
    }

    @Override
    public Observable<Boolean> insertBook(BookBean book) {
        return null;
    }

    @Override
    public Observable<Boolean> insertBooks(List<BookBean> books) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteBook(long bookId) {
        return null;
    }

    @Override
    public Observable<Boolean> deleteBooks() {
        return null;
    }

    @Override
    public Observable<BookBean> queryBook(long bookId) {
        return null;
    }

    @Override
    public Observable<List<BookBean>> queryBooks() {
        return null;
    }

    @Override
    public Observable<Boolean> updateBook(BookBean book) {
        return null;
    }

    @Override
    public Observable<Boolean> updateBooks(List<BookBean> books) {
        return null;
    }
}
