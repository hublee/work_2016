package com.zeustel.top9.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * viewpager adapter
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/16 15:30
 */
public class AdapterFragmentPager extends FragmentPagerAdapter {
    private List<Fragment> data;

    public AdapterFragmentPager(FragmentManager fm, List<Fragment> data) {
        super(fm);
        this.data = data;
        if (this.data == null)
            throw new NullPointerException("AdapterFragmentPager data == null");
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
