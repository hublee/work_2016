package com.zeustel.top9.utils.operate;

import android.os.Handler;

import com.loopj.android.http.RequestParams;
import com.zeustel.top9.bean.CommunityTopic;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/20 10:28
 */
public class DataCommunityTopic extends DataHtml<CommunityTopic> {

    public DataCommunityTopic(Handler handler, DBBaseOperate<CommunityTopic> dbBaseImp, int type) {
        super(handler, dbBaseImp, type);
    }

    public DataCommunityTopic(Handler handler, DBBaseOperate<CommunityTopic> dbBaseImp) {
        super(handler, dbBaseImp);
    }

    @Override
    public void publishData(CommunityTopic data) {
        if (data != null) {
            CommunityTopic communityTopic = (CommunityTopic) data;
            RequestParams params = new RequestParams();
            params.put("userId", Constants.USER.getId());
            params.put("gameId", communityTopic.getGameId());
            params.put("type", communityTopic.getTopicType());
            params.put("title", communityTopic.getTitle());
            params.put("content", communityTopic.getContent());

            List<String> imgs = Tools.convertPathList(communityTopic.getImages());
            if (imgs != null && !imgs.isEmpty()) {
                for (int i = 0; i < imgs.size(); i++) {
                    try {
                        params.put("image" + (i + 1), new File(imgs.get(i)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            NetHelper.post(Constants.URL_TOP9_TOPIC_PUBLISH, params, getPublishResponseHandler());
        }
    }
}
