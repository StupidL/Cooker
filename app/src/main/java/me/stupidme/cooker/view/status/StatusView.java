package me.stupidme.cooker.view.status;

import java.util.List;

import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/4/5.
 */

public interface StatusView {

    void showDialog(boolean show);

    void removeBook(long bookId);

    void onCancelFailed();

    void onCancelSuccess(BookBean bookBean);

    void acceptData(List<BookBean> list);

    void removeItem(BookBean bookBean);
}
