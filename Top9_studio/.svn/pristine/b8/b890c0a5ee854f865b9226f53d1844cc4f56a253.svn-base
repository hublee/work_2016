package com.zeustel.top9.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 网络连接帮助类
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/30 15:19
 */
public class NetHelper {

    public static final int MSG_REQUEST_SUCCESS = 11111;
    public static final int MSG_REQUEST_NO = 11112;
    public static final int MSG_REQUEST_FAILED = 11113;
    public static final int ARG1_NATIVE = 11114;
    public static final int ARG1_NET = 11115;
    public static final int ARG2_UPDATE = 11116;
    public static final int ARG2_HISTORY = 11117;
    public static final int ARG2_SINGLE = 11118;
    public static final int MSG_PUBLISH_SUCCESS = 11119;
    public static final int MSG_PUBLISH_FAILED = 11120;
    //网络连接超时
    private static final int TIMEOUT_MILLIS = 5000;

    /**
     * 获取完整路径
     *
     * @param relativeUrl 相对路径
     * @return 绝对路径
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return Constants.URL_BASE + relativeUrl;
    }

    /**
     * get请求
     *
     * @param url             请求相对路径
     * @param params          参数
     * @param responseHandler 回调
     */
    public static void get(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("token", Constants.USER.getToken());
        client.setConnectTimeout(TIMEOUT_MILLIS);
        client.get(context, url, params, responseHandler);
    }

    /**
     * post请求
     *
     * @param url             请求相对路径
     * @param params          参数
     * @param responseHandler 回调
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        Tools.log_i(NetHelper.class,"post","url : "+url);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("token", Constants.USER.getToken());
        client.setConnectTimeout(TIMEOUT_MILLIS);
        client.post(url, params, responseHandler);
    }

    /**
     * 请求flag
     */
    public static class Flag {
        //更新
        public static final int UPDATE = 1;
        //历史
        public static final int HISTORY = 0;
        //首次进入页面
        public static final int FIRST = 2;
    }
}
