package me.stupidme.cooker.model;

import java.util.List;

/**
 * Created by StupidL on 2017/3/7.
 * <p>
 * 该类定义了本地数据库对<code>CookerBean</code>的所有操作，所有的变动只影响本地数据库，不影响服务器。
 */

public interface ICookerModel {

    /**
     * 删除数据库的关于该电饭锅记录
     *
     * @param cookerId 电饭锅ID
     */
    void deleteCooker(long cookerId);

    /**
     * 删除本地数据库所有的电饭锅记录
     */
    void deleteCookers();

    /**
     * 插入关于电饭锅的记录到数据库
     *
     * @param bean 电饭锅
     */
    void insertCooker(CookerBean bean);

    /**
     * 批量插入电饭锅信息到本地数据库
     *
     * @param cookers 电饭锅列表
     */
    void insertCookers(List<CookerBean> cookers);

    /**
     * 从数据库取出所有电饭锅信息
     */
    List<CookerBean> queryCookers();

    /**
     * 查询某个电饭锅信息
     *
     * @param cookerId 电饭锅ID
     * @return 电饭锅信息
     */
    CookerBean queryCooker(long cookerId);

    /**
     * 更新状态到数据库
     *
     * @param bean 电饭锅
     */
    void updateCooker(CookerBean bean);

    /**
     * 批量更新状态到数据库
     *
     * @param list 电饭锅列表
     */
    void updateCookers(List<CookerBean> list);

}
