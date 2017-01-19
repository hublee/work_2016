package com.zeustel.top9.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.AdapterChoice;
import com.zeustel.top9.base.WrapOneAndHandleFragment;
import com.zeustel.top9.bean.Note;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBBaseOperate;
import com.zeustel.top9.utils.operate.DataBaseOperate;
import com.zeustel.top9.widgets.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录base
 */
public abstract class ChoiceFragment<T extends Note> extends WrapOneAndHandleFragment implements SwipeRefreshLayout.OnRefreshListener {
    //数据集
    private List<T> data = new ArrayList();
    //列表组件
    private ListView choice_list;
    //选择适配器
    private AdapterChoice<T> adapter;
    //上拉下拉刷新组件
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //数据库操作
    private DBBaseOperate<T> dbOperate;
    //数据操作
    private DataBaseOperate<T> dataOperate;

    public ChoiceFragment() {

    }

    /**
     * 获取定制数据操作类
     *
     * @param handler
     * @param dbOperate
     * @return
     */
    protected abstract DataBaseOperate<T> getDataOperate(Handler handler, DBBaseOperate<T> dbOperate);

    /**
     * 获取定制数据库操作类
     */
    protected abstract DBBaseOperate<T> getDbOperate();
    /**
     * gson解析所需参数
     * @return
     */
    protected abstract Class<? extends Result<List<T>>> getListResultCls();

    /**
     * 获取列表路径
     * @return
     */
    protected abstract String getListUrl();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_choice, container, false);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        choice_list = (ListView) mSwipeRefreshLayout.findViewById(R.id.choice_list);
        adapter = new AdapterChoice(data);
        choice_list.setAdapter(adapter);
        return mSwipeRefreshLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbOperate = getDbOperate();
        dataOperate = getDataOperate(getHandler(), dbOperate);
        if (dataOperate == null) {
            throw new NullPointerException("dataOperate must be not null !");
        }
        onPullDownRefresh();
    }

    @Override
    public void onDestroyView() {
        choice_list.setAdapter(null);
        data.clear();
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        data = null;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onPullDownRefresh() {
        onPullRefresh(true);
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onPullUpRefresh() {
        onPullRefresh(false);
    }

    /**
     * 刷新
     * @param isDown 下拉?
     */
    protected void onPullRefresh(boolean isDown) {
        final T item;
        long time = 0;
        if (isDown) {
            item = Tools.getFirstData(data);
        } else {
            item = Tools.getLastData(data);
        }
        if (item != null) {
            time = item.getTime();
        }
        Bundle listBundle = dataOperate.createListBundle(getListUrl(), time, isDown ? NetHelper.Flag.UPDATE : NetHelper.Flag.HISTORY);
        try {
            dataOperate.postListData(listBundle, getListResultCls());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消刷新
     */
    protected void cancelRefresh() {
        if (mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onHandleListHistory(Result result, Object obj) {
        super.onHandleListHistory(result, obj);
        cancelRefresh();
        final List<T> tempList = (List<T>) obj;
        if (!Tools.isEmpty(tempList)) {
            data.addAll(tempList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHandleListUpdate(Result result, Object obj) {
        super.onHandleListUpdate(result, obj);
        cancelRefresh();
        final List<T> tempList = (List<T>) obj;
        if (!Tools.isEmpty(tempList)) {
            data.addAll(0, tempList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHandleNo() {
        super.onHandleNo();
        cancelRefresh();
    }

    @Override
    public void onHandleFailed() {
        cancelRefresh();
        Tools.showToast(getActivity(), R.string.load_request_failed);
    }

    public AdapterChoice<T> getAdapter() {
        return adapter;
    }

    public void setAdapter(AdapterChoice<T> adapter) {
        this.adapter = adapter;
    }

    public ListView getChoice_list() {
        return choice_list;
    }

    public void setChoice_list(ListView choice_list) {
        this.choice_list = choice_list;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public DataBaseOperate<T> getDataOperate() {
        return dataOperate;
    }

    public void setDataOperate(DataBaseOperate<T> dataOperate) {
        this.dataOperate = dataOperate;
    }

    public void setDbOperate(DBBaseOperate<T> dbOperate) {
        this.dbOperate = dbOperate;
    }

    public SwipeRefreshLayout getmSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    public void setmSwipeRefreshLayout(SwipeRefreshLayout mSwipeRefreshLayout) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
    }
}
