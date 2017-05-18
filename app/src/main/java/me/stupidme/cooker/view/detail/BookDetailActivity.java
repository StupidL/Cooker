package me.stupidme.cooker.view.detail;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Calendar;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.presenter.BookDetailMockPresenterImpl;
import me.stupidme.cooker.presenter.BookDetailPresenter;
import me.stupidme.cooker.util.ImageUtil;
import me.stupidme.cooker.util.ToastUtil;
import me.stupidme.cooker.view.base.BaseActivity;

public class BookDetailActivity extends BaseActivity implements BookDetailView {

    private BookBean mBookBean;
    private String mTime;
    private BookDetailPresenter mPresenter;
    private ProgressDialog mDialog;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void handleIntent() {
        if (mBookBean == null)
            mBookBean = new BookBean();

        Intent intent = getIntent();
        if (intent == null)
            return;

        mBookBean.setUserId(intent.getLongExtra("userId", 0L));
        mBookBean.setBookId(intent.getLongExtra("bookId", 0L));
        mBookBean.setCookerId(intent.getLongExtra("cookerId", 0L));
        mBookBean.setCookerName(intent.getStringExtra("cookerName"));
        mBookBean.setCookerLocation(intent.getStringExtra("cookerLocation"));
        mBookBean.setCookerStatus(intent.getStringExtra("cookerStatus"));
        mBookBean.setPeopleCount(intent.getIntExtra("peopleCount", 0));
        mBookBean.setRiceWeight(intent.getIntExtra("riceWeight", 0));
        mBookBean.setTaste(intent.getStringExtra("taste"));
        mBookBean.setTime(intent.getLongExtra("time", 0L));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mBookBean.getTime());
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        mTime = hour + ":" + minute;
    }

    @Override
    protected void initView() {
        TextView bookId = (TextView) findViewById(R.id.detail_book_id);
        TextView cookerId = (TextView) findViewById(R.id.detail_cooker_id);
        TextView cookerName = (TextView) findViewById(R.id.detail_cooker_name);
        TextView cookerLocation = (TextView) findViewById(R.id.detail_cooker_location);
        TextView cookerStatus = (TextView) findViewById(R.id.detail_cooker_status);
        TextView peopleCount = (TextView) findViewById(R.id.detail_people_count);
        TextView riceWeight = (TextView) findViewById(R.id.detail_rice_weight);
        TextView taste = (TextView) findViewById(R.id.detail_taste);
        TextView time = (TextView) findViewById(R.id.detail_time);

        bookId.setText(String.valueOf(mBookBean.getBookId()));
        cookerId.setText(String.valueOf(mBookBean.getCookerId()));
        cookerName.setText(mBookBean.getCookerName());
        cookerLocation.setText(mBookBean.getCookerLocation());
        cookerStatus.setText(mBookBean.getCookerStatus());
        peopleCount.setText(String.valueOf(mBookBean.getPeopleCount()));
        riceWeight.setText(String.valueOf(mBookBean.getRiceWeight()));
        taste.setText(mBookBean.getTaste());
        time.setText(mTime);

        mPresenter = new BookDetailMockPresenterImpl(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.detail_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.cancel(mBookBean.getUserId(), mBookBean.getBookId());
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.detail_image_view);
        Glide.with(this)
                .load(ImageUtil.nextImageResId())
                .placeholder(ImageUtil.placeHolder())
                .into(imageView);

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Cancel a book");
        mDialog.setMessage("Please wait a few seconds...");
    }

    @Override
    public void onCancelSuccess() {
        ToastUtil.showToastShort(this, "Cancel book success!");
    }

    @Override
    public void onCancelFailed() {
        ToastUtil.showToastShort(this, "Cancel book failed!");
    }

    @Override
    public void showDialog(boolean show) {
        if (show) {
            if (!mDialog.isShowing())
                mDialog.show();
        } else {
            if (mDialog.isShowing())
                mDialog.dismiss();
        }
    }
}
