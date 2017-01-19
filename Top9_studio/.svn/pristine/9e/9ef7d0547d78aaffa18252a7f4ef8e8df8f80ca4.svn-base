package com.zeustel.top9.utils.operate;

import android.os.Handler;

import com.loopj.android.http.RequestParams;
import com.zeustel.top9.bean.HtmlComment;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;

/**
 * 评论数据操作
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/31 15:54
 */
public class DataHtmlComment extends DataBaseOperate<HtmlComment> {
    public DataHtmlComment(Handler handler, DBBaseOperate<HtmlComment> dbBaseImp) {
        super(handler, dbBaseImp);
    }

    @Override
    public void publishData(HtmlComment data) {
        if (data != null) {
            int commentType = data.getCommentType();
            String url = null;
            switch (commentType) {
                case HtmlComment.CommentType.TYPE_EVALUATING:
                    url = Constants.URL_EVALUATING_COMMENT_PUBLISH;
                    break;
                case HtmlComment.CommentType.TYPE_INFO:
                    url = Constants.URL_TOP9_INFO_COMMENT_PUBLISH;
                    break;
                case HtmlComment.CommentType.TYPE_TOPIC:
                    url = Constants.URL_TOP9_TOPIC_COMMENT_PUBLISH;
                    break;
            }
            RequestParams params = new RequestParams();
            params.put("userId", Constants.USER.getId());
            params.put("sourceId", data.getWhichId());
            params.put("Content", data.getContent());
            NetHelper.post(url, params, getPublishResponseHandler());
        }
    }
}
