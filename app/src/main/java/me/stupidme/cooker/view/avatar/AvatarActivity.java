package me.stupidme.cooker.view.avatar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.hdodenhof.circleimageview.CircleImageView;
import me.stupidme.cooker.R;
import me.stupidme.cooker.model.UserAvatarManager;
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

    private static final int REQUEST_CAMERA = 0x02;
    private static final int REQUEST_CROP = 0x03;

    private String mImageUrl;

    private CircleImageView mCircleImage;

    private TextView mUserName;

    private ChoosePicDialog mDialog;

    private Bitmap mAvatar;

    private ProgressDialog mProgressDialog;

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
            attemptSaveAvatar(mAvatar);
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
        Long userId = SharedPreferenceUtil.getAccountUserId(0L);
        String path = UserAvatarManager.getInstance().getAvatarPath(userId);
//        Glide.with(this)
//                .load(path)
//                .placeholder(R.drawable.head_male2)
//                .into(mCircleImage);
//
        mCircleImage.setImageBitmap(BitmapFactory.decodeFile(path));

        mUserName = (TextView) findViewById(R.id.user_name);
        mUserName.setText(SharedPreferenceUtil.getAccountUserName("Cooker"));
        Log.v("Avatar", "username: " + SharedPreferenceUtil.getAccountUserName("Cooker"));

        ChoosePicDialog.ChoosePicListener mListener = new ChoosePicDialog.ChoosePicListener() {
            @Override
            public void chooseFromAlbum() {
                PermissionUtil.attemptSelectImages(AvatarActivity.this);
            }

            @Override
            public void takeFromCamera() {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
                        new File(Environment.getExternalStorageDirectory(),
                                "/" + Math.abs(SharedPreferenceUtil.getAccountUserId(0L)) + ".jpg")));
                startActivityForResult(intent2, REQUEST_CAMERA);
            }
        };
        mDialog = new ChoosePicDialog(this, mListener);
        mCircleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("AvatarActivity", "Clicked...");
                mDialog.show();
            }
        });

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Saving avatar");
        mProgressDialog.setMessage("Please wait a few seconds!");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionUtil.REQUEST_SELECT_IMAGES && resultCode == RESULT_OK) {

            if (data.getData() != null) {
                mImageUrl = ResourceUtil.getPath(this, data.getData());
                mAvatar = BitmapUtil.decodeSampledBitmap(mImageUrl,
                        mCircleImage.getWidth(), mCircleImage.getHeight());
//                mCircleImage.setImageBitmap(mAvatar);
                Glide.with(this)
                        .load(new File(mImageUrl))
                        .into(mCircleImage);
                Toast.makeText(this, getString(R.string.select_image_success), Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            File temp = new File(Environment.getExternalStorageDirectory() + "/"
                    + Math.abs(SharedPreferenceUtil.getAccountUserId(0L)) + ".jpg");
            cropImage(Uri.fromFile(temp));
        }

        if (requestCode == REQUEST_CROP && data != null) {

            Uri uri = data.getData();
            if (uri != null) {
                try {
                    mAvatar = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    if (mAvatar != null)
                        mCircleImage.setImageBitmap(mAvatar);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            Bundle bundle = data.getExtras();
            if (bundle == null)
                return;
            mAvatar = bundle.getParcelable("data");
            mCircleImage.setImageBitmap(mAvatar);

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

    private void attemptSaveAvatar(Bitmap bitmap) {
        showTipsDialog();
        if (bitmap == null) {
            dismissTipsDialog();
            showToast("Save avatar failed!");
            return;
        }
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            dismissTipsDialog();
            showToast("No SD card found!");
            return;
        }
        FileOutputStream fileOutputStream = null;
        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path);
//        if (!file.mkdirs()) {
//            dismissTipsDialog();
//            showToast("Create file directory failed@");
//            return;
//        }
        file.mkdirs();
        String filename = path + "/" + Math.abs(SharedPreferenceUtil.getAccountUserId(0L)) + ".jpg";

        try {
            fileOutputStream = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        UserAvatarManager.getInstance().putAvatarPath(SharedPreferenceUtil.getAccountUserId(0L), filename);
        dismissTipsDialog();
        finish();
    }

    private void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CROP);
    }

    private void showTipsDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void dismissTipsDialog() {
        if (mProgressDialog == null)
            return;
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }
}
