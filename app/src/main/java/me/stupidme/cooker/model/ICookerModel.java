package me.stupidme.cooker.model;

import java.util.List;

/**
 * Created by StupidL on 2017/3/7.
 */

public interface ICookerModel {

    /**
     * 删除数据库的关于该电饭锅记录
     *
     * @param bean 电饭锅
     */
    void deleteCooker(CookerBean bean);

    /**
     * 插入关于电饭锅的记录到数据库
     *
     * @param bean 电饭锅
     */
    void insertCooker(CookerBean bean);

    /**
     * 从数据库取出所有电饭锅信息
     */
    List<CookerBean> queryCookers();

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
