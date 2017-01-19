package com.zeustel.top9.utils.operate;

import android.content.Context;

import com.zeustel.top9.bean.Game;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/12 14:28
 */
public class DBGameImp extends DBBaseOperate<Game> {
    public DBGameImp(Context context) {
        super(context);
    }

    @Override
    public Class<Game> getEntity() {
        return Game.class;
    }

}
