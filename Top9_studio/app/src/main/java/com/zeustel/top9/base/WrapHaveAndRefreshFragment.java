package com.zeustel.top9.base;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBBaseOperate;
import com.zeustel.top9.utils.operate.DataBaseOperate;
import com.zeustel.top9.widgets.CustomRecyclerView;
import com.zeustel.top9.widgets.LoadingView;
import com.zeustel.top9.widgets.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/31 17:33
 */
public abstract class WrapHaveAndRefreshFragment<T> extends WrapHaveAndHandleFragment implements LoadingView.OnFailedClickListener, SwipeRefreshLayout.OnRefreshListener {
    //刷新组件
    private SwipeRefreshLayout refresh;
    //列表组件
    //    protected RecyclerView recyler;
    private CustomRecyclerView recyler;
    //加载组件
    private LoadingView loading;
    // RecyclerView适配器
    private OverallRecyclerAdapter<T> adapter;
    //数据操作
    private DataBaseOperate dataOperate;
    //数据库操作
    private DBBaseOperate dbOperate;
    private List<T> data = new ArrayList();
    private boolean needRefresh = true;

    @Override
    public void onDestroyView() {
        if (recyler != null && adapter != null) {
            recyler.setAdapter(null);
        }
        super.onDestroyView();
        if (data != null) {
            data.clear();
        }

    }

    @Override
    public void onDestroy() {
        if (data != null) {
            data = null;
        }
        super.onDestroy();
    }


    /**
     * 用于注册去除刷新固定布局之外的组件
     */
    public View onBeforeCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_refresh_base, container, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = onBeforeCreateView(inflater, container, savedInstanceState);
        View refreshView = contentView.findViewById(R.id.include_refresh_layout);
        if (refreshView != null) {
            initRefresh(refreshView);
        } else {
            initRefresh(contentView);
        }
        return contentView;
    }


    private void initRefresh(View contentView) {
        refresh = (SwipeRefreshLayout) contentView.findViewById(R.id.refresh);
        recyler = (CustomRecyclerView) contentView.findViewById(R.id.recyler);
        loading = (LoadingView) contentView.findViewById(R.id.loading);
        //添加加载失败监听
        loading.addOnFailedClickListener(this);
        //添加OnItemClickListener事件
        //        recyler.setOnItemClickListener(this);
        //设置默认动画
        recyler.setItemAnimator(new DefaultItemAnimator());
        recyler.setHasFixedSize(true);
        //线性垂直显示 如listview
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyler.setLayoutManager(mLayoutManager);
        data.clear();
        //第一次进入页面获取数据
        onAfterCreateView(contentView);
        //设置RecyclerView适配器 重写适配
        adapter = getAdapter();
        if (adapter != null) {
            recyler.setAdapter(adapter);
        }
        if (needRefresh) {
            //添加刷新监听
            refresh.setOnRefreshListener(this);
        }
        //获取数据库操作对象
        dbOperate = getDbOperate();
        //获取数据操作对象
        dataOperate = getDataOperate();
        onFirstRefresh();
    }

    protected void onFirstRefresh() {
        onPullDownRefresh();
    }
/*
    *//**
     * 创建数据库操作对象
     *//*
    protected abstract DBBaseOperate onCreateDBBaseOperate();

    *//**
     * 创建数据操作对象
     *
     * @return
     *//*
    protected abstract DataBaseOperate onCreateDataBaseOperate();

    *//**
     * 创建RecyclerView适配器
     *//*
    protected abstract RecyclerView.Adapter OnCreateRecyAdapter();

    */

    /**
     * onCreateView后调用
     */
    protected abstract void onAfterCreateView(View contentView);

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public OverallRecyclerAdapter<T> getAdapter() {
        return adapter;
    }

    public void setAdapter(OverallRecyclerAdapter<T> adapter) {
        this.adapter = adapter;
    }

    public DataBaseOperate getDataOperate() {
        return dataOperate;
    }

    public void setDataOperate(DataBaseOperate dataOperate) {
        this.dataOperate = dataOperate;
    }

    public DBBaseOperate getDbOperate() {
        return dbOperate;
    }

    public void setDbOperate(DBBaseOperate dbOperate) {
        this.dbOperate = dbOperate;
    }

    public List<T> getData() {
        return data;
    }

    public LoadingView getLoading() {
        return loading;
    }

    public CustomRecyclerView getRecyler() {
        return recyler;
    }

    public SwipeRefreshLayout getRefresh() {
        return refresh;
    }

    /**
     * 取消刷新
     */
    protected void cancelRefresh() {
        if (refresh.isRefreshing())
            refresh.setRefreshing(false);
    }

    @Override
    public void onHandlePublishSuccess() {
        Tools.showToast(getContext(), R.string.publish_success);
        onPullDownRefresh();
    }

    @Override
    public void onHandlePublishFailed() {
        Tools.showToast(getContext(), R.string.load_request_failed);
    }

    @Override
    public void onHandleUpdate(Object obj) {
        //没有数据时 设置加载成功 成功后添加数据
        loading.loadSuccess();
        cancelRefresh();
        List<T> tempList = (List<T>) obj;
        if (!Tools.isEmpty(tempList)) {
            data.addAll(0, tempList);
            adapter.notifyItemRangeInserted(0, tempList.size());
            recyler.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onHandleNo() {
        /*if (Tools.isEmpty(data))
            loading.loadSuccess();*/
        loading.loadSuccess();
        cancelRefresh();

    }

    @Override
    public void onHandleHistory(Object obj) {
        loading.loadSuccess();
        cancelRefresh();
        List<T> tempList = (List<T>) obj;
        if (!Tools.isEmpty(tempList)) {
            data.addAll(tempList);
            adapter.notifyItemRangeInserted(data.size() - 1, tempList.size());
        }
    }

    @Override
    public void onHandleFailed() {
        cancelRefresh();
        if (Tools.isEmpty(data)) {
            loading.loadFailed();
        } else {
            Tools.showToast(getActivity(), R.string.load_request_failed);
        }

    }

    @Override
    public void onHandleListUpdate(Result result, Object obj) {

    }

    @Override
    public void onHandleListFailed(Result result) {
    }

    @Override
    public void onHandleListHistory(Result result, Object obj) {
    }

    @Override
    public void onHandleListNo(Result result) {
        loading.loadSuccess();
        cancelRefresh();
    }

    @Override
    public void onFailedClick() {
        onPullDownRefresh();
    }
}
