package com.zeustel.cp.net;

import android.text.TextUtils;
import android.util.Log;

import com.zeustel.cp.ZSStatusCode;
import com.zeustel.cp.intf.HttpCallBack;
import com.zeustel.cp.utils.SdkUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpRequest implements Runnable {
    private HttpCallBack requestCallback = null;
    private List<RequestParameter> parameter = null;
    private String type;
    private String urlStr;

    private URL url;
    private HttpURLConnection conn;

    private Map<String, String> heads;

    private static final int CONNECT_TIME_OUT = 8000;

    public HttpRequest(String type, String urlStr, Map<String, String> heads, final List<RequestParameter> params, final HttpCallBack callBack) {
        this.type = type;
        this.urlStr = urlStr;
        this.parameter = params;
        this.heads = heads;
        requestCallback = callBack;
    }

    public HttpRequest(String type, String urlStr, final List<RequestParameter> params, final HttpCallBack callBack) {
        this.type = type;
        this.urlStr = urlStr;
        this.parameter = params;
        requestCallback = callBack;
    }

    public void cancel() {
        if (conn != null) {
            conn.disconnect();
        }
    }

    @Override
    public void run() {
        if (type.equals("GET")) {
            if (heads != null) {
                // 添加参数
                doGet(urlStr, parameter, CONNECT_TIME_OUT);
                return;
            }
            doGetWithHead(urlStr, parameter, CONNECT_TIME_OUT);
        } else if (type.equals("POST")) {

            if (heads != null) {
                System.out.println("POST-----heads----");
                doPostWithHead(urlStr, parameter, CONNECT_TIME_OUT);
                return;
            }
            System.out.println("POST---------");
            doPost(urlStr, parameter, CONNECT_TIME_OUT);
        } else {
            return;
        }
    }

    public String inputStreamToString(final InputStream is) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    /**
     * post方法 有token会保存
     *
     * @param urlStr
     * @param params
     * @param timeOut
     */
    public void doPost(String urlStr, List<RequestParameter> params, int timeOut) {
        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            String paramStr = prepareParam(params);
            conn.setDoInput(true); // 允许输入流，即允许下载
            conn.setDoOutput(true); // 允许输出流，即允许上传
            conn.setUseCaches(false); // 不使用缓冲
            conn.setConnectTimeout(timeOut);
            OutputStream os = conn.getOutputStream();
            os.write(paramStr.toString().getBytes("utf-8"));
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            SdkUtils.LogD("RESULT:", result);

            final Map<String, List<String>> headerFields = conn.getHeaderFields();
            Set<String> keys = headerFields.keySet();
            for (String key : keys) {
                System.out.println("--- key : " + key);
                String val = conn.getHeaderField(key);
                System.out.println("--- val : " + val);
                //				if (key != null && key.equals("token")) {
                //					// 保存token
                //					SdkUtils.SetToken(val);
                //				}
            }

            requestCallback.callBackWithHeads(ZSStatusCode.SUCCESS, ZSStatusCode.SUCCESS_MSG, result, headerFields);

            br.close();

        } catch (MalformedURLException e) {
            requestCallback.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG, null);
        } catch (IOException e) {
            requestCallback.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG, null);
        }
    }

    /**
     * 有消息头的post
     *
     * @param urlStr
     * @param params
     * @param timeOut
     */
    public void doPostWithHead(String urlStr, List<RequestParameter> params, int timeOut) {
        try {
            url = new URL(urlStr);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            System.out.println("doPostWithHead....");
            // 有消息头
            if (heads != null) {
                Iterator<Entry<String, String>> entries = heads.entrySet().iterator();
                while (entries.hasNext()) {
                    Entry<String, String> entry = entries.next();
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    String value = entry.getValue();
                    //第一次使用  没有点击获取验证码 进行验证  token 为null
                    if (value == null) {
                        value = "";
                    }
                    //缺少参数
                    if (entry.getKey().equals("token")) {
                        if (TextUtils.isEmpty(value)) {
                            value = "aaaaa";
                        }

                    }
                    conn.setRequestProperty(entry.getKey(), value);
                }
            }
            System.out.println("setDoInput....");
            conn.setDoInput(true); // 允许输入流，即允许下载
            conn.setDoOutput(true); // 允许输出流，即允许上传
            conn.setUseCaches(false); // 不使用缓冲
            conn.setConnectTimeout(timeOut);
            // 没有参数
            if (params != null) {
                String paramStr = prepareParam(params);
                OutputStream os = conn.getOutputStream();
                String params11 = paramStr.toString();
                System.out.println("params11 : " + params11);
                os.write(params11.getBytes("utf-8"));
                os.close();
            }
            int code = conn.getResponseCode();
            System.out.println(" code : " + code);

            if (code == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                String result = "";
                while ((line = br.readLine()) != null) {
                    result += line;
                }

                final Map<String, List<String>> headerFields = conn.getHeaderFields();
                Set<String> keys = headerFields.keySet();
                for (String key : keys) {
                    String val = conn.getHeaderField(key);
                    //					if (key != null && key.equals("token")) {
                    //						// 保存token
                    //						SdkUtils.SetToken(val);
                    //					}

                    if (key != null && key.equals("lastAccessTime")) {
                        // 保存上次请求时间
                        Log.e("保存上次请求时间", val);
                        SdkUtils.setLastAccessTime(val);
                    }
                }


                requestCallback.callBackWithHeads(ZSStatusCode.SUCCESS, ZSStatusCode.SUCCESS_MSG, result, headerFields);

                br.close();
            } else {
                requestCallback.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG, null);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("MalformedURLException");
            requestCallback.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG, null);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException");
            requestCallback.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG, null);
        }
    }

    public void doGet(String urlStr, List<RequestParameter> params, int timeOut) {
        try {
            String paramStr = prepareParam(params);
            if (paramStr == null || paramStr.trim().length() < 1) {

            } else {
                urlStr += "?" + paramStr;
            }
            System.out.println(urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
            conn.setConnectTimeout(timeOut);
            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            SdkUtils.LogD("RESULT:", result);

            final Map<String, List<String>> headerFields = conn.getHeaderFields();
            Set<String> keys = headerFields.keySet();
            for (String key : keys) {
                String val = conn.getHeaderField(key);
                if (key != null && key.equals("lastAccessTime")) {
                    // 保存上次请求时间
                    SdkUtils.setLastAccessTime(val);
                }
            }


            requestCallback.callBackWithHeads(ZSStatusCode.SUCCESS, ZSStatusCode.SUCCESS_MSG, result, headerFields);

            br.close();
        } catch (MalformedURLException e) {
            requestCallback.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG, null);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("request",e.getMessage());
            requestCallback.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG, null);
        }
    }

    public void doGetWithHead(String urlStr, List<RequestParameter> params, int timeOut) {
        try {
            String paramStr = prepareParam(params);
            if (paramStr == null || paramStr.trim().length() < 1) {

            } else {
                urlStr += "?" + paramStr;
            }
            System.out.println(urlStr);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 有消息头
            if (heads != null) {
                Iterator<Entry<String, String>> entries = heads.entrySet().iterator();
                while (entries.hasNext()) {
                    Entry<String, String> entry = entries.next();
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            conn.setRequestProperty("Content-Type", "text/html; charset=UTF-8");
            conn.setConnectTimeout(timeOut);
            conn.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            SdkUtils.LogD("RESULT:", result);

            final Map<String, List<String>> headerFields = conn.getHeaderFields();
            Set<String> keys = headerFields.keySet();
            for (String key : keys) {
                String val = conn.getHeaderField(key);
                if (key != null && key.equals("lastAccessTime")) {
                    // 保存上次请求时间
                    Log.e("保存上次请求时间", val);
                    SdkUtils.setLastAccessTime(val);
                }
            }

            requestCallback.callBackWithHeads(ZSStatusCode.SUCCESS, ZSStatusCode.SUCCESS_MSG, result, headerFields);

            br.close();
        } catch (MalformedURLException e) {
            requestCallback.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG, null);
        } catch (IOException e) {
            Log.e("request",e.getMessage());
            requestCallback.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG, null);
        }
    }

    private String prepareParam(List<RequestParameter> params) {
        StringBuffer sb = new StringBuffer();
        if (params.isEmpty()) {
            return "";
        } else {
            for (RequestParameter param : params) {
                String value = param.getValue();
                if (sb.length() < 1) {
                    sb.append(param.getName()).append("=").append(value);
                } else {
                    sb.append("&").append(param.getName()).append("=").append(value);
                }
            }
            return sb.toString();
        }
    }
}