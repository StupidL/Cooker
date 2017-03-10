package me.stupidme.cooker.presenter;

import java.util.Map;

import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/3/8.
 */

public interface IBookPresenter {

    /**
     * 插入一条预约信息，本地数据库和服务器均要插入
     *
     * @param book 预约
     */
    void insertBook(BookBean book);

    /**
     * 删除一个预约，本地数据库和服务器均要删除
     *
     * @param book 预约
     */
    void deleteBook(BookBean book);

    /**
     * 从本地数据库查找所有预约信息，在界面第一次加载的时候使用
     */
    void queryBooksFromDB();

    /**
     * 从服务器同步最新的预约信息，同时要更新本地数据库和界面
     *
     * @param map 查询参数
     */
    void queryBooksFromServer(Map<String, String> map);
}
