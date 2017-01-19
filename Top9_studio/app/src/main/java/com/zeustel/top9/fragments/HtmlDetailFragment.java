package com.zeustel.top9.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.AdapterWeb;
import com.zeustel.top9.base.WrapNotAndRefreshFragment;
import com.zeustel.top9.bean.CommunityInfo;
import com.zeustel.top9.bean.CommunityTopic;
import com.zeustel.top9.bean.GameEvaluating;
import com.zeustel.top9.bean.HtmlComment;
import com.zeustel.top9.bean.HtmlPaper;
import com.zeustel.top9.result.CommunityInfoResult;
import com.zeustel.top9.result.CommunityTopicResult;
import com.zeustel.top9.result.GameEvaluatingResult;
import com.zeustel.top9.result.HtmlCommentListResult;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.HtmlFactory;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBHtmlCommentImp;
import com.zeustel.top9.utils.operate.DataBaseOperate;
import com.zeustel.top9.utils.operate.DataHtml;
import com.zeustel.top9.utils.operate.DataHtmlComment;
import com.zeustel.top9.widgets.InputView;

import java.util.List;

/**
 * 评测详细
 */
@Deprecated
public class HtmlDetailFragment extends WrapNotAndRefreshFragment<HtmlComment> implements InputView.OnInputView {
    private static final String EXTRA_NAME = "htmlPager";
    private static final String EXTRA_NAME_SIMPLE = "simple";
    private HtmlPaper htmlPaper;
    private InputView detail_html_input;
    private int whichId;
    private int commentType;
    private String url;
    private DataBaseOperate dataBaseOperate;
    //请求单个htmlpager对象
    private DataHtml dataHtml;
    private int type;
    private Class cls;
    private boolean singleDone;
    private OnFragmentInteractionListener interactionListener;

    public HtmlDetailFragment() {
    }

