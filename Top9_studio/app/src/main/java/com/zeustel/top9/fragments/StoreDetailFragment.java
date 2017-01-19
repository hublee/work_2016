package com.zeustel.top9.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zeustel.top9.ExhibitionActivity;
import com.zeustel.top9.LoginActivity;
import com.zeustel.top9.R;
import com.zeustel.top9.base.WrapNotAndHandleFragment;
import com.zeustel.top9.bean.ExchangeGoods;
import com.zeustel.top9.bean.OrderGoods;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.LoadProgress;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataBaseOperate;
import com.zeustel.top9.utils.operate.DataGoods;
import com.zeustel.top9.widgets.AnimViewFlipper;
import com.zeustel.top9.widgets.ExhibitionView;

/**
 * 商品详细
 */
public class StoreDetailFragment extends WrapNotAndHandleFragment implements View.OnClickListener, AnimViewFlipper.OnItemClickCallBack, ConsigneeDialog.OnConsigneeListener {
    private static final String EXTRA_NAME = "Goods";
    private ExchangeGoods exchangeGoods;
    //货币不足提示
    private LackMoneyDialog lackMoneyDialog = null;
    //收货人信息收集
    private ConsigneeDialog consigneeDialog = null;
    //商品名
    private TextView goods_name;
    //价格
    private TextView goods_price;
    //剩余
    private TextView goods_residue;
    //简介
    private TextView goods_describe;
    //兑换按钮
    private Button goods_exchange;
    //图片展览
    private ExhibitionView goods_exhibtion;
    private String[] array;
    private DataBaseOperate<ExchangeGoods> dataGoods;
    //    private DBGoodsImp mDBGoodsImp = null;
    private LoadProgress loadProgress;
    public int residue;

    public StoreDetailFragment() {
    }

    public static StoreDetailFragment newInstance(ExchangeGoods exchangeGoods) {
        StoreDetailFragment fragment = new StoreDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, exchangeGoods);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exchangeGoods = (ExchangeGoods) getArguments().getSerializable(EXTRA_NAME);
        }
        if (exchangeGoods == null) {
            getFragmentManager().popBackStack();
        }
        //        mDBGoodsImp = new DBGoodsImp(getContext());
        Tools.log_i(StoreDetailFragment.class, "onCreate", "exchangeGoods : " + exchangeGoods.toString());
        dataGoods = new DataGoods(getHandler(), null);
        loadProgress = new LoadProgress(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_store_detail, container, false);
        goods_name = (TextView) contentView.findViewById(R.id.goods_name);
        goods_price = (TextView) contentView.findViewById(R.id.goods_price);
        goods_residue = (TextView) contentView.findViewById(R.id.goods_residue);
        goods_describe = (TextView) contentView.findViewById(R.id.goods_describe);
        goods_exchange = (Button) contentView.findViewById(R.id.goods_exchange);
        goods_exhibtion = (ExhibitionView) contentView.findViewById(R.id.goods_exhibtion);
        if (exchangeGoods != null) {
            goods_name.setText(exchangeGoods.getName());
            goods_price.setText(exchangeGoods.getPrice() + " " + getString(R.string.store_item_price));
            residue = exchangeGoods.getResidue();
            goods_residue.setText(residue + " " + getString(R.string.store_item_residue));
            goods_describe.setText(exchangeGoods.getDescribe());
            String exhibition = exchangeGoods.getExhibition();
            array = Tools.convertPathArray(exhibition);
            if (!Tools.isEmpty(array)) {
                goods_exhibtion.fillContent(this.array);
            }
        }
        goods_exhibtion.setParentScrollAble(true);
        goods_exhibtion.addOnItemClickCallBack(this);
        goods_exchange.setOnClickListener(this);
        return contentView;
    }

    private void updateUI() {
        residue = exchangeGoods.getResidue();
        goods_residue.setText(residue + " " + getString(R.string.store_item_residue));
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
    public void onClick(View v) {
        if (goods_exchange.equals(v)) {
            if (!Constants.USER.isOnline()) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                return;
            }

            if (consigneeDialog == null) {
                consigneeDialog = new ConsigneeDialog();
                consigneeDialog.setListener(this);
            }
            consigneeDialog.show(getFragmentManager(), "ConsigneeDialog");
        }
    }

    @Override
    public void onItemClick(int index) {
        Intent intent = new Intent(getContext(), ExhibitionActivity.class);
        intent.putExtra(ExhibitionActivity.EXTRA_NAME_PATH, array);
        intent.putExtra(ExhibitionActivity.EXTRA_NAME_POSITION, index);
        startActivity(intent);
    }

    @Override
    public void onConsigneeListener(String name, String address, String tel) {
        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setClientName(name);
        orderGoods.setAddress(address);
        orderGoods.setTel(tel);
        orderGoods.setId(exchangeGoods.getId());
        dataGoods.publishData(orderGoods);
        loadProgress.show(R.string.wait_order);
    }

    @Override
    public void onHandlePublishFailed(Result result) {
        loadProgress.dismiss();
        if (getString(R.string.lack_of_balance).equals(result.getMsg())) {
            if (lackMoneyDialog == null) {
                lackMoneyDialog = new LackMoneyDialog();
            }
            lackMoneyDialog.show(getFragmentManager(), "LackMoneyDialog");
        }
    }

    public interface OnDeatilUpdateListener {
        void onDetailUpdate(ExchangeGoods exchangeGoods);
    }

    private OnDeatilUpdateListener onDeatilUpdateListener;

    public void setOnDeatilUpdateListener(OnDeatilUpdateListener onDeatilUpdateListener) {
        this.onDeatilUpdateListener = onDeatilUpdateListener;
    }

    @Override
    public void onHandlePublishSuccess(Result result) {
        loadProgress.dismiss();
        if (result != null) {
            //货币不足提醒
            residue = result.getCount();
            exchangeGoods.setResidue(residue);
            updateUI();
            if (onDeatilUpdateListener != null) {
                onDeatilUpdateListener.onDetailUpdate(exchangeGoods);
            }
            Tools.log_i(StoreDetailFragment.class, "onHandlePublishFailed", "residue : " + residue);
            Tools.showToast(getContext(), result.getMsg());
        }
    }
}
