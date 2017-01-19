package com.zeustel.top9.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderEvaluating;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.base.IAnimFragment;
import com.zeustel.top9.base.WrapOneAndRefreshFragment;
import com.zeustel.top9.bean.GameEvaluating;
import com.zeustel.top9.result.GameEvaluatingListResult;
import com.zeustel.top9.temp.TempEvaluatingActivity;
import com.zeustel.top9.temp.TempHtmlActivity;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBGameEvaluatingImp;
import com.zeustel.top9.utils.operate.DataGameEvaluating;
import com.zeustel.top9.widgets.CustomRecyclerView;

/**
 * 评测页面
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/26
 */
public class EvaluatingFragment extends WrapOneAndRefreshFragment<GameEvaluating> implements CustomRecyclerView.OnItemClickListener, IAnimFragment {
    //根据类型 显示不同页面
    public static final String EVALUATING_TYPE = "type";
    //悬浮标题背景
    public static final String BACKGROUND = "background";
    //当前类型
    private int type;
    //当前颜色
    private int background;

    public EvaluatingFragment() {
    }

    public static EvaluatingFragment newInstance(int type, int background) {
        EvaluatingFragment fragment = new EvaluatingFragment();
        Bundle args = new Bundle();
        args.putInt(EVALUATING_TYPE, type);
        args.putInt(BACKGROUND, background);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(EVALUATING_TYPE);
            background = getArguments().getInt(BACKGROUND);
        }
    }

    @Override
    public String getFloatTitle() {
        return GameEvaluating.EvaluatingType.getName(getResources(), type);
    }

    @Override
    public int getBackgroundColor() {
        return getResources().getColor(background);
    }

    @Override
    public String getClassName() {
        return EvaluatingFragment.class.getSimpleName() + "-" + GameEvaluating.EvaluatingType.getNameEN(getResources(), type);
    }

    @Override
    public void onPullDownRefresh() {
       final GameEvaluating gameEvaluating = Tools.getFirstData(getData());
        long time = 0;
        if (gameEvaluating != null) {
            time = gameEvaluating.getTime();
        }
        try {
            getDataOperate().getListData(getDataOperate().createListBundle(Constants.URL_EVALUATING_LIST + "?flag=" + NetHelper.Flag.UPDATE + "&type=" + type + "&time=" + time, time, NetHelper.Flag.UPDATE), GameEvaluatingListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPullUpRefresh() {
       final GameEvaluating gameEvaluating = Tools.getLastData(getData());
        long time = 0;
        if (gameEvaluating != null) {
            time = gameEvaluating.getTime();
        }
        try {
            getDataOperate().getListData(getDataOperate().createListBundle(Constants.URL_EVALUATING_LIST + "?flag=" + NetHelper.Flag.HISTORY + "&type=" + type + "&time=" + time, time, NetHelper.Flag.HISTORY), GameEvaluatingListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onAfterCreateView(View contentView) {

        setDbOperate(new DBGameEvaluatingImp(getActivity()));
        setDataOperate(new DataGameEvaluating(getHandler(), getDbOperate(), type));
        try {
            setAdapter(new OverallRecyclerAdapter<GameEvaluating>(getData(), R.layout.list_item_evaluating, HolderEvaluating.class));//, evaluating_refresh
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        getRecyler().setOnItemClickListener(this);
    }

    @Override
    public boolean onItemClick(RecyclerView parent, RecyclerView.ViewHolder viewHolder, View itemView, int viewType, int position) {
        Tools.log_i(EvaluatingFragment.class, "onItemClick", "viewType : " + viewType + "position" +
                " : " + position);
        if (getRefresh() != null) {
            if (getRefresh().isRefreshing()) {
                Tools.showToast(getActivity(), R.string.load_ing);
                return false;
            }
        }
        if (Constants.SHOW_HOW) {
            Intent intent = new Intent(getContext(), TempEvaluatingActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getContext(), TempHtmlActivity.class);
            intent.putExtra(TempHtmlActivity.EXTRA_NAME,getData().get(position));
            startActivity(intent);
        }
        return true;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return getRecyler();
    }
}
