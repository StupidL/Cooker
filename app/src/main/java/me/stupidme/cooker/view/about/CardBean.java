package me.stupidme.cooker.view.about;

/**
 * Created by StupidL on 2017/5/16.
 */

public class CardBean {

    private String mTitle;
    private String mContent;

    public CardBean(String title, String content) {
        mTitle = title;
        mContent = content;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getContent() {
        return mContent;
    }
}
