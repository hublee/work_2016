package com.zeustel.top9.utils.operate;

import android.os.Handler;

import com.loopj.android.http.RequestParams;
import com.zeustel.top9.bean.community.Comment;
import com.zeustel.top9.bean.community.Content;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/10 14:48
 */
public class DataComment extends DataBaseOperate<Comment> {
    public DataComment(Handler handler, DBBaseOperate<Comment> dbBaseImp) {
        super(handler, dbBaseImp);
    }

    //语音文件
    @Override
    public void publishData(Comment data) {
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
        NetHelper.post(Constants.URL_COMMENT_PUBLISH, params, getPublishResponseHandler());
    }
}
