package com.zeustel.top9.utils.operate;

import android.os.Handler;

import com.loopj.android.http.RequestParams;
import com.zeustel.top9.bean.Feedback;
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
 * @date 2015/9/9 09:21
 */
public class DataFeedback extends DataBaseOperate<Feedback> {
    public DataFeedback(Handler handler, DBBaseOperate<Feedback> dbBaseImp) {
        super(handler, dbBaseImp);
    }

    @Override
    public void publishData(Feedback data) {
        RequestParams params = new RequestParams();
        params.put("userId", Constants.USER.getId());
        params.put("Content", data.getFeedbackContent());
        String imgs = data.getImgs();
        Tools.log_i(DataFeedback.class, "publishData", "imgs : " + imgs);
        if (!Tools.isEmpty(imgs)) {
            List<String> path = Tools.convertPathList(imgs);
            if (!Tools.isEmpty(path)) {
                try {
                    for (int i = 0; i < path.size(); i++) {
                        Tools.log_i(DataFeedback.class, "publishData", "path : " + path.get(i));
                        params.put("imgs" + i, new File(path.get(i)));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        NetHelper.post(Constants.URL_FEEDBACK_PUBLISH, params, getPublishResponseHandler());
    }
}
