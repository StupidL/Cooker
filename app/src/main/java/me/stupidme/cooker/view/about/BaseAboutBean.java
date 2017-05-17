package me.stupidme.cooker.view.about;

/**
 * Created by StupidL on 2017/5/16.
 */

public class BaseAboutBean<T> {

    public static final int TYPE_TITLE_ITEM = 0x01;
    public static final int TYPE_CARD_ITEM = 0x02;

    protected int mType;
    protected T mData;

    public T getData() {
        return mData;
    }

    public int getType() {
        return mType;
    }
}
