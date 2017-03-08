package me.stupidme.cooker.presenter;

import java.util.List;

import me.stupidme.cooker.model.BookBean;

/**
 * Created by StupidL on 2017/3/8.
 */

public interface IBookPresenter {

    /**
     * 数据库增加一个预约记录，并且需要上传服务器
     *
     * @param bookBean 预约记录
     */
    void addBook(BookBean bookBean);

    /**
     * 得到所有电饭锅的名称，以提供选择
     *
     * @return 电饭锅名称列表
     */
    List<String> getCookersName();

}
