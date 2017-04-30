package me.stupidme.cooker.model.db;

import java.util.List;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/4/30.
 */

public interface RealmCookerManager {

    CookerBean insertCooker(CookerBean cookerBean);

    List<CookerBean> insertCookers(List<CookerBean> cookerBeanList);

    List<CookerBean> deleteCookers(String where, String equalTo);

//    List<CookerBean> deleteCookers(Map<String,String> conditions);

    CookerBean updateCooker(CookerBean cookerBean);

    List<CookerBean> updateCookers(List<CookerBean> cookerBeanList);

    List<CookerBean> queryCookers(String where,String equalTo);

//    List<CookerBean> queryCookers(Map<String,String> conditions);
}
