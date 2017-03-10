package me.stupidme.cooker.view.cooker;

import java.util.List;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/9.
 */

public interface ICookerView {

    void setRefreshing(boolean show);

    void removeCooker(CookerBean cooker);

    void insertCooker(CookerBean cooker);

    void insertCookers(List<CookerBean> list);

    void updateCooker(int position, CookerBean cooker);

    void updateCookers(List<CookerBean> list);

    void showMessage(String message);
}
