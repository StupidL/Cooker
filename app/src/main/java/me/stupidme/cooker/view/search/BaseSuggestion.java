package me.stupidme.cooker.view.search;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by StupidL on 2017/5/15.
 */

public class BaseSuggestion implements SearchSuggestion {

    public static final int TYPE_BOOK = 0x01;
    public static final int TYPE_COOKER = 0x02;

    protected int mType;
    protected String mContent;

    public BaseSuggestion(String content) {
        this.mContent = content;
    }

    public BaseSuggestion(Parcel in) {
        mType = in.readInt();
        mContent = in.readString();
    }

    @Override
    public String getBody() {
        return mContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mType);
        dest.writeString(mContent);
    }

    public static final Creator<BaseSuggestion> CREATOR = new Creator<BaseSuggestion>() {
        @Override
        public BaseSuggestion createFromParcel(Parcel in) {
            return new BaseSuggestion(in);
        }

        @Override
        public BaseSuggestion[] newArray(int size) {
            return new BaseSuggestion[size];
        }
    };

    public int getType() {
        return mType;
    }

    public String getContent() {
        return mContent;
    }

}
