package com.zeustel.top9.fragments.html;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zeustel.top9.R;
import com.zeustel.top9.ScreenshotExhibition;
import com.zeustel.top9.VideoActivity;
import com.zeustel.top9.bean.html.SystemTemplate;
import com.zeustel.top9.bean.html.SystemTemplateItem;
import com.zeustel.top9.bean.html.TopItem;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.OverScrollView;
import com.zeustel.top9.widgets.OverScrollWarpLayout;
import com.zeustel.top9.widgets.PrintTextView;
import com.zeustel.top9.widgets.ScrollGroup;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HtmlSystemFragment extends HtmlAnimFragment implements View.OnClickListener, OverScrollView.OnScrollListener, OverScrollWarpLayout.OnScrollChange {
    private static final String EXTRA_NAME = "SystemTemplate";
    private SystemTemplate systemTemplate;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageView main_video_cover;
    private RelativeLayout main_video_layout;
    private FrameLayout listenerFlag;
    private ImageView main_video_toggle;
    private LinearLayout main_items;
    private LinearLayout main_video_bootom;
    private WebView fragment_html_sys_web;
    private View contentView;
    private OverScrollView main_over;
    private ScrollGroup main_video_layout_flag;
    private PrintTextView main_big_title;
    private int video_height;
    private TopItem topItem;
    private String url;
    private String dataUrl;
    private List<SystemTemplateItem> items;

    @Override
    public void onDestroyView() {
        Tools.recycleImg(main_video_cover);
        super.onDestroyView();
    }

    public HtmlSystemFragment() {
    }

    public static HtmlSystemFragment newInstance(SystemTemplate systemTemplate) {
        HtmlSystemFragment fragment = new HtmlSystemFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, systemTemplate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            systemTemplate = (SystemTemplate) getArguments().getSerializable(EXTRA_NAME);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_system, container, false);
        main_over = (OverScrollView) contentView.findViewById(R.id.main_over);
        main_video_layout_flag = (ScrollGroup) contentView.findViewById(R.id.main_video_layout_flag);
        fragment_html_sys_web = (WebView) contentView.findViewById(R.id.fragment_html_sys_web);
        main_video_bootom = (LinearLayout) contentView.findViewById(R.id.main_video_bootom);
        main_items = (LinearLayout) contentView.findViewById(R.id.main_items);
        main_video_cover = (ImageView) contentView.findViewById(R.id.main_video_cover);

        listenerFlag = (FrameLayout) contentView.findViewById(R.id.listenerFlag);
        main_video_layout = (RelativeLayout) contentView.findViewById(R.id.main_video_layout);
        main_video_toggle = (ImageView) contentView.findViewById(R.id.main_video_toggle);
        main_big_title = (PrintTextView) contentView.findViewById(R.id.main_big_title);

        main_big_title.setDuration(500);

        listenerFlag.setOnClickListener(this);
        main_video_cover.setOnClickListener(this);
        main_video_toggle.setOnClickListener(this);
        main_big_title.setText(systemTemplate.getName());
        updateFragment_html_sys_top();
        initWeb(fragment_html_sys_web);
        if (systemTemplate != null && fragment_html_sys_web != null) {
            try {
                fragment_html_sys_web.loadUrl(Constants.URL_SERVER + systemTemplate.getDetailUrl());
            } catch (Exception e) {
                e.printStackTrace();
//                Tools.log_i(HtmlSystemFragment.class, "onCreateView", "load web failed ...");
            }
        }
        Tools.endCreateOperate(contentView, new Runnable() {
            @Override
            public void run() {
//                Tools.log_i(HtmlSystemFragment.class, "run", "???????????");
                int[] displayMetrics = Tools.getDisplayMetrics(getActivity());
                int stateHeight = Tools.measureStateBarHeight(getActivity());
                int height = displayMetrics[1] - main_items.getTop() - stateHeight - main_video_bootom.getTop();
                main_items.setMinimumHeight(height);
            }
        });
        main_over.setOnScrollChange(this);
        main_over.setOnScrollListener(this);
        Tools.endCreateOperate(main_video_layout, new Runnable() {
            @Override
            public void run() {
                video_height = main_video_layout.getHeight();
//                Tools.log_i(HtmlSystemFragment.class, "run", "video_height : " + video_height);
            }
        });
        return contentView;
    }

    TopItem.Type type;

    /**
     * 头布局
     */
    private void updateFragment_html_sys_top() {
        if (systemTemplate != null) {
            topItem = systemTemplate.getTopItem();
            if (topItem != null) {
                type = topItem.getType();
                url = topItem.getUrl();
                dataUrl = topItem.getDataUrl();
                if (type != null) {
                    if (type.equals(TopItem.Type.VIDEO)) {
                        main_video_toggle.setVisibility(View.VISIBLE);
                    } else {
                        main_video_toggle.setVisibility(View.GONE);
                    }
                }
                imageLoader.displayImage(Constants.TEST_IMG + url, main_video_cover, Tools.options);
            }
            items = systemTemplate.getItems();
            updateSystemTemplateItem(items);
        }
    }

    private ViewGroup.LayoutParams layoutParams;

    @Override
    public void onScrollChange(int x, int y) {
        if (y > 0) {
            //            main_video_layout_flag.scrollBy(0, y);
        } else {
            layoutParams = main_video_layout.getLayoutParams();
            layoutParams.height = video_height - y;
            main_video_layout.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onScroll(int l, int t, int oldl, int oldt) {
        if (t - oldt > 400/*异常距离*/) {

        } else {
            main_video_layout_flag.scrollTo(0, (int) (t * 0.5f));
        }

//        Tools.log_i(HtmlSystemFragment.class, "onScroll", "l: " + l + " , t: " + t + " , oldl : " + oldl + " , oldt : " + oldt);
    }

    private class Item {
        protected PrintTextView sys_item_small_name;
        protected PrintTextView sys_item_small_title;
        protected PrintTextView sys_item_small_des;

        public Item(PrintTextView sys_item_small_name, PrintTextView sys_item_small_title, PrintTextView sys_item_small_des) {
            this.sys_item_small_name = sys_item_small_name;
            this.sys_item_small_title = sys_item_small_title;
            this.sys_item_small_des = sys_item_small_des;
        }
    }

    private List<Item> mItems = new ArrayList();

    @Override
    public void onDestroy() {
        if (mItems != null) {
            mItems.clear();
            mItems = null;
        }
        super.onDestroy();
    }

    private void updateSystemTemplateItem(List<SystemTemplateItem> items) {
        if (!Tools.isEmpty(items)) {
            PrintTextView sys_item_small_name;
            ImageView sys_item_small_icon;
            PrintTextView sys_item_small_title;
            PrintTextView sys_item_small_des;
            String name;
            String icon;
            String title;
            String des;
            LinearLayout.LayoutParams params;
            for (int i = 0; i < items.size(); i++) {
                SystemTemplateItem item = items.get(i);
                if (item != null) {
                    View itemView = getActivity().getLayoutInflater().inflate(R.layout.list_item_small_sys, null);

                    sys_item_small_name = (PrintTextView) itemView.findViewById(R.id.sys_item_small_name);
                    sys_item_small_icon = (ImageView) itemView.findViewById(R.id.sys_item_small_icon);
                    sys_item_small_title = (PrintTextView) itemView.findViewById(R.id.sys_item_small_title);
                    sys_item_small_des = (PrintTextView) itemView.findViewById(R.id.sys_item_small_des);
                    sys_item_small_name.setDuration(500);
                    sys_item_small_title.setDuration(800);
                    sys_item_small_des.setDuration(800);
                    mItems.add(new Item(sys_item_small_name, sys_item_small_title, sys_item_small_des));

                    if (item != null) {
                        name = item.getName();
                        icon = item.getIcon();
                        title = item.getTitle();
                        des = item.getDes();
                        imageLoader.displayImage(Constants.TEST_IMG + icon, sys_item_small_icon, Tools.options);
                        sys_item_small_name.setText(Tools.isEmpty(name) ? "" : name);
                        sys_item_small_title.setText(Tools.isEmpty(title) ? "" : title);
                        try {
                            //                sys_item_small_title.setTextColor(Color.parseColor(themeColor));
                        } catch (Exception e) {
                            e.printStackTrace();
                            sys_item_small_title.setTextColor(Color.RED);
                        }
                        sys_item_small_des.setText(des);
                    }
                    params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.topMargin = Tools.dip2px(getActivity(), 15);/*15dp*/
                    main_items.addView(itemView, params);

                }
            }
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
    public void onClick(View v) {
        if (listenerFlag.equals(v) || main_video_cover.equals(v) || main_video_toggle.equals(v)) {
            if (systemTemplate != null) {
                TopItem topItem = systemTemplate.getTopItem();
                if (topItem != null) {
                    TopItem.Type type = topItem.getType();
                    String url = topItem.getUrl();
                    String dataUrl = topItem.getDataUrl();
                    if (type.equals(TopItem.Type.VIDEO)) {
                        Intent intent = new Intent(getContext(), VideoActivity.class);
                        intent.putExtra(VideoActivity.EXTRA_VIDEO_UR, Constants.URL_SERVER + dataUrl);
                        intent.putExtra(VideoActivity.EXTRA_VIDEO_COVER_NET, Constants.TEST_IMG + url);
                        startActivity(intent);
                    } else {
                       /* Intent exIntent = new Intent(getContext(), TempScreenshotExhibition.class);
                        exIntent.putExtra(TempScreenshotExhibition.EXTRA_NAME_ID, systemTemplate.getId());
                        exIntent.putExtra(TempScreenshotExhibition.EXTRA_NAME_URL, Constants.URL_SERVER + dataUrl);*/
                        Intent exIntent = new Intent(getContext(), ScreenshotExhibition.class);
                        exIntent.putExtra(ScreenshotExhibition.EXTRA_NAME_ID, systemTemplate.getId());
                        exIntent.putExtra(ScreenshotExhibition.EXTRA_NAME_URL, Constants.URL_SERVER + dataUrl);
                        startActivity(exIntent);
                    }
                }
            }
        }
    }

    @Override
    public void startAnim() {
        main_big_title.print(main_big_title.getText(), +100);
        if (mItems != null && !mItems.isEmpty()) {
            Item item;
            for (int i = 0; i < mItems.size(); i++) {
                item = mItems.get(i);
                if (item != null) {
                    item.sys_item_small_title.print(item.sys_item_small_title.getText());
                    item.sys_item_small_des.print(item.sys_item_small_des.getText());
                    item.sys_item_small_name.print(item.sys_item_small_name.getText());
                }
            }
        }
    }
}
