package me.stupidme.cooker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.model.CookerBean;

/**
 * Created by StupidL on 2017/3/4.
 */

public class StupidDBManager {

    private static volatile StupidDBManager sInstance;

    private static WeakReference<Context> mContextWeakRef;

    private SQLiteDatabase mDataBase;

    public static void init(Context context) {
        mContextWeakRef = new WeakReference<>(context);
    }

    private StupidDBManager() {
        if (mContextWeakRef.get() != null) {
            mDataBase = new StupidDBHelper(mContextWeakRef.get()).getWritableDatabase();
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
        ContentValues values = new ContentValues();
        values.put(StupidDBHelper.COOKER_NAME, bean.getName());
        values.put(StupidDBHelper.COOKER_LOCATION, bean.getLocation());
        values.put(StupidDBHelper.COOKER_STATUS, bean.getStatus());
        mDataBase.insert(StupidDBHelper.COOKER_INFO_TABLE_NAME, null, values);
        Log.v("StupidDBManager", "CookerBean inserted: \n" + bean.toString());
    }

    /**
     * 删除一个电饭锅信息
     *
     * @param bean 电饭锅
     */
    public void deleteCooker(CookerBean bean) {
        mDataBase.delete(StupidDBHelper.COOKER_INFO_TABLE_NAME,
                StupidDBHelper.COOKER_NAME + " = ?",
                new String[]{bean.getName()});

        Log.v("StupidDBManager", " Delete a cooker: \n" + bean.toString());
    }

    /**
     * 更新一个电饭锅信息
     *
     * @param bean 电饭锅
     */
    public void updateCooker(CookerBean bean) {
        ContentValues values = new ContentValues();
        values.put(StupidDBHelper.COOKER_NAME, bean.getName());
        values.put(StupidDBHelper.COOKER_LOCATION, bean.getLocation());
        values.put(StupidDBHelper.COOKER_STATUS, bean.getStatus());
        mDataBase.update(StupidDBHelper.COOKER_INFO_TABLE_NAME, values,
                StupidDBHelper.COOKER_NAME + " =?",
                new String[]{bean.getName()});

        Log.v("StupidDBManager", "Update a cooker: \n" + bean.toString());
    }

    /**
     * 查询所有的电饭锅信息
     *
     * @return 信息列表
     */
    public List<CookerBean> queryCookers() {
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
    }

}
