package me.stupidme.cooker.view.cooker;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/3/7.
 * <p>
 * A dialog to create a cooker.
 */

public class CookerDialog extends Dialog {

    /**
     * A key for map to save and find name of cooker.
     */
    public static final String COOKER_NAME_KEY = "name";

    /**
     * A key for a map to save and find location of cooker.
     */
    public static final String COOKER_LOCATION_KEY = "location";

    /**
     * A key for a map to save and find id of cooker.
     */
    public static final String COOKER_ID_KEY = "id";

    /**
     * A callback interface to save a cooker.
     */
    private CookerAddListener mListener;

    private Context mContext;

    private TextInputEditText mName;

    private TextInputEditText mLocation;

    private TextInputEditText mId;

    private Button mOk;

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
        mId = (TextInputEditText) findViewById(R.id.cooker_id);

        mOk.setOnClickListener(v -> {

            String name = mName.getText().toString();
            String location = mLocation.getText().toString();
            String id = mId.getText().toString();

            if (TextUtils.isEmpty(name)) {
                mName.setError(mContext.getString(R.string.error_field_required));
                return;
            }
            if (TextUtils.isEmpty(location)) {
                mLocation.setError(mContext.getString(R.string.error_field_required));
                return;
            }
            if (TextUtils.isEmpty(id)) {
                mId.setError(mContext.getString(R.string.error_field_required));
                return;
            }
            Map<String, String> map = new HashMap<>();
            map.put(COOKER_NAME_KEY, name);
            map.put(COOKER_LOCATION_KEY, location);
            map.put(COOKER_ID_KEY, id);
            mListener.onSave(map);
            dismiss();
        });

        mCancel.setOnClickListener(v -> dismiss());

    }


    /**
     * A callback interface to save a cooker.
     */
    public interface CookerAddListener {

        /**
         * save cooker info to a map.
         * called when ok button clicked.
         *
         * @param map
         */
        void onSave(Map<String, String> map);
    }

}
