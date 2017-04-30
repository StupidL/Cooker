package me.stupidme.cooker.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by StupidL on 2017/3/21.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cooker_app.db";

    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_COOKER = "CREATE TABLE cooker("
            + "userId INTEGER NOT NULL, "
            + "cookerId INTEGER UNIQUE NOT NULL, "
            + "cookerName VARCHAR(20) NOT NULL, "
            + "cookerLocation VARCHAR(20) NOT NULL, "
            + "cookerStatus VARCHAR(10) NOT NULL"
            + ")";

    private static final String CREATE_TABLE_BOOK = "CREATE TABLE book("
            + "userId INTEGER NOT NULL, "
            + "bookId INTEGER UNIQUE NOT NULL, "
            + "cookerId INTEGER NOT NULL, "
            + "cookerName VARCHAR(20) NOT NULL, "
            + "cookerLocation VARCHAR(20) NOT NULL, "
            + "cookerStatus VARCHAR(10) NOT NULL, "
            + "riceWeight INTEGER NOT NULL, "
            + "peopleCount INTEGER NOT NULL, "
            + "taste VARCHAR(10) NOT NULL, "
            + "time INTEGER NOT NULL"
            + ")";

    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COOKER);
        db.execSQL(CREATE_TABLE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
