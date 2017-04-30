package me.stupidme.cooker.mock;

import java.io.IOException;

import me.stupidme.cooker.BuildConfig;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by StupidL on 2017/4/30.
 */

public class MockInterceptor implements Interceptor {


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        if (BuildConfig.DEBUG) {
            HttpUrl url = chain.request().url();

        } else
            response = chain.proceed(chain.request());
        return response;
    }
}
