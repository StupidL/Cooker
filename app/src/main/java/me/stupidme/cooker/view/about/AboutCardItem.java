package me.stupidme.cooker.view.about;

/**
 * Created by StupidL on 2017/5/16.
 */

public class AboutCardItem extends BaseAboutBean<CardBean> {

    public AboutCardItem(CardBean cardBean) {
        mType = TYPE_CARD_ITEM;
        mData = cardBean;
    }

}
