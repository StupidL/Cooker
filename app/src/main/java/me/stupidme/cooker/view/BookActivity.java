package me.stupidme.cooker.view;

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

import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.model.BookBean;
import me.stupidme.cooker.widget.BookPagerAdapter;

/**
 * Created by StupidL on 2017/3/5
 */

public class BookActivity extends AppCompatActivity {

    private static List<OnRefreshBookInfoListener> mListenerList = new ArrayList<>();

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

        list.add(nowFragment2);
        list.add(historyFragment2);

        BookPagerAdapter mAdapter = new BookPagerAdapter(getSupportFragmentManager(), list);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mAdapter);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.book_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0xaa);
            }
        };

        mHandler.postDelayed(runnable, 5000);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 数据更新监听器
     */
    interface OnRefreshBookInfoListener {
        void onRefresh(List<BookBean> list);
    }
}
