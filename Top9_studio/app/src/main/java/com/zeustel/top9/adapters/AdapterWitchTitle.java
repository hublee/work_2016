package com.zeustel.top9.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/12/4 16:06
 */
public class AdapterWitchTitle extends AdapterFragmentPager {
    private String[] titles;

    public AdapterWitchTitle(FragmentManager fm, List<Fragment> data, String[] titles) {
        super(fm, data);
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
