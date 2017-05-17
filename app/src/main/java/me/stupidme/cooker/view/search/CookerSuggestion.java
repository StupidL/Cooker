package me.stupidme.cooker.view.search;

import android.os.Parcel;

/**
 * Created by StupidL on 2017/5/15.
 */

public class CookerSuggestion extends BaseSuggestion {

    public CookerSuggestion(String content) {
        super(content);
        mType = TYPE_COOKER;
    }

    public CookerSuggestion(Parcel in) {
        super(in);
    }
}
