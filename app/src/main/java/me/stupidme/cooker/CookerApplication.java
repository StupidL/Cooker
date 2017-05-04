package me.stupidme.cooker;

import android.app.Application;

import io.realm.Realm;
import me.stupidme.cooker.model.db.CookerDbManagerSQLiteImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.util.SharedPreferenceUtil;

/**
 * Created by StupidL on 2017/3/4.
 */

public class CookerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CookerDbManagerSQLiteImpl.init(getApplicationContext());
        CookerRetrofit.init(getApplicationContext());
        SharedPreferenceUtil.init(getApplicationContext());
        Realm.init(this);
    }
}
