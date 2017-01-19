package com.zeustel.cp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zeustel.cp.ZSSDK;
import com.zeustel.cp.bean.CPInfo;
import com.zeustel.cp.bean.JSLogic;
import com.zeustel.cp.intf.IJs;
import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.NetCache;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.foxconn.cp_sdk.R;

import java.util.HashMap;

@SuppressLint("SetJavaScriptEnabled")
public class ForumActivity extends Activity implements IJs{
	private static WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		setContentView(R.layout.view_userinfo);
		
		initView();
	}

	private void initView(){
		// TODO Auto-generated method stub
		webView = (WebView)findViewById(R.id.webview);
		
		//webview可以执行js
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        
        JSLogic logic = new JSLogic(this, this);
        webView.addJavascriptInterface(logic, "Logic");
        
        HashMap<String,String>  head = new HashMap<String,String>();
        //head.put("mobile", ZSSDK.getDefault().getMobile());
        head.put("token",SdkUtils.getToken());
        //head.put("appkey",getCp().getAppKey());
        head.put("baseurl",Constants.BASE_URL);
        head.put("appId", getCp().getAppId());
        head.put("deviceInfo", SdkUtils.getDevicesInfo(this));
        head.put("osType", "0");//安卓
     
        webView.loadUrl(Constants.BASE_URL_M,head);
   
        webView.setWebViewClient(new WebViewClient(){
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		// TODO Auto-generated method stub
        		view.loadUrl(url);
        		return true;
        	}
        	
        	@Override
        	public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
        		// TODO Auto-generated method stub
        		return super.shouldOverrideKeyEvent(view, event);
        	}
        	
        });
        
	}

	 /**
     * cp信息
     * @return
     */
    private CPInfo getCp(){
    	Object obj = NetCache.getInstance().getObjectCache(Constants.CPINFO);
    	if(obj == null){
    		return null;
    	}
    	return (CPInfo)obj;
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {       
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {       
            webView.goBack();       
            return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }

	@Override
	public void back() {
		// TODO Auto-generated method stub
		handler.sendEmptyMessage(1);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		this.finish();
	}

	@Override
	public void copy(String val) {

	}

	private static Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				webView.goBack();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void deal(int code, String msg) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "code:"+code+",msg:"+msg, Toast.LENGTH_SHORT).show();
		
		switch(code){
		case Constants.TOSHOP:
			toShop();
			break;
		case Constants.TOLOGIN:
			toLogin();
			break;
		}
	}

	@Override
	public void pay() {

	}

	/**
	 * 跳转到商城
	 */
	private void toShop(){
		SdkUtils.openApp(this, Constants.PACKAGE_NAME_JFT);
	}
	
	/**
	 * 清除数据，登录页面
	 */
	private void toLogin(){
		ZSSDK.getDefault().logOut();
		ZSSDK.getDefault().addView(Constants.CHECK_VIEWID);
		finish();
	}
	
	
}
