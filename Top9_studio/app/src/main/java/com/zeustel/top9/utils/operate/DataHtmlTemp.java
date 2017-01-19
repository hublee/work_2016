package com.zeustel.top9.utils.operate;

import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.zeustel.top9.bean.html.HtmlTemp;
import com.zeustel.top9.result.HtmlTempResult;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.SimpleResponseHandler;
import com.zeustel.top9.utils.Tools;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/27 15:44
 */
public class DataHtmlTemp extends DataBaseOperate<HtmlTemp> {

    private DBHtmlTempImp dbHtmlTempImp;
    private Gson gson;

    public DataHtmlTemp(Handler handler, DBBaseOperate<HtmlTemp> dbBaseImp) {
        super(handler, dbBaseImp);
        this.dbHtmlTempImp = (DBHtmlTempImp) dbBaseImp;
    }
    @Override
    protected void executeGetNetData(final SimpleResponseHandler simpleResponseHandler, final Bundle extras) {
        final String url = extras.getString(EXTRAS_NAME_URL);
        NetHelper.get(null/*Context*/, url, null/* RequestParams*/, new SimpleResponseHandler() {
            @Override
            public void onCallBack(int code, String json) {
                if (simpleResponseHandler != null) {
                    simpleResponseHandler.onCallBack(code, json);
                }
                Tools.log_i(DataHtmlTemp.class, "onCallBack", "json : " + json);
                if (json != null) {
                    if (gson == null) {
                        gson = new Gson();
                    }
                    HtmlTempResult htmlTempResult = gson.fromJson(json, HtmlTempResult.class);
                    if (htmlTempResult != null && htmlTempResult.getSuccess() == Result.SUCCESS) {
                        HtmlTemp data = htmlTempResult.getData();
                        final int id = extras.getInt(EXTRAS_NAME_ID);
                        data.setId(id);
                        if (data != null) {
                            String htmlJson = gson.toJson(data);
                            data.setJson(htmlJson);
                            if (dbHtmlTempImp != null) {
                                try {
                                    dbHtmlTempImp.insertSingle(data,id);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}
