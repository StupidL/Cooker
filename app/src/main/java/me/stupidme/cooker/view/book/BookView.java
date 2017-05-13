package me.stupidme.cooker.view.book;

import java.util.List;

import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/3/10.
 */

public interface BookView {

    /**
     * 控制刷新控件的显示与否。
     * 当进行网络请求的时候为true。网络请求结束或者错误的时候为false。
     *
     * @param show 是否显示
     */
    void setRefreshing(boolean show);

    /**
     * 界面上移除一个项目
     *
     * @param book 要移除的预约项目
     */
    void removeBook(BookBean book);

    /**
     * 界面上插入一个预约项目
     *
     * @param book 要插入的项目
     */
    void insertBook(BookBean book);

    /**
     * 界面上批量插入预约项目,在数据库获取信息的情况下使用
     *
     * @param list 项目列表
     */
    void insertBooks(List<BookBean> list);

    /**
     * 更新某个具体的预约项目。当该预约的状态为booking的时候，
     * 使用该方法可以定时刷新状态。
     *
     * @param position 预约项在适配器的位置
     * @param book     更新后的预约项
     */
    void updateBook(int position, BookBean book);

    /**
     * 批量更新预约项目。在服务器获取信息的情况下使用
     *
     * @param list 项目列表
     */
    void updateBooks(List<BookBean> list);

    /**
     * 界面弹出提示信息
     *
     * @param message 信息内容
     */
    void showMessage(String message);

}
