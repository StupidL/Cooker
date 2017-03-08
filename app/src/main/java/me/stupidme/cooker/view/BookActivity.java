package me.stupidme.cooker.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.R;
import me.stupidme.cooker.db.StupidDBHelper;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
import me.stupidme.cooker.widget.BookPagerAdapter;

/**
 * Created by StupidL on 2017/3/5
 */

public class BookActivity extends AppCompatActivity {

    private static List<OnRefreshBookInfoListener> mListenerList = new ArrayList<>();

    private CookerService mService;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        List<Fragment> list = new ArrayList<>();

        BookHistoryFragment2 historyFragment2 = BookHistoryFragment2.newInstance();
        mListenerList.add(historyFragment2);

        BookNowFragment2 nowFragment2 = BookNowFragment2.newInstance();
        mListenerList.add(nowFragment2);

        list.add(0, nowFragment2);
        list.add(1, historyFragment2);

        BookPagerAdapter mAdapter = new BookPagerAdapter(getSupportFragmentManager(), list);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mAdapter);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.book_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        Runnable runnable = () -> {
            mHandler.sendEmptyMessage(0xaa);
            mDialog.dismiss();
        };

        mHandler.postDelayed(runnable, 5000);

        mService = CookerRetrofit.getInstance().getCookerService();

        initProgressDialog();
    }

    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == 0xaa) {
                List<BookBean> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    BookBean bookBean = new BookBean();
                    bookBean.setDeviceId(i);
                    bookBean.setDeviceName("Book" + i);
                    bookBean.setDevicePlace("Place" + i);
                    bookBean.setDeviceStatus("free");
                    bookBean.setPeopleCount(i);
                    bookBean.setRiceWeight(500 + i);
                    bookBean.setTaste("soft");
                    bookBean.setTime("17:30");
                    list.add(bookBean);
                }

                for (OnRefreshBookInfoListener listener : mListenerList) {
                    listener.onRefresh(list);
                }
            }
        }
    };

    private void initProgressDialog() {
        mDialog = new ProgressDialog(BookActivity.this);
        mDialog.setTitle(getResources().getString(R.string.title_book_activity_progress));
        mDialog.setMessage(getResources().getString(R.string.message_book_activity_dialog));
        mDialog.setCancelable(false);
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL",
                (dialog, which) -> mDialog.dismiss());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            doRefresh();
        }

        return super.onOptionsItemSelected(item);
    }

    private void doRefresh() {

        mDialog.show();

        Map<String, String> map = new HashMap<>();

        mService.getAllBooksInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<BookBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<BookBean> value) {
                        List<BookBean> mBookingList = new ArrayList<>();
                        List<BookBean> mHistoryList = new ArrayList<>();
                        for (BookBean bookBean : value) {
                            if (StupidDBHelper.BOOK_STATUS_BOOKING.equals(bookBean.getDeviceStatus())) {
                                mBookingList.add(bookBean);
                            } else if (StupidDBHelper.BOOK_STATUS_FINISHED.equals(bookBean.getDeviceStatus())) {
                                mHistoryList.add(bookBean);
                            }
                        }

                        mListenerList.get(0).onRefresh(mBookingList);
                        mListenerList.get(1).onRefresh(mHistoryList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                        showToastMessage(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        if (mDialog.isShowing())
                            mDialog.dismiss();
                    }
                });
    }

    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 数据更新监听器
     */
    interface OnRefreshBookInfoListener {
        void onRefresh(List<BookBean> list);
    }
}
