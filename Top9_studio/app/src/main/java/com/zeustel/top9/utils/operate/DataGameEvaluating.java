package com.zeustel.top9.utils.operate;

import android.os.Handler;

import com.zeustel.top9.bean.GameEvaluating;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/20 10:28
 */
public class DataGameEvaluating extends DataHtml<GameEvaluating> {
    public DataGameEvaluating(Handler handler, DBBaseOperate<GameEvaluating> dbBaseImp, int type) {
        super(handler, dbBaseImp, type);
    }

    public DataGameEvaluating(Handler handler, DBBaseOperate<GameEvaluating> dbBaseImp) {
        super(handler, dbBaseImp);
    }
}
