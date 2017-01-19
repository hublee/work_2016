package com.zeustel.top9.widgets;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.AdapterFragmentPager;
import com.zeustel.top9.bean.PaperInfo;
import com.zeustel.top9.utils.Tools;

import java.util.ArrayList;

/**
 * 带指示器的viewpager
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/7 09:42
 */
@Deprecated
public class IndicatorViewPager extends FrameLayout implements ViewPager.OnPageChangeListener {
    private Context context;
    private ViewPager mViewPager;
    private Indicator mIndicator;
    private AdapterFragmentPager adapter;

    public Indicator getmIndicator() {
        return mIndicator;
    }

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    public IndicatorViewPager(Context context) {
        super(context);
        init(context);
    }

    public IndicatorViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IndicatorViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        mViewPager = new ViewPager(context);
        mIndicator = new Indicator(context);
        FrameLayout.LayoutParams paramsViewPager = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        FrameLayout.LayoutParams paramsIndicator = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        paramsIndicator.bottomMargin = Tools.getDimension(getResources(), R.dimen.indicator_margin_bottom);
        addView(mViewPager, paramsViewPager);
        addView(mIndicator, paramsIndicator);
        mIndicator.setGravity(Gravity.CENTER);
        mIndicator.setBackgroundDrawable(null);
        mIndicator.setItem(Tools.getDimension(getResources(), R.dimen.viewpager_indicator_item_width), Tools.getDimension(getResources(), R.dimen.viewpager_indicator_height), Tools.getDimension(getResources(), R.dimen.viewpager_indicator_item_margin));
        mViewPager.addOnPageChangeListener(this);
    }


    //    public void prepare(FragmentManager fragmentManager, List<PaperInfo> mPaperInfos) {
    //        if (mPaperInfos != null && !mPaperInfos.isEmpty()) {
    //            ArrayList<Fragment> data = new ArrayList();
    //            for (PaperInfo mPaperInfo : mPaperInfos) {
    //                if (mPaperInfo != null) {
    //                    if (mPaperInfo.fragment != null && mPaperInfo.tab != null) {
    //                        data.add(mPaperInfo.fragment);
    //                        mIndicator.addView(mPaperInfo.tab);
    //                    }
    //                }
    //            }
    //            adapter = new AdapterFragmentPager(fragmentManager, data);
    //            mViewPager.setAdapter(adapter);
    //            //            //预加载所有页面
    //            mViewPager.setOffscreenPageLimit(adapter.getCount());
    //        }
    //    }
    ArrayList<Fragment> data = new ArrayList();
    public void addItem(PaperInfo mPaperInfo) {
        if (mPaperInfo.fragment != null && mPaperInfo.tab != null) {
            data.add(mPaperInfo.fragment);
            mIndicator.addView(mPaperInfo.tab);
        }
    }
    public void done(FragmentManager fragmentManager){
        adapter = new AdapterFragmentPager(fragmentManager, data);
                    mViewPager.setAdapter(adapter);
                    //            //预加载所有页面
                    mViewPager.setOffscreenPageLimit(adapter.getCount());
    }

    public void setCurrentItem(int index) {
        if (index >= 0 && index < mViewPager.getAdapter().getCount()) {
            mIndicator.check(index);//默认选择第一个
            mViewPager.setCurrentItem(index);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 切换到 目标 页面
     *
     * @param position 目标页
     */
    @Override
    public void onPageSelected(int position) {
        //同时切换指示器
        mIndicator.check(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
