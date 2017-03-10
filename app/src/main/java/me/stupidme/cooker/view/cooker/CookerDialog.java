package me.stupidme.cooker.view.cooker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/3/7.
 */

public class CookerDialog extends Dialog {

    public static final String COOKER_NAME_KEY = "name";

    public static final String COOKER_LOCATION_KEY = "location";

    private CookerAddListener mListener;

    private Context mContext;

    //Cooker的名字
    private TextInputEditText mName;
    //Cooker的位置
    private TextInputEditText mLocation;
    //保存信息
    private Button mOk;
    //取消
    private Button mCancel;

    public CookerDialog(@NonNull Context context, CookerAddListener listener) {
        super(context);

        setContentView(R.layout.dialog_cooker);

        mContext = context;
        mListener = listener;

        mName = (TextInputEditText) findViewById(R.id.cooker_name);
        mLocation = (TextInputEditText) findViewById(R.id.cooker_location);
        mOk = (Button) findViewById(R.id.cooker_ok);
        mCancel = (Button) findViewById(R.id.cooker_cancel);

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mName.getText().toString();
                String location = mLocation.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    mName.setError(mContext.getString(R.string.error_field_required));
                    return;
                }
                if (TextUtils.isEmpty(location)) {
                    mLocation.setError(mContext.getString(R.string.error_field_required));
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put(COOKER_NAME_KEY, name);
                map.put(COOKER_LOCATION_KEY, location);
                mListener.onSave(map);

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    /**
     * 回调接口
     */
    public interface CookerAddListener {
        void onSave(Map<String, String> map);
    }

}
