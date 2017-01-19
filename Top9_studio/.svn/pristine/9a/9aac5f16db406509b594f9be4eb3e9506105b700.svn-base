package com.zeustel.top9.fragments.html;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderItemInfo;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.bean.html.DesItem;
import com.zeustel.top9.bean.html.DesTemplate;
import com.zeustel.top9.bean.html.ItemInfo;
import com.zeustel.top9.bean.html.Label;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.CustomRecyclerView;

import java.util.List;

/**
 * 简介模板界面
 */
public class HtmlDesBakFragment extends HtmlAnimFragment {
    private static final String EXTRA_NAME = "htmlDes";
    //
    private TextView subTitle, promote, gameName, gameEnName, contentDes;
    private LinearLayout des_linearLayout;
    private DesTemplate htmlDes = null;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    //
    private ImageView des_detail_gameIcon;
    private TextView des_detail_gameName;
    private TextView des_detail_gameEnName;
    private LinearLayout des_detail_lable;
    private TextView des_detail_reason;
    private LinearLayout des_detail_items;
    private String gameEnName1;
    private String gameName1;

    public HtmlDesBakFragment() {
    }

    public static HtmlDesBakFragment newInstance(DesTemplate htmlDes) {
        HtmlDesBakFragment fragment = new HtmlDesBakFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, htmlDes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            htmlDes = (DesTemplate) getArguments().getSerializable(EXTRA_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View scrollContentView = inflater.inflate(R.layout.fragment_html_des_scroll, container, false);
        View fragment_html_des = scrollContentView.findViewById(R.id.fragment_html_des);
        //        View fragment_html_des_detail = scrollContentView.findViewById(R.id.fragment_html_des_detail);
        //init fragment_html_des
        subTitle = (TextView) fragment_html_des.findViewById(R.id.des_subTitle);
        des_linearLayout = (LinearLayout) fragment_html_des.findViewById(R.id.des_linearLayout);
        promote = (TextView) fragment_html_des.findViewById(R.id.des_promote);
        gameName = (TextView) fragment_html_des.findViewById(R.id.des_gameName);
        gameEnName = (TextView) fragment_html_des.findViewById(R.id.des_gameEnName);
        contentDes = (TextView) fragment_html_des.findViewById(R.id.des_contentDes);

        //init fragment_html_des_detail
        //        des_detail_gameIcon = (ImageView) fragment_html_des_detail.findViewById(R.id.des_detail_gameIcon);
        //        des_detail_gameName = (TextView) fragment_html_des_detail.findViewById(R.id.des_detail_gameName);
        //        des_detail_gameEnName = (TextView) fragment_html_des_detail.findViewById(R.id.des_detail_gameEnName);
        //        des_detail_lable = (LinearLayout) fragment_html_des_detail.findViewById(R.id.des_detail_lable);
        //        des_detail_reason = (TextView) fragment_html_des_detail.findViewById(R.id.des_detail_reason);
        //        des_detail_items = (LinearLayout) fragment_html_des_detail.findViewById(R.id.des_detail_items);

        updateFragment_html_des();
        //        updateFragment_html_des_detail();
        return scrollContentView;
    }

  /*  private void updateFragment_html_des_detail() {
        if (htmlDes != null) {
            imageLoader.displayImage(Constants.TEST_IMG + htmlDes.getGameIcon(), des_detail_gameIcon, Tools.options);
            des_detail_gameName.setText(Tools.isEmpty(gameName1) ? "" : gameName1);
            des_detail_gameEnName.setText(Tools.isEmpty(gameEnName1) ? "" : gameEnName1);
            List<Label> labels = htmlDes.getLabels();
            updateLableLayout(labels);
            String reason = htmlDes.getReason();
            des_detail_reason.setText(Tools.isEmpty(reason) ? "" : reason);
            List<DesItem> desItems = htmlDes.getDesItems();
            updateItems(desItems);
        }
    }*/

    private void updateItems(List<DesItem> desItems) {
        if (Tools.isEmpty(desItems)) {
            return;
        }
        for (int i = 0; i < desItems.size(); i++) {
            DesItem desItem = desItems.get(i);
            if (desItem != null) {

                View list_item_desView = getActivity().getLayoutInflater().inflate(R.layout.list_item_des, null);
                final TextView desItem_name = (TextView) list_item_desView.findViewById(R.id.desItem_name);
                CustomRecyclerView desItem_infos = (CustomRecyclerView) list_item_desView.findViewById(R.id.desItem_infos);
                String icon = desItem.getIcon();
                String name = desItem.getName();
                List<ItemInfo> detailInfos = desItem.getDetailInfos();
                desItem_name.setText(name);
                imageLoader.loadImage(Constants.TEST_IMG + icon, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        desItem_name.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(getResources(), loadedImage), null, null, null);
                    }
                });
                if (!Tools.isEmpty(detailInfos)) {
                    try {
                        desItem_infos.setAdapter(new OverallRecyclerAdapter<ItemInfo>(detailInfos, R.layout.list_item_des_info, HolderItemInfo.class));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void updateLableLayout(List<Label> labels) {
        if (Tools.isEmpty(labels)) {
            return;
        }
        for (int i = 0; i < labels.size(); i++) {
            Label label = labels.get(i);
            TextView textView = new TextView(getActivity());
            textView.setText(Tools.isEmpty(label.getName()) ? "" : label.getName());
            try {
                textView.setBackgroundColor(Color.parseColor(label.getColor()));
            } catch (Exception e) {
                e.printStackTrace();
                textView.setBackgroundColor(Color.RED);
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = Tools.dip2px(getActivity(), 10);
            params.rightMargin = Tools.dip2px(getActivity(), 10);
            des_detail_lable.addView(textView);
        }
    }

    private void updateFragment_html_des() {
        if (htmlDes != null) {
            String subTitle = htmlDes.getSubTitle();
            String promote = htmlDes.getPromote();
            gameEnName1 = htmlDes.getGameEnName();
            gameName1 = htmlDes.getGameName();
            String contentDes = htmlDes.getContentDes();
            String detailUrl = htmlDes.getDetailUrl();
            String theme = htmlDes.getTheme();
            imageLoader.loadImage(Constants.TEST_IMG + theme, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (loadedImage != null) {
                        //                        des_linearLayout.setBackground(new BitmapDrawable(loadedImage));
                        des_linearLayout.setBackgroundDrawable(new BitmapDrawable(loadedImage));
                    }
                }
            });
            this.subTitle.setText(Tools.isEmpty(subTitle) ? "" : subTitle);
            this.promote.setText(Tools.isEmpty(promote) ? "" : promote);
            this.gameName.setText(Tools.isEmpty(gameName1) ? "" : gameName1);
            this.gameEnName.setText(Tools.isEmpty(gameEnName1) ? "" : gameEnName1);
            this.contentDes.setText(Tools.isEmpty(contentDes) ? "" : contentDes);
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
    public void startAnim() {
    }
}
