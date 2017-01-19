package com.zeustel.top9;

import android.os.Bundle;

import com.zeustel.top9.base.WrapNotAndHandleActivity;
import com.zeustel.top9.bean.ExchangeGoods;
import com.zeustel.top9.fragments.StoreDetailFragment;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/10 20:15
 */
public class StoreDetailActivity extends WrapNotAndHandleActivity {
    public static final String EXTRA_NAME = "ExchangeGoods";

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    StoreDetailFragment MStoreDetailFragment = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        ExchangeGoods exchangeGoods = null;
        if (getIntent() != null) {
            exchangeGoods = (ExchangeGoods) getIntent().getSerializableExtra(EXTRA_NAME);
        }
        if (exchangeGoods != null) {
            if (savedInstanceState == null) {
                MStoreDetailFragment = MStoreDetailFragment.newInstance(exchangeGoods);
                getSupportFragmentManager().beginTransaction().add(R.id.store_detail, MStoreDetailFragment).commit();
            }
        } else {
            finish();
        }
    }
}
