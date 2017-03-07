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
    void deleteFromDataBase(CookerBean bean);

    /**
     * 插入关于电饭锅的记录到数据库
     *
     * @param bean 电饭锅
     */
    void insertToDataBase(CookerBean bean);

    /**
     * 从数据库取出所有电饭锅信息
     */
    List<CookerBean> loadCookersFromDataBase();

    /**
     * 从网络上获取电饭锅信息并且更新状态到界面和数据库
     *
     * @param bean 电饭锅
     */
    CookerBean updateStatusToDataBase(CookerBean bean);

}
