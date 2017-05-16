package me.stupidme.cooker.view.about;

/**
 * Created by StupidL on 2017/5/16.
 */

public class AboutGroupItem extends BaseAboutBean<String> {

    public AboutGroupItem(String title) {
        mType = TYPE_TITLE_ITEM;
        mData = title;
    }
}
