package com.zeustel.top9.utils.operate;

import android.os.Bundle;
import android.os.Handler;

import com.zeustel.top9.bean.HtmlPaper;

import java.util.List;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/18 17:27
 */
public class DataHtml<T extends HtmlPaper> extends DataBaseOperate<T> {
    private int type = -1;

    public DataHtml(Handler handler, DBBaseOperate<T> dbBaseImp) {
        super(handler, dbBaseImp);
    }

    public DataHtml(Handler handler, DBBaseOperate<T> dbBaseImp, int type) {
        super(handler, dbBaseImp);
        this.type = type;
    }

    @Override
    protected List<T> executeNativeListData(DBBaseOperate<T> dbBaseImp, Bundle extras) throws Exception {
        if (type != -1) {
            final long time = extras.getLong(EXTRAS_NAME_TIME);
            final boolean isHistory = extras.getBoolean(EXTRAS_NAME_IS_HISTORY);
            if (dbBaseImp instanceof DBHtmlImp) {
                return ((DBHtmlImp) dbBaseImp).queueList(type, time, isHistory);
            }
        }
        return super.executeNativeListData(dbBaseImp, extras);
    }
}
