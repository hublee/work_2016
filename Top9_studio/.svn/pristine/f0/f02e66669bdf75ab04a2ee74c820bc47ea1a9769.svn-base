package com.zeustel.top9.utils.operate;

import android.content.Context;

import com.google.gson.Gson;
import com.zeustel.top9.bean.html.HtmlTemp;
import com.zeustel.top9.utils.Tools;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/27 15:45
 */
public class DBHtmlTempImp extends DBBaseOperate<HtmlTemp> {
    private Gson gson = null;
    private HtmlTemp byId;
    private String json;
    private HtmlTemp htmlTemp;

    public DBHtmlTempImp(Context context) {
        super(context);
    }

    @Override
    public Class<HtmlTemp> getEntity() {
        return HtmlTemp.class;
    }

    @Override
    public boolean insertSingle(HtmlTemp data) throws Exception {
        return false;
    }

    public boolean insertSingle(HtmlTemp data, int id) throws Exception {
        getDbUtils().save(data);
        return true;
    }

    @Override
    public HtmlTemp queueSingle(int id) throws Exception {
        if (gson == null) {
            gson = new Gson();
        }
        byId = getDbUtils().findById(getEntity(), id);
        if (byId != null) {
            json = byId.getJson();
            if (json != null) {
                Tools.log_i(DBHtmlTempImp.class,"queueSingle","json : "+json);
                htmlTemp = gson.fromJson(json, HtmlTemp.class);
                return htmlTemp;
            }
        }
        return super.queueSingle(id);
    }
}
