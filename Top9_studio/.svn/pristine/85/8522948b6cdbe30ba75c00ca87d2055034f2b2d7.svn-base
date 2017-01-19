package com.zeustel.top9;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.loopj.android.http.RequestParams;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.SimpleResponseHandler;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    /*获币记录*/
    public void testIntegralUrl() {
        RequestParams params = new RequestParams();
        params.put("userId", 7);
        params.put("time", 0);
        params.put("flag", NetHelper.Flag.UPDATE);
        NetHelper.post(Constants.URL_INTEGRAL_NOTE, params, new SimpleResponseHandler() {
            @Override
            public void onCallBack(int code, String json) {
                super.onCallBack(code, json);
            }
        });
    }
}