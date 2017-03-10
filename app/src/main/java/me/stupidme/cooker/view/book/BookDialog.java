package me.stupidme.cooker.view.book;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/3/10.
 */

public class BookDialog extends Dialog {

    private Spinner mCookerNames;

    private Spinner mTastes;

    private EditText mPeopleCount;

    private EditText mRiceWeight;

    private EditText mBookTime;

    private BookDialogListener mListener;

    private List<String> mNamesList;

    public BookDialog(@NonNull Context context, BookDialogListener listener) {
        super(context);

        setContentView(R.layout.dialog_book);

        mListener = listener;

        if (mNamesList == null)
            mNamesList = new ArrayList<>();

        mCookerNames = (Spinner) findViewById(R.id.book_spinner_name);
        mTastes = (Spinner) findViewById(R.id.book_spinner_taste);
        mPeopleCount = (EditText) findViewById(R.id.book_et_people);
        mRiceWeight = (EditText) findViewById(R.id.book_et_rice);
        mBookTime = (EditText) findViewById(R.id.book_et_time);

        Button ok = (Button) findViewById(R.id.book_ok);
        Button cancel = (Button) findViewById(R.id.book_cancel);

        cancel.setOnClickListener(v -> {
            dismiss();
        });

        ok.setOnClickListener(v -> {
            mListener.onSave(checkContent());
        });
    }

    @Override
    public void show() {
        if (mNamesList == null) {
            mNamesList = new ArrayList<>();
        }
        mNamesList.clear();
        mNamesList = mListener.getCookerNames();


    }

    private Map<String, String> checkContent() {
        Map<String, String> map = new ArrayMap<>();


        return map;
    }

    public interface BookDialogListener {

        void onSave(Map<String, String> map);

        List<String> getCookerNames();
    }
}
