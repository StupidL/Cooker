package me.stupidme.cooker.model;

import java.util.List;

import me.stupidme.cooker.model.db.DBManager;

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
        if (sInstance == null) {
            synchronized (CookerModel.class) {
                if (sInstance == null)
                    sInstance = new CookerModel();
            }
        }
        return sInstance;
    }

    @Override
    public void deleteCooker(long cookerId) {
        mManager.deleteCooker(cookerId);
    }

    @Override
    public void deleteCookers() {
        mManager.deleteCookers();
    }

    @Override
    public void insertCooker(CookerBean bean) {
        mManager.insertCooker(bean);
    }

    @Override
    public void insertCookers(List<CookerBean> cookers) {
        mManager.insertCookers(cookers);
    }

    @Override
    public List<CookerBean> queryCookers() {
        return mManager.queryCookers();
    }

    @Override
    public CookerBean queryCooker(long cookerId) {
        return mManager.queryCooker(cookerId);
    }

    @Override
    public void updateCooker(CookerBean bean) {
        mManager.updateCooker(bean);
    }

    @Override
    public void updateCookers(List<CookerBean> list) {
        mManager.updateCookers(list);
    }
}
