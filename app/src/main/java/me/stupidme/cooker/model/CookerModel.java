package me.stupidme.cooker.model;

import java.util.List;

import me.stupidme.cooker.db.DBManager;

/**
 * Created by StupidL on 2017/3/7.
 */

public class CookerModel implements ICookerModel {

    private static CookerModel sInstance;

    private DBManager mManager;

    private CookerModel() {
        mManager = DBManager.getInstance();
    }

    public static CookerModel getInstance() {
        if (sInstance == null)
            sInstance = new CookerModel();
        return sInstance;
    }

    @Override
    public void deleteCooker(CookerBean bean) {
        mManager.deleteCooker(bean.getCookerId());
    }

    @Override
    public void insertCooker(CookerBean bean) {
        mManager.insertCooker(bean);
    }

    @Override
    public List<CookerBean> queryCookers() {
        return mManager.queryCookers();
    }

    @Override
    public void updateCooker(CookerBean bean) {
        mManager.updateCooker(bean);
    }

    @Override
    public void updateCookers(List<CookerBean> list) {
        list.forEach(this::updateCooker);
    }
}
