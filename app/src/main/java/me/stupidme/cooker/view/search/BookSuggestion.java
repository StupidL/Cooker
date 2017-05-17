package me.stupidme.cooker.view.search;

import android.os.Parcel;

/**
 * Created by StupidL on 2017/5/15.
 */

public class BookSuggestion extends BaseSuggestion {

    public BookSuggestion(String content) {
        super(content);
        mType = TYPE_BOOK;
    }

    public BookSuggestion(Parcel in) {
        super(in);
    }
}
