package com.zeustel.top9.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zeustel.top9.GameDetailActivity;
import com.zeustel.top9.R;
import com.zeustel.top9.adapters.Grid9WheelAdapter;
import com.zeustel.top9.adapters.HolderGame;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.base.IAnimFragment;
import com.zeustel.top9.base.WrapOneAndRefreshFragment;
import com.zeustel.top9.bean.Game;
import com.zeustel.top9.bean.GameEvaluating;
import com.zeustel.top9.bean.Search;
import com.zeustel.top9.result.GameListResult;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBGameImp;
import com.zeustel.top9.utils.operate.DataGame;
import com.zeustel.top9.widgets.SwipeRefreshLayoutDirection;

import java.util.List;
import java.util.Random;

import kankan.wheel.widget.WheelView;

/**
 * Top9页面 9个
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/26
 */
@Deprecated
public class Grid9Fragment extends WrapOneAndRefreshFragment<Game> implements
        OverallRecyclerAdapter.OnitemClickListener,IAnimFragment {
    private WheelView grid9_wheel = null;
    private Intent intent;
    private OverallRecyclerAdapter<Game> adapter;
    private ImageView test_top9;
    private LinearLayout contentLayout;

    public Grid9Fragment() {

    }

    @Override
    public View onBeforeCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_grid9, container, false);
        grid9_wheel = (WheelView) contentView.findViewById(R.id.grid9_wheel);
        test_top9 = (ImageView) contentView.findViewById(R.id.test_top9);
        contentLayout = (LinearLayout) contentView.findViewById(R.id.contentLayout);
        grid9_wheel.setVisibleItems(3/*count*/);
        grid9_wheel.setViewAdapter(new Grid9WheelAdapter(getActivity()));
        grid9_wheel.setWheelBackground(R.drawable.wheel_bg_holo);
        grid9_wheel.setWheelForeground(R.drawable.wheel_val_holo);
        grid9_wheel.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        if (Constants.SHOW_HOW) {
            test_top9.setVisibility(View.VISIBLE);
            contentLayout.setVisibility(View.GONE);
            test_top9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int size = getData().size();
                    if (size >= 1) {
                        intent.putExtra(GameDetailActivity.EXTRA_NAME, getAdapter().getItem(new Random().nextInt(size - 1)));
                        startActivity(intent);
                    } else {
                        Tools.showToast(getActivity(), "相关数据");
                    }

                }
            });
        } else {
            test_top9.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
        }
        return contentView;
    }

    @Override
    protected void onAfterCreateView(View contentView) {
        setDbOperate(new DBGameImp(getActivity()));
        setDataOperate(new DataGame(getHandler(), getDbOperate()));
        try {
            adapter = new OverallRecyclerAdapter<>(getData(), R.layout.list_item_grid9_game,
                    HolderGame.class);
            setAdapter(adapter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        adapter.setOnitemClickListener(this);
        getRefresh().setDirection(SwipeRefreshLayoutDirection.TOP);
        intent = new Intent(getActivity(), GameDetailActivity.class);
        getLoading().setVisibility(View.GONE);
    }

    @Override
    public String getClassName() {
        return Grid9Fragment.class.getSimpleName();
    }

    @Override
    public String getFloatTitle() {
        return getString(R.string.title_top9);
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public void onPullDownRefresh() {
        try {
            getDataOperate().getListData(getDataOperate().createListBundle(Constants
                    .URL_TOP9_LIST + "?flag=" + NetHelper.Flag.UPDATE + "&time=0", 0/*time*/,
                    NetHelper.Flag.UPDATE), GameListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHandleUpdate(Object obj) {
        if (Tools.isEmpty(getData())) {

        }
        getLoading().loadSuccess();
        cancelRefresh();

        List<Game> tempList = (List<Game>) obj;

        if (!Tools.isEmpty(tempList)) {
            getData().clear();
            getData().addAll(tempList);
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onPullUpRefresh() {

    }

    @Override
    public void onitemClick(RecyclerView.Adapter adapter, int position, String tag) {
        if ("all".equals(tag)) {
            if (getRefresh() != null) {
                if (getRefresh().isRefreshing()) {
                    Tools.showToast(getActivity(), R.string.load_ing);
                    return;
                }
                if (intent != null) {
                    intent.putExtra(GameDetailActivity.EXTRA_NAME, getAdapter().getItem(position));
                    startActivity(intent);
                }
            }
        } else if ("des".equals(tag)) {
            int evluatingId = getAdapter().getItem(position).getEvluatingId();
            GameEvaluating gameEvaluating = new GameEvaluating();
            Tools.log_i(Grid9Fragment.class, "onitemClick", "evluatingId : " + evluatingId);
            gameEvaluating.setId(evluatingId);
            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container,
                    HtmlDetailFragment.newInstance(gameEvaluating, Search.Type.EVALUATING.value()
                    )).commit();
        } else if ("download".equals(tag)) {
            Tools.log_i(Grid9Fragment.class, "onitemClick", getAdapter().getItem(position)
                    .getName() + " download");
        }
    }

    @Override
    public RecyclerView getRecyclerView() {
        return getRecyler();
    }
}
