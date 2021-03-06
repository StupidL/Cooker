package me.stupidme.cooker.view.feedback;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.stupidme.cooker.R;
import me.stupidme.cooker.util.BitmapUtil;
import me.stupidme.cooker.util.PermissionUtil;
import me.stupidme.cooker.util.ResourceUtil;

public class FeedbackActivity extends AppCompatActivity {

    private EditText mEditText;
    private CheckBox mCheckBox;
    private ImageView mImageView;
    private TextView mTextView;

    private String mImageUrl;

    private FeedbackIntentInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // handle intent
        handleIntent(getIntent());
        //set status color. only works when API >= 21.
        getWindow().setStatusBarColor(mInfo.getColorPrimaryDark());

        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_feedback);
        // set toolbar color
        toolbar.setBackgroundColor(mInfo.getColorPrimary());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        mEditText = (EditText) findViewById(R.id.feedback_edit_text);
        mCheckBox = (CheckBox) findViewById(R.id.feedback_checkbox);
        mImageView = (ImageView) findViewById(R.id.feedback_image_view);

        mTextView = (TextView) findViewById(R.id.feedback_text_view);
        mTextView.setOnClickListener(v -> PermissionUtil.attemptSelectImages(this));
        // set text view background.
        mTextView.setBackgroundColor(mInfo.getColorPrimary());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_send_feedback) {
            attemptSendFeedback();
            return true;
        }
        return false;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PermissionUtil.REQUEST_APP_SETTINGS) {
            if (PermissionUtil.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                PermissionUtil.selectImage(this);
            } else {
                PermissionUtil.showTipsDialog(this);
            }
        } else if (requestCode == PermissionUtil.REQUEST_SELECT_IMAGES && resultCode == RESULT_OK) {

            if (data.getData() != null) {
                mImageUrl = ResourceUtil.getPath(this, data.getData());
                mImageView.setImageBitmap(BitmapUtil.decodeSampledBitmap(mImageUrl,
                        mImageView.getWidth(), mImageView.getHeight()));
                mTextView.setVisibility(View.GONE);
                Toast.makeText(this, getString(R.string.select_image_success), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void attemptSendFeedback() {
        if (mEditText.getText().toString().isEmpty()) {
            mEditText.setError(getString(R.string.edit_text_empty));
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(mEditText.getText().toString()).append("\n");
        if (mCheckBox.isChecked()) {
            String deviceInfo = ResourceUtil.getAllDeviceInfo(this, false);
            builder.append(deviceInfo);
        }
        Intent emailIntent = ResourceUtil.createEmailIntent(this, builder, mImageUrl, mInfo.getEmailTo());

        startActivity(ResourceUtil.createEmailOnlyChooserIntent(this, emailIntent, getString(R.string.send_feedback)));

    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        mInfo = new FeedbackIntentInfo();
        if (FeedbackConstants.ACTION_START_FEEDBACK.equals(action)) {
            Bundle bundle = intent.getExtras();
            int colorPrimary = bundle.getInt(FeedbackConstants.KEY_COLOR_PRIMARY);
            int colorPrimaryDark = bundle.getInt(FeedbackConstants.KEY_COLOR_PRIMARY_DARK);
            String emailTo = bundle.getString(FeedbackConstants.KEY_EMAIL_TO);
            mInfo.setColorPrimary(colorPrimary);
            mInfo.setColorPrimaryDark(colorPrimaryDark);
            mInfo.setEmailTo(emailTo);
        } else {
            mInfo.setColorPrimary(Color.parseColor("#546E7A"));
            mInfo.setColorPrimaryDark(Color.parseColor("#455A64"));
            mInfo.setEmailTo("562117676@qq.com");
        }
    }

}
