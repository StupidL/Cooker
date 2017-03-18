package me.stupidme.cooker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/4.
 */

public class StupidDBManager {

    private static volatile StupidDBManager sInstance;

    private static WeakReference<Context> mContextWeakRef;

    private SQLiteDatabase mDataBase;

    private ExecutorService mExecutor;

    public static void init(Context context) {
        mContextWeakRef = new WeakReference<>(context);
    }

    private StupidDBManager() {
        if (mContextWeakRef.get() != null) {
            mDataBase = new StupidDBHelper(mContextWeakRef.get()).getWritableDatabase();
            mExecutor = Executors.newCachedThreadPool();
        } else {
            throw new RuntimeException("Context is null. Create Database failed.");
        }
    }

    /**
     * Singleton pattern
     *
     * @return instance of StupidDBManager
     */
    public static StupidDBManager getInstance() {
        if (sInstance == null) {
            synchronized (StupidDBManager.class) {
                if (sInstance == null)
                    sInstance = new StupidDBManager();
            }
        }
        return sInstance;
    }

    /**
     * 插入一个电饭锅信息
     *
     * @param bean 电饭锅
     */
    public void insertCooker(CookerBean bean) {
        mExecutor.submit(() -> {
            ContentValues values = new ContentValues();
            values.put(StupidDBHelper.COOKER_NAME, bean.getName());
            values.put(StupidDBHelper.COOKER_LOCATION, bean.getLocation());
            values.put(StupidDBHelper.COOKER_STATUS, bean.getStatus());
            mDataBase.insert(StupidDBHelper.COOKER_INFO_TABLE_NAME, null, values);
            Log.v("StupidDBManager", "CookerBean inserted: \n" + bean.toString());
        });
    }

    /**
     * 删除一个电饭锅信息
     *
     * @param bean 电饭锅
     */
    public void deleteCooker(CookerBean bean) {
        mExecutor.submit(() -> {
            mDataBase.delete(StupidDBHelper.COOKER_INFO_TABLE_NAME,
                    StupidDBHelper.COOKER_NAME + " = ?",
                    new String[]{bean.getName()});

            Log.v("StupidDBManager", " Delete a cooker: \n" + bean.toString());
        });
    }

    /**
     * 更新一个电饭锅信息
     *
     * @param bean 电饭锅
     */
    public void updateCooker(CookerBean bean) {
        mExecutor.submit(() -> {
            ContentValues values = new ContentValues();
            values.put(StupidDBHelper.COOKER_NAME, bean.getName());
            values.put(StupidDBHelper.COOKER_LOCATION, bean.getLocation());
            values.put(StupidDBHelper.COOKER_STATUS, bean.getStatus());
            mDataBase.update(StupidDBHelper.COOKER_INFO_TABLE_NAME, values,
                    StupidDBHelper.COOKER_NAME + " =?",
                    new String[]{bean.getName()});

            Log.v("StupidDBManager", "Update a cooker: \n" + bean.toString());
        });
    }

    /**
     * 查询所有的电饭锅信息
     *
     * @return 信息列表
     */
    public List<CookerBean> queryCookers() {
        Future<List<CookerBean>> future = mExecutor.submit(() -> {

            List<CookerBean> list = new ArrayList<>();
            Cursor cursor = mDataBase.rawQuery("SELECT * FROM " + StupidDBHelper.COOKER_INFO_TABLE_NAME +
                    " WHERE " + StupidDBHelper.COOKER_ID + " >= ?", new String[]{"0"});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CookerBean cooker = new CookerBean();
                cooker.setName(cursor.getString(cursor.getColumnIndex(StupidDBHelper.COOKER_NAME)));
                cooker.setLocation(cursor.getString(cursor.getColumnIndex(StupidDBHelper.COOKER_LOCATION)));
                cooker.setStatus(cursor.getString(cursor.getColumnIndex(StupidDBHelper.COOKER_STATUS)));
                list.add(cooker);
                cursor.moveToNext();
            }
            cursor.close();

            Log.v("StupidDBManager", " Cooker List Size: " + list.size());

            return list;
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 插入一个预约信息,要防止数据重复！！！
     *
     * @param bean 预约信息
     */
    public void insertBook(BookBean bean) {
//        ContentValues values = new ContentValues();
//        values.put(StupidDBHelper.BOOK_ID, bean.getDeviceId());
//        values.put(StupidDBHelper.BOOK_COOKER_NAME, bean.getDeviceName());
//        values.put(StupidDBHelper.BOOK_COOKER_LOCATION, bean.getDevicePlace());
//        values.put(StupidDBHelper.BOOK_PEOPLE_COUNT, bean.getPeopleCount());
//        values.put(StupidDBHelper.BOOK_RICE_WEIGHT, bean.getRiceWeight());
//        values.put(StupidDBHelper.BOOK_STATUS, bean.getDeviceStatus());
//        values.put(StupidDBHelper.BOOK_TIME, bean.getTime());
//        mDataBase.insert(StupidDBHelper.BOOK_RECORD_TABLE_NAME, null, values);
    }

    /**
     * 批量插入预约信息。为防止重复，可以先清空表再插入，也可以replace插入
     *
     * @param list 预约信息列表
     */
    public void insertBooks(List<BookBean> list) {

    }

    /**
     * 删除一个预约信息
     *
     * @param bean 预约信息
     */
    public void deleteBook(BookBean bean) {

    }

    /**
     * 更新一个预约信息
     *
     * @param bean 预约信息
     */
    public void updateBook(BookBean bean) {

    }

    /**
     * 批量更新预约信息
     *
     * @param list 预约信息列表
     */
    public void updateBooks(List<BookBean> list) {

    }

    /**
     * 查询所有预约信息
     *
     * @return 预约信息列表
     */
    public List<BookBean> queryBooks() {
        return new ArrayList<>();
    }

}
