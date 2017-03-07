package me.stupidme.cooker.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by StupidL on 2017/3/7.
 */

public class CookerModel implements ICookerModel {

    private static CookerModel sInstance;

    private CookerModel() {

    }

    public static CookerModel getInstance() {
        if (sInstance == null)
            sInstance = new CookerModel();
        return sInstance;
    }

    @Override
    public void deleteFromDataBase(CookerBean bean) {

    }

    @Override
    public void insertToDataBase(CookerBean bean) {

    }

    @Override
    public List<CookerBean> loadCookersFromDataBase() {
        List<CookerBean> list = new ArrayList<>();

        return list;
    }

    @Override
    public CookerBean updateStatusToDataBase(CookerBean bean) {
        CookerBean b = new CookerBean();

        return b;
    }
}
