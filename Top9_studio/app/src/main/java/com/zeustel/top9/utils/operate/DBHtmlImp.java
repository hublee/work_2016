package com.zeustel.top9.utils.operate;

import android.content.Context;

import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.zeustel.top9.bean.HtmlPaper;
import com.zeustel.top9.utils.Tools;

import java.util.List;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/18 17:28
 */
public abstract class DBHtmlImp<T extends HtmlPaper> extends DBBaseOperate<T> {
    public DBHtmlImp(Context context) {
        super(context);
    }

    public List<T> queueList(int type, long time, boolean isHistory) throws Exception {
        return getDbUtils().findAll(Selector.from(getEntity()).where(getTypeStr(), "=", type).and("time", isHistory ? "<" : ">", time).orderBy("time", true).limit(LIMIT_PAGE_SIZE));
    }

    public abstract String getTypeStr();

    @Override
    public boolean updateSingle(T data) throws Exception {
        Tools.log_i(DBHtmlImp.class, "updateSingle", "getCommentAmount : " + data.getCommentAmount());
        getDbUtils().update(data, WhereBuilder.b("id", "=", data.getId()), "commentAmount");
        return true;
    }
}
