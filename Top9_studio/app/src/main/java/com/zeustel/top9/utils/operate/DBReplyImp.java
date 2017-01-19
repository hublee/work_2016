package com.zeustel.top9.utils.operate;

import android.content.Context;

import com.zeustel.top9.bean.community.Reply;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/10 15:50
 */
public class DBReplyImp extends DBBaseOperate<Reply> {
    public DBReplyImp(Context context) {
        super(context);
    }

    @Override
    public Class<Reply> getEntity() {
        return Reply.class;
    }
}
