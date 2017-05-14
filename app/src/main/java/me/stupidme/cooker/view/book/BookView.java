package me.stupidme.cooker.view.book;

import java.util.List;

import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/3/10.
 * <p>
 * Callback interface for presenter to communicate with activity or fragment.
 */

public interface BookView {

    /**
     * Show a refresh control
     *
     * @param show show control if true
     */
    void setRefreshing(boolean show);

    /**
     * Show a tips dialog.
     *
     * @param show show dialog if true
     */
    void showDialog(boolean show);

    /**
     * Remove a specified book.
     *
     * @param book book
     */
    void removeBook(BookBean book);

    /**
     * Remove a specified book.
     *
     * @param bookId id of book
     */
    void removeBook(Long bookId);

    /**
     * Insert a single book to a activity or fragment.
     *
     * @param book book
     */
    void insertBook(BookBean book);

    /**
     * Insert books to activity or fragment.
     *
     * @param list books
     */
    void insertBooks(List<BookBean> list);

    /**
     * Update a specified book.
     *
     * @param position position in adapter
     * @param book     book
     */
    void updateBook(int position, BookBean book);

    /**
     * Toast a message.
     *
     * @param what    message type
     * @param message message content
     */
    void showMessage(int what, CharSequence message);

}
