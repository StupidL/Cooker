package me.stupidme.cooker.view.cooker;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import me.stupidme.cooker.R;
import me.stupidme.cooker.view.base.BaseActivity;
import me.stupidme.cooker.view.custom.ChoosePicDialog;

/**
 * Created by StupidL 2017/4/8.
 */

public class UserActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user;
    }

    @Override
    protected void handleIntent() {

    }

    @Override
    protected void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> {
                setResult(RESULT_OK);
                finish();
            });
        }

        CircleImageView mCircleImage = (CircleImageView) findViewById(R.id.user_head_big);
        TextView mTextView = (TextView) findViewById(R.id.user_name);
        mTextView.setClickable(false);
        mTextView.setFocusable(false);

        ChoosePicDialog.ChoosePicListener mListener = new ChoosePicDialog.ChoosePicListener() {
            @Override
            public void chooseFromAlbum() {

            }

            @Override
            public void takeFromCamera() {

            }
        };
        mCircleImage.setOnClickListener(v ->
                new ChoosePicDialog(this, mListener).show());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
