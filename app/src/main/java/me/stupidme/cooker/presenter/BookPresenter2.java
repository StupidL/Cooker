package me.stupidme.cooker.presenter;

import android.util.Log;

import java.util.List;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.BookModel2;
import me.stupidme.cooker.model.IBookModel2;
import me.stupidme.cooker.view.book.IBookView;

/**
 * Created by StupidL on 2017/3/29.
 */

public class BookPresenter2 implements IBookPresenter2 {

    private static final String TAG = "BookPresenter2";

    private IBookView mView;

    private IBookModel2 mModel;

    public BookPresenter2(IBookView view) {
        mView = view;
        mModel = BookModel2.getInstance();
    }

    @Override
    public void insertBook(BookBean book) {
        if (mModel.insertBook(mView.getUserId(), book)) {
            mView.insertBook(book);

            Log.v(TAG, "insertBook success!");
        }
    }

    @Override
    public void insertBooks(List<BookBean> books) {
        if (mModel.insertBooks(mView.getUserId(), books)) {
            mView.insertBooks(books);

            Log.v(TAG, "insertBooks success!");
        }
    }

    @Override
    public void deleteBook(BookBean book) {
        if (mModel.deleteBook(mView.getUserId(), book)) {
            mView.removeBook(book);

            Log.v(TAG, "deleteBook success!");
        }
    }

    @Override
    public void deleteBooks() {
        if (mModel.deleteBooks(mView.getUserId())) {

            Log.v(TAG, "deleteBooks success!");
        }
    }

    @Override
    public void queryBookFromDB(BookBean book) {
        mView.setRefreshing(true);
        mView.insertBook(mModel.queryBookFromDB(mView.getUserId(), book.getBookId()));
        mView.setRefreshing(false);
    }

    @Override
    public void queryBooksFromDB() {
        mView.setRefreshing(true);
        mView.insertBooks(mModel.queryBooksFromDB(mView.getUserId()));
        mView.setRefreshing(false);
    }

    @Override
    public void queryBookFromNet(BookBean book) {
        mView.setRefreshing(true);
        mView.insertBook(mModel.queryBookFromNet(mView.getUserId(), book.getBookId()));
        mView.setRefreshing(false);
    }

    @Override
    public void queryBooksFromNet() {
        mView.setRefreshing(true);
        mView.insertBooks(mModel.queryBooksFromNet(mView.getUserId()));
        mView.setRefreshing(false);
    }
}
