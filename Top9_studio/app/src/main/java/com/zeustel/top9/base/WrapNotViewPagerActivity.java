package com.zeustel.top9.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.CustomViewPager;
import com.zeustel.top9.widgets.Indicator;

import java.util.ArrayList;
import java.util.List;

public abstract class WrapNotViewPagerActivity extends WrapNotActivity implements ViewPager.OnPageChangeListener {
    private CustomViewPager viewPager;
    private Indicator indicator;
    private List<Fragment> data = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrap_not_view_pager);
        viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        indicator = (Indicator) findViewById(R.id.indicator);
        indicator.setGravity(Gravity.CENTER);
        indicator.setBackgroundDrawable(null);
        indicator.setItem(Tools.getDimension(getResources(), R.dimen.viewpager_indicator_item_width), Tools.getDimension(getResources(), R.dimen.viewpager_indicator_height), Tools.getDimension(getResources(), R.dimen.viewpager_indicator_item_margin));
        viewPager.addOnPageChangeListener(this);
    }

    public void setCurrentItem(final int position) {
        viewPager.setCurrentItem(position);
        Tools.endCreateOperate(viewPager, new Runnable() {
            @Override
            public void run() {
                indicator.check(position);
            }
        });
    }

    public Indicator getIndicator() {
        return indicator;
    }

    public CustomViewPager getViewPager() {
        return viewPager;
    }

    public List<Fragment> getData() {
        return data;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /*解决当软键盘弹出时 奇幻当页面 软键盘没有隐藏*/
        Tools.hideSoftInput(WrapNotViewPagerActivity.this, viewPager.getWindowToken());
    }

    @Override
    public void onPageSelected(int position) {
        //同时切换指示器
        indicator.check(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
