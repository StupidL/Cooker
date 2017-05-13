package me.stupidme.cooker.model.http;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

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
 * Created by StupidL on 2017/3/8.
 */

public class CookerRetrofit {

    /**
     * Application Context, 持久化Cookie用到SharedPreference，需要Context
     */
    private static WeakReference<Context> mContextRef;

    /**
     * 单例,线程安全
     */
    private static CookerRetrofit sInstance;

    /**
     * 对外提供获取Retrofit对象的接口
     */
    private Retrofit mRetrofit;

    /**
     * 对外提供获取CookerService对象的接口
     */
    private CookerService mService;

    /**
     * 对外提供OkHttpClient对象的接口
     */
    private OkHttpClient mClient;

    /**
     * Cookie本地持久化存储
     */
    private ClearableCookieJar mCookieJar;

    private MockCookerService mMockService;

    /**
     * 静态方法，用来获取Application Context，并且本类持有该Context的弱引用
     *
     * @param context application context
     */
    public static void init(Context context) {
        mContextRef = new WeakReference<>(context);
    }

    /**
     * 私有构造器
     */
    private CookerRetrofit() {
        Context context = mContextRef.get();
        if (context != null) {
            mCookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
            mClient = new OkHttpClient.Builder()
                    .cookieJar(mCookieJar)
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
     * 获取单例对象
     *
     * @return 单例
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

    /**
     * 获取Retrofit对象
     *
     * @return mRetrofit成员
     */
    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    /**
     * 获取Service对象
     *
     * @return mService成员
     */
    public CookerService getCookerService() {
        return mService;
    }

    /**
     * 获取OkHttpClient对象
     *
     * @return mClient成员
     */
    public OkHttpClient getOkHttpClient() {
        return mClient;
    }

    /**
     * 获取PersistentCookieJar对象
     *
     * @return mCookieJar成员
     */
    public ClearableCookieJar getCookieJar() {
        return mCookieJar;
    }

    public MockCookerService getMockService() {
        return mMockService;
    }
}
