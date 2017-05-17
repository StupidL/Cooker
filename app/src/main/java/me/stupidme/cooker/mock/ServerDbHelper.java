package me.stupidme.cooker.mock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by StupidL on 2017/5/1.
 */

public class ServerDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cooker_server.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE_USER = "CREATE TABLE user_server("
            + "userId INTEGER UNIQUE NOT NULL, "
            + "userName VARCHAR NOT NULL, "
            + "userPassword VARCHAR NOT NULL"
            + ")";

    private static final String CREATE_TABLE_COOKER_SERVER = "CREATE TABLE cooker_server("
            + "userId INTEGER NOT NULL, "
            + "cookerId INTEGER UNIQUE NOT NULL, "
            + "cookerName VARCHAR(20) NOT NULL, "
            + "cookerLocation VARCHAR(20) NOT NULL, "
            + "cookerStatus VARCHAR(10) NOT NULL"
            + ")";

    private static final String CREATE_TABLE_BOOK_SERVER = "CREATE TABLE book_server("
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

    public ServerDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_BOOK_SERVER);
        db.execSQL(CREATE_TABLE_COOKER_SERVER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
