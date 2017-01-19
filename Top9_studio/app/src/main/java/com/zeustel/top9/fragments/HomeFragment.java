package com.zeustel.top9.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.AdapterFragmentPager;
import com.zeustel.top9.base.WrapOneFragment;
import com.zeustel.top9.bean.GameEvaluating;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.CustomViewPager;
import com.zeustel.top9.widgets.Indicator;

import java.util.ArrayList;

/**
 * 首页
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/26
 *
 * 由于viewpager双嵌套出现页面丢失的情况
 */
@Deprecated
public class HomeFragment extends WrapOneFragment {
    private View contentView = null;
    //单机
    private EvaluatingFragment offLineFragment = null;
    //网游
    private EvaluatingFragment onLineFragment = null;
    //手游
    private EvaluatingFragment handFragment = null;
    //指示器
    private Indicator home_indicator;
    //内容
    private CustomViewPager homeViewPager;
    //适配器
    private AdapterFragmentPager adapter;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_indicator_viewpager, container, false);
        homeViewPager = (CustomViewPager) contentView.findViewById(R.id.home_viewPager);
        /*viewpager 指示器*/
        home_indicator = (Indicator) contentView.findViewById(R.id.home_indicator);
        /*实例化各种类型评测*/
        offLineFragment = EvaluatingFragment.newInstance(GameEvaluating.EvaluatingType.OFFLINE_GAME, R.color.red);
        onLineFragment = EvaluatingFragment.newInstance(GameEvaluating.EvaluatingType.ONLINE_GAME, R.color.yellow);
        handFragment = EvaluatingFragment.newInstance(GameEvaluating.EvaluatingType.HAND_GAME, R.color.blue);
        ArrayList<Fragment> data = new ArrayList();
        data.add(offLineFragment);
        data.add(onLineFragment);
        data.add(handFragment);
        home_indicator.setGravity(Gravity.CENTER);
        home_indicator.setBackgroundDrawable(null);
        home_indicator.setItem(Tools.getDimension(getResources(), R.dimen.viewpager_indicator_item_width), Tools.getDimension(getResources(), R.dimen.viewpager_indicator_height), Tools.getDimension(getResources(), R.dimen.viewpager_indicator_item_margin));
        home_indicator.addView(home_indicator.newChildBtn(0/*index*/, R.color.black, R.color.red));
        home_indicator.addView(home_indicator.newChildBtn(1/*index*/, R.color.black, R.color.yellow));
        home_indicator.addView(home_indicator.newChildBtn(2/*index*/, R.color.black, R.color.blue));
        adapter = new AdapterFragmentPager(getFragmentManager(), data);
        homeViewPager.setAdapter(adapter);
        //预加载所有页面
        /*添加页面切换监听*/
        homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            /**
             * 切换到 目标 页面
             * @param position 目标页
             */
            @Override
            public void onPageSelected(int position) {
                //同时切换指示器
                home_indicator.check(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Tools.endCreateOperate(homeViewPager, new Runnable() {
            @Override
            public void run() {
                homeViewPager.setCurrentItem(0/*index*/);
                home_indicator.check(0/*index*/);//默认选择第一个
            }
        });
        return contentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public String getFloatTitle() {
        return "";
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }
}
