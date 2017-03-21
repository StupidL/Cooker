package me.stupidme.cooker.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.stupidme.cooker.db.DBManager;
import me.stupidme.cooker.db.StupidDBManager;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookModel implements IBookModel {

    private static final String TAG = "BookModel";

    private static BookModel sInstance;

    private StupidDBManager mManager;

    private DBManager mManager2;

    private BookModel() {
        mManager = StupidDBManager.getInstance();
        mManager2 = DBManager.getInstance();
    }

    public static BookModel getInstance() {
        if (sInstance == null)
            sInstance = new BookModel();
        return sInstance;
    }

    @Override
    public void insertBook(BookBean book) {
        mManager.insertBook(book);
        mManager2.insertBook(book)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v(TAG, d.toString());
                    }

                    @Override
                    public void onNext(Boolean value) {
                        Log.v(TAG, value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.v(TAG, "onComplete");
                    }
                });
    }

    @Override
    public void insertBooks(List<BookBean> list) {
        mManager.insertBooks(list);
        mManager2.insertBooks(list)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v(TAG, d.toString());
                    }

                    @Override
                    public void onNext(Boolean value) {
                        Log.v(TAG, value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.v(TAG, "onComplete");
                    }
                });
    }

    @Override
    public void updateBook(BookBean book) {
        mManager.updateBook(book);
        mManager2.updateBook(book)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v(TAG, d.toString());
                    }

                    @Override
                    public void onNext(Boolean value) {
                        Log.v(TAG, value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.v(TAG, "onComplete");
                    }
                });
    }

    @Override
    public void updateBooks(List<BookBean> list) {
        mManager.updateBooks(list);
        mManager2.updateBooks(list)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v(TAG, d.toString());
                    }

                    @Override
                    public void onNext(Boolean value) {
                        Log.v(TAG, value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.v(TAG, "onComplete");
                    }
                });
    }

    @Override
    public List<BookBean> queryBooks() {
//        return mManager.queryBooks();
        List<BookBean> list = new ArrayList<>();

        mManager2.queryBooks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v(TAG, d.toString());
                    }

                    @Override
                    public void onNext(List<BookBean> value) {
                        list.addAll(value);
                        Log.v(TAG, value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.v(TAG, "onComplete");
                    }
                });
        return list;
    }

    @Override
    public void deleteBook(BookBean book) {
        mManager.deleteBook(book);
        mManager2.deleteBook(book.getBookId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v(TAG, d.toString());
                    }

                    @Override
                    public void onNext(Boolean value) {
                        Log.v(TAG, value.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v(TAG, e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.v(TAG, "onComplete");
                    }
                });
    }
}
