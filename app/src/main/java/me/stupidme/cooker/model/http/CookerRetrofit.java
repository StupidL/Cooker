package me.stupidme.cooker.model.http;

import android.content.Context;

import java.lang.ref.WeakReference;

import me.stupidme.cooker.mock.MockCookerService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * A class to manage retrofit service.
 */

public class CookerRetrofit {

    /**
     * Application Context, used in shared preference.
     */
    private static WeakReference<Context> mContextRef;

    /**
     * Single instance.
     */
    private volatile static CookerRetrofit sInstance;

    /**
     * Retrofit instance to create {@link #mService}.
     */
    private Retrofit mRetrofit;

    /**
     * A interface to request data from server.
     */
    private CookerService mService;

    /**
     * A client instance of {@link OkHttpClient}.
     */
    private OkHttpClient mClient;

    /**
     * A mock server for test.
     */
    private MockCookerService mMockService;

    /**
     * A weak reference of context.
     *
     * @param context application context
     */
    public static void init(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    private CookerRetrofit() {
        Context context = mContextRef.get();
        if (context != null) {
            mClient = new OkHttpClient.Builder()
                    .build();

            NetworkBehavior behavior = NetworkBehavior.create();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(CookerService.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(mClient)
                    .build();

            MockRetrofit mockRetrofit = new MockRetrofit.Builder(mRetrofit)
                    .networkBehavior(behavior)
                    .build();

            BehaviorDelegate<CookerService> behaviorDelegate = mockRetrofit.create(CookerService.class);
            mMockService = new MockCookerService(behaviorDelegate);

            mService = mRetrofit.create(CookerService.class);
        }
    }

    /**
     * DCL singleton pattern.
     *
     * @return instance of {@link CookerRetrofit}
     */
    public static CookerRetrofit getInstance() {
        if (sInstance == null) {
            synchronized (CookerRetrofit.class) {
                if (sInstance == null)
                    sInstance = new CookerRetrofit();
            }
        }
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

    public MockCookerService getMockService() {
        return mMockService;
    }
}
