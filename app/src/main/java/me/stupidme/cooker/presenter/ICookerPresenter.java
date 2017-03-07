package me.stupidme.cooker.presenter;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/7.
 */

public interface ICookerPresenter {

    /**
     * 删除数据库里面关于该电饭锅的信息
     *
     * @param bean 电饭锅
     */
    void onDeleteCooker(CookerBean bean);

    /**
     * 插入一条电饭锅信息到数据库
     *
     * @param bean 电饭锅
     */
    void onInsertCooker(CookerBean bean);

    /**
     * 从数据库加载所有的电饭锅信息
     */
    void onLoadFromDataBase();

    /**
     * 更新电饭锅的状态
     *
     * @param bean     电饭锅
     * @param position 原来电饭锅在列表的位置
     */
    void onUpdateStatus(int position, CookerBean bean);
}
