package me.stupidme.cooker.view.status;

import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/4/5.
 */

public interface StatusView {

//    void updateBook(BookBean book);

    void removeBook(long bookId);

    void showMessage(CharSequence msg);

}
