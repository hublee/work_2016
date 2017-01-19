package com.zeustel.top9.utils.operate;

import android.os.Handler;

import com.loopj.android.http.RequestParams;
import com.zeustel.top9.bean.ExchangeGoods;
import com.zeustel.top9.bean.OrderGoods;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;

/**
 * 兑换商品操作类
 * @author NiuLei
 * @date 2015/7/30 21:25
 */
public class DataGoods extends DataBaseOperate<ExchangeGoods> {
    public DataGoods(Handler handler, DBBaseOperate<ExchangeGoods> dbBaseImp) {
        super(handler, dbBaseImp);
    }

    @Override
    public void publishData(ExchangeGoods data) {
        if (data instanceof OrderGoods) {
            OrderGoods orderGoods = (OrderGoods) data;
            RequestParams params = new RequestParams();
            params.put("userId", Constants.USER.getId());
            params.put("shopAwardId", orderGoods.getId());
            params.put("name", orderGoods.getClientName());
            params.put("address", orderGoods.getAddress());
            params.put("tel", orderGoods.getTel());
            NetHelper.post(Constants.URL_SHOP_PUBLISH, params, getPublishResponseHandler());
        }
    }
}
