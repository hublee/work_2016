package com.zeustel.top9.fragments;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zeustel.top9.R;
import com.zeustel.top9.bean.GameEvaluating;
import com.zeustel.top9.bean.HtmlPaper;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.VideoPlayer;

/**
 */
@Deprecated
public class EvaDetailFragment extends Fragment implements View.OnClickListener {
    public static final int LAYOUT_ID = R.layout.list_item_eva_head;
    private static final String EXTRA_NAME = "EvaDetail";
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private GameEvaluating gameEvaluating;
    private ImageView head_eva_banner;
    private TextView head_eva_subtitle;
    private LinearLayout head_eva_download;
    private LinearLayout head_eva_video;
    private TextView head_eva_title;
    private VideoPlayer head_eva_player;

    public EvaDetailFragment() {

    }

    public static EvaDetailFragment newInstance(HtmlPaper htmlPaper) {
        EvaDetailFragment fragment = new EvaDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, htmlPaper);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameEvaluating = (GameEvaluating) getArguments().getSerializable(EXTRA_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_eva_detail, container, false);
        AppBarLayout appBarLayout = (AppBarLayout) contentView.findViewById(R.id.appBar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                Tools.log_i(EvaDetailFragment.class, "onOffsetChanged", "i : " + i);
            }
        });
        initItemView(contentView);
        set();
        return contentView;
    }

    public void initItemView(View itemView) {
        head_eva_banner = (ImageView) itemView.findViewById(R.id.head_eva_banner);
        head_eva_subtitle = (TextView) itemView.findViewById(R.id.head_eva_subtitle);
        head_eva_title = (TextView) itemView.findViewById(R.id.head_eva_title);
        head_eva_download = (LinearLayout) itemView.findViewById(R.id.head_eva_download);
        head_eva_video = (LinearLayout) itemView.findViewById(R.id.head_eva_video);
        //        head_eva_player = (VideoPlayer) itemView.findViewById(R.id.head_eva_player);
        head_eva_download.setOnClickListener(this);
        head_eva_video.setOnClickListener(this);
    }

    public void set() {
        if (this.gameEvaluating != null) {
            imageLoader.displayImage(Constants.TEST_IMG + gameEvaluating.getBanner(), head_eva_banner, Tools.options);
            head_eva_subtitle.setText(gameEvaluating.getSubTitle());
            head_eva_title.setText(gameEvaluating.getTitle());
            //            head_eva_player.setVideoURI(Uri.parse(Constants.PATH_VIDEO + gameEvaluating.getVideoUrl()));
        }
    }

    @Override
    public void onClick(View v) {

    }
}
