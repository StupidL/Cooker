package me.stupidme.cooker.presenter;

import java.util.List;
import java.util.Map;

import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/3/8.
 * <p>
 * Presenter of book activity or fragment. Manage all books.
 */

public interface BookPresenter {

    int MESSAGE_INSERT_BOOK_ERROR = 0x10;
    int MESSAGE_INSERT_BOOK_FAILED = 0x11;
    int MESSAGE_INSERT_BOOK_SUCCESS = 0x12;
    int MESSAGE_INSERT_BOOK_SUCCESS_BUT_EMPTY = 0x13;
    int MESSAGE_INSERT_BOOK_DB_FAILED = 0x14;
    int MESSAGE_INSERT_BOOK_HISTORY_FAILED = 0x15;

    int MESSAGE_DELETE_BOOK_ERROR = 0x20;
    int MESSAGE_DELETE_BOOK_FAILED = 0x21;
    int MESSAGE_DELETE_BOOK_SUCCESS = 0x22;
    int MESSAGE_DELETE_BOOK_SUCCESS_BUT_EMPTY = 0x23;
    int MESSAGE_DELETE_BOOK_DB_FAILED = 0x24;
    int MESSAGE_DELETE_BOOK_HISTORY_FAILED = 0x25;

    int MESSAGE_QUERY_BOOK_ERROR = 0x30;
    int MESSAGE_QUERY_BOOK_FAILED = 0x31;
    int MESSAGE_QUERY_BOOK_SUCCESS_BUT_EMPTY = 0x32;

    int MESSAGE_UPDATE_BOOK_DB_FAILED = 0x41;

    int MESSAGE_UPDATE_COOKER_FAILED = 0x50;

    /**
     * Insert a single book.
     *
     * @param book book to be inserted
     */
    void insertBook(BookBean book);

    /**
     * Insert a book by key-value pairs both in local db and server.
     *
     * @param map key-value pairs
     */
    void insertBook(Map<String, String> map);

    /**
     * Delete a single book both in local db and server.
     *
     * @param book book to be deleted
     */
    void deleteBook(BookBean book);

    /**
     * Delete a book in table history in local db.
     *
     * @param book book
     */
    void deleteBookHistory(BookBean book);

    /**
     * Query a specified book by id from local db.
     *
     * @param bookId id of book
     */
    void queryBookFromDB(long bookId);

    /**
     * Query book from local db. Used when first enter the activity or fragment.
     */
    void queryBooksFromDB();

    /**
     * Query a specified book by id from server, and update local db at the same time.
     *
     * @param bookId id of book
     */
    void queryBookFromServer(long bookId);

    /**
     * Query books from server, and update local db at the same time.
     */
    void queryBooksFromServer();

    /**
     * Query all cooker names. When create a book, we need choose a existed cooker.
     * So we must know what cookers we have first.
     *
     * @return names of cookers
     */
    List<String> queryCookerNamesFromDB();

}
