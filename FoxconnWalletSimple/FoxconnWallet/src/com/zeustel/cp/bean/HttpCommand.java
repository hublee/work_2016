package com.zeustel.cp.bean;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zeustel.cp.ZSSDK;
import com.zeustel.cp.ZSStatusCode;
import com.zeustel.cp.intf.CallBack;
import com.zeustel.cp.intf.HttpCallBack;
import com.zeustel.cp.intf.ICommand;
import com.zeustel.cp.intf.NextCallBack;
import com.zeustel.cp.net.DefaultThreadPool;
import com.zeustel.cp.net.JsonParams;
import com.zeustel.cp.net.Parameter;
import com.zeustel.cp.net.RequestManager;
import com.zeustel.cp.net.RequestParameter;
import com.zeustel.cp.utils.Base64;
import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.NetCache;
import com.zeustel.cp.utils.ReqUtil;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.SharedPreferencesUtils;
import com.zeustel.cp.utils.Tools;
import com.zeustel.cp.wallet.WalletActivity;
import com.zeustel.cp.wallet.views.ProgressDialogUtils;
import com.zeustel.foxconn.cp_sdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 请求命令
 *
 * @author Do
 */
public class HttpCommand implements ICommand {
    private Context context;
    private Context walletContext;
    private ProgressDialogUtils mProgressDialogUtils;

    public void setContext(Context context) {
        this.context = context;
    }

