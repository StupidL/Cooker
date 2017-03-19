package me.stupidme.cooker;

import android.app.Application;

import me.stupidme.cooker.db.StupidDBManager;
import me.stupidme.cooker.retrofit.CookerRetrofit;

/**
 * Created by StupidL on 2017/3/4.
 */

public class CookerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StupidDBManager.init(getApplicationContext());
        CookerRetrofit.init(getApplicationContext());
    }
}
