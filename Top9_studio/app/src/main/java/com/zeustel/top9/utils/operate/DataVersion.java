package com.zeustel.top9.utils.operate;

import android.os.Bundle;
import android.os.Handler;

import com.zeustel.top9.bean.Version;
import com.zeustel.top9.utils.SimpleResponseHandler;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/24 11:09
 */
public class DataVersion extends DataBaseOperate<Version> {
    public DataVersion(Handler handler) {
        super(handler, null);
    }

    @Override
    protected void executeGetNetData(SimpleResponseHandler simpleResponseHandler, Bundle extras) {
        super.executeGetNetData(simpleResponseHandler, extras);
    }
}
