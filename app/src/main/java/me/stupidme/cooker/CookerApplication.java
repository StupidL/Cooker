package me.stupidme.cooker;

import android.app.Application;

import me.stupidme.cooker.mock.ServerDbManagerImpl;
import me.stupidme.cooker.model.UserAvatarManager;
import me.stupidme.cooker.model.db.DbManagerImpl;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.util.SharedPreferenceUtil;

/**
 * Application.
 */

public class CookerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //inject application context to Database
        DbManagerImpl.init(getApplicationContext());
        //inject application context to Retrofit
        CookerRetrofit.init(getApplicationContext());
        //inject application context to SharedPreference
        SharedPreferenceUtil.init(getApplicationContext());
        //inject application context to mock server database
        ServerDbManagerImpl.init(this);
        //inject application context to avatar manager
        UserAvatarManager.init(this);
    }
}
