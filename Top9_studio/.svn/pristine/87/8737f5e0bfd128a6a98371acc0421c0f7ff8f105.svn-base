package com.zeustel.top9.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderCommunity;
import com.zeustel.top9.adapters.HolderEvaluating;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.base.WrapOneFragment;
import com.zeustel.top9.bean.HtmlPaper;
import com.zeustel.top9.bean.Search;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索列表
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/18 14:48
 */
public class SearchPagerFragment<T extends HtmlPaper> extends WrapOneFragment implements
        CustomRecyclerView.OnItemClickListener {
    protected static final String EXTRA_NAME = "list";
    protected static final String EXTRA_NAME_TYPE = "type";
    protected static final String EXTRA_NAME_LIGHT = "light";
    //列表组件
    private CustomRecyclerView search_pager_recycler;
    //item布局
    private int layoutId;
    //内容集合
    private List<T> data;
    //搜索类型
    private Search.Type type;
    //高亮字符
    private String lightText;
    //holder.class
    private Class cls;

    public SearchPagerFragment() {

    }

    public static <T extends HtmlPaper> SearchPagerFragment newInstance(ArrayList<T> data, Search
            .Type type, String lightText) {
        Bundle args = new Bundle();
        SearchPagerFragment<T> fragment = new SearchPagerFragment<T>();
        args.putSerializable(EXTRA_NAME, data);
        args.putInt(EXTRA_NAME_TYPE, type.value());
        args.putString(EXTRA_NAME_LIGHT, lightText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (List<T>) getArguments().getSerializable(EXTRA_NAME);
            int typeFlag = getArguments().getInt(EXTRA_NAME_TYPE);
            type = Search.Type.getInstance(typeFlag);
            lightText = getArguments().getString(EXTRA_NAME_LIGHT);

        }
        if (!Search.Type.EVALUATING.equals(type)) {
            /*社区类型 ： info topic */
            layoutId = R.layout.list_item_community;
            Tools.log_i(SearchPagerFragment.class, "onCreateView", "list_item_community");
            cls = HolderCommunity.class;
        } else {
            /*评测*/
            Tools.log_i(SearchPagerFragment.class, "onCreateView", "list_item_evaluating");
            layoutId = R.layout.list_item_evaluating;
            cls = HolderEvaluating.class;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_search_pager, container, false);
        search_pager_recycler = (CustomRecyclerView) contentView.findViewById(R.id
                .search_pager_recycler);
        search_pager_recycler.setItemAnimator(new DefaultItemAnimator());
        search_pager_recycler.setHasFixedSize(true);
        //线性垂直显示 如listview
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        search_pager_recycler.setLayoutManager(mLayoutManager);
        search_pager_recycler.setOnItemClickListener(this);
        if (!Tools.isEmpty(data)) {
            try {
                OverallRecyclerAdapter<T> adapter = new OverallRecyclerAdapter(data, layoutId, cls);
                adapter.setLightText(lightText);
                search_pager_recycler.setAdapter(adapter);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return contentView;
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
    public boolean onItemClick(RecyclerView parent, RecyclerView.ViewHolder viewHolder, View
            itemView, int viewType, int position) {
        if (parent.getAdapter() != null && !Tools.isEmpty(data)) {
            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.selfDetail, HtmlDetailFragment.newInstance(data.get(position), type.value())).commit();
        }
        return true;
    }
}
