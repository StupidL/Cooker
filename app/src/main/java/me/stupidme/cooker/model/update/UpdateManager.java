package me.stupidme.cooker.model.update;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.util.PackageUtil;
import me.stupidme.cooker.model.http.CookerRetrofit;
import me.stupidme.cooker.model.http.HttpResult;
import okhttp3.ResponseBody;

/**
 * Created by StupidL on 2017/4/16.
 */

public class UpdateManager implements IUpdateManager {

    private CheckCallback mCheckCallback;

    private DownloadCallback mDownloadCallback;

    private String mFileUrl;
    private String mVersionName;
    private String mVersionChanges;
    private File mFile;

    private Context mContext;

    public UpdateManager(Context context) {
        mContext = context;
    }

    @Override
    public void check() {
        CookerRetrofit.getInstance().getCookerService()
                .checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResult<VersionBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HttpResult<VersionBean> value) {
                        if (mCheckCallback == null)
                            return;
                        if (value.getResultCode() != 200 || value.getData() == null) {
                            mCheckCallback.onResult(false, null, "no data available!");
                            return;
                        }
                        String name = value.getData().getVersionName();
                        String changes = value.getData().getChanges();
                        String url = value.getData().getUrl();
                        if (PackageUtil.getVersionName(mContext).equals(name)) {
                            mCheckCallback.onResult(false, null, "your current version is the last version!");
                            return;
                        }
                        if ("".equals(url)) {
                            mCheckCallback.onResult(false, null, "no file url found!");
                            return;
                        }
                        mFileUrl = url;
                        mVersionName = name;
                        mVersionChanges = changes;
                        mCheckCallback.onResult(true, url, changes);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCheckCallback.onResult(false, null, "Error: " + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void download() {
        CookerRetrofit.getInstance().getCookerService()
                .downloadFile(mFileUrl)
                .subscribeOn(Schedulers.io())
                .doOnNext(responseBody -> {
                    if (responseBody != null)
                        writeToDisk(responseBody);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody value) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mDownloadCallback.onFailure(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        if (mFile != null && mFile.exists()) {
                            mDownloadCallback.onSuccess(mFile.getAbsolutePath());
                            return;
                        }
                        mDownloadCallback.onFailure("download file failed!");
                    }
                });
    }

    @Override
    public void install() {
        Uri uri = Uri.fromFile(mFile);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(install);
    }

    private void writeToDisk(ResponseBody value) {
        mFile = new File(mContext.getExternalFilesDir(null) + File.separator + mVersionName + ".apk");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            byte[] fileReader = new byte[4096];

            long fileSize = value.contentLength();
            long fileSizeDownloaded = 0;

            inputStream = value.byteStream();
            outputStream = new FileOutputStream(mFile);

            while (true) {
                int read = inputStream.read(fileReader);

                if (read == -1) {
                    break;
                }

                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
                mDownloadCallback.onProgress(fileSizeDownloaded / fileSize);
            }
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public UpdateManager setCheckCallback(CheckCallback callback) {
        mCheckCallback = callback;
        return this;
    }

    public UpdateManager setDownloadCallback(DownloadCallback callback) {
        mDownloadCallback = callback;
        return this;
    }

    public interface CheckCallback {
        void onResult(boolean hasNewVersion, String url, String message);
    }

    public interface DownloadCallback {

        void onProgress(long percent);

        void onSuccess(String path);

        void onFailure(String message);
    }

}
