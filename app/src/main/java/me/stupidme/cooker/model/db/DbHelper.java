package me.stupidme.cooker.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class is aimed to create three tables to the database.
 */

public class DbHelper extends SQLiteOpenHelper {

    /**
     * name of the database.
     */
    private static final String DB_NAME = "cooker_app.db";

    /**
     * version of the database.
     */
    private static final int DB_VERSION = 1;

    /**
     * sql string to create table 'cooker'.
     */
    private static final String CREATE_TABLE_COOKER = "CREATE TABLE cooker("
            + "userId INTEGER NOT NULL, "
            + "cookerId INTEGER UNIQUE NOT NULL, "
            + "cookerName VARCHAR(20) NOT NULL, "
            + "cookerLocation VARCHAR(20) NOT NULL, "
            + "cookerStatus VARCHAR(10) NOT NULL"
            + ")";

    /**
     * sql string to create table 'book'.
     */
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

    /**
     * sql string to create table 'book_history'.
     */
    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE book_history("
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

    DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COOKER);
        db.execSQL(CREATE_TABLE_BOOK);
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
