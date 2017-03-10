package me.stupidme.cooker.model;

import java.util.List;

import me.stupidme.cooker.db.StupidDBManager;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookModel implements IBookModel {

    private static BookModel sInstance;

    private StupidDBManager mManager;

    private BookModel() {
        mManager = StupidDBManager.getInstance();
    }

    public static BookModel getInstance() {
        if (sInstance == null)
            sInstance = new BookModel();
        return sInstance;
    }

    @Override
    public void insertBook(BookBean book) {
        mManager.insertBook(book);
    }

    @Override
    public void insertBooks(List<BookBean> list) {
        mManager.insertBooks(list);
    }

    @Override
    public void updateBook(BookBean book) {
        mManager.updateBook(book);
    }

    @Override
    public void updateBooks(List<BookBean> list) {
        mManager.updateBooks(list);
    }

    @Override
    public List<BookBean> queryBooks() {
        return mManager.queryBooks();
    }

    @Override
    public void deleteBook(BookBean book) {
        mManager.deleteBook(book);
    }
}
