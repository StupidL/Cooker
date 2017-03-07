package me.stupidme.cooker.view;

import java.util.List;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/7.
 */

public interface ICookerFragmentView {

    /**
     * 更新电饭锅的状态
     *
     * @param bean     电饭锅
     * @param position 电饭锅的位置
     */
    void updateStatusToView(int position, CookerBean bean);

    /**
     * 移除该电饭锅，界面上移除并且数据库也要删除
     *
     * @param bean 电饭锅
     */
    void removeItem(CookerBean bean);

    /**
     * 增加一个电饭锅信息，界面上要添加，数据库也要增加记录
     *
     * @param bean 电饭锅
     */
    void insertItem(CookerBean bean);

    /**
     * 从数据库加载所有的电饭锅信息
     *
     * @param list 电饭锅列表
     */
    void loadCookersFromDataBase(List<CookerBean> list);

}
