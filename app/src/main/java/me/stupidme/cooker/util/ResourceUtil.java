package me.stupidme.cooker.util;

import android.app.ActivityManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.TimeZone;

/**
 * Created by StupidL on 2017/4/4.
 */

public class ResourceUtil {

    public static String getAppLabel(Context context) {

        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
        } catch (final PackageManager.NameNotFoundException ignored) {
        }
        return (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : "Unknown");
    }

    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes

            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static Intent createEmailOnlyChooserIntent(Context context, Intent source, CharSequence chooserTitle) {
        Stack<Intent> intents = new Stack<>();
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "562117676@qq.com", null));
        List<ResolveInfo> activities = context.getPackageManager().queryIntentActivities(i, 0);

        for (ResolveInfo ri : activities) {
            Intent target = new Intent(source);
            target.setPackage(ri.activityInfo.packageName);
            intents.add(target);
        }

        if (!intents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(intents.remove(0), chooserTitle);
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[intents.size()]));
            return chooserIntent;
        } else {
            return Intent.createChooser(source, chooserTitle);
        }
    }

    public static Intent createEmailIntent(Context context, StringBuilder builder,
                                           String imageUrl, String emailTo) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("*/*");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, ResourceUtil.getAppLabel(context) + " Feedback");

        if (imageUrl != null) {
            Uri uri = Uri.parse("file://" + imageUrl);
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
        emailIntent.putExtra(Intent.EXTRA_TEXT, builder.toString());
        return emailIntent;
    }

    public static String getAllDeviceInfo(Context context, boolean fromDialog) {

        StringBuilder stringBuilder = new StringBuilder();

        if (!fromDialog)
            stringBuilder = new StringBuilder("\n\n ==== SYSTEM-INFO ===\n\n");
        stringBuilder.append("\n Device: ").append(getDeviceInfo(context, Device.DEVICE_SYSTEM_VERSION));
        stringBuilder.append("\n SDK Version: ").append(getDeviceInfo(context, Device.DEVICE_VERSION));
        stringBuilder.append("\n App Version: ").append(getAppVersion(context));
        stringBuilder.append("\n Language: ").append(getDeviceInfo(context, Device.DEVICE_LANGUAGE));
        stringBuilder.append("\n TimeZone: ").append(getDeviceInfo(context, Device.DEVICE_TIME_ZONE));
        stringBuilder.append("\n Total Memory: ").append(getDeviceInfo(context, Device.DEVICE_TOTAL_MEMORY));
        stringBuilder.append("\n Free Memory: ").append(getDeviceInfo(context, Device.DEVICE_FREE_MEMORY));
        stringBuilder.append("\n Device Type: ").append(getDeviceInfo(context, Device.DEVICE_TYPE));
        stringBuilder.append("\n Data Type: ").append(getDataType(context));

        return stringBuilder.toString();
    }

    public static String getDeviceInfo(Context activity, Device device) {

        try {
            switch (device) {
                case DEVICE_LANGUAGE:
                    return Locale.getDefault().getDisplayLanguage();

                case DEVICE_TIME_ZONE:
                    return TimeZone.getDefault().getID();//(false, TimeZone.SHORT);

                case DEVICE_TOTAL_MEMORY:
                    if (Build.VERSION.SDK_INT >= 16)
                        return String.valueOf(getTotalMemory(activity));

                case DEVICE_FREE_MEMORY:
                    return String.valueOf(getFreeMemory(activity));

                case DEVICE_SYSTEM_VERSION:
                    return String.valueOf(getDeviceName());

                case DEVICE_VERSION:
                    return String.valueOf("SDK " + android.os.Build.VERSION.SDK_INT);

                case DEVICE_TYPE:
                    if (isTablet(activity)) {
                        if (getDeviceMoreThan5Inch(activity)) {
                            return "Tablet";
                        }
                        return "Mobile";
                    }
                    return "Mobile";
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static long getTotalMemory(Context activity) {
        try {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            return mi.totalMem / 1048576L;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static long getFreeMemory(Context activity) {
        try {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.getMemoryInfo(mi);
            return mi.availMem / 1048576L;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        }
        return Character.toUpperCase(first) + s.substring(1);
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean getDeviceMoreThan5Inch(Context activity) {
        try {
            DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
            float yInches = displayMetrics.heightPixels / displayMetrics.ydpi;
            float xInches = displayMetrics.widthPixels / displayMetrics.xdpi;
            double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
            return diagonalInches >= 7;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDataType(Context activity) {
        String type = "Mobile Data";
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        switch (tm.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                type = "Mobile Data 3G";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                type = "Mobile Data 4G";
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                type = "Mobile Data GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                type = "Mobile Data EDGE 2G";
                break;
        }
        return type;
    }

    public static String getAppVersion(Context context) {
        PackageInfo pInfo = null;
        String version = " ";
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public enum Device {
        DEVICE_TYPE, DEVICE_VERSION, DEVICE_SYSTEM_VERSION,
        DEVICE_LANGUAGE, DEVICE_TIME_ZONE,
        DEVICE_TOTAL_MEMORY, DEVICE_FREE_MEMORY,
    }

}
