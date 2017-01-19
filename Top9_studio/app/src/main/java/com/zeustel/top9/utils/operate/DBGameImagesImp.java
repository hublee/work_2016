package com.zeustel.top9.utils.operate;

import android.content.Context;

import com.zeustel.top9.bean.GameImages;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/6 13:50
 */
public class DBGameImagesImp extends DBBaseOperate<GameImages> {
    public DBGameImagesImp(Context context) {
        super(context);
    }

    @Override
    public Class<GameImages> getEntity() {
        return null;
    }
}
