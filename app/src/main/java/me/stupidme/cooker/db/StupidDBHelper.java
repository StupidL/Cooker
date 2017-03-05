package me.stupidme.cooker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by StupidL on 2017/3/4.
 * 该类用来创建数据库。
 * 所有的预约记录和电饭锅信息都将保存在SQLite数据库中。
 */

public class StupidDBHelper extends SQLiteOpenHelper {

    // 数据库名称
    private static final String DB_NAME = "cooker.db";

    // 数据库版本号
    private static final int DB_VERSION = 1;

    // 表名
    public static final String BOOK_RECORD_TABLE_NAME = "book_record";

    // 预约ID
    public static final String BOOK_ID = "id";

    // 预约创建时间
    public static final String BOOK_CREATED_TIME = "createdTime";

    // 预约更新时间
    public static final String BOOK_UPDATED_TIME = "updatedTime";

    // 预约电饭锅名称
    public static final String BOOK_COOKER_NAME = "cookerName";

    // 预约电饭锅地点
    public static final String BOOK_COOKER_LOCATION = "cookerLocation";

    // 预约设置的人数
    public static final String BOOK_PEOPLE_COUNT = "peopleCount";

    // 预约设置的口味，可以设置为 “偏软”、 “偏硬”
    public static final String BOOK_TASTE = "taste";

    // 预约的当前状态， 可以设置为 “预约中”、 “已完成”
    public static final String BOOK_STATUS = "status";

    // 偏软口味
    public static final String BOOK_TASTE_SOFT = "soft";

    // 偏硬口味
    public static final String BOOK_TASTE_HARD = "hard";

    // 预约中状态
    public static final String BOOK_STATUS_BOOKING = "booking";

    // 已完成状态
    public static final String BOOK_STATUS_FINISHED = "finished";

    // 预约记录建表语句
    private static final String CREATE_TABLE_BOOK_RECORD = "CREATED TABLE "
            + BOOK_RECORD_TABLE_NAME + "( "
            + BOOK_ID + " INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL, "
            + BOOK_CREATED_TIME + " INTEGER, "
            + BOOK_UPDATED_TIME + " INTEGER, "
            + BOOK_COOKER_NAME + " VARCHAR(10), "
            + BOOK_COOKER_LOCATION + " VARCHAR(10), "
            + BOOK_PEOPLE_COUNT + " INT(2), "
            + BOOK_TASTE + " VARCHAR(4), "
            + BOOK_STATUS + " VARCHAR(10) "
            + ")";

    // 电饭锅信息表名
    public static final String COOKER_INFO_TABLE_NAME = "cooker_info";

    // 电饭锅ID
    public static final String COOKER_ID = "id";

    // 电饭锅名称
    public static final String COOKER_NAME = "name";

    // 电饭锅位置
    public static final String COOKER_LOCATION = "location";

    // 电饭锅状态
    public static final String COOKER_STATUS = "status";

    // 电饭锅使用中状态
    public static final String COOKER_STATUS_USING = "using";

    // 电饭锅空闲状态
    public static final String COOKER_STATUS_FREE = "free";

    // 电饭锅信息建表语句
    private static final String CREATE_TABLE_COOKER_INFO = "CREATE TABLE "
            + COOKER_INFO_TABLE_NAME + " ("
            + COOKER_ID + " INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL, "
            + COOKER_NAME + " VARCHAR(10), "
            + COOKER_LOCATION + " VARCHAR(20), "
            + COOKER_STATUS + " VARCHAR(10) "
            + ")";

    StupidDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOK_RECORD);
        db.execSQL(CREATE_TABLE_COOKER_INFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
