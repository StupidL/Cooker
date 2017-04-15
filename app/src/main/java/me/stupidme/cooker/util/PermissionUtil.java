package me.stupidme.cooker.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/4/4.
 */

public class PermissionUtil {

    public static final int REQUEST_PERMISSIONS = 0xa0;

    public static final int REQUEST_SELECT_IMAGES = 0xa1;

    public static final int REQUEST_APP_SETTINGS = 0xa2;

    public static void attemptSelectImages(AppCompatActivity activity) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = activity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
            }
//            selectImage(activity);
        }
        selectImage(activity);
    }

    public static void selectImage(AppCompatActivity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.chooser_title)),
                REQUEST_SELECT_IMAGES);
    }

    public static void showTipsDialog(AppCompatActivity activity) {
        new AlertDialog.Builder(activity)
                .setMessage(activity.getString(R.string.feedback_permission_tips))
                .setPositiveButton("OK", (dialog, which) -> startSystemSettings(activity))
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static void startSystemSettings(AppCompatActivity activity) {
        Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity.getPackageName()));
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
        myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(myAppSettings, REQUEST_APP_SETTINGS);
    }

    public static boolean hasPermissions(Context context, @NonNull String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(context, permission))
                return false;
        return true;
    }
}
