package me.stupidme.cooker.model;

import java.util.List;

import me.stupidme.cooker.model.db.CookerDbManagerSQLiteImpl;
import me.stupidme.cooker.model.db.CookerDbManager;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookDbModelImpl implements BookDbModel {

    private static final String TAG = "BookModel";

    private static BookDbModelImpl sInstance;

    private CookerDbManager mManager2;

    private BookDbModelImpl() {
        mManager2 = CookerDbManagerSQLiteImpl.getInstance();
    }

    public static BookDbModelImpl getInstance() {
        if (sInstance == null) {
            synchronized (BookDbModelImpl.class) {
                if (sInstance == null)
                    sInstance = new BookDbModelImpl();
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