    public static HtmlDetailFragment newInstance(HtmlPaper htmlPaper) {
        HtmlDetailFragment fragment = new HtmlDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, htmlPaper);
        fragment.setArguments(args);
        return fragment;
    }

    public static HtmlDetailFragment newInstance(HtmlPaper htmlPaper, int type) {
        HtmlDetailFragment fragment = new HtmlDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, htmlPaper);
        args.putInt(EXTRA_NAME_SIMPLE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            interactionListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            interactionListener = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            htmlPaper = (HtmlPaper) getArguments().getSerializable(EXTRA_NAME);
            if (htmlPaper != null) {
                if (htmlPaper instanceof GameEvaluating) {
                    url = Constants.URL_EVALUATING_COMMENT_LIST;
                    commentType = HtmlComment.CommentType.TYPE_EVALUATING;
                    cls = GameEvaluatingResult.class;
                } else if (htmlPaper instanceof CommunityInfo) {
                    url = Constants.URL_TOP9_INFO_COMMENT_LIST;
                    commentType = HtmlComment.CommentType.TYPE_INFO;
                    cls = CommunityInfoResult.class;
                } else if (htmlPaper instanceof CommunityTopic) {
                    url = Constants.URL_TOP9_TOPIC_COMMENT_LIST;
                    commentType = HtmlComment.CommentType.TYPE_TOPIC;
                    cls = CommunityTopicResult.class;
                }
                whichId = htmlPaper.getId();
            }
            type = getArguments().getInt(EXTRA_NAME_SIMPLE, -1);
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
    public View onBeforeCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_detail_html, container, false);
        detail_html_input = (InputView) contentView.findViewById(R.id.detail_html_input);
        if (detail_html_input != null) {
        detail_html_input.setOnInputView(this);
        }
        return contentView;
    }

    @Override
    protected void onAfterCreateView(View contentView) {
        setDbOperate(new DBHtmlCommentImp(getActivity()));
        setDataOperate(new DataHtmlComment(getHandler(), getDbOperate()));
        try {
            setAdapter(new AdapterWeb(getData(), htmlPaper));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        dataBaseOperate = getDataOperate();
        dataHtml = HtmlFactory.newDataHtml(getActivity(), getHandler(), commentType);
    }

    @Override
    protected void onFirstRefresh() {
        if (type != -1) {
            try {
                Tools.log_i(HtmlDetailFragment.class, "onFirstRefresh", "sourceId : " + whichId + ",type : " + type);
                dataHtml.getSingleData(dataHtml.createSingleBundle(Constants.URL_SEARCH_DETAIL + "?sourceId=" + whichId + "&type=" + type, whichId), cls);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.onFirstRefresh();
        }
    }

    @Override
    public void onPullDownRefresh() {
        HtmlComment htmlComment = Tools.getFirstDataWithIgnore(getData(), AdapterWeb.NEED_SPACE/*title,web*/);
        long time = 0;
        if (htmlComment != null) {
            time = htmlComment.getTime();
        }
        try {
            dataBaseOperate.getListData(dataBaseOperate.createListBundle(url + "?flag=" +
                    NetHelper.Flag.UPDATE + "&time=" + time + "&sourceId=" + whichId, time, NetHelper.Flag.UPDATE), HtmlCommentListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPullUpRefresh() {
        HtmlComment htmlComment = Tools.getLastDataWithIgnore(getData(), AdapterWeb.NEED_SPACE/*title,web*/);
        long time = 0;
        if (htmlComment != null) {
            time = htmlComment.getTime();
        }
        try {
            dataBaseOperate.getListData(dataBaseOperate.createListBundle(url + "?flag=" +
                    NetHelper.Flag.HISTORY + "&time=" + time + "&sourceId=" + whichId, time, NetHelper.Flag.HISTORY), HtmlCommentListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHandleSingleSuccess(Result result, Object obj) {
        Tools.log_i(HtmlDetailFragment.class, "onHandleSingleSuccess", "");
        if (obj != null) {
            htmlPaper = (HtmlPaper) obj;
            getHandler().post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Tools.log_i(HtmlDetailFragment.class, "run", "post");
                        setAdapter(new AdapterWeb(getData(), htmlPaper));
                        getRecyler().setAdapter(getAdapter());
                        singleDone = true;
                        onPullDownRefresh();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        Tools.log_i(HtmlDetailFragment.class, "run", "NoSuchMethodException");
                    }
                }
            });

        }
    }

    @Override
    public void onHandleSingleFailed(Result result) {
        super.onHandleSingleFailed(result);
    }

    @Override
    public void onFailedClick() {
        if (singleDone) {
            super.onFailedClick();
        } else {
            try {
                dataHtml.getSingleData(dataHtml.createSingleBundle(Constants.URL_SEARCH_DETAIL + "?sourceId=" + whichId + "&type=" + type, whichId), cls);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onHandlePublishSuccess(Result result) {
        super.onHandlePublishSuccess(result);
        detail_html_input.done();
    }

    @Override
    public void onHandlePublishFailed(Result result) {
        detail_html_input.done();
    }

    @Override
    public void onHandleUpdate(Object obj) {
        //没有数据时 设置加载成功 成功后添加数据
    }

    @Override
    public void onHandleHistory(Object obj) {
    }

    @Override
    public void onHandleListUpdate(Result result, Object obj) {
        if (Tools.isEmptyWithIgnore(getData(), AdapterWeb.NEED_SPACE)) {
            //            getLoading().loadSuccess();
        }
        getLoading().loadSuccess();
        cancelRefresh();
        List<HtmlComment> tempList = (List<HtmlComment>) obj;
        if (!Tools.isEmpty(tempList)) {
            getData().addAll(AdapterWeb.NEED_SPACE, tempList);
            getAdapter().notifyItemRangeInserted(AdapterWeb.NEED_SPACE, tempList.size());
        }
        updateHtml(result.getCount());
    }

    @Override
    public void onHandleListHistory(Result result, Object obj) {
        if (Tools.isEmptyWithIgnore(getData(), AdapterWeb.NEED_SPACE)) {
            //            getLoading().loadSuccess();
        }
        getLoading().loadSuccess();
        cancelRefresh();
        List<HtmlComment> tempList = (List<HtmlComment>) obj;
        if (!Tools.isEmpty(tempList)) {
            int size = getData().size();
            getData().addAll(tempList);
            getAdapter().notifyItemRangeInserted(size - 1, tempList.size());
        }
        updateHtml(result.getCount());
    }

    private void updateHtml(int count) {
        if (count > 0) {
            htmlPaper.setCommentAmount(count);
            if (interactionListener != null) {
                Tools.log_i(HtmlDetailFragment.class,"updateHtml"," interactionListener.updateCommentCount(htmlPaper);");
                interactionListener.updateCommentCount(htmlPaper,commentType);
            }
            try {
                dataHtml.getDbBaseImp().updateSingle(htmlPaper);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onHandleNo() {
        if (Tools.isEmptyWithIgnore(getData(), AdapterWeb.NEED_SPACE))
            getLoading().loadSuccess();
        getLoading().loadSuccess();
        cancelRefresh();
    }

    @Override
    public void onHandleFailed() {
        if (Tools.isEmptyWithIgnore(getData(), AdapterWeb.NEED_SPACE)) {
            //            getLoading().loadFailed();
        } else {
            Tools.showToast(getActivity(), R.string.load_request_failed);
        }
        getLoading().loadSuccess();
        cancelRefresh();
    }

    @Override
    public void onInputView(String content) {
        HtmlComment htmlComment = new HtmlComment();
        htmlComment.setCommentType(commentType);
        htmlComment.setWhichId(whichId);
        htmlComment.setContent(content);
        getDataOperate().publishData(htmlComment);
    }

    public interface OnFragmentInteractionListener {
        void updateCommentCount(HtmlPaper htmlPaper,int commentType);
    }
}
