package com.zeustel.top9.fragments;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderSupportNote;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.base.WrapNotAndRefreshFragment;
import com.zeustel.top9.bean.Feedback;
import com.zeustel.top9.result.FeedbackListResult;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBFeedbackImp;
import com.zeustel.top9.utils.operate.DataFeedback;
import com.zeustel.top9.widgets.CircleImage;
import com.zeustel.top9.widgets.CustomRecyclerView;

/**
 * 反馈列表
 */
public class SupportNoteFragment extends WrapNotAndRefreshFragment<Feedback> implements CustomRecyclerView.OnItemClickListener {
    private CircleImage note_icon;
    private TextView note_title;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private OverallRecyclerAdapter<Feedback> adapter;

    public SupportNoteFragment() {
    }

    @Override
    public View onBeforeCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_note, container, false);
        note_icon = (CircleImage) contentView.findViewById(R.id.note_icon);
        note_title = (TextView) contentView.findViewById(R.id.note_title);
        return contentView;
    }

    @Override
    protected void onAfterCreateView(View contentView) {
        setDbOperate(new DBFeedbackImp(getContext()));
        setDataOperate(new DataFeedback(getHandler(), getDbOperate()));
        try {
            adapter = new OverallRecyclerAdapter<Feedback>(getData(), R.layout.list_item_support_note, HolderSupportNote.class);
            setAdapter(adapter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        getRecyler().setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        note_title.setText((Html.fromHtml(getString(R.string.html_font, "#484D57"/*color*/, getString(R.string.self_support_feedback)))));
        imageLoader.displayImage(Constants.TEST_IMG + Constants.USER.getIcon(), note_icon, Tools.options);
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
    public void onPullDownRefresh() {
        Feedback feedback = Tools.getFirstData(getData());
        long time = 0;
        if (feedback != null) {
            time = feedback.getFeedbackTime();
        }
        try {
            getDataOperate().postListData(getDataOperate().createListBundle(Constants.URL_FEEDBACK_LIST, time, NetHelper.Flag.UPDATE), FeedbackListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPullUpRefresh() {
        Feedback feedback = Tools.getLastData(getData());
        long time = 0;
        if (feedback != null) {
            time = feedback.getFeedbackTime();
        }
        try {
            getDataOperate().postListData(getDataOperate().createListBundle(Constants.URL_FEEDBACK_LIST, time, NetHelper.Flag.HISTORY), FeedbackListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onItemClick(RecyclerView parent, RecyclerView.ViewHolder viewHolder, View itemView, int viewType, int position) {
        getFragmentManager().beginTransaction().addToBackStack(null).add(R.id.support, SupportDetailFragment.newInstance(adapter.getItem(position))).commit();
        return true;
    }
}
