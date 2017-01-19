package com.zeustel.top9.utils.operate;

import android.content.Context;

import com.zeustel.top9.bean.FeedbackReply;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/10 09:56
 */
public class DBFeedbakReplyImp extends DBBaseOperate<FeedbackReply> {
    public DBFeedbakReplyImp(Context context) {
        super(context);
    }

    @Override
    public Class<FeedbackReply> getEntity() {
        return FeedbackReply.class;
    }
}
