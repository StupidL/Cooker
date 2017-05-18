package me.stupidme.cooker.view.detail;

/**
 * Created by stupidl on 17-5-17.
 */

public interface BookDetailView {

    void onCancelSuccess();

    void onCancelFailed();

    void showDialog(boolean show);
}
