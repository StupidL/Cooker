package me.stupidme.cooker.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.retrofit.CookerRetrofit;
import me.stupidme.cooker.retrofit.CookerService;
import me.stupidme.cooker.widget.BookPagerAdapter;

/**
 * Created by StupidL on 2017/3/5
 */

public class BookActivity extends AppCompatActivity {

    private static final int MESSAGE_WHAT_LOAD_LOCAL_DATA_FINISHED = 0xa1;

    private static final int MESSAGE_WHAT_LOAD_NET_DATA_FINISHED = 0xa2;

    private static final int MESSAGE_WHAT_LOCAL_NO_DATA = 0xa3;

    private static List<OnRefreshBookInfoListener> mListenerList = new ArrayList<>();

    private CookerService mService;

    private ProgressDialog mDialog;

    private ViewPager mViewPager;

    private List<BookBean> localList = new ArrayList<>();

    private BookHandler mHandler = new BookHandler(BookActivity.this);

    private Runnable testLocalDataRunnable = () -> {
        //先清空后添加
        localList.clear();
        localList.addAll(getLocalData());

        if (localList.size() > 0) {
            List<BookBean> l1 = new ArrayList<>();
            List<BookBean> l2 = new ArrayList<>();
            for (BookBean book : localList) {
                if ("free".equals(book.getDeviceStatus())) {
                    l1.add(book);
                } else if ("booking".equals(book.getDeviceStatus())) {
                    l2.add(book);
                }
            }

            Log.v("testLocalDataRunnable", "l1 Size: " + l1.size());
            Log.v("testLocalDataRunnable", "l2 Size: " + l2.size());

            mListenerList.get(0).onRefresh(l2);
            mListenerList.get(1).onRefresh(l1);

            Message m = mHandler.obtainMessage(MESSAGE_WHAT_LOAD_LOCAL_DATA_FINISHED);
            mHandler.sendMessage(m);
        } else {
            Message m2 = mHandler.obtainMessage(MESSAGE_WHAT_LOCAL_NO_DATA);
            mHandler.sendMessage(m2);
        }
    };

    private Runnable testNetDataRunnable = () -> {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_WHAT_LOAD_NET_DATA_FINISHED));
        showProgressDialog(false);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //初始化Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        //创建两个Fragment
        BookHistoryFragment2 historyFragment2 = BookHistoryFragment2.newInstance();
        BookNowFragment2 nowFragment2 = BookNowFragment2.newInstance();

        //为Fragment添加监听
        mListenerList.add(0, historyFragment2);
        mListenerList.add(1, nowFragment2);

        //设置适配器
        List<Fragment> l = new ArrayList<>(2);
        l.add(0, nowFragment2);
        l.add(1, historyFragment2);
        BookPagerAdapter mAdapter = new BookPagerAdapter(getSupportFragmentManager(), l);

        //设置ViewPager和TabLayout
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mAdapter);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.book_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //初始化网络服务
        mService = CookerRetrofit.getInstance().getCookerService();

        //初始化加载对话框
        initProgressDialog();

        //新线程加载本地数据
        new Thread(testLocalDataRunnable).start();
    }

    /**
     * 获取本地的数据
     *
     * @return
     */
    private List<BookBean> getLocalData() {
        List<BookBean> l = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BookBean b = new BookBean();
            b.setDeviceId(i);
            b.setDeviceName("Book" + i);
            b.setDevicePlace("Place" + i);
            b.setPeopleCount(i);
            b.setRiceWeight(500 * i);
            b.setTaste("soft");
            b.setDeviceStatus(i % 2 == 0 ? "free" : "booking");
            b.setTime("17:30");
            l.add(b);
        }
        return l;
    }

    /**
     * 初始化对话框进度条
     */
    private void initProgressDialog() {
        mDialog = new ProgressDialog(BookActivity.this);
        mDialog.setTitle(getResources().getString(R.string.title_book_activity_progress));
        mDialog.setMessage(getResources().getString(R.string.message_book_activity_dialog));
        mDialog.setCancelable(false);
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL",
                (dialog, which) -> {
                    showProgressDialog(false);
                });

    }

    /**
     * 关闭或者显示加载对话框
     *
     * @param show true说明显示， false说明关闭
     */
    private void showProgressDialog(boolean show) {

        mViewPager.setVisibility(show ? View.GONE : View.VISIBLE);
        mViewPager.animate().setDuration(500)
                .alpha(show ? 0 : 1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mViewPager.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

        if (show) {
            mDialog.show();
        } else {
            mDialog.dismiss();
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (testLocalDataRunnable != null)
            mHandler.removeCallbacks(testLocalDataRunnable);
        if (testNetDataRunnable != null)
            mHandler.removeCallbacks(testNetDataRunnable);
    }

    private void doRefresh() {

        showProgressDialog(true);

        //模拟网络请求
        mHandler.postDelayed(testNetDataRunnable, 3000);

        //真实的网络请求
        Map<String, String> map = new HashMap<>();

        mService.rxGetAllBooksInfo(map)
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
                            if ("booking".equals(bookBean.getDeviceStatus())) {
                                mBookingList.add(bookBean);
                            } else if ("free".equals(bookBean.getDeviceStatus())) {
                                mHistoryList.add(bookBean);
                            }
                        }

                        mListenerList.get(0).onRefresh(mBookingList);
                        mListenerList.get(1).onRefresh(mHistoryList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mDialog.isShowing())
                            showProgressDialog(false);
                        showToastMessage(e.toString());
                    }

                    @Override
                    public void onComplete() {
                        if (mDialog.isShowing())
                            showProgressDialog(false);
                    }
                });
    }

    /**
     * 弹出消息提示
     *
     * @param message 消息
     */
    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 数据更新监听器
     */
    interface OnRefreshBookInfoListener {
        void onRefresh(List<BookBean> list);
    }

    /**
     * Handler
     */
    private static class BookHandler extends Handler {

        WeakReference<AppCompatActivity> activityRef;

        BookHandler(AppCompatActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            AppCompatActivity activity = activityRef.get();
            if (activity != null) {
                handleAllMessage(message);
            }
        }

        private void handleAllMessage(Message message) {
            int what = message.what;
            switch (what) {
                case MESSAGE_WHAT_LOAD_LOCAL_DATA_FINISHED:
                    Log.v("BookHandler", "Message: " + MESSAGE_WHAT_LOAD_LOCAL_DATA_FINISHED);
                    break;
                case MESSAGE_WHAT_LOAD_NET_DATA_FINISHED:
                    Log.v("BookHandler", "Message: " + MESSAGE_WHAT_LOAD_NET_DATA_FINISHED);
                    break;

                case MESSAGE_WHAT_LOCAL_NO_DATA:
                    BookActivity activity = (BookActivity) activityRef.get();
                    activity.showToastMessage(
                            activity.getString(R.string.toast_book_activity_local_no_data)
                    );
                    break;
                default:
                    break;
            }
        }
    }
}
