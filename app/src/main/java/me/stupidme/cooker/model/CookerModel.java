package me.stupidme.cooker.model;

import java.util.List;

import me.stupidme.cooker.db.StupidDBManager;

/**
 * Created by StupidL on 2017/3/7.
 */

public class CookerModel implements ICookerModel {

    private static CookerModel sInstance;

    private StupidDBManager mManager;

    private CookerModel() {
        mManager = StupidDBManager.getInstance();
    }

    public static CookerModel getInstance() {
        if (sInstance == null)
            sInstance = new CookerModel();
        return sInstance;
    }

    @Override
    public void deleteCooker(CookerBean bean) {
        mManager.deleteCooker(bean);
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
