package com.zeustel.top9.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;

import java.util.List;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/18 17:47
 */
public class AdapterJazzy extends FragmentPagerAdapter {
    private JazzyViewPager jazzyViewPager;
    private List<Fragment> data;

    public AdapterJazzy(JazzyViewPager jazzyViewPager, FragmentManager fm, List<Fragment> data) {
        super(fm);
        this.jazzyViewPager = jazzyViewPager;
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        jazzyViewPager.setObjectForPosition(obj, position);
        return obj;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (object != null) {
            return ((Fragment) object).getView() == view;
        } else {
            return false;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        super.destroyItem(container, position, object);
    }
}
