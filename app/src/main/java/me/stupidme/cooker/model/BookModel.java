package me.stupidme.cooker.model;

import java.util.List;

/**
 * Created by StupidL on 2017/3/8.
 */

public class BookModel implements IBookModel {

    private static BookModel sInstance;

    private BookModel() {
    }

    public static BookModel getInstance() {
        if (sInstance == null)
            sInstance = new BookModel();
        return sInstance;
    }

    @Override
    public void insertBook(BookBean book) {

    }

    @Override
    public void insertBooks(List<BookBean> list) {

    }

    @Override
    public void updateBook(BookBean book) {

    }

    @Override
    public List<BookBean> queryBooks() {
        return null;
    }
}
