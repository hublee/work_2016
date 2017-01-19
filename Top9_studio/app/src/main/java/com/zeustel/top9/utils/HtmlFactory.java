package com.zeustel.top9.utils;

import android.content.Context;
import android.os.Handler;

import com.zeustel.top9.bean.HtmlComment;
import com.zeustel.top9.utils.operate.DBCommunityInfoImp;
import com.zeustel.top9.utils.operate.DBCommunityTopicImp;
import com.zeustel.top9.utils.operate.DBGameEvaluatingImp;
import com.zeustel.top9.utils.operate.DBHtmlImp;
import com.zeustel.top9.utils.operate.DataCommunityInfo;
import com.zeustel.top9.utils.operate.DataCommunityTopic;
import com.zeustel.top9.utils.operate.DataGameEvaluating;
import com.zeustel.top9.utils.operate.DataHtml;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/27 10:25
 */
public class HtmlFactory {
    public static DBHtmlImp newDBHtmlImp(Context context, int commentType) {
        DBHtmlImp mDBHtmlImp = null;
        switch (commentType) {
            case HtmlComment.CommentType.TYPE_EVALUATING
                    :
                mDBHtmlImp = new DBGameEvaluatingImp(context);
                break;
            case HtmlComment.CommentType.TYPE_TOPIC
                    :
                mDBHtmlImp = new DBCommunityTopicImp(context);
                break;
            case HtmlComment.CommentType.TYPE_INFO
                    :
                mDBHtmlImp = new DBCommunityInfoImp(context);
        }
        return mDBHtmlImp;
    }

    public static DataHtml newDataHtml(Context context, Handler handler, int type, int commentType) {
        DataHtml mDataHtml = null;
        DBHtmlImp mDBHtmlImp = newDBHtmlImp(context, commentType);
        switch (commentType) {
            case HtmlComment.CommentType.TYPE_EVALUATING
                    :
                mDataHtml = new DataGameEvaluating(handler, mDBHtmlImp, type);
                break;
            case HtmlComment.CommentType.TYPE_TOPIC
                    :
                mDataHtml = new DataCommunityTopic(handler, mDBHtmlImp, type);
                break;
            case HtmlComment.CommentType.TYPE_INFO
                    :
                mDataHtml = new DataCommunityInfo(handler, mDBHtmlImp, type);
        }
        return mDataHtml;
    }

    public static DataHtml newDataHtml(Context context, Handler handler, int commentType) {
        DataHtml mDataHtml = null;
        DBHtmlImp mDBHtmlImp = newDBHtmlImp(context, commentType);
        switch (commentType) {
            case HtmlComment.CommentType.TYPE_EVALUATING
                    :
                mDataHtml = new DataGameEvaluating(handler, mDBHtmlImp);
                break;
            case HtmlComment.CommentType.TYPE_TOPIC
                    :
                mDataHtml = new DataCommunityTopic(handler, mDBHtmlImp);
                break;
            case HtmlComment.CommentType.TYPE_INFO
                    :
                mDataHtml = new DataCommunityInfo(handler, mDBHtmlImp);
        }
        return mDataHtml;
    }

}
