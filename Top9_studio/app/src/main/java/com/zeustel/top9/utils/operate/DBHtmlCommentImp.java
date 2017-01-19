package com.zeustel.top9.utils.operate;

import android.content.Context;

import com.zeustel.top9.bean.HtmlComment;

/**
 * 评论数据库操作
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/31 15:56
 */
public class DBHtmlCommentImp extends DBBaseOperate<HtmlComment> {
    public DBHtmlCommentImp(Context context) {
        super(context);
    }

    @Override
    public Class<HtmlComment> getEntity() {
        return null;
    }

}
