package me.stupidme.cooker.presenter;

import java.util.List;

import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/3/29.
 */

public interface IBookPresenter2 {

    void insertBook(BookBean book);

    void insertBooks(List<BookBean> books);

    void deleteBook(BookBean book);

    void deleteBooks();

    void queryBookFromDB(BookBean book);

    void queryBooksFromDB();

    void queryBookFromNet(BookBean book);

    void queryBooksFromNet();
}
