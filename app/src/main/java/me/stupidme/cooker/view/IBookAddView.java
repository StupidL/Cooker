package me.stupidme.cooker.view;

import java.util.List;

/**
 * Created by StupidL on 2017/3/8.
 */

public interface IBookAddView {

    void updateNameSpinner(List<String> names);

    void showMessage(String message);

    void showProgressDialog(boolean show);
}
