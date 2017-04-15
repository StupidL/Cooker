package me.stupidme.cooker.service;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by StupidL on 2017/4/9.
 */

public class CookerUpdateHelper {

    private static final int DEF_CONN_TIMEOUT = 30000;
    private static final int DEF_READ_TIMEOUT = 30000;
    private Context context;

    private String url;

    private boolean hasNewVersion;

    private HttpURLConnection mConnection;

    public boolean check() {

        Callable<Boolean> callable = () -> {
            HttpURLConnection connection = createConnection();
            if (connection != null) {
                String result = getResultString(connection);
                return result != null && parseResult(result);
            }
            return false;
        };
        FutureTask<Boolean> futureTask = new FutureTask<>(callable);
        futureTask.run();

        try {
            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void install() {

    }

    private HttpURLConnection createConnection() {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(DEF_CONN_TIMEOUT);
            connection.setReadTimeout(DEF_READ_TIMEOUT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private String getResultString(HttpURLConnection connection) {
        String result = null;
        try {
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                InputStream is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String string = null;
                while ((string = reader.readLine()) != null) {
                    builder.append(string);
                }
                result = builder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    private boolean parseResult(String result) {
        Gson gson = new Gson();
        UpdateInfo info = gson.fromJson(result, UpdateInfo.class);
        if (info.version > 1)
            return true;
        return false;
    }

    public static class Builder {

        private Context context;
        private String url;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder checkInfoUrl(String url) {
            this.url = url;
            return this;
        }

        public CookerUpdateHelper build() {
            CookerUpdateHelper helper = new CookerUpdateHelper();
            helper.context = this.context;
            helper.url = this.url;
            return helper;
        }

    }

    public static class UpdateInfo {
        public int version;
    }

}
