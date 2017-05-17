package me.stupidme.cooker;

import android.app.Application;

import me.stupidme.cooker.mock.ServerDbManagerImpl;
import me.stupidme.cooker.model.db.DbManagerImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.util.SharedPreferenceUtil;

/**
 * Created by StupidL on 2017/3/4.
 */

public class CookerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DbManagerImpl.init(getApplicationContext());
        CookerRetrofit.init(getApplicationContext());
        SharedPreferenceUtil.init(getApplicationContext());
        ServerDbManagerImpl.init(this);
    }
}
