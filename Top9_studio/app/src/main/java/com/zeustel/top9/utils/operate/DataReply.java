package com.zeustel.top9.utils.operate;

import android.os.Bundle;
import android.os.Handler;

import com.loopj.android.http.RequestParams;
import com.zeustel.top9.bean.community.Content;
import com.zeustel.top9.bean.community.Reply;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.SimpleResponseHandler;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/10 15:49
 */
public class DataReply extends DataBaseOperate<Reply> {
    public DataReply(Handler handler, DBBaseOperate<Reply> dbBaseImp) {
        super(handler, dbBaseImp);
    }


    //语音文件
    @Override
    public void publishData(Reply data) {
        RequestParams params = new RequestParams();
        params.put("userId", Constants.USER.getId());
        params.put("sourceId", data.getTagId());
        Content content = data.getContent();
        params.put("contentType", content == null ? Content.ContentType.TEXT.toInt() : data.getContent().getContentType().toInt());
        params.put("content", content.getMsg());
        data.getContent().getAtUser();
        if (content != null && content.getAtUser() != null) {
            params.put("atUser", content.getAtUser().getId());
        }
        NetHelper.post(Constants.URL_COMMENT_REPLY, params, getPublishResponseHandler());
    }

    public Bundle createReplyBundle(String url, long time, int flag, int id) {
        Bundle extras = new Bundle();
        extras.putString(EXTRAS_NAME_URL, url);
        extras.putLong(EXTRAS_NAME_TIME, time);
        extras.putBoolean(EXTRAS_NAME_IS_HISTORY, NetHelper.Flag.HISTORY == flag ? true : false);
        extras.putInt("sourceId", id);
        return extras;
    }

    @Override
    protected void executePostNetData(SimpleResponseHandler simpleResponseHandler, Bundle extras) {
        final String url = extras.getString(EXTRAS_NAME_URL);
        final long time = extras.getLong(EXTRAS_NAME_TIME);
        final boolean isHistory = extras.getBoolean(EXTRAS_NAME_IS_HISTORY);
        final int sourceId = extras.getInt("sourceId");
        RequestParams params = new RequestParams();
        params.put("userId", Constants.USER.getId());
        params.put("time", time);
        params.put("flag", isHistory ? NetHelper.Flag.HISTORY : NetHelper.Flag.UPDATE);
        params.put("sourceId", sourceId);
        NetHelper.post(url, params, simpleResponseHandler);
    }
}
