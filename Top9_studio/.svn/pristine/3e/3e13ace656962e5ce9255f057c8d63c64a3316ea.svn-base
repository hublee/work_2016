package com.zeustel.top9.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderRankingGood;
import com.zeustel.top9.adapters.HolderRankingReply;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.adapters.OverallRecyclerHolder;
import com.zeustel.top9.base.WrapOneAndRefreshFragment;
import com.zeustel.top9.bean.SubUserInfo;
import com.zeustel.top9.result.SubUserListResult;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataSubUser;
import com.zeustel.top9.widgets.CustomRecyclerView;

import java.util.List;

/**
 * 点赞排行榜 回复排行榜
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/12/4 17:00
 */
public class RankingFragment extends WrapOneAndRefreshFragment<SubUserInfo> {
    private static final String EXTRA_NAME = "RANKING";
    //点赞排行
    public static final int RANKING_GOOD = 1;
    //回复排行
    public static final int RANKING_REPLY = 2;
    //对应holder
    private Class<? extends OverallRecyclerHolder> cls;
    private int ranking;
    //路径
    private String url;
    //请求参数
    private Bundle listBundle;
    //适配器
    private OverallRecyclerAdapter adapter;
    //列表组件
    private CustomRecyclerView recyler;
    //列表组件布局管理
    private LinearLayoutManager layoutManager;

    public static RankingFragment newInstance(int ranking) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_NAME, ranking);
        RankingFragment fragment = new RankingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ranking = getArguments().getInt(EXTRA_NAME);
        }
        if (ranking != RANKING_GOOD || ranking != RANKING_REPLY) {
            getFragmentManager().popBackStack();
        }
        if (RANKING_GOOD == ranking) {
            url = Constants.URL_RANKING_GOOD;
            cls = HolderRankingGood.class;
        } else if (RANKING_REPLY == ranking) {
            url = Constants.URL_RANKING_REPLY;
            cls = HolderRankingReply.class;
        }
    }

    @Override
    protected void onAfterCreateView(View contentView) {
        setDataOperate(new DataSubUser(getHandler(), null));
        listBundle = getDataOperate().createListBundle(url, 0, NetHelper.Flag.UPDATE);
        try {
            adapter = new OverallRecyclerAdapter<SubUserInfo>(getData(), R.layout.list_item_ranking, cls);
            setAdapter(adapter);//,
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        recyler = getRecyler();
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
        try {
            getDataOperate().getListData(listBundle, SubUserListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPullUpRefresh() {

    }

    @Override
    public void onHandleUpdate(Object obj) {
        getLoading().loadSuccess();
        cancelRefresh();
        if (obj != null) {
            List<SubUserInfo> tempList = (List<SubUserInfo>) obj;

            if (!Tools.isEmpty(tempList)) {
                getData().clear();
                getData().addAll(tempList);
                getAdapter().notifyDataSetChanged();
            }
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    redFirst();
                }
            }, 1000);
        }
    }

    /**
     * 标红点赞排行榜第一项
     */
    private void redFirst() {
        if (RANKING_GOOD == ranking) {
            layoutManager = (LinearLayoutManager) getRecyler().getLayoutManager();
            RecyclerView.ViewHolder viewHolder = recyler.getChildViewHolder(layoutManager.findViewByPosition(0));
            if (viewHolder instanceof HolderRankingGood) {
                ((HolderRankingGood) viewHolder).select(0);
            }
        }
    }
}
