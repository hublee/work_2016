package com.zeustel.top9.utils.operate;

import android.os.Handler;

import com.google.gson.Gson;
import com.zeustel.top9.bean.VoiceRoom;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.SimpleResponseHandler;
import com.zeustel.top9.utils.Tools;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/17 10:23
 */
public class DataVoiceRoom extends DataBaseOperate<VoiceRoom> {
    public DataVoiceRoom(Handler handler) {
        super(handler, null);
    }

    private void listenRoom(final String url) {
        SimpleResponseHandler listenResponseHandler = new SimpleResponseHandler() {
            @Override
            public void onCallBack(int code, String json) {
                super.onCallBack(code, json);
                if (200 == code) {
                    Gson gson = new Gson();
                    Result result = gson.fromJson(json, Result.class);
                    Tools.log_i(DataVoiceRoom.class, "onCallBack", "url : " + url);
                    Tools.log_i(DataVoiceRoom.class, "onCallBack", "msg : " + result.getMsg());
                    if (result != null && Result.SUCCESS == result.getSuccess()) {

                    } else {

                    }
                }
            }
        };
        NetHelper.get(null/*Context*/, url, null/* RequestParams*/, listenResponseHandler);
    }

    public void listenEnterRoom(int roomId) {
        listenRoom(Constants.URL_TOP9_CHART_ENTER + "?userId=" + Constants.USER.getId() + "&roomId=" + roomId);
    }

    public void listenExitRoom(int roomId) {
        listenRoom(Constants.URL_TOP9_CHART_EXIT + "?userId=" + Constants.USER.getId() + "&roomId=" + roomId);
    }

}
