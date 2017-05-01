package me.stupidme.cooker.view.cooker;

import java.util.List;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/9.
 */

public interface ICookerView {

    /**
     * 控制刷新控件的显示
     *
     * @param show true则显示，false则不显示
     */
    void setRefreshing(boolean show);

    /**
     * 界面上移除一个设备信息
     *
     * @param cooker 要移除的设备信息
     */
    void removeCooker(CookerBean cooker);

    /**
     * 批量删除设备信息
     *
     * @param cookers 设备集合
     */
    void removeCookers(List<CookerBean> cookers);

    /**
     * 插入一个设备信息
     *
     * @param cooker 要插入的设备信息
     */
    void insertCooker(CookerBean cooker);

    /**
     * 批量插入设备信息
     *
     * @param cookers 设备集合
     */
    void insertCookers(List<CookerBean> cookers);

    /**
     * 界面上批量插入设备信息，在从数据库获取数据的时候调用
     *
     * @param list 设备信息列表
     */
    void insertCookersFromDB(List<CookerBean> list);

    /**
     * 界面上更新某个设备信息
     *
     * @param position 设备在列表中的位置
     * @param cooker   要更新的设备
     */
    void updateCooker(int position, CookerBean cooker);

    /**
     * 界面上批量更新设备信息
     *
     * @param list 设备信息列表
     */
    void updateCookersFromServer(List<CookerBean> list);

    /**
     * 弹出提示信息
     *
     * @param message 要展示的信息
     */
    void showMessage(String message);

}
