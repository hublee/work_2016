package com.zeustel.foxconnwalletsimple;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zeustel.cp.ZSSDK;
import com.zeustel.cp.ZSStatusCode;
import com.zeustel.cp.bean.JSLogic;
import com.zeustel.cp.intf.CPCallBack;
import com.zeustel.cp.intf.CallBack;
import com.zeustel.cp.intf.ExchangeCallBack;
import com.zeustel.cp.intf.HttpCallBack;
import com.zeustel.cp.intf.IJs;
import com.zeustel.cp.net.DefaultThreadPool;
import com.zeustel.cp.net.JsonParams;
import com.zeustel.cp.net.Parameter;
import com.zeustel.cp.net.RequestManager;
import com.zeustel.cp.net.RequestParameter;
import com.zeustel.cp.paybill.PaybillActivity;
import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity implements ExchangeCallBack{
    public static final String TAG = "TestCP2";
    public String authCode;

    private final String APPID = "d4b62e61dc8b4c9d9d25a63c9a92e9c1";
    private final static String APPKEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIsbM7dmWqPMe4TpFEFXOILOZmUzEePweszDnS+HhzI4lA7+QPkcJzYyJZFgFa5FRPwlyV0KdcHXAWGOfjAMmtNSZytBTIaef8t+2lLApNoBx85YfwB82arWKj5NWJWfTEYVPiVeVyk6034kOHCQtinZm8TNjgSHmOjny4TNbTklAgMBAAECgYBgjRDoSvK+nj9dkSmt8IwYvonz0+6m91NErMF9K+R6xyp8/Zy7Sv8sbsIElVx3CT1IhjGtkoUJdeJPhEexsg4DePT7ndeixSuM9xfcfP/7xsN6xtTd5+iypMnHp7zk+kGb5KJGpy2gjMfx8Z0XbaaNVZzAopAxPhD9xJviI558QQJBAMZVAgWpmd38Bk/392m8W7OBEYOsKApybxr6ZBNZRSn/AMkNVmXHj2sQ7SRA11qSSVVIW8b5pzHjs0rfR7pJsAsCQQCzja2VkuSqdoWBhN6CPXJ4yDb3Ig8fbxkT5u94A2KEZHfHNBOfZD7Oiqe6RvfKAbAYCLopJwcUrqjXCbxP6omPAkEAgVrATuEnMdlGZLHd9GCdjp4HxlFqbCXmwTm+O8Blk9Z1tKu2Rfu6VqJbiOGhk7kFi1H+SfBaU/em4JZwOdgFsQJBAK1d/CAzEGVjQS84ytu4VwI2V5Qxp6ZzUCR+U/RuWD56/4gxSU0aruYgzSi4g2LiVFQwgQdhLp4SsuXG+bO0QxMCQA/trDlJggNoK7VaC4i+yWIwdj6EPOVV1eqDFCVivRwAkmCDCqnkS2Tg8FsJTaAaJAzdfNB/fvdfmtTwQPcBqOc=";
    //正式
    //	private final static String EX_URL = "http://sdkapi.zeustel.com/sapi/integral/exchange/outer/v1";
    //测试
    private final static String EX_URL = "http://115.29.5.34:8088/sdk/sapi/integral/exchange/outer/v1";
    //"http://10.1.1.226:93/sapi/integral/exchange";//"http://115.29.5.34:8088/sdk/sapi/integral/exchange";

    private String mobile;
    private int amount;
    private String sid;

    @Override
    protected void onDestroy() {
        ZSSDK.getDefault().loginOut();
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_main);

        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        ZSSDK.getDefault().initSDK(this,false,APPID,APPKEY,new CPCallBack() {
            @Override
            public void callBack(int code, String msg) {
                // TODO Auto-generated method stub
                switch(code){
                    case ZSStatusCode.CP_INFO_ERROR:
                        Log.e(TAG, msg);
                        return;
                    case ZSStatusCode.SUCCESS://可进行下面操作
                        authCode = msg;
                        Log.e("授权码", msg);
                        ZSSDK.getDefault().setTotal(9999);

                        break;
                    case ZSStatusCode.ERROR:
                        Message message = new Message();
                        message.what = 1;
                        message.obj = msg;
                        handler.sendMessage(message);
                        break;
                    default:
                        Log.e(TAG, msg);
                        break;
                }
            }
        });





        ZSSDK.getDefault().registExchangeCallBack(this);

    }


    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch(msg.what){
                case 1:
                    Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                case 3:
                    hideTips();
                    Toast.makeText(MainActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    hideTips();
                    ZSSDK.getDefault().showExchangeResult(true);
                    break;

            }
        };
    };


    @Override
    public void callBack(String mobile, int amount, String sid) {
        // TODO Auto-generated method stub
        Log.e("ExchangeCallBack", mobile+","+amount+","+sid);
        this.mobile = mobile;
        this.amount = amount;
        this.sid = sid;

        toExchange();
    }


    private void toExchange(){
        if(sid==null||sid==""){
            Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
            return;
        }
        //		ZSSDK.getDefault().showExchangeResult(true);

        showTips();

        JsonParams jsonParams = new JsonParams();
        jsonParams.addParams(new Parameter("amount",amount+""));
        jsonParams.addParams(new Parameter("appId",APPID));
        jsonParams.addParams(new Parameter("mobile","18616973375"));
        //    	jsonParams.addParams(new Parameter("sid",sid));
        List<RequestParameter> params = new ArrayList<RequestParameter>();
        params.add(new RequestParameter("sign",getSign(jsonParams)));
        params.add(new RequestParameter("reqData",jsonParams.toJson()));
        params.add(new RequestParameter("signType","0"));
        params.add(new RequestParameter("osType","0"));
        DefaultThreadPool.getInstance().execute(new RequestManager().createRequest(Constants.POST, EX_URL, params, new HttpCallBack(){
            @Override
            public void callBack(int code, String msg, String result) {
                // TODO Auto-generated method stub
                try{
                    Message message = new Message();
                    switch(code){
                        case ZSStatusCode.URL_ERROR:
                            message.what = 2;
                            message.obj = ZSStatusCode.URL_ERROR_MSG;
                            return;
                        case ZSStatusCode.NETWORK_ERROR:
                            message.what = 3;
                            message.obj = ZSStatusCode.URL_ERROR_MSG;
                            return;
                        case ZSStatusCode.SUCCESS:
                            //处理结果
                            getDataFromJSONObject(result, new CallBack() {
                                @Override
                                public void callBack(int code, String msg) {
                                    // TODO Auto-generated method stub
                                    Log.e("EXCHANGE RESULT", msg);
                                    Message message1 = new Message();
                                    message1.what = 3;
                                    message1.obj = msg;
                                    handler.sendMessage(message1);
                                }
                            });


                            break;
                        default:
                            Log.e("callback:"+code, msg);
                            break;
                    }

                    handler.sendMessage(message);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }));
    }

    private String getSign(JsonParams params){
        String data = "";
        for(Parameter parameter:params.getParams()){
            data += parameter.getKey() +"="+ parameter.getValue()+"&";
        }
        data += "0ef818eefcd00d17625c3b585716168b";

        try {
            return Tools.md5Encrypt(data);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    private AlertDialog dialog;
    private void showTips(){
        if(dialog!=null && dialog.isShowing()){
            return;
        }
        dialog = new AlertDialog.Builder(this).setTitle("系统提示").setMessage("兑换中，请稍等。。。").setCancelable(true).create();
        dialog.show();
    }
    private void hideTips(){
        if(dialog.isShowing()){
            dialog.cancel();
        }
    }

    private void getDataFromJSONObject(String jsonString,CallBack callBack){
        try{
            if(jsonString==null){
                return;
            }
            JSONObject obj = new JSONObject(jsonString);
            int errorCode = obj.getInt("error");
            if(errorCode!=0){
                callBack.callBack(ZSStatusCode.ERROR, obj.getString("msg"));
                return;
            }

            handler.sendEmptyMessage(4);

        }catch(JSONException e){
            e.printStackTrace();
        }
        return;
    }
}
