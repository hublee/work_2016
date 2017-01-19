package com.zeustel.top9;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.zeustel.top9.adapters.AdapterFragmentPager;
import com.zeustel.top9.adapters.AdapterJazzy;
import com.zeustel.top9.base.WrapNotActivity;
import com.zeustel.top9.bean.FM;
import com.zeustel.top9.fragments.CompereFragment;
import com.zeustel.top9.temp.TempProgramFragment;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.Indicator;

import java.util.ArrayList;
import java.util.List;

public class FMDetailActivity extends WrapNotActivity implements ViewPager.OnPageChangeListener {
    public static final String EXTRA_NAME_DATA = "data";
    public static final String EXTRA_NAME_POSITION = "position";
    private FM fm;
    private int position;
    private AdapterFragmentPager adapter;
    private Indicator indicator;
    private JazzyViewPager viewPager;
    private List<Fragment> data = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            fm = (FM) intent.getSerializableExtra(EXTRA_NAME_DATA);
            position = intent.getIntExtra(EXTRA_NAME_POSITION, position);
        }
        if (fm == null) {
            finish();
        } else {

            setContentView(R.layout.fm_detail);
            viewPager = (JazzyViewPager) findViewById(R.id.viewPager);
            indicator = (Indicator) findViewById(R.id.indicator);
            indicator.setGravity(Gravity.CENTER);
            indicator.setBackgroundDrawable(null);
            indicator.setItem(Tools.getDimension(getResources(), R.dimen.viewpager_indicator_item_width), Tools.getDimension(getResources(), R.dimen.viewpager_indicator_height), Tools.getDimension(getResources(), R.dimen.viewpager_indicator_item_margin));
            viewPager.addOnPageChangeListener(this);

            indicator.addView(indicator.newChildBtn(0/*index*/, R.color.black, R.color.blue));
            indicator.addView(indicator.newChildBtn(1/*index*/, R.color.black, R.color.yellow));
            data.add(CompereFragment.newInstance(fm.getCompere()));
            data.add(TempProgramFragment.newInstance(fm.getPrograms()));
            adapter = new AdapterFragmentPager(getSupportFragmentManager(), data);

            viewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.Tablet);
            viewPager.setPageMargin(30);
            AdapterJazzy adapter = new AdapterJazzy(viewPager, getSupportFragmentManager(), data);
            //        adapter = new AdapterFragmentPager(getSupportFragmentManager(), data);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(data.size());
            viewPager.setCurrentItem(position);
        }
    }

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /*解决当软键盘弹出时 奇幻当页面 软键盘没有隐藏*/
        Tools.hideSoftInput(getApplicationContext(), viewPager.getWindowToken());
    }

    @Override
    public void onPageSelected(int position) {
        //同时切换指示器
        indicator.check(position);
        if (data != null) {
            Fragment fragment = data.get(viewPager.getCurrentItem());
            if (fragment != null && fragment instanceof TempProgramFragment) {
                ((TempProgramFragment) fragment).startAnim();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
