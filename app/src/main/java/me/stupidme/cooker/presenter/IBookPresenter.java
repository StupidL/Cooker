package me.stupidme.cooker.presenter;

import java.util.List;

import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/3/8.
 */

public interface IBookPresenter extends DisposablePresenter{

    /**
     * 插入一条预约信息，本地数据库和服务器均要插入
     *
     * @param book 预约
     */
    void insertBook(BookBean book);

    /**
     * 批量插入数据，本地数据库和服务器均要更新
     *
     * @param books 预约信息列表
     */
    void insertBooks(List<BookBean> books);

    /**
     * 删除一个预约，本地数据库和服务器均要删除
     *
     * @param book 预约
     */
    void deleteBook(BookBean book);

    /**
     * 批量删除预约信息，本地数据库和服务器均要删除
     *
     * @param books 批量预约信息
     */
    void deleteBooks(List<BookBean> books);

    /**
     * 从本地数据库查询单个预约信息
     *
     * @param bookId 预约信息ID
     */
    void queryBookFromDB(long bookId);

    /**
     * 从本地数据库查找所有预约信息，在界面第一次加载的时候使用
     */
    void queryBooksFromDB();

    /**
     * 从服务器查询单个预约信息
     *
     * @param bookId 预约
     */
    void queryBookFromServer(long bookId);

    /**
     * 从服务器同步最新的预约信息，同时要更新本地数据库和界面
     */
    void queryBooksFromServer();
}
