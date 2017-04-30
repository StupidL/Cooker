package me.stupidme.cooker.model.db;

import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;
import me.stupidme.cooker.model.UserBean;

/**
 * Created by StupidL on 2017/4/30.
 */

public class RealmServerManager implements IServerDbManager {

    private static volatile RealmServerManager sInstance;

    private Realm mRealm;

    private RealmServerManager() {
        mRealm = Realm.getInstance(new RealmConfiguration.Builder().name("cooker_server").build());
    }

    public static RealmServerManager getInstance() {
        if (sInstance == null) {
            synchronized (RealmManager.class) {
                if (sInstance == null)
                    sInstance = new RealmServerManager();
            }
        }
        return sInstance;
    }

    @Override
    public UserBean insertUser(UserBean userBean) {
        mRealm.beginTransaction();
        userBean.setUserId(new Random().nextLong());
        UserBean bean = mRealm.copyToRealmOrUpdate(userBean);
        mRealm.commitTransaction();
        return bean;
    }

    @Override
    public UserBean queryUser(Long userId) {
        mRealm.beginTransaction();
        UserBean userBean = mRealm.where(UserBean.class).equalTo("userId", userId).findFirst();
        mRealm.commitTransaction();
        return userBean;
    }

    @Override
    public CookerBean insertCooker(CookerBean cooker) {
        mRealm.beginTransaction();
        CookerBean bean = mRealm.copyToRealmOrUpdate(cooker);
        mRealm.commitTransaction();
        return bean;
    }

    @Override
    public List<CookerBean> insertCookers(List<CookerBean> cookers) {
        mRealm.beginTransaction();
        List<CookerBean> cookerBeanList = mRealm.copyToRealmOrUpdate(cookers);
        mRealm.commitTransaction();
        return cookerBeanList;
    }

    @Override
    public CookerBean deleteCooker(Long cookerId) {
        mRealm.beginTransaction();
        CookerBean cookerBean = mRealm.where(CookerBean.class).equalTo("cookerId", cookerId).findFirstAsync();
        if (cookerBean != null) {
            cookerBean.deleteFromRealm();
        }
        mRealm.commitTransaction();
        return cookerBean;
    }

    @Override
    public boolean deleteCookers() {
        mRealm.beginTransaction();
        List<CookerBean> cookerBeanList = mRealm.where(CookerBean.class).findAllAsync();
        if (cookerBeanList.size() > 0) {
            for (CookerBean cookerBean : cookerBeanList)
                cookerBean.deleteFromRealm();
        }
        mRealm.commitTransaction();
        return true;
    }

    @Override
    public CookerBean queryCooker(Long cookerId) {
        mRealm.beginTransaction();
        CookerBean cookerBean = mRealm.where(CookerBean.class).equalTo("cookerId", cookerId).findFirstAsync();
        mRealm.commitTransaction();
        return cookerBean;
    }

    @Override
    public List<CookerBean> queryCookers() {
        mRealm.beginTransaction();
        List<CookerBean> cookerBeanList = mRealm.where(CookerBean.class).findAllAsync();
        mRealm.commitTransaction();
        return cookerBeanList;
    }

    @Override
    public CookerBean updateCooker(CookerBean cooker) {
        mRealm.beginTransaction();
        CookerBean bean = mRealm.copyToRealmOrUpdate(cooker);
        mRealm.commitTransaction();
        return bean;
    }

    @Override
    public List<CookerBean> updateCookers(List<CookerBean> cookers) {
        mRealm.beginTransaction();
        List<CookerBean> beanList = mRealm.copyToRealmOrUpdate(cookers);
        mRealm.commitTransaction();
        return beanList;
    }

    @Override
    public BookBean insertBook(BookBean book) {
        mRealm.beginTransaction();
        BookBean bookBean = mRealm.copyToRealmOrUpdate(book);
        mRealm.commitTransaction();
        return bookBean;
    }

    @Override
    public List<BookBean> insertBooks(List<BookBean> books) {
        mRealm.beginTransaction();
        List<BookBean> bookBeanList = mRealm.copyToRealmOrUpdate(books);
        mRealm.commitTransaction();
        return bookBeanList;
    }

    @Override
    public BookBean deleteBook(Long bookId) {
        mRealm.beginTransaction();
        BookBean bookBean = mRealm.where(BookBean.class).equalTo("bookId", bookId).findFirstAsync();
        if (bookBean != null)
            bookBean.deleteFromRealm();
        mRealm.commitTransaction();
        return bookBean;
    }

    @Override
    public boolean deleteBooks() {
        mRealm.beginTransaction();
        List<BookBean> bookBeanList = mRealm.where(BookBean.class).findAllAsync();
        if (bookBeanList.size() > 0) {
            for (BookBean bookBean : bookBeanList)
                bookBean.deleteFromRealm();
        }
        mRealm.commitTransaction();
        return true;
    }

    @Override
    public BookBean queryBook(Long bookId) {
        mRealm.beginTransaction();
        BookBean bookBean = mRealm.where(BookBean.class).equalTo("bookId", bookId).findFirstAsync();
        mRealm.commitTransaction();
        return bookBean;
    }

    @Override
    public List<BookBean> queryBooks() {
        mRealm.beginTransaction();
        List<BookBean> bookBeanList = mRealm.where(BookBean.class).findAllAsync();
        mRealm.commitTransaction();
        return bookBeanList;
    }

    @Override
    public BookBean updateBook(BookBean book) {
        mRealm.beginTransaction();
        BookBean bookBean = mRealm.copyToRealmOrUpdate(book);
        mRealm.commitTransaction();
        return bookBean;
    }

    @Override
    public List<BookBean> updateBooks(List<BookBean> books) {
        mRealm.beginTransaction();
        List<BookBean> bookBeanList = mRealm.copyToRealmOrUpdate(books);
        mRealm.commitTransaction();
        return bookBeanList;
    }
}
