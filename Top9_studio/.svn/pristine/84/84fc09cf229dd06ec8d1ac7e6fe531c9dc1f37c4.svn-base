package com.zeustel.top9.utils.operate;

import android.os.Bundle;
import android.os.Handler;

import com.loopj.android.http.RequestParams;
import com.zeustel.top9.bean.Search;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.SimpleResponseHandler;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/18 14:16
 */
public class DataSearch extends DataBaseOperate<Search> {
    public DataSearch(Handler handler, DBBaseOperate<Search> dbBaseImp) {
        super(handler, dbBaseImp);
    }

    /* public void search(String queryStr) {
         RequestParams params = new RequestParams();
         params.put("queryStr",queryStr);
         NetHelper.post(Constants.URL_SEARCH_LIST,params);
     }*/
    public Bundle createSearchBundle(String url, String queryStr) {
        Bundle extras = new Bundle();
        extras.putString(EXTRAS_NAME_URL, url);
        extras.putString("queryStr", queryStr);
        return extras;
    }

    protected void executeGetNetData(SimpleResponseHandler simpleResponseHandler, Bundle extras) {
        final String url = extras.getString(EXTRAS_NAME_URL);
        RequestParams params = new RequestParams();
        params.put("queryStr", extras.getString("queryStr"));
        NetHelper.post(url, params, simpleResponseHandler);
    }
}
