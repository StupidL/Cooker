package me.stupidme.cooker.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by StupidL on 2017/3/20.
 */

public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        request = request.newBuilder().addHeader("Cookie", "").build();

        return chain.proceed(request);
    }
}
