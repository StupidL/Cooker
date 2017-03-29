package me.stupidme.cooker.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.db.DBManager2;
import me.stupidme.cooker.db.IDBManager2;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;

/**
 * Created by StupidL on 2017/3/29.
 */

public class BookModel2 implements IBookModel2 {

    private static final String TAG = "BookModel2";

    private static BookModel2 sInstance;

    private IDBManager2 mManager;

    private CookerService mService;

    private BookModel2() {
        mManager = DBManager2.getInstance();
        mService = CookerRetrofit.getInstance().getCookerService();
    }

    public static BookModel2 getInstance() {
        if (sInstance == null) {
            synchronized (BookModel2.class) {
                if (sInstance == null)
                    sInstance = new BookModel2();
            }
        }
        return sInstance;
    }

    @Override
    public boolean insertBook(long userId, BookBean book) {
        Log.v(TAG, "insertBook()...");

        boolean[] success = new boolean[1];
        mService.insertBook(userId, book)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(listHttpResult -> {
                    mManager.insertBook(listHttpResult.getData().get(0));
                    success[0] = true;
                });

        return success[0];
    }

    @Override
    public boolean insertBooks(long userId, List<BookBean> books) {
        Log.v(TAG, "insertBooks()...");

        return false;
    }

    @Override
    public boolean updateBook(long userId, BookBean book) {
        Log.v(TAG, "updateBook()...");

        boolean[] success = new boolean[1];
        mService.updateBook(userId, book)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(listHttpResult -> {
                    mManager.updateBook(listHttpResult.getData().get(0));
                    success[0] = true;
                });
        return success[0];
    }

    @Override
    public boolean updateBooks(long userId, List<BookBean> books) {
        Log.v(TAG, "updateBooks()...");

        return false;
    }

    @Override
    public boolean deleteBook(long userId, BookBean book) {
        Log.v(TAG, "deleteBook()...");

        boolean[] success = new boolean[1];
        mService.deleteBook(userId, book.getBookId())
                .subscribeOn(Schedulers.io())   //上游网络操作在io线程
                .observeOn(Schedulers.io())     //下游数据库操作在io线程
                .subscribe(bean -> {
                    mManager.deleteBook(bean.getBookId());
                    success[0] = true;
                });
        return success[0];
    }

    @Override
    public boolean deleteBooks(long userId) {
        Log.v(TAG, "deleteBooks()...");

        boolean[] success = new boolean[1];
        mService.deleteBooks(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(bean -> {
                    mManager.deleteBooks();
                    success[0] = true;
                });
        return success[0];
    }

    @Override
    public BookBean queryBookFromNet(long userId, long bookId) {
        Log.v(TAG, "queryBookFromNet()...");

        final BookBean[] bookBeans = {null};
        mService.queryBook(userId, bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())     //顺便更新数据库
                .subscribe(listHttpResult -> {
                    bookBeans[0] = listHttpResult.getData().get(0);
                    mManager.updateBook(bookBeans[0]);
                    Log.v(TAG, "queryBookFromNet success!");
                });
        return bookBeans[0];
    }

    @Override
    public List<BookBean> queryBooksFromNet(long userId) {
        Log.v(TAG, "queryBooksFromNet()...");

        final List<BookBean> list = new ArrayList<>();

        mService.queryBooks(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())     //顺便更新数据库
                .subscribe(listHttpResult -> {
                    list.addAll(listHttpResult.getData());
                    mManager.updateBooks(listHttpResult.getData());
                    Log.v(TAG, "queryBooksFromNet success!");
                });

        return list;
    }

    @Override
    public BookBean queryBookFromDB(long userId, long bookId) {
        Log.v(TAG, "queryBookFromDB()...");

        return mManager.queryBook(bookId);
    }

    @Override
    public List<BookBean> queryBooksFromDB(long userId) {
        Log.v(TAG, "queryBooksFromDB()...");

        return mManager.queryBooks();
    }
}
