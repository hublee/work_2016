package com.zeustel.top9.utils.operate;

import android.os.Handler;

import com.zeustel.top9.bean.SubUserInfo;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/12/4 17:05
 */
public class DataSubUser extends DataBaseOperate<SubUserInfo> {
    public DataSubUser(Handler handler, DBBaseOperate<SubUserInfo> dbBaseImp) {
        super(handler, dbBaseImp);
    }
}
