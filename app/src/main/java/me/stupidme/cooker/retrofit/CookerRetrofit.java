package me.stupidme.cooker.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by StupidL on 2017/3/8.
 */

public class CookerRetrofit {

    private static CookerRetrofit sInstance;
    private Retrofit mRetrofit;
    private CookerService mService;
    private OkHttpClient mClient;

    private CookerRetrofit() {
        mClient = new OkHttpClient.Builder().build();
        mRetrofit = new Retrofit.Builder().baseUrl(CookerService.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(mClient)
                .build();
        mService = mRetrofit.create(CookerService.class);
    }

    public static CookerRetrofit getInstance() {
        if (sInstance == null)
            sInstance = new CookerRetrofit();
        return sInstance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public CookerService getCookerService() {
        return mService;
    }

    public OkHttpClient getOkHttpClient() {
        return mClient;
    }
}
