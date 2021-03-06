package me.stupidme.cooker.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import me.stupidme.cooker.R;

/**
 * Created by StupidL on 2017/4/8.
 */

public class ChoosePicDialog extends Dialog {

    private ChoosePicListener mListener;

    public ChoosePicDialog(@NonNull Context context, ChoosePicListener listener) {
        super(context);

        if (getWindow() != null)
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.dialog_change_user_head);
        TextView mAlbumTextView = (TextView) findViewById(R.id.choose_from_album);
        TextView mCameraTextView = (TextView) findViewById(R.id.take_from_camera);
        mListener = listener;

        mAlbumTextView.setOnClickListener(v -> {
            mListener.chooseFromAlbum();
            dismiss();
        });

        mCameraTextView.setOnClickListener(v -> {
            mListener.takeFromCamera();
            dismiss();
        });
    }

    public interface ChoosePicListener {

        void chooseFromAlbum();

        void takeFromCamera();
    }
}
