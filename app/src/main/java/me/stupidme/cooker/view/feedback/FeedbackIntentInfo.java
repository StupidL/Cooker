package me.stupidme.cooker.view.feedback;

/**
 * Created by StupidL on 2017/4/5.
 */

public class FeedbackIntentInfo {

    private int mColorPrimary;
    private int mColorPrimaryDark;
    private String mEmailTo;

    public int getColorPrimary() {
        return mColorPrimary;
    }

    public void setColorPrimary(int mColorPrimary) {
        this.mColorPrimary = mColorPrimary;
    }

    public int getColorPrimaryDark() {
        return mColorPrimaryDark;
    }

    public void setColorPrimaryDark(int mColorPrimaryDark) {
        this.mColorPrimaryDark = mColorPrimaryDark;
    }

    public String getEmailTo() {
        return mEmailTo;
    }

    public void setEmailTo(String mEmail) {
        this.mEmailTo = mEmail;
    }
}
