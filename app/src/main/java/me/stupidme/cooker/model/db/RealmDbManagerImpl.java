package me.stupidme.cooker.model.db;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/4/30.
 */

public class RealmDbManagerImpl implements RealmCookerManager, RealmBookManager {

    private static final String TAG = "RealmDbManager";

    private static volatile RealmDbManagerImpl sInstance;

    private Realm mRealm;

    private RealmDbManagerImpl() {
        mRealm = Realm.getInstance(new RealmConfiguration.Builder().name("cooker_client.realm").build());
    }

    public static RealmDbManagerImpl getInstance() {
        if (sInstance == null) {
            synchronized (RealmDbManagerImpl.class) {
                if (sInstance == null)
                    sInstance = new RealmDbManagerImpl();
            }
        }
        return sInstance;
    }

    @Override
    public BookBean insertBook(BookBean bookBean) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(bookBean));
        return bookBean;
    }

    @Override
    public List<BookBean> insertBooks(List<BookBean> bookBeanList) {
        final List<BookBean> list = new ArrayList<>();
        mRealm.executeTransaction(realm -> list.addAll(realm.copyToRealmOrUpdate(bookBeanList)));
        return list;
    }

    @Override
    public List<BookBean> deleteBooks(String where, String equalTo) {
        final List<BookBean> bookBeanList = new ArrayList<>();
        mRealm.executeTransaction(realm -> {
            List<BookBean> list = realm.where(BookBean.class).equalTo(where, equalTo).findAllAsync();
            for (BookBean bookBean : list) {
                bookBeanList.add(bookBean);
                bookBean.deleteFromRealm();
            }
        });
        return bookBeanList;
    }

    @Override
    public List<BookBean> deleteBooks(String where, Long equalTo) {
        final List<BookBean> bookBeanList = new ArrayList<>();
        mRealm.executeTransaction(realm -> {
            List<BookBean> list = realm.where(BookBean.class).equalTo(where, equalTo).findAllAsync();
            for (BookBean bookBean : list) {
                bookBeanList.add(bookBean);
                bookBean.deleteFromRealm();
            }
        });
        return bookBeanList;
    }

    @Override
    public BookBean updateBook(BookBean bookBean) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(bookBean));
        return bookBean;
    }

    @Override
    public List<BookBean> updateBooks(List<BookBean> bookBeanList) {
        final List<BookBean> list = new ArrayList<>();
        mRealm.executeTransaction(realm -> list.addAll(realm.copyToRealmOrUpdate(bookBeanList)));
        return list;
    }

    @Override
    public List<BookBean> queryBooks(String where, String equalTo) {
        final List<BookBean> list = new ArrayList<>();
        mRealm.executeTransaction(realm -> list.addAll(realm.where(BookBean.class).equalTo(where, equalTo).findAllAsync()));
        return list;
    }

    @Override
    public List<BookBean> queryBooks(String where, Long equalTo) {
        final List<BookBean> list = new ArrayList<>();
        mRealm.executeTransaction(realm -> list.addAll(realm.where(BookBean.class).equalTo(where, equalTo).findAllAsync()));
        return list;
    }

    @Override
    public CookerBean insertCooker(CookerBean cookerBean) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(cookerBean));
        return cookerBean;
    }

    @Override
    public List<CookerBean> insertCookers(List<CookerBean> cookerBeanList) {
        final List<CookerBean> list = new ArrayList<>();
        mRealm.executeTransaction(realm -> list.addAll(realm.copyToRealmOrUpdate(cookerBeanList)));
        return list;
    }

    @Override
    public List<CookerBean> deleteCookers(String where, String equalTo) {
        final List<CookerBean> list = new ArrayList<>();
        mRealm.executeTransaction(realm -> {
            List<CookerBean> cookerBeanList = realm.where(CookerBean.class).equalTo(where, equalTo).findAllAsync();
            for (CookerBean cookerBean : cookerBeanList) {
                list.add(cookerBean);
                cookerBean.deleteFromRealm();
            }
        });
        return list;
    }

    @Override
    public List<CookerBean> deleteCookers(String where, Long equalTo) {
        final List<CookerBean> list = new ArrayList<>();
        mRealm.executeTransaction(realm -> {
            List<CookerBean> cookerBeanList = realm.where(CookerBean.class).equalTo(where, equalTo).findAllAsync();
            for (CookerBean cookerBean : cookerBeanList) {
                list.add(cookerBean);
                cookerBean.deleteFromRealm();
            }
        });
        return list;
    }

    @Override
    public CookerBean updateCooker(CookerBean cookerBean) {
        mRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(cookerBean));
        return cookerBean;
    }

    @Override
    public List<CookerBean> updateCookers(List<CookerBean> cookerBeanList) {
        final List<CookerBean> list = new ArrayList<>();
        mRealm.executeTransaction(realm -> list.addAll(realm.copyToRealmOrUpdate(cookerBeanList)));
        return list;
    }

    @Override
    public List<CookerBean> queryCookers(String where, String equalTo) {
        final List<CookerBean> list = new ArrayList<>();
        mRealm.executeTransaction(realm -> list.addAll(realm.where(CookerBean.class).equalTo(where, equalTo).findAllAsync()));
        return list;
    }

    @Override
    public List<CookerBean> queryCookers(String where, Long equalTo) {
        final List<CookerBean> list = new ArrayList<>();
        mRealm.executeTransaction(realm -> list.addAll(realm.where(CookerBean.class).equalTo(where, equalTo).findAllAsync()));
        return list;
    }

}
