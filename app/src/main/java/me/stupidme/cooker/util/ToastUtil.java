package me.stupidme.cooker.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/5/9.
 */

public class ToastUtil {

    private static Toast mToast;

    public static void showToastShort(Context context, CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(message);
        mToast.setView(view);
        mToast.show();
    }

    public static void showToastLong(Context context, CharSequence message) {
        if (mToast == null) {
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.toast, null);
        TextView textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(message);
        mToast.setView(view);
        mToast.show();
    }
}
