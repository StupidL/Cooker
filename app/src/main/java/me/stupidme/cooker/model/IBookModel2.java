package me.stupidme.cooker.model;

import java.util.List;

/**
 * Created by StupidL on 2017/3/29.
 */

public interface IBookModel2 {

    boolean insertBook(long userId, BookBean book);

    boolean insertBooks(long userId, List<BookBean> books);

    boolean updateBook(long userId, BookBean book);

    boolean updateBooks(long userId, List<BookBean> books);

    boolean deleteBook(long userId, BookBean book);

    boolean deleteBooks(long userId);

    BookBean queryBookFromNet(long userId, long bookId);

    List<BookBean> queryBooksFromNet(long userId);

    BookBean queryBookFromDB(long userId, long bookId);

    List<BookBean> queryBooksFromDB(long userId);
}
