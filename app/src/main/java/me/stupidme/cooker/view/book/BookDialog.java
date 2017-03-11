package me.stupidme.cooker.view.book;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/3/10.
 */

public class BookDialog extends Dialog {

    private Spinner mCookerNameSpinner;

    private Spinner mTasteSpinner;

    private EditText mPeopleCountEditText;

    private EditText mRiceWeightEditText;

    private EditText mBookTimeEditText;

    private BookDialogListener mListener;

    private List<String> mNamesList;

    private ArrayAdapter<String> mNamesAdapter;

    private Map<String, String> mInfoMap = new ArrayMap<>();

    private Context mContext;

    public BookDialog(@NonNull Context context, BookDialogListener listener) {
        super(context);

        setContentView(R.layout.dialog_book);

        mContext = context;
        mListener = listener;

        if (mNamesList == null)
            mNamesList = new ArrayList<>();

        mCookerNameSpinner = (Spinner) findViewById(R.id.book_spinner_name);
        mTasteSpinner = (Spinner) findViewById(R.id.book_spinner_taste);

        mPeopleCountEditText = (EditText) findViewById(R.id.book_et_people);
        mRiceWeightEditText = (EditText) findViewById(R.id.book_et_rice);
        mBookTimeEditText = (EditText) findViewById(R.id.book_et_time);

        Button ok = (Button) findViewById(R.id.book_ok);
        Button cancel = (Button) findViewById(R.id.book_cancel);

        cancel.setOnClickListener(v -> dismiss());

        ok.setOnClickListener(v -> {
            if (checkContent() != null) {
                mListener.onSave(checkContent());
                dismiss();
            }
            Log.v("BookDialog", mInfoMap.toString());
        });

        initCookerNameSpinner();

        initTasteSpinner();

        Log.v("BookDialog", "onCreate()");
    }

    private void initCookerNameSpinner() {
        mNamesList.add("CookerX");
        mNamesList.add("CookerY");
        mNamesList.add("CookerZ");
        if (mNamesAdapter == null) {
            mNamesAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, mNamesList);
            mNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        mCookerNameSpinner.setAdapter(mNamesAdapter);
        mCookerNameSpinner.setPrompt("Choose Cooker");
        mCookerNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mInfoMap.put("CookerName", mNamesList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (TextUtils.isEmpty(mNamesList.get(0)))
                    mInfoMap.put("CookerName", mNamesList.get(0));
            }
        });
        TextView view = new TextView(mContext);
        view.setText("Empty");
        mCookerNameSpinner.setEmptyView(view);
    }

    private void initTasteSpinner() {
        List<String> mTastesList = new ArrayList<>();
        mTastesList.add("soft");
        mTastesList.add("hard");
        ArrayAdapter<String> mTastesAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_spinner_item, mTastesList);
        mTastesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTasteSpinner.setAdapter(mTastesAdapter);
        mTasteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mInfoMap.put("Taste", mTastesList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (TextUtils.isEmpty(mTastesList.get(0))) {
                    mInfoMap.put("Taste", mTastesList.get(0));
                }
            }
        });
    }

    @Override
    public void show() {
        mNamesList.clear();
        mNamesList.addAll(mListener.getCookerNames());

        mNamesAdapter.notifyDataSetChanged();

        Log.v("BookDialog", mNamesList.toString());

        super.show();
    }

    /**
     * 判断内容是否完整有效
     *
     * @return 信息映射表
     */
    private Map<String, String> checkContent() {
        String peopleCount = mPeopleCountEditText.getText().toString();
        if (TextUtils.isEmpty(peopleCount)) {
            mPeopleCountEditText.setError(mContext.getString(R.string.error_field_required));
            return null;
        }

        String riceWeight = mRiceWeightEditText.getText().toString();
        if (TextUtils.isEmpty(riceWeight)) {
            mRiceWeightEditText.setError(mContext.getString(R.string.error_field_required));
            return null;
        }

        String bookTime = mBookTimeEditText.getText().toString();
        if (TextUtils.isEmpty(bookTime)) {
            mBookTimeEditText.setError(mContext.getString(R.string.error_field_required));
            return null;
        }

        mInfoMap.put("peopleCount", peopleCount);
        mInfoMap.put("riceWeight", riceWeight);
        mInfoMap.put("bookTime", bookTime);

        return mInfoMap;
    }

    /**
     * 回调接口
     */
    public interface BookDialogListener {

        /**
         * 将创建预约的信息传递给BookNowFragment
         *
         * @param map 预约信息
         */
        void onSave(Map<String, String> map);

        /**
         * 从BookNowFragment获取设备名称，以供选择
         *
         * @return 名称列表
         */
        List<String> getCookerNames();
    }
}
