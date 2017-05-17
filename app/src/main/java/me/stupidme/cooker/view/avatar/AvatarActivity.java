package me.stupidme.cooker.view.avatar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import me.stupidme.cooker.R;
import me.stupidme.cooker.util.BitmapUtil;
import me.stupidme.cooker.util.PermissionUtil;
import me.stupidme.cooker.util.ResourceUtil;
import me.stupidme.cooker.util.SharedPreferenceUtil;
import me.stupidme.cooker.view.base.BaseActivity;
import me.stupidme.cooker.view.custom.ChoosePicDialog;

/**
 * Created by StupidL 2017/4/8.
 */

public class AvatarActivity extends BaseActivity {

    private String mImageUrl;

    private CircleImageView mCircleImage;

    private ChoosePicDialog mDialog;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_user;
    }

    @Override
    protected void handleIntent() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            attemptSaveAvatar();
            return true;
        }
        return false;
    }

    @Override
    protected void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        mCircleImage = (CircleImageView) findViewById(R.id.user_head_big);
        TextView mTextView = (TextView) findViewById(R.id.user_name);
        mTextView.setClickable(false);
        mTextView.setFocusable(false);

        ChoosePicDialog.ChoosePicListener mListener = new ChoosePicDialog.ChoosePicListener() {
            @Override
            public void chooseFromAlbum() {
                PermissionUtil.attemptSelectImages(AvatarActivity.this);
            }

            @Override
            public void takeFromCamera() {

            }
        };
        mDialog = new ChoosePicDialog(this, mListener);
        mCircleImage.setOnClickListener(v -> mDialog.show());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionUtil.REQUEST_SELECT_IMAGES && resultCode == RESULT_OK) {

            if (data.getData() != null) {
                mImageUrl = ResourceUtil.getPath(this, data.getData());
                mCircleImage.setImageBitmap(BitmapUtil.decodeSampledBitmap(mImageUrl,
                        mCircleImage.getWidth(), mCircleImage.getHeight()));
                Toast.makeText(this, getString(R.string.select_image_success), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PermissionUtil.selectImage(this);
                } else {
                    PermissionUtil.showTipsDialog(this);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void attemptSaveAvatar() {
        if (mImageUrl != null) {
            SharedPreferenceUtil.putAvatarImageUrl(mImageUrl);
//            Intent intent = new Intent(UserActivity.this, CookerActivity.class);
//            intent.putExtra(SharedPreferenceUtil.KEY_AVATAR_IMAGE_URL, mImageUrl);
//            startActivity(intent);
            this.finish();
            return;
        }
        showToast("Nothing Changed!");
    }
}