    private void request(final String url, String method, RequestManager requestManager, final NextCallBack callBack, JsonParams jsonParams,String ...args) {

        if (walletContext != null && mProgressDialogUtils == null) {
            mProgressDialogUtils = new ProgressDialogUtils();
        }
        mProgressDialogUtils.show(walletContext, "...");
        final Map<String, String> header = new HashMap<String, String>();
        String token = "";
        if (args != null && args.length > 0 && args[0] != null) {
            token = args[0].toString();
        }
        header.put("token", token);
        header.put("lastAccessTime", SdkUtils.getLastAccessTime());
        header.put("appId", getCp().getAppId());
        header.put("deviceInfo", SdkUtils.getDevicesInfo(context));
        header.put("osType", "0");
        List<RequestParameter> params = null;
        if (jsonParams != null) {
            params = new ArrayList<RequestParameter>();
            params.add(new RequestParameter("reqData", encode(Constants.ZS_PUBLIC_KEY, jsonParams.toJson())));
        }

        DefaultThreadPool.getInstance().execute(requestManager.createRequestWithHead(header, method, SdkUtils.getServerInfo().getServer() + url, params, new HttpCallBack() {
            private Map<String, List<String>> responseHeads;

            @Override
            public void callBack(final int code, final String msg, final String result) {
                Log.e("request", "url:"+SdkUtils.getServerInfo().getServer()+url+"，code : " + code + ",msg : " + msg + ",result : " + result);
                new Handler(walletContext.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        String text = null;
                        mProgressDialogUtils.cacel();
                        try {
                            switch (code) {
                                case ZSStatusCode.URL_ERROR:
                                    callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
                                    text = ZSStatusCode.URL_ERROR_MSG;
                                    return;
                                case ZSStatusCode.NETWORK_ERROR:
                                    callBack.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG);
                                    text = ZSStatusCode.NETWORK_ERROR_MSG;
                                    return;
                                case ZSStatusCode.SUCCESS:
                                    final String dataFromResult = getDataFromResult(result);
                                    Log.e("request", "run (line 104): " + dataFromResult);
                                    //...START
                                    //...END
                                    // 处理结果
                                    JSONObject data = getDataFromJSONObject(dataFromResult, callBack);
                                    if (data != null) {
                                        if (data.has(Constants.SID)) {
                                            ZSSDK.getDefault().setAuthCode(data.getString(Constants.SID));
                                        }

                                        if ((Constants.URL_LOGIN_EMAIL.equals(url) || (Constants.URL_LOGIN_OTHER.equals(url) || (Constants.URL_TOKEN_LOGIN.equals(url) || (Constants.URL_REGISTER_EMAIL.equals(url)))))) {
                                            String token ;
                                            final AccountInfo getAccountInfo = getAccountInfo(data);
                                            Gson gson = new Gson();
                                            if (Constants.URL_LOGIN_EMAIL.equals(url) || (Constants.URL_LOGIN_OTHER.equals(url) || (Constants.URL_TOKEN_LOGIN.equals(url)))) {
                                                if (responseHeads != null && !responseHeads.isEmpty()) {
                                                    if (responseHeads.containsKey("token")) {
                                                        final List<String> tokenList = responseHeads.get("token");
                                                        if (tokenList != null && !tokenList.isEmpty()) {
                                                            token = tokenList.get(0);
                                                            getAccountInfo.setToken(token);
                                                        }
                                                    }
                                                }
                                                Users user = new Users();
                                                user.setUserName(getAccountInfo.getAccount());
                                                user.setUserId(getAccountInfo.getId());
                                                SdkUtils.setuserId(getAccountInfo.getId());
                                                ZSSDK.getDefault().setUsers(user);
                                                Tools.SaveUserInfo(user);
                                                if (Constants.URL_LOGIN_EMAIL.equals(url)) {
                                                    SharedPreferencesUtils.updatePreferences(context, WalletActivity.ACCOUNT_PREFIX + getAccountInfo.getAccount(), gson.toJson(getAccountInfo));
                                                }
                                            }
                                            callBack.callBack(ZSStatusCode.SUCCESS, gson.toJson(getAccountInfo));
                                            return;
                                        } else if (Constants.URL_VALIDBIND.equals(url)) {
                                            int isBind = 0;
                                            if (data.has("isBind")) {
                                                isBind = data.getInt("isBind");
                                            } else {
                                                Log.e("URL_LOGIN_EMAIL", "run (line 135): not has isBind key");
                                            }
                                            callBack.callBack(ZSStatusCode.SUCCESS, String.valueOf(isBind));
                                            return;
                                        }
                                        callBack.callBack(ZSStatusCode.SUCCESS, msg);
                                    } else {
                                        Gson gson = new Gson();
                                        if (!TextUtils.isEmpty(dataFromResult)) {
                                            final Result responseResult = gson.fromJson(dataFromResult, Result.class);
                                            if (responseResult != null) {
                                                text = responseResult.getMsg();
                                                callBack.callBack(responseResult.getError(), text);
                                            }
                                        }

                                    }
                                    break;
                                default:
                                    callBack.callBack(ZSStatusCode.DATA_ERROR, ZSStatusCode.DATA_ERROR_MSG);
                                    text = ZSStatusCode.DATA_ERROR_MSG;
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            callBack.callBack(ZSStatusCode.EX_ERROR, ZSStatusCode.EX_ERROR_MSG);
                            text = ZSStatusCode.EX_ERROR_MSG;
                        } finally {
                            if (Result.SUCCESS != code && text != null) {
                                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                            }
                            responseHeads = null;
                        }
                    }
                });
            }


            @Override
            public void callBackWithHeads(int code, String msg, String string, Map<String, List<String>> heads) {
                this.responseHeads = heads;
                super.callBackWithHeads(code, msg, string, heads);
            }
        }));
    }
    /**
     * token登录
     * @param requestManager
     * @param callBack
     */
    @Override
   public void tokenLogin(String token,RequestManager requestManager,
                     NextCallBack callBack){
        request(Constants.URL_TOKEN_LOGIN, Constants.POST, requestManager,callBack,null,token);
    }

    private AccountInfo getAccountInfo(JSONObject data) throws JSONException {
        AccountInfo accountInfo = new AccountInfo();
        if (data.has("nickName")) {
            String nickName = data.getString("nickName");
        }
        if (data.has("name")) {
            String userName = data.getString("name");
            accountInfo.setAccount(userName);
            accountInfo.setEmail(userName);
        }
        if (data.has("userId")) {
            String userId = data.getString("userId");
            accountInfo.setId(userId);
            accountInfo.setPlatform(Platform.ZEUSTEL);
            accountInfo.setAccountIcon(R.drawable.email_icon);
        }
        return accountInfo;
    }

    public void thirdLogin(String openId, String platform, String userinfo, RequestManager requestManager, final NextCallBack callBack) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("openId", openId));
        jsonParams.addParams(new Parameter("platform", platform));
        jsonParams.addParams(new Parameter("userinfo", userinfo));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));
        request(Constants.URL_LOGIN_OTHER, Constants.POST, requestManager, callBack, jsonParams);
    }


    public void registViaEmail(String email, String password, RequestManager requestManager, final NextCallBack callBack) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("email", email));
        jsonParams.addParams(new Parameter("password", password));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));

        request(Constants.URL_REGISTER_EMAIL, Constants.POST, requestManager, callBack, jsonParams);

    }

    public void loginViaEmail(String email, String password, RequestManager requestManager, final NextCallBack callBack) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("email", email));
        jsonParams.addParams(new Parameter("password", password));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));
        request(Constants.URL_LOGIN_EMAIL, Constants.POST, requestManager, callBack, jsonParams);

    }

    public void retrieveViaEmail(String email, String password, String digitalCode, RequestManager requestManager, final NextCallBack callBack) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("email", email));
        jsonParams.addParams(new Parameter("password", password));
        jsonParams.addParams(new Parameter("digitalCode", digitalCode));
        String sign = getSign(jsonParams.getParams());
        Log.e("retrieveViaEmail", "sign : " + sign);
        jsonParams.addParams(new Parameter("sign", sign));

        request(Constants.URL_FORGET, Constants.POST, requestManager, callBack, jsonParams);
    }

    public void getCodeViaMail(String email, RequestManager requestManager, final NextCallBack callBack) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("email", email));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));
        request(Constants.URL_GET_VER, Constants.GET, requestManager, callBack, jsonParams);
    }

    /**
     * 绑定第三方账号
     *
     * @param openid
     * @param platform
     * @param userId
     * @param requestManager
     * @param callBack
     */
    @Override
    public void bindThirdUser(String openid, String platform, String userId, RequestManager requestManager, NextCallBack callBack) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("openId", openid));
        jsonParams.addParams(new Parameter("platform", platform));
        jsonParams.addParams(new Parameter("userId", userId));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));
        request(Constants.URL_BIND_USER, Constants.POST, requestManager, callBack, jsonParams);
    }

    /**
     * 检查是否绑定
     *
     * @param openid
     * @param platform
     * @param requestManager
     * @param callBack
     */
    @Override
    public void validBind(String openid, String platform, RequestManager requestManager, NextCallBack callBack) {
        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("openId", openid));
        jsonParams.addParams(new Parameter("platform", platform));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));
        request(Constants.URL_VALIDBIND, Constants.POST, requestManager, callBack, jsonParams);
    }

    /**
     * 获取配置
     *
     * @param requestManager
     * @param callBack
     */
    public void getConfig(RequestManager requestManager, final NextCallBack callBack) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("token", SdkUtils.getToken());
        header.put("lastAccessTime", SdkUtils.getLastAccessTime());
        header.put("appId", getCp().getAppId());
        header.put("deviceInfo", SdkUtils.getDevicesInfo(context));
        header.put("osType", "0");

        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("appName", Constants.APPNAME));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));

        List<RequestParameter> params = new ArrayList<RequestParameter>();
        String url = "http://120.76.189.57:8081/sdk";
        params.add(new RequestParameter("reqData", encode(Constants.ZS_PUBLIC_KEY, jsonParams.toJson())));
        DefaultThreadPool.getInstance().execute(requestManager.createRequestWithHead(header, Constants.POST, url + Constants.CONFIG_URL, params, new HttpCallBack() {
            @Override
            public void callBack(int code, String msg, String result) {
                // TODO Auto-generated method stub
                try {
                    switch (code) {
                        case ZSStatusCode.URL_ERROR:
                            callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
                            return;
                        case ZSStatusCode.NETWORK_ERROR:
                            callBack.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG);
                            return;
                        case ZSStatusCode.SUCCESS:
                            // 处理结果
                            JSONObject data = getDataFromJSONObject(getDataFromResult(result), callBack);
                            if (data == null) {
                                return;
                            }
                            Log.e("getConfig:" + code, data.toString());
                            // 保存ip信息
                            SdkUtils.saveServerInfo(data.getString(Constants.SERVER), data.getString(Constants.TESTSERVER), data.getString(Constants.msdk));
                            callBack.callBack(ZSStatusCode.SUCCESS, msg);
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * 登录
     *
     * @param requestManager
     * @param callBack
     */
    public void login(RequestManager requestManager, final NextCallBack callBack) {
        System.out.println("login ??????????????");
        Map<String, String> header = new HashMap<String, String>();
        header.put("token", SdkUtils.getToken());
        header.put("sid", ZSSDK.getDefault().getAuthCode());
        header.put("lastAccessTime", SdkUtils.getLastAccessTime());
        header.put("appId", getCp().getAppId());
        header.put("deviceInfo", SdkUtils.getDevicesInfo(context));
        header.put("osType", "0");

        DefaultThreadPool.getInstance().execute(requestManager.createRequestWithHead(header, Constants.POST, Constants.BASE_URL + Constants.LOGIN_URL, null, new HttpCallBack() {
            @Override
            public void callBack(int code, String msg, String result) {
                // TODO Auto-generated method stub
                try {
                    switch (code) {
                        case ZSStatusCode.URL_ERROR:
                            callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
                            return;
                        case ZSStatusCode.NETWORK_ERROR:
                            callBack.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG);
                            return;
                        case ZSStatusCode.SUCCESS:
                            // 处理结果
                            JSONObject data = getDataFromJSONObject(getDataFromResult(result), callBack);
                            if (data == null) {
                                return;
                            }
                            Log.e("login :", data.toString());
                            ZSSDK.getDefault().setAuthCode(data.getString(Constants.SID));
                            callBack.callBack(ZSStatusCode.SUCCESS, msg);
                            break;
                        default:
                            SdkUtils.LogD("callback:" + code, msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * 发送验证码
     *
     * @param requestManager
     * @param phoneno
     * @param callBack
     */
    public void sendSMS(RequestManager requestManager, String phoneno, final NextCallBack callBack) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("token", SdkUtils.getToken());
        header.put("lastAccessTime", SdkUtils.getLastAccessTime());
        header.put("appId", getCp().getAppId());
        header.put("deviceInfo", SdkUtils.getDevicesInfo(context));
        header.put("osType", "0");

        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("mobile", phoneno));
        jsonParams.addParams(new Parameter("sid", ZSSDK.getDefault().getAuthCode()));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));
        List<RequestParameter> params = new ArrayList<RequestParameter>();
        params.add(new RequestParameter("reqData", encode(Constants.ZS_PUBLIC_KEY, jsonParams.toJson())));
        DefaultThreadPool.getInstance().execute(requestManager.createRequestWithHead(header, Constants.POST, Constants.BASE_URL + Constants.SMS_URL, params, new HttpCallBack() {
            @Override
            public void callBack(int code, String msg, String result) {
                // TODO Auto-generated method stub
                try {
                    switch (code) {
                        case ZSStatusCode.URL_ERROR:
                            callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
                            return;
                        case ZSStatusCode.NETWORK_ERROR:
                            callBack.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG);
                            return;
                        case ZSStatusCode.SUCCESS:
                            // 处理结果
                            JSONObject data = getDataFromJSONObject(getDataFromResult(result), callBack);
                            ;
                            if (data == null) {
                                return;
                            }
                            ZSSDK.getDefault().setAuthCode(data.getString(Constants.SID));
                            callBack.callBack(ZSStatusCode.SUCCESS, msg);
                            break;
                        default:
                            SdkUtils.LogD("callback:" + code, msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * 校验验证码
     *
     * @param requestManager
     * @param phone
     * @param code
     * @param callBack
     */
    public void checkCode(RequestManager requestManager, final String phone, String code, final NextCallBack callBack) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("token", SdkUtils.getToken());
        header.put("lastAccessTime", SdkUtils.getLastAccessTime());
        header.put("appId", getCp().getAppId());
        header.put("deviceInfo", SdkUtils.getDevicesInfo(context));
        header.put("osType", "0");

        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("code", code));
        jsonParams.addParams(new Parameter("mobile", phone));
        jsonParams.addParams(new Parameter("sid", ZSSDK.getDefault().getAuthCode()));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));
        List<RequestParameter> params = new ArrayList<RequestParameter>();
        params.add(new RequestParameter("reqData", encode(Constants.ZS_PUBLIC_KEY, jsonParams.toJson())));
        System.out.println("checkCode");
        DefaultThreadPool.getInstance().execute(requestManager.createRequestWithHead(header, Constants.POST, Constants.BASE_URL + Constants.AUTH_TOKEN_URL, params, new HttpCallBack() {
            @Override
            public void callBack(int code, String msg, String result) {
                // TODO Auto-generated method stub
                System.out.println("callBack -----------" + code + "msg : " + msg);
                try {
                    switch (code) {
                        case ZSStatusCode.URL_ERROR:
                            callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
                            return;
                        case ZSStatusCode.NETWORK_ERROR:
                            callBack.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG);
                            return;
                        case ZSStatusCode.SUCCESS:
                            // 处理结果
                            JSONObject data = getDataFromJSONObject(getDataFromResult(result), callBack);

                            if (data == null) {
                                return;
                            }
                            Log.e("checkCode :", data.toString());
                            ZSSDK.getDefault().setAuthCode(data.getString(Constants.SID));
                            ZSSDK.getDefault().setMobile(phone);
                            callBack.callBack(ZSStatusCode.SUCCESS, msg);
                            break;
                        default:
                            SdkUtils.LogD("callback:" + code, msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * 兑换
     *
     * @param requestManager
     * @param phoneno
     * @param callBack
     */
    public void exchange(RequestManager requestManager, String phoneno, int amount, final NextCallBack callBack) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("token", SdkUtils.getToken());
        header.put("lastAccessTime", SdkUtils.getLastAccessTime());
        header.put("appId", getCp().getAppId());
        header.put("deviceInfo", SdkUtils.getDevicesInfo(context));
        header.put("osType", "0");

        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("amount", amount + ""));
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("mobile", phoneno));
        jsonParams.addParams(new Parameter("sid", ZSSDK.getDefault().getAuthCode()));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));

        List<RequestParameter> params = new ArrayList<RequestParameter>();
        params.add(new RequestParameter("reqData", encode(Constants.ZS_PUBLIC_KEY, jsonParams.toJson())));

        DefaultThreadPool.getInstance().execute(requestManager.createRequestWithHead(header, Constants.POST, Constants.BASE_URL + Constants.EXCHANGE_URL, params, new HttpCallBack() {
            @Override
            public void callBack(int code, String msg, String result) {
                // TODO Auto-generated method stub
                try {
                    switch (code) {
                        case ZSStatusCode.URL_ERROR:
                            callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
                            return;
                        case ZSStatusCode.NETWORK_ERROR:
                            callBack.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG);
                            return;
                        case ZSStatusCode.SUCCESS:
                            // 处理结果
                            JSONObject data = getDataFromJSONObject(getDataFromResult(result), callBack);
                            if (data == null) {
                                return;
                            }
                            callBack.callBack(ZSStatusCode.SUCCESS, msg);
                            break;
                        default:
                            SdkUtils.LogD("callback:" + code, msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * 授权
     *
     * @param requestManager
     * @param callBack
     */
    public void cpAuth(RequestManager requestManager, final NextCallBack callBack) {
        Log.e("cpAuth : ", "---------------");
        Map<String, String> header = new HashMap<String, String>();
        header.put("token", SdkUtils.getToken());
        header.put("lastAccessTime", SdkUtils.getLastAccessTime());
        header.put("appId", getCp().getAppId());
        header.put("deviceInfo", SdkUtils.getDevicesInfo(context));
        header.put("osType", "0");

        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));

        List<RequestParameter> params = new ArrayList<RequestParameter>();
        params.add(new RequestParameter("reqData", encode(Constants.ZS_PUBLIC_KEY, jsonParams.toJson())));
        DefaultThreadPool.getInstance().execute(requestManager.createRequestWithHead(header, Constants.POST, Constants.BASE_URL + Constants.AUTH_URL, params, new HttpCallBack() {
            @Override
            public void callBack(int code, String msg, String result) {
                Log.e("cpAuth : ", "code : " + code + "msg : " + msg + "result : " + result);
                try {
                    switch (code) {
                        case ZSStatusCode.URL_ERROR:
                            callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
                            return;
                        case ZSStatusCode.NETWORK_ERROR:
                            callBack.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG);
                            return;
                        case ZSStatusCode.SUCCESS:
                            // 处理结果
                            JSONObject data = getDataFromJSONObject(getDataFromResult(result), callBack);
                            if (data == null) {
                                return;
                            }
                            ZSSDK.getDefault().setAuthCode(data.getString(Constants.SID));
                            // 保存货币信息
                            SdkUtils.saveCoinInfo(data.getString(Constants.COIN_NAME), data.getInt(Constants.SCALE));
                            callBack.callBack(ZSStatusCode.SUCCESS, msg);
                            break;
                        default:
                            SdkUtils.LogD("callback:" + code, msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    // /**
    // * 获取配置参数
    // * @param requestManager
    // * @param callBack
    // */
    // public void getConfig(RequestManager requestManager,final NextCallBack
    // callBack){
    // JsonParams jsonParams = new JsonParams();
    // jsonParams.addParams(new Parameter("appId",getCp().getAppId()));
    // jsonParams.addParams(new Parameter("appName","sdk"));
    // jsonParams.addParams(new
    // Parameter("sign",getSign(jsonParams.getParams())));
    // List<RequestParameter> params = new ArrayList<RequestParameter>();
    // params.add(new RequestParameter("reqData",encode(Constants.ZS_PUBLIC_KEY,
    // jsonParams.toJson())));
    // DefaultThreadPool.getInstance().execute(requestManager.createRequest(Constants.POST,
    // Constants.BASE_URL+Constants.AUTH_URL, params, new HttpCallBack(){
    // @Override
    // public void callBack(int code, String msg, String result) {
    // // TODO Auto-generated method stub
    // try{
    // switch(code){
    // case ZSStatusCode.URL_ERROR:
    // callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
    // return;
    // case ZSStatusCode.NETWORK_ERROR:
    // callBack.callBack(ZSStatusCode.NETWORK_ERROR,
    // ZSStatusCode.NETWORK_ERROR_MSG);
    // return;
    // case ZSStatusCode.SUCCESS:
    // //处理结果
    // JSONObject data = getDataFromJSONObject(getDataFromResult(result),
    // callBack); ;
    // if(data==null){
    // return;
    // }
    // callBack.callBack(ZSStatusCode.SUCCESS, msg);
    // break;
    // default:
    // SdkUtils.LogD("callback:"+code, msg);
    // break;
    // }
    // }catch(Exception e){
    // e.printStackTrace();
    // }
    // }
    // }));
    // }

    /**
     * 校验token
     *
     * @param requestManager
     * @param callBack
     */
    public void validToken(RequestManager requestManager, final NextCallBack callBack) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("token", SdkUtils.getToken());
        header.put("lastAccessTime", SdkUtils.getLastAccessTime());
        header.put("appId", getCp().getAppId());
        header.put("deviceInfo", SdkUtils.getDevicesInfo(context));
        header.put("osType", "0");

        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("sid", ZSSDK.getDefault().getAuthCode()));
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));
        List<RequestParameter> params = new ArrayList<RequestParameter>();
        params.add(new RequestParameter("reqData", encode(Constants.ZS_PUBLIC_KEY, jsonParams.toJson())));
        DefaultThreadPool.getInstance().execute(requestManager.createRequestWithHead(header, Constants.POST, Constants.BASE_URL + Constants.VALID_TOKEN, params, new HttpCallBack() {
            @Override
            public void callBack(int code, String msg, String result) {
                // TODO Auto-generated method stub
                try {
                    switch (code) {
                        case ZSStatusCode.URL_ERROR:
                            callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
                            return;
                        case ZSStatusCode.NETWORK_ERROR:
                            callBack.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG);
                            return;
                        case ZSStatusCode.SUCCESS:
                            // 处理结果
                            JSONObject data = getDataFromJSONObject(getDataFromResult(result), callBack);
                            ;
                            if (data == null) {
                                return;
                            }
                            ZSSDK.getDefault().setAuthCode(data.getString(Constants.SID));
                            callBack.callBack(ZSStatusCode.SUCCESS, msg);
                            break;
                        default:
                            SdkUtils.LogD("callback:" + code, msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * 数字密码验证
     *
     * @param requestManager
     * @param phone
     * @param pwd
     * @param callBack
     */
    public void verNumPwd(RequestManager requestManager, final String phone, String pwd, final NextCallBack callBack) {
        System.out.println("pwd : " + pwd);
        Map<String, String> header = new HashMap<String, String>();
        header.put("token", SdkUtils.getToken());
        header.put("lastAccessTime", SdkUtils.getLastAccessTime());
        header.put("appId", getCp().getAppId());
        header.put("deviceInfo", SdkUtils.getDevicesInfo(context));
        header.put("osType", "0");

        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("mobile", phone));
        jsonParams.addParams(new Parameter("password", pwd));
        jsonParams.addParams(new Parameter("sid", ZSSDK.getDefault().getAuthCode()));// ????
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));
        jsonParams.addParams(new Parameter("type", String.valueOf(1)));
        List<RequestParameter> params = new ArrayList<RequestParameter>();

        params.add(new RequestParameter("reqData", encode(Constants.ZS_PUBLIC_KEY, jsonParams.toJson())));
        DefaultThreadPool.getInstance().execute(requestManager.createRequestWithHead(header, Constants.POST, Constants.BASE_URL + Constants.AUTH_VER_NUM_PWD, params, new HttpCallBack() {
            @Override
            public void callBack(int code, String msg, String result) {
                // TODO Auto-generated method stub
                try {
                    switch (code) {
                        case ZSStatusCode.URL_ERROR:
                            callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
                            return;
                        case ZSStatusCode.NETWORK_ERROR:
                            callBack.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG);
                            return;
                        case ZSStatusCode.SUCCESS:
                            // 处理结果
                            // JSONObject data =
                            // getDataFromJSONObject(new
                            // String(Base64.decode(result)),
                            // callBack);
                            JSONObject data = getDataFromJSONObject(getDataFromResult(result), callBack);
                            ;
                            if (data == null) {
                                return;
                            }
                            ZSSDK.getDefault().setAuthCode(data.getString(Constants.SID));
                            callBack.callBack(ZSStatusCode.SUCCESS, data.toString());
                            break;
                        default:
                            SdkUtils.LogD("callback:" + code, msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * 是否有密码
     *
     * @param requestManager
     * @param phone
     * @param callBack
     */
    public void hasVer(RequestManager requestManager, final String phone, final NextCallBack callBack) {
        Map<String, String> header = new HashMap<String, String>();
        header.put("token", SdkUtils.getToken());
        header.put("lastAccessTime", SdkUtils.getLastAccessTime());
        header.put("appId", getCp().getAppId());
        header.put("deviceInfo", SdkUtils.getDevicesInfo(context));
        header.put("osType", "0");

        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("appId", getCp().getAppId()));
        jsonParams.addParams(new Parameter("mobile", phone));
        jsonParams.addParams(new Parameter("sid", ZSSDK.getDefault().getAuthCode()));// ????
        jsonParams.addParams(new Parameter("sign", getSign(jsonParams.getParams())));
        List<RequestParameter> params = new ArrayList<RequestParameter>();
        params.add(new RequestParameter("reqData", encode(Constants.ZS_PUBLIC_KEY, jsonParams.toJson())));

        DefaultThreadPool.getInstance().execute(requestManager.createRequestWithHead(header, Constants.POST, Constants.BASE_URL + Constants.AUTH_HAS_VER, params, new HttpCallBack() {
            @Override
            public void callBack(int code, String msg, String result) {
                // TODO Auto-generated method stub
                try {
                    switch (code) {
                        case ZSStatusCode.URL_ERROR:
                            callBack.callBack(ZSStatusCode.URL_ERROR, ZSStatusCode.URL_ERROR_MSG);
                            return;
                        case ZSStatusCode.NETWORK_ERROR:
                            callBack.callBack(ZSStatusCode.NETWORK_ERROR, ZSStatusCode.NETWORK_ERROR_MSG);
                            return;
                        case ZSStatusCode.SUCCESS:
                            // 处理结果
                            JSONObject data = getDataFromJSONObject(getDataFromResult(result), callBack);
                            ;

                            // JSONObject data =
                            // getDataFromJSONObject(new
                            // String(Base64.decode(result)),
                            // callBack);
                            if (data == null) {
                                return;
                            }
                            ZSSDK.getDefault().setAuthCode(data.getString(Constants.SID));
                            Log.e("处理结果", data.toString());
                            callBack.callBack(ZSStatusCode.SUCCESS, data.toString());
                            break;
                        default:
                            SdkUtils.LogD("callback:" + code, msg);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * 生成sid
     *
     * @param params
     * @return
     */
    private String getSign(List<Parameter> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (Parameter parameter : params) {
            map.put(parameter.getKey(), parameter.getValue());
        }
        String paramsString = ReqUtil.createLinkString(ReqUtil.paraFilter(map)) + "&" + getCp().getAppKey();

        //		String paramsString = "";
        //		for (Parameter parameter : params) {
        //			paramsString += parameter.getKey() + "=" + parameter.getValue()
        //					+ "&";
        //		}
        //		paramsString += getCp().getAppKey();

        Log.e("getSign : ", paramsString);
        try {
            return Tools.md5Encrypt(paramsString);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String encode(String key, String value) {
        try {
            PublicKey publicKey = getPublicKey(key);
            // return encrypt(value,publicKey);
            return encryptByPublicKey(value.getBytes(), publicKey);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private String encrypt(String str, PublicKey mPublicKey) {
        String strEncrypt = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            byte[] plainText = str.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, mPublicKey);
            byte[] enBytes = cipher.doFinal(plainText);
            strEncrypt = Base64.encode(enBytes);
            strEncrypt = URLEncoder.encode(strEncrypt, "UTF-8");
            strEncrypt = strEncrypt.replace("+", "%2B");
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return strEncrypt;
    }

    private String decrypt(String encString, PublicKey mPublicKey) {
        if (encString == null) {
            return null;
        }

        Cipher cipher = null;
        String strDecrypt = null;
        try {
            cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, mPublicKey);

            byte[] deBytes = cipher.doFinal(Base64.decode(encString));
            strDecrypt = new String(deBytes, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strDecrypt;
    }

    /**
     * 获取公钥
     *
     * @param key
     * @return
     * @throws Exception
     */
    private PublicKey getPublicKey(String key) throws Exception {
        byte[] keyBytes = Base64.decode(key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 解密结果
     *
     * @param result
     */
    private String getDataFromResult(String result) {
        try {
            String string = decryptByPrivateKey(result, getPublicKey(Constants.ZS_PUBLIC_KEY));
            return string;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    private String encryptByPublicKey(byte[] data, PublicKey publicKey) throws Exception {
        // 对数据加密
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 117;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        String strEncrypt = Base64.encode(encryptedData);
        strEncrypt = URLEncoder.encode(strEncrypt, "UTF-8");
        strEncrypt = strEncrypt.replace("+", "%2B");
        return strEncrypt;
    }

    /**
     * 公钥解密
     *
     * @param encString
     * @param mPublicKey
     * @return
     * @throws Exception
     */
    private String decryptByPrivateKey(String encString, PublicKey mPublicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, mPublicKey);
        byte[] encryptedData = Base64.decode(encString);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 128) {
                cache = cipher.doFinal(encryptedData, offSet, 128);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 128;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return new String(decryptedData, "UTF-8");
    }

    /**
     * cp信息
     *
     * @return
     */
    private CPInfo getCp() {
        Object obj = NetCache.getInstance().getObjectCache(Constants.CPINFO);
        if (obj == null) {
            return null;
        }
        return (CPInfo) obj;
    }

    /**
     * 获取结果
     *
     * @return JSONObject
     * @callBack
     */
    private JSONObject getDataFromJSONObject(String jsonString, CallBack callBack) {
        try {
            if (jsonString == null) {
                return null;
            }
            JSONObject obj = new JSONObject(jsonString);
            int errorCode = obj.getInt("error");
            if (errorCode != 0) {
                if (errorCode == Constants.TOKEN_EXPIRE || errorCode == Constants.TOKEN_EXPIRE2) {// token过期
                    callBack.callBack(errorCode, obj.getString("msg"));
                    return null;
                }

                if (errorCode == Constants.SID_ERROR) {
                    callBack.callBack(errorCode, jsonString);
                    return null;
                }

                callBack.callBack(ZSStatusCode.ERROR, obj.getString("msg"));
                return null;
            }
            JSONObject data = null;
            try {
                data = obj.getJSONObject("data");
            } catch (JSONException e) {
                return null;
            }
            if (data == null) {
                return null;
            }
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateContext(Context walletContext) {
        this.walletContext = walletContext;
    }
}
