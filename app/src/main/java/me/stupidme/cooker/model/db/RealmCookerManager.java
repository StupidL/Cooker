package me.stupidme.cooker.model.db;

import java.util.List;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/4/30.
 */

public interface RealmCookerManager {

    String KEY_USER_ID = "userId";
    String KEY_COOKER_ID = "cookerId";
    String KEY_COOKER_NAME = "cookerName";
    String KEY_COOKER_LOCATION = "cookerLocation";
    String KEY_COOKER_STATUS = "cookerStatus";

    CookerBean insertCooker(CookerBean cookerBean);

    List<CookerBean> insertCookers(List<CookerBean> cookerBeanList);

    List<CookerBean> deleteCookers(String where, String equalTo);

//    List<CookerBean> deleteCookers(Map<String,String> conditions);

    CookerBean updateCooker(CookerBean cookerBean);

    List<CookerBean> updateCookers(List<CookerBean> cookerBeanList);

    List<CookerBean> queryCookers(String where, String equalTo);

//    List<CookerBean> queryCookers(Map<String,String> conditions);
}
