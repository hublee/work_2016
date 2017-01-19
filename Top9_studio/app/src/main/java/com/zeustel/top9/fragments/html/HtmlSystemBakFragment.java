package com.zeustel.top9.fragments.html;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zeustel.top9.R;
import com.zeustel.top9.ScreenshotExhibition;
import com.zeustel.top9.VideoActivity;
import com.zeustel.top9.adapters.HolderItemSys;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.bean.html.ParamMini;
import com.zeustel.top9.bean.html.SystemTemplate;
import com.zeustel.top9.bean.html.SystemTemplateItem;
import com.zeustel.top9.bean.html.TopItem;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.CustomRecyclerView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HtmlSystemBakFragment extends HtmlAnimFragment implements View.OnClickListener {
    private static final String EXTRA_NAME = "SystemTemplate";
    private SystemTemplate systemTemplate;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private RelativeLayout main_video_layout;
    private ImageView main_video_cover;
    private ImageView main_video_toggle;
    private CustomRecyclerView main_items;
    private ImageView sys_bottom_mark;
    private TextView sys_bottom_name;
    private LinearLayout sys_bottom_item;

    public HtmlSystemBakFragment() {
    }

    public static HtmlSystemBakFragment newInstance(SystemTemplate systemTemplate) {
        HtmlSystemBakFragment fragment = new HtmlSystemBakFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View systemView = null;//inflater.inflate(R.layout.fragment_html_system, container, false);
        //        View fragment_html_sys_top = systemView.findViewById(R.id.fragment_html_sys_top);
        //        View fragment_html_sys_bottom = systemView.findViewById(R.id.fragment_html_sys_bottom);
        //
        //        main_video_layout = (RelativeLayout) fragment_html_sys_top.findViewById(R.id.main_video_layout);
        //        main_video_cover = (ImageView) fragment_html_sys_top.findViewById(R.id.main_video_cover);
        //        main_video_toggle = (ImageView) fragment_html_sys_top.findViewById(R.id.main_video_toggle);
        //        main_items = (CustomRecyclerView) fragment_html_sys_top.findViewById(R.id.main_items);

        main_video_cover.setOnClickListener(this);
        main_video_toggle.setOnClickListener(this);

        //        sys_bottom_mark = (ImageView) fragment_html_sys_bottom.findViewById(R.id.sys_bottom_mark);
        //        sys_bottom_name = (TextView) fragment_html_sys_bottom.findViewById(R.id.sys_bottom_name);
        //        sys_bottom_item = (LinearLayout) fragment_html_sys_bottom.findViewById(R.id.sys_bottom_item);

        updateFragment_html_sys_top();
        //        updateFragment_html_sys_bottom();
        //
        return systemView;
    }

  /*  private void updateFragment_html_sys_bottom() {
        if (systemTemplate != null) {
            String markUrl = systemTemplate.getMarkUrl();
            String name = systemTemplate.getName();
            if (!Tools.isEmpty(markUrl)) {
                imageLoader.displayImage(Constants.TEST_IMG + markUrl, sys_bottom_mark, Tools.options);

            }
            sys_bottom_name.setText(Tools.isEmpty(name) ? "" : name);
            List<SystemTemplateItem> items = systemTemplate.getItems();
            updateSys_bottom_item(items);
        }
    }*/

  /*  private void updateSys_bottom_item(List<SystemTemplateItem> items) {
        if (!Tools.isEmpty(items)) {
            for (int i = 0; i < items.size(); i++) {
                SystemTemplateItem mSystemTemplateItem = items.get(i);
                if (mSystemTemplateItem != null) {
                    View list_item_sys = getActivity().getLayoutInflater().inflate(R.layout.list_item_sys, null);
                    TextView sys_item_name = (TextView) list_item_sys.findViewById(R.id.sys_item_name);
                    TextView sys_item_title = (TextView) list_item_sys.findViewById(R.id.sys_item_title);
                    LinearLayout sys_paramMinis = (LinearLayout) list_item_sys.findViewById(R.id.sys_paramMinis);
                    String title = mSystemTemplateItem.getTitle();
                    String name = mSystemTemplateItem.getName();
                    String themeColor = mSystemTemplateItem.getThemeColor();
                    String lineColor = mSystemTemplateItem.getLineColor();
                    List<ParamMini> paramMinis = mSystemTemplateItem.getParamMinis();
                    sys_item_name.setText(Tools.isEmpty(name) ? " " : name);
                    try {
                        sys_item_name.setBackgroundColor(Color.parseColor(themeColor));
                    } catch (Exception e) {
                        e.printStackTrace();
                        sys_item_name.setBackgroundColor(Color.RED);
                    }
                    sys_item_title.setText(Tools.isEmpty(title) ? " " : title);
                    View view = updateParamMini(paramMinis);
                    if (view != null) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        sys_paramMinis.addView(view, params);
                    }
                    sys_bottom_item.addView(list_item_sys);
                }
            }
        }
    }*/

    private View updateParamMini(List<ParamMini> paramMinis) {
        if (Tools.isEmpty(paramMinis)) {
            return null;
        }
        LinearLayout mLinearLayout = new LinearLayout(getActivity());
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < paramMinis.size(); i++) {
            ParamMini mParamMini = paramMinis.get(i);
            if (mParamMini != null) {
                ParamMini.Type type = mParamMini.getType();
                String flagImg = mParamMini.getFlagImg();
                String value = mParamMini.getValue();
                if (ParamMini.Type.IMG.equals(type)) {
                    //图片
                    ImageView imageView = new ImageView(getActivity());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mLinearLayout.addView(imageView, params);
                    imageLoader.displayImage(Constants.TEST_IMG + value, imageView, Tools.options);
                } else {
                    final TextView textView = new TextView(getContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mLinearLayout.addView(textView, params);
                    textView.setText(Tools.isEmpty(value) ? "" : value);
                    imageLoader.loadImage(Constants.TEST_IMG + flagImg, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            textView.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(), loadedImage), null, null, null);
                        }
                    });

                }
            }
        }
        return mLinearLayout;
    }

    private void updateFragment_html_sys_top() {
        if (systemTemplate != null) {
            TopItem topItem = systemTemplate.getTopItem();
            if (topItem != null) {
                TopItem.Type type = topItem.getType();
                String url = topItem.getUrl();
                String dataUrl = topItem.getDataUrl();
                if (type.equals(TopItem.Type.VIDEO)) {
                    main_video_toggle.setVisibility(View.VISIBLE);
                } else {
                    main_video_toggle.setVisibility(View.GONE);
                }
                imageLoader.displayImage(Constants.TEST_IMG + url, main_video_cover, Tools.options);
            }
            List<SystemTemplateItem> items = systemTemplate.getItems();
            if (!Tools.isEmpty(items)) {
                try {
                    main_items.setAdapter(new OverallRecyclerAdapter<SystemTemplateItem>(items, R.layout.list_item_small_sys, HolderItemSys.class));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
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
        if (main_video_cover.equals(v) || main_video_toggle.equals(v)) {
            if (systemTemplate != null) {
                TopItem topItem = systemTemplate.getTopItem();
                if (topItem != null) {
                    TopItem.Type type = topItem.getType();
                    String url = topItem.getUrl();
                    String dataUrl = topItem.getDataUrl();
                    if (type.equals(TopItem.Type.VIDEO)) {
                        Intent intent = new Intent(getActivity(), VideoActivity.class);
                        intent.putExtra(VideoActivity.EXTRA_VIDEO_UR, Constants.URL_SERVER + dataUrl);
                        intent.putExtra(VideoActivity.EXTRA_VIDEO_COVER_NET, Constants.TEST_IMG + url);
                        startActivity(intent);
                    } else {
                        Intent exIntent = new Intent(getActivity(), ScreenshotExhibition.class);
                        exIntent.putExtra(ScreenshotExhibition.EXTRA_NAME_ID, systemTemplate.getId());
                        exIntent.putExtra(ScreenshotExhibition.EXTRA_NAME_URL, dataUrl);
                        startActivity(exIntent);
                    }
                }
            }
        }
    }

    @Override
    public void startAnim() {
    }
}
