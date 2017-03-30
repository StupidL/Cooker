package me.stupidme.cooker.presenter;

import java.util.List;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/7.
 * <p>
 * 该类定义了所有关于电饭锅信息的操作，同时影响服务器数据和本地数据库
 */

public interface ICookerPresenter {

    /**
     * 删除关于该电饭锅的信息
     *
     * @param cookerId 电饭锅ID
     */
    void deleteCooker(long cookerId);

    /**
     * 删除所有的电饭锅信息
     */
    void deleteCookers();

    /**
     * 插入一条电饭锅信息
     *
     * @param bean 电饭锅
     */
    void insertCooker(CookerBean bean);

    /**
     * 批量插入电饭锅信息
     *
     * @param cookers 电饭锅列表
     */
    void insertCookers(List<CookerBean> cookers);

    /**
     * 查询单个电饭锅信息
     *
     * @param cookerId 电饭锅ID
     */
    void queryCookerFromDB(long cookerId);

    /**
     * 从数据库加载所有的电饭锅信息
     */
    void queryCookersFromDB();

    /**
     * 更新单个电饭锅的信息，在本地更新了数据，并且要同步至服务器的情况下使用。
     * CookerBean在服务器可以根据id来查找更改之前的数据
     *
     * @param bean     电饭锅编辑之后的
     * @param position 原来电饭锅在列表的位置
     */
    void updateCooker(int position, CookerBean bean);

    /**
     * 查询单个电饭锅的信息
     *
     * @param position 电饭锅在列表的位置
     * @param cookerId 电饭锅ID
     */
    void queryCookerFromServer(int position, long cookerId);

    /**
     * 从服务器获取所有电饭锅信息
     */
    void queryCookersFromServer();
}
