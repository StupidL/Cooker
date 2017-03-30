package me.stupidme.cooker;

import android.app.Application;

import me.stupidme.cooker.db.DBManager;
import me.stupidme.cooker.retrofit.CookerRetrofit;

/**
 * Created by StupidL on 2017/3/4.
 */

public class CookerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.init(getApplicationContext());
        CookerRetrofit.init(getApplicationContext());
    }
}
