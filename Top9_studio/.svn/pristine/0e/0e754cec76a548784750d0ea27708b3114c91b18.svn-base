package com.zeustel.top9.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zeustel.gallery.Gallery2Utils;
import com.zeustel.top9.LoginActivity;
import com.zeustel.top9.R;
import com.zeustel.top9.adapters.AdapterSupportPicture;
import com.zeustel.top9.base.WrapNotAndHandleFragment;
import com.zeustel.top9.bean.CommunityTopic;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataCommunityTopic;

import java.util.ArrayList;
import java.util.List;

/**
 * 发表话题
 */
public class PublishTopicFragment extends WrapNotAndHandleFragment implements View.OnClickListener, TabLayout.OnTabSelectedListener {
    private static final String EXTRA_NAME = "gameId";
    private static final int MAX_COUNT = 6;
    private static final int SPAN_COUNT = 3;
    private int gameId;
    private int topicType;
    private List<String> path = new ArrayList();
    private TabLayout publish_title_tab;
    private EditText publish_title_edit;
    private EditText publish_content_edit;
    private TextView publish_add_emoticon;
    private TextView publish_add_file;
    private ImageButton publish_back;
    private Button publish_send;
    private RecyclerView publish_picture_group;
    private AdapterSupportPicture adapter;
    private DataCommunityTopic mDataCommunity;
    private ProgressDialog loginDialog;
    private String[] classify = null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (path != null) {
            path.clear();
            path = null;
        }
    }

    public PublishTopicFragment() {
    }

    public static PublishTopicFragment newInstance(int gameId) {
        PublishTopicFragment fragment = new PublishTopicFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_NAME, gameId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameId = getArguments().getInt(EXTRA_NAME);
        }
        if (gameId == 0) {
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_publish_topic, container, false);
        init(contentView);
        mDataCommunity = new DataCommunityTopic(getHandler(), null);
        return contentView;
    }

    private void init(View contentView) {
        publish_title_tab = (TabLayout) contentView.findViewById(R.id.publish_title_tab);
        publish_title_edit = (EditText) contentView.findViewById(R.id.publish_title_edit);
        publish_content_edit = (EditText) contentView.findViewById(R.id.publish_content_edit);
        publish_add_emoticon = (TextView) contentView.findViewById(R.id.publish_add_emoticon);
        publish_add_file = (TextView) contentView.findViewById(R.id.publish_add_file);
        publish_back = (ImageButton) contentView.findViewById(R.id.publish_back);
        publish_send = (Button) contentView.findViewById(R.id.publish_send);
        publish_picture_group = (RecyclerView) contentView.findViewById(R.id.publish_picture_group);
        publish_title_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        publish_title_tab.setTabTextColors(getResources().getColor(R.color.def_font), getResources().getColor(R.color.red));
        classify = getResources().getStringArray(R.array.topicClassify);
        for (int i = 0; i < classify.length; i++) {
            String title = classify[i];
            publish_title_tab.addTab(publish_title_tab.newTab().setText(title));
        }
        publish_title_tab.setOnTabSelectedListener(this);
        publish_add_emoticon.setOnClickListener(this);
        publish_add_file.setOnClickListener(this);
        publish_back.setOnClickListener(this);
        publish_send.setOnClickListener(this);
        publish_picture_group.setItemAnimator(new DefaultItemAnimator());
        publish_picture_group.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), SPAN_COUNT);
        publish_picture_group.setLayoutManager(mLayoutManager);
        try {
            adapter = new AdapterSupportPicture(path);
            publish_picture_group.setAdapter(adapter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Tools.endCreateOperate(publish_picture_group, new Runnable() {
            @Override
            public void run() {
                adapter.setSpanCount(MAX_COUNT, SPAN_COUNT, publish_picture_group.getWidth(), publish_picture_group.getHeight());
            }
        });
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
        switch (v.getId()) {
            case R.id.publish_add_emoticon:
                break;
            case R.id.publish_add_file:
                if (MAX_COUNT > path.size()) {
                    Gallery2Utils.startGallery(PublishTopicFragment.this, MAX_COUNT - path.size());
                } else {
                    Tools.showToast(getContext(), R.string.self_support_limit);
                }
                break;
            case R.id.publish_back:
                getActivity().finish();
                break;
            case R.id.publish_send:
                send();
                break;
        }
    }

    private void send() {
        if (!Constants.USER.isOnline()) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        String title = publish_title_edit.getText().toString();
        String content = publish_content_edit.getText().toString();
        if (!Tools.isEmpty(title)) {
            if (!Tools.isEmpty(content)) {
                CommunityTopic communityTopic = new CommunityTopic();
                communityTopic.setGameId(gameId);
                communityTopic.setTopicType(topicType);
                communityTopic.setTitle(title);
                communityTopic.setContent(content);
                communityTopic.setImages(Tools.convertPathStr(path));
                if (loginDialog == null) {
                    loginDialog = ProgressDialog.show(getContext(), "", getResources().getString(R.string.input_send_ing));
                } else {
                    loginDialog.show();
                }
                mDataCommunity.publishData(communityTopic);
            } else {
                publish_content_edit.setError(getString(R.string.publish_content_empty));
            }
        } else {
            publish_title_edit.setError(getString(R.string.publish_title_empty));
        }

    }

    @Override
    public void onHandlePublishSuccess() {
        super.onHandlePublishSuccess();
        Tools.log_i(PublishTopicFragment.class,"onHandlePublishSuccess","");
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
        getActivity().finish();
    }

    @Override
    public void onHandlePublishFailed() {
        super.onHandlePublishFailed();
        Tools.log_i(PublishTopicFragment.class,"onHandlePublishFailed","");
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
    }

    @Override
    public void onHandleFailed() {
        Tools.log_i(PublishTopicFragment.class,"onHandleFailed","");
        super.onHandleFailed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<String> temp = Gallery2Utils.onActivityResult(requestCode, resultCode, data);
        if (!Tools.isEmpty(temp)) {
            final int start = path.size();
            path.addAll(temp);
            if (adapter != null) {
                adapter.notifyItemRangeInserted(start, path.size());
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        topicType = tab.getPosition();
        Tools.log_i(PublishTopicFragment.class, "onTabSelected", "topicType : " + topicType);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Tools.log_i(PublishTopicFragment.class, "onTabUnselected", "");
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Tools.log_i(PublishTopicFragment.class, "onTabReselected", "");
    }
}
