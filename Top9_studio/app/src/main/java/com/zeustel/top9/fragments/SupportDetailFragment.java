package com.zeustel.top9.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.AdapterSupport;
import com.zeustel.top9.base.WrapNotAndRefreshFragment;
import com.zeustel.top9.bean.Feedback;
import com.zeustel.top9.bean.FeedbackReply;
import com.zeustel.top9.result.FeedbackReplyListResult;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBFeedbakReplyImp;
import com.zeustel.top9.utils.operate.DataBaseOperate;
import com.zeustel.top9.utils.operate.DataFeedbackReply;
import com.zeustel.top9.widgets.InputView;

import java.util.List;

/**
 * 反馈详细界面
 */
public class SupportDetailFragment extends WrapNotAndRefreshFragment<FeedbackReply> implements InputView.OnInputView {
    private static final String EXTRA_NAME = "Feedback";
    private Feedback feedback;
    private InputView inputView;
    private DataBaseOperate dataOperate;
    private AdapterSupport adapterSupport;

    public SupportDetailFragment() {
    }

    public static SupportDetailFragment newInstance(Feedback feedback) {
        SupportDetailFragment instance = new SupportDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, feedback);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            feedback = (Feedback) getArguments().getSerializable(EXTRA_NAME);
        }
        if (feedback == null) {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public View onBeforeCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_detail_html, container, false);
        inputView = (InputView) contentView.findViewById(R.id.detail_html_input);
        inputView.setOnInputView(this);
        return contentView;
    }

    @Override
    protected void onAfterCreateView(View contentView) {
        dataOperate = new DataFeedbackReply(getHandler(), getDbOperate());
        setDbOperate(new DBFeedbakReplyImp(getActivity()));
        setDataOperate(dataOperate);
        try {
            getData().clear();
            List<FeedbackReply> replyList = feedback.getReplyList();
            if (!Tools.isEmpty(replyList)) {
                getData().addAll(replyList);
            }
            adapterSupport = new AdapterSupport(getActivity(), getData(), feedback);
            setAdapter(adapterSupport);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
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
    public void onPullDownRefresh() {
        FeedbackReply feedbackReply = Tools.getFirstDataWithIgnore(getData(), AdapterSupport.NEED_SPACE/*title*/);
        long time = 0;
        if (feedbackReply != null) {
            time = feedbackReply.getReplyTime();
        }
        try {
            Bundle listBundle = getDataOperate().createListBundle(Constants.URL_FEEDBACK_REPLY_LIST, time, NetHelper.Flag.UPDATE);
            listBundle.putInt("sourceId", feedback.getId());
            getDataOperate().postListData(listBundle, FeedbackReplyListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPullUpRefresh() {
        FeedbackReply feedbackReply = Tools.getLastDataWithIgnore(getData(), AdapterSupport.NEED_SPACE/*title*/);
        long time = 0;
        if (feedbackReply != null) {
            time = feedbackReply.getReplyTime();
        }
        try {
            Bundle listBundle = getDataOperate().createListBundle(Constants.URL_FEEDBACK_REPLY_LIST, time, NetHelper.Flag.HISTORY);
            listBundle.putInt("sourceId", feedback.getId());
            getDataOperate().postListData(listBundle, FeedbackReplyListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHandlePublishSuccess(Result result) {
        inputView.done();
    }

    @Override
    public void onHandlePublishFailed(Result result) {
        inputView.done();
    }

    @Override
    public void onHandleListUpdate(Result result, Object obj) {
        getLoading().loadSuccess();
        cancelRefresh();
        List<FeedbackReply> tempList = (List<FeedbackReply>) obj;
        if (!Tools.isEmpty(tempList)) {
            getData().addAll(AdapterSupport.NEED_SPACE, tempList);
            getAdapter().notifyItemRangeInserted(AdapterSupport.NEED_SPACE, tempList.size());
            getAdapter().notifyItemChanged(AdapterSupport.NEED_SPACE + tempList.size());
        }
    }

    @Override
    public void onHandleUpdate(Object obj) {
    }

    @Override
    public void onHandleNo() {
        getLoading().loadSuccess();
        cancelRefresh();
    }

    @Override
    public void onHandleHistory(Object obj) {
        getLoading().loadSuccess();
        cancelRefresh();
        List<FeedbackReply> tempList = (List<FeedbackReply>) obj;
        if (!Tools.isEmpty(tempList)) {
            int size = getData().size();
            getData().addAll(tempList);
            getAdapter().notifyItemRangeInserted(size - 1, tempList.size());
        }
    }

    @Override
    public void onHandleListHistory(Result result, Object obj) {
    }

    @Override
    public void onHandleFailed() {
        if (Tools.isEmptyWithIgnore(getData(), AdapterSupport.NEED_SPACE)) {
        } else {
            Tools.showToast(getActivity(), R.string.load_request_failed);
        }
        getLoading().loadSuccess();
        cancelRefresh();
    }

    @Override
    public void onInputView(String content) {
        FeedbackReply feedbackReply = new FeedbackReply();
        feedbackReply.setFeedbackId(feedback.getId());
        feedbackReply.setReplyContent(content);
        getDataOperate().publishData(feedbackReply);
    }
}
