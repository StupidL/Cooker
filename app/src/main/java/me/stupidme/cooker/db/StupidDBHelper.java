package me.stupidme.cooker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by StupidL on 2017/3/4.
 * 该类用来创建数据库。
 * 所有的预约记录都将保存在SQLite数据库中。
 */

public class StupidDBHelper extends SQLiteOpenHelper {

    /**
     * The name of database to be created.
     * 数据库名称
     */
    private static final String DB_NAME = "cooker.db";

    /**
     * the version number of database.
     * 数据库版本号
     */
    private static final int DB_VERSION = 1;

    /**
     * the name of table "book_record".
     * 表名
     */
    public static final String BOOK_RECORD_TABLE_NAME = "book_record";

    /**
     * the id of this book.
     * 预约ID
     */
    public static final String BOOK_ID = "id";

    /**
     * created time of this book.
     * 预约创建时间
     */
    public static final String BOOK_CREATED_TIME = "createdTime";

    /**
     * last update time of this book.
     * 预约更新时间
     */
    public static final String BOOK_UPDATED_TIME = "updatedTime";

    /**
     * name of the cooker.
     * 预约选中的电饭锅名称
     */
    public static final String BOOK_COOKER_NAME = "cookerName";

    /**
     * location of the cooker.
     * 预约选中的电饭锅地点
     */
    public static final String BOOK_COOKER_LOCATION = "cookerLocation";

    /**
     * people count
     * 预约设置的人数
     */
    public static final String BOOK_PEOPLE_COUNT = "peopleCount";

    /**
     * each book can choose a taste between soft and hard.
     * 预约设置的口味，可以设置为 “偏软”、 “偏硬”
     */
    public static final String BOOK_TASTE = "taste";

    /**
     * status of this book, which can be set as booking or finished.
     * 预约的当前状态， 可以设置为 “预约中”、 “已完成”
     */
    public static final String BOOK_STATUS = "status";

    /**
     * soft taste.
     * 偏软口味
     */
    public static final String BOOK_TASTE_SOFT = "soft";

    /**
     * hard taste.
     * 偏硬口味
     */
    public static final String BOOK_TASTE_HARD = "hard";

    /**
     * booking status.
     * 预约中状态
     */
    public static final String BOOK_STATUS_BOOKING = "booking";

    /**
     * finished status
     * 已完成状态
     */
    public static final String BOOK_STATUS_FINISHED = "finished";


    private static final String CREATE_TABLE_COOK_RECORD = "CREATED TABLE "
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

    StupidDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COOK_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
