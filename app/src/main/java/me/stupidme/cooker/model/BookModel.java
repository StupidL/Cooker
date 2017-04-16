package me.stupidme.cooker.model;

import java.util.List;

import me.stupidme.cooker.model.db.DBManager;
import me.stupidme.cooker.model.db.IDBManager;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookModel implements IBookModel {

    private static final String TAG = "BookModel";

    private static BookModel sInstance;

    private IDBManager mManager2;

    private BookModel() {
        mManager2 = DBManager.getInstance();
    }

    public static BookModel getInstance() {
        if (sInstance == null) {
            synchronized (BookModel.class) {
                if (sInstance == null)
                    sInstance = new BookModel();
            }
        }
        return sInstance;
    }

    @Override
    public void insertBook(BookBean book) {
        mManager2.insertBook(book);
    }

    @Override
    public void insertBooks(List<BookBean> list) {
        mManager2.insertBooks(list);
    }

    @Override
    public void updateBook(BookBean book) {
        mManager2.updateBook(book);
    }

    @Override
    public void updateBooks(List<BookBean> list) {
        mManager2.updateBooks(list);
    }

    @Override
    public List<BookBean> queryBooks() {
        return mManager2.queryBooks();
    }

    @Override
    public BookBean queryBook(long bookId) {
        return mManager2.queryBook(bookId);
    }

    @Override
    public void deleteBook(BookBean book) {
        mManager2.deleteBook(book.getBookId());
    }

    @Override
    public void deleteBooks() {
        mManager2.deleteBooks();
    }
}
