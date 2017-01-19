package com.zeustel.top9.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zeustel.top9.R;
import com.zeustel.top9.base.WrapOneAndHandleFragment;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.CircleImage;

/**
 * 内置选项卡界面
 */
public abstract class ChoiceContainerFragment extends WrapOneAndHandleFragment {
    private CircleImage overall_self_icon;
    private TextView overall_self_title;
    private TabLayout overall_self_tab;
    private ViewPager overall_self_pager;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private PagerAdapter adapter;

    public ChoiceContainerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_choice_container, container, false);
        overall_self_icon = (CircleImage) contentView.findViewById(R.id.overall_self_icon);
        overall_self_title = (TextView) contentView.findViewById(R.id.overall_self_title);
        overall_self_tab = (TabLayout) contentView.findViewById(R.id.overall_self_tab);
        overall_self_pager = (ViewPager) contentView.findViewById(R.id.overall_self_pager);
        overall_self_tab.setTabMode(TabLayout.MODE_FIXED);
        overall_self_tab.setTabTextColors(getResources().getColor(R.color.def_font), getResources().getColor(R.color.red));
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = getAdapterWithPageTitles();
        overall_self_tab.setTabsFromPagerAdapter(adapter);
        overall_self_tab.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(overall_self_pager));
        overall_self_pager.setAdapter(adapter);
        overall_self_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(overall_self_tab));
        imageLoader.displayImage(Constants.TEST_IMG + Constants.USER.getIcon(), overall_self_icon, Tools.options);
        overall_self_title.setText(getTitle());
    }

    /**
     * 获取标题
     *
     * @return
     */
    protected abstract CharSequence getTitle();

    /**
     * 获取重写PageTitle的适配器
     *
     * @return
     */
    protected abstract PagerAdapter getAdapterWithPageTitles();

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }
}
