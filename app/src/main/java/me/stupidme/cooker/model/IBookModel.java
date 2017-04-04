package me.stupidme.cooker.model;

import java.util.List;

/**
 * Created by StupidL on 2017/3/8.
 * <p>
 * 该类定义了本地数据的所有操作，只影响本地数据，不会印象服务器数据
 */

public interface IBookModel {

    /**
     * 插入一条预约信息到数据库
     *
     * @param book 预约信息
     */
    void insertBook(BookBean book);

    /**
     * 批量插入预约信息到数据库
     *
     * @param list 预约列表
     */
    void insertBooks(List<BookBean> list);

    /**
     * 更新某个预约的信息到数据库
     *
     * @param book 预约项
     */
    void updateBook(BookBean book);

    /**
     * 批量更新。可以删除所有数据再插入
     *
     * @param list 预约列表
     */
    void updateBooks(List<BookBean> list);

    /**
     * 从本地数据库查询所有的预约信息
     *
     * @return 预约信息列表
     */
    List<BookBean> queryBooks();

    /**
     * 查询本地数据库中某个具体的预约
     *
     * @param bookId 预约ID
     * @return 预约
     */
    BookBean queryBook(long bookId);

    /**
     * 删除本地数据库某条预约记录
     *
     * @param book 预约
     */
    void deleteBook(BookBean book);

    /**
     * 删除数据库所有的预约记录
     */
    void deleteBooks();

}
