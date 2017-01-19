package com.zeustel.top9.utils.operate;

import android.os.Bundle;
import android.os.Handler;

import com.loopj.android.http.RequestParams;
import com.zeustel.top9.bean.FeedbackReply;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.SimpleResponseHandler;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/9 09:42
 */
public class DataFeedbackReply extends DataBaseOperate<FeedbackReply> {
    public DataFeedbackReply(Handler handler, DBBaseOperate<FeedbackReply> dbBaseImp) {
        super(handler, dbBaseImp);
    }

    @Override
    protected void executePostNetData(SimpleResponseHandler simpleResponseHandler, Bundle extras) {
        final String url = extras.getString(EXTRAS_NAME_URL);
        final long time = extras.getLong(EXTRAS_NAME_TIME);
        final boolean isHistory = extras.getBoolean(EXTRAS_NAME_IS_HISTORY);
        final int sourceId = extras.getInt("sourceId");
        RequestParams params = new RequestParams();
        params.put("sourceId", sourceId);
        params.put("time", time);
        params.put("flag", isHistory ? NetHelper.Flag.HISTORY : NetHelper.Flag.UPDATE);
        NetHelper.post(url, params, simpleResponseHandler);
    }

    @Override
    public void publishData(FeedbackReply data) {
        RequestParams params = new RequestParams();
        params.put("userId", Constants.USER.getId());
        params.put("Content", data.getReplyContent());
        params.put("feedbackId", data.getFeedbackId());
        NetHelper.post(Constants.URL_FEEDBACK_PUBLISH, params, getPublishResponseHandler());
    }
}
