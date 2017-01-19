package com.zeustel.top9.fragments;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderStore;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.base.IAnimFragment;
import com.zeustel.top9.base.WrapOneAndRefreshFragment;
import com.zeustel.top9.bean.ExchangeGoods;
import com.zeustel.top9.result.ExchangeGoodsListResult;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBGoodsImp;
import com.zeustel.top9.utils.operate.DataGoods;
import com.zeustel.top9.widgets.CustomRecyclerView;
import com.zeustel.top9.widgets.SwipeRefreshLayoutDirection;

import java.util.List;

/**
 * 商城页面
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/26
 */
public class StoreFragment extends WrapOneAndRefreshFragment<ExchangeGoods> implements CustomRecyclerView.OnItemClickListener, IAnimFragment {

    public StoreFragment() {

    }

    @Override
    public String getClassName() {
        return StoreFragment.class.getSimpleName();
    }

    @Override
    public String getFloatTitle() {
        return getString(R.string.title_store);
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public void onPullDownRefresh() {
        //只有9条 一直都是最新数据 所以time为0
        try {
            getDataOperate().getListData(getDataOperate().createListBundle(Constants.URL_SHOP_LIST, 0, NetHelper.Flag.UPDATE), ExchangeGoodsListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHandleUpdate(Object obj) {
        //没有数据时 设置加载成功 成功后添加数据
        super.onHandleUpdate(obj);
        if (Tools.isEmpty(getData())) {
            getLoading().loadSuccess();
        }
        cancelRefresh();

        List<ExchangeGoods> tempList = (List<ExchangeGoods>) obj;

        if (!Tools.isEmpty(tempList)) {
            getData().clear();
            getData().addAll(tempList);
            getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onPullUpRefresh() {
        //只有9条
    }

    @Override
    protected void onAfterCreateView(View contentView) {
        setDbOperate(new DBGoodsImp(getActivity()));
        setDataOperate(new DataGoods(getHandler(), getDbOperate()));
        try {
            setAdapter(new OverallRecyclerAdapter<ExchangeGoods>(getData(), R.layout.list_item_store, HolderStore.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        getRefresh().setDirection(SwipeRefreshLayoutDirection.TOP);
        getRecyler().setOnItemClickListener(this);
    }

    private static final int REQUEST_CODE = 100;
    public static final int RESULT_CODE = 1001;
    public static final String EXTRA_NAME_GOOD = "good";

    @Override
    public boolean onItemClick(RecyclerView parent, RecyclerView.ViewHolder viewHolder, View itemView, int viewType, int position) {
        if (getRefresh() != null) {
            if (getRefresh().isRefreshing()) {
                Tools.showToast(getActivity(), R.string.load_ing);
                return false;
            }
        }
        /*Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
        intent.putExtra(StoreDetailActivity.EXTRA_NAME, getData().get(position));
        getActivity().startActivityForResult(intent, REQUEST_CODE);*/
        mStoreDetailFragment = StoreDetailFragment.newInstance(getData().get(position));
        mStoreDetailFragment.setOnDeatilUpdateListener(new StoreDetailFragment.OnDeatilUpdateListener() {
            @Override
            public void onDetailUpdate(ExchangeGoods exchangeGoods) {
                Tools.log_i(StoreFragment.class, "onDetailUpdate", "");
                if (exchangeGoods != null) {
                    int index = getData().indexOf(exchangeGoods);
                    if (index != -1) {
                        getData().set(index, exchangeGoods);
                        getAdapter().notifyItemChanged(index);
                    }
                }
            }
        });
        getFragmentManager().beginTransaction().addToBackStack(null).add(R.id.container, mStoreDetailFragment).commit();
        return false;
    }

    private StoreDetailFragment mStoreDetailFragment = null;


    @Override
    public RecyclerView getRecyclerView() {
        return getRecyler();
    }
}
