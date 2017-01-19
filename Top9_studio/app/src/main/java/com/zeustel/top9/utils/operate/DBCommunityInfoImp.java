package com.zeustel.top9.utils.operate;

import android.content.Context;

import com.zeustel.top9.bean.CommunityInfo;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/20 10:30
 */
public class DBCommunityInfoImp extends DBHtmlImp<CommunityInfo> {
    public DBCommunityInfoImp(Context context) {
        super(context);
    }

    @Override
    public String getTypeStr() {
        return "infoType";
    }

    @Override
    public Class<CommunityInfo> getEntity() {
        return CommunityInfo.class;
    }

}
