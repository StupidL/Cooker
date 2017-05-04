package me.stupidme.cooker.model;

import java.util.List;

import me.stupidme.cooker.model.db.CookerDbManagerSQLiteImpl;

/**
 * Created by StupidL on 2017/3/7.
 */

public class CookerDbModelImpl implements CookerDbModel {

    private static CookerDbModelImpl sInstance;

    private CookerDbManagerSQLiteImpl mManager;

    private CookerDbModelImpl() {
        mManager = CookerDbManagerSQLiteImpl.getInstance();
    }

    public static CookerDbModelImpl getInstance() {
        if (sInstance == null) {
            synchronized (CookerDbModelImpl.class) {
                if (sInstance == null)
                    sInstance = new CookerDbModelImpl();
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
