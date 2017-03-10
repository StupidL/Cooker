package me.stupidme.cooker.view.book;

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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import me.stupidme.cooker.R;
import me.stupidme.cooker.widget.BookPagerAdapter;

/**
 * Created by StupidL on 2017/3/5
 */

public class BookActivity extends AppCompatActivity {

    private static final int MESSAGE_WHAT_LOAD_LOCAL_DATA_FINISHED = 0xa1;

    private static final int MESSAGE_WHAT_LOAD_NET_DATA_FINISHED = 0xa2;

    private static final int MESSAGE_WHAT_LOCAL_NO_DATA = 0xa3;

    private BookHandler mHandler = new BookHandler(BookActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        //初始化Toolbar
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

        //创建两个Fragment
        BookHistoryFragment historyFragment = BookHistoryFragment.newInstance();
        BookNowFragment nowFragment = BookNowFragment.newInstance();

        //设置适配器
        List<Fragment> l = new ArrayList<>();
        l.add(nowFragment);
        l.add(historyFragment);
        BookPagerAdapter mAdapter = new BookPagerAdapter(getSupportFragmentManager(), l);

        //设置ViewPager和TabLayout
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mAdapter);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.book_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() != R.id.book_fragment_refresh;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
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

                    break;
                default:
                    break;
            }
        }
    }
}
