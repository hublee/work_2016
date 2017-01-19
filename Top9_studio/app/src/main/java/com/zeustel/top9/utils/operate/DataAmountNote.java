package com.zeustel.top9.utils.operate;

import android.os.Handler;

import com.zeustel.top9.bean.AmountNote;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/10 14:29
 */
public class DataAmountNote extends DataBaseOperate<AmountNote> {
    public DataAmountNote(Handler handler, DBBaseOperate<AmountNote> dbBaseImp) {
        super(handler, dbBaseImp);
    }
}
