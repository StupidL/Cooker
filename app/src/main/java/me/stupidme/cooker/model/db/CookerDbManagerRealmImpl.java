package me.stupidme.cooker.model.db;

import java.util.List;

import io.realm.Realm;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/4/15.
 */

public class CookerDbManagerRealmImpl implements CookerDbManager {

    private static volatile CookerDbManagerRealmImpl sInstance;

    private Realm mRealm;

    private CookerDbManagerRealmImpl() {
        mRealm = Realm.getDefaultInstance();
    }

    public static CookerDbManagerRealmImpl getInstance() {
        if (sInstance == null) {
            synchronized (CookerDbManagerRealmImpl.class) {
                if (sInstance == null)
                    sInstance = new CookerDbManagerRealmImpl();
            }
        }
        return sInstance;
    }

    @Override
    public boolean insertCooker(CookerBean cooker) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(cooker);
        mRealm.commitTransaction();
        return true;
    }

    @Override
    public boolean insertCookers(List<CookerBean> cookers) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(cookers);
        mRealm.commitTransaction();
        return true;
    }

    @Override
    public boolean deleteCooker(long cookerId) {
        mRealm.beginTransaction();
        CookerBean cookerBean = mRealm.where(CookerBean.class).equalTo("cookerId", cookerId).findFirstAsync();
        if (cookerBean != null) {
            cookerBean.deleteFromRealm();
        }
        mRealm.commitTransaction();
        return true;
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
    public CookerBean queryCooker(long cookerId) {
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
    public boolean updateCooker(CookerBean cooker) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(cooker);
        mRealm.commitTransaction();
        return true;
    }

    @Override
    public boolean updateCookers(List<CookerBean> cookers) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(cookers);
        mRealm.commitTransaction();
        return true;
    }

    @Override
    public boolean insertBook(BookBean book) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(book);
        mRealm.commitTransaction();
        return true;
    }

    @Override
    public boolean insertBooks(List<BookBean> books) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(books);
        mRealm.commitTransaction();
        return true;
    }

    @Override
    public boolean deleteBook(long bookId) {
        mRealm.beginTransaction();
        BookBean bookBean = mRealm.where(BookBean.class).equalTo("bookId", bookId).findFirstAsync();
        if (bookBean != null)
            bookBean.deleteFromRealm();
        mRealm.commitTransaction();
        return true;
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
    public BookBean queryBook(long bookId) {
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
    public boolean updateBook(BookBean book) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(book);
        mRealm.commitTransaction();
        return true;
    }

    @Override
    public boolean updateBooks(List<BookBean> books) {
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(books);
        mRealm.commitTransaction();
        return true;
    }
}
