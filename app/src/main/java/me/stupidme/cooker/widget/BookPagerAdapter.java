package me.stupidme.cooker.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by StupidL on 2017/3/5.
 */

public class BookPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public BookPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        mFragments = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Booking" : "History";
    }
}
