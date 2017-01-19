package com.zeustel.cp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeustel.cp.ZSSDK;
import com.zeustel.cp.bean.CPInfo;
import com.zeustel.cp.bean.JSLogic;
import com.zeustel.cp.intf.IJs;
import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.NetCache;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("SetJavaScriptEnabled")
public class UserInfoActivity extends Activity implements IJs{
	private static WebView webView;
	private Map<String,String> head;
	private AlertDialog dialog;//弹窗
	
	private ArrayList<String> loadHistoryUrls = new ArrayList<String>();

	
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
		
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        
        JSLogic logic = new JSLogic(this, this);
        webView.addJavascriptInterface(logic, "Logic");
        
        head = new HashMap<String,String>();
        head.put("token",SdkUtils.getToken());
        head.put("baseurl",Constants.BASE_URL);
        head.put("appId", getCp().getAppId());
        head.put("deviceInfo", SdkUtils.getDevicesInfo(this));
        head.put("osType", "0");//安卓
        String url = Constants.BASE_URL_M +"index.jsp";
        webView.loadUrl(url,head);
        //设置可以弹窗
        webView.setWebChromeClient(new WebChromeClient(){
        	@Override public boolean onJsAlert(WebView view,String url,String message,JsResult result){
        		createDialog(message);
        		return true;
        	}
        });
        
        webView.setWebViewClient(new WebViewClient(){
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		// TODO Auto-generated method stub
        		view.loadUrl(url);
        		addHistory(url);
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
        if (keyCode == KeyEvent.KEYCODE_BACK) {       
            loadHistory();   
            return true;       
        }       
        return super.onKeyDown(keyCode, event);       
    }

	@Override
	public void back() {
		// TODO Auto-generated method stub
		mHandler.sendEmptyMessage(2);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		this.finish();
	}

	@Override
	public void copy(String val) {

	}


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
		case Constants.TOREFRESH:
			toRefresh();
			break;
		case Constants.TOALERT:
			showMessage(msg);
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
		ZSSDK.getDefault().reCheck();
		finish();
	}
	
	/**
	 * 刷新
	 */
	private void toRefresh(){
		//webView.reload();
	}
	
	
	private void createDialog(String message){
		Tools.tips(this, message);
	}


	public void showMessage(String titleAndMsg) {
		if(titleAndMsg == null || titleAndMsg == ""){
			return;
		}
		String title = titleAndMsg.split("&&")[0];
		String content = titleAndMsg.split("&&")[1];
		final String jsFunction = titleAndMsg.split("&&")[2];
		
		String type = titleAndMsg.split("&&")[3];
		
		dialog = new AlertDialog.Builder(this).create();
		dialog.show();
		dialog.getWindow().setContentView(Tools.getResourse(this, "layout", "view_dialog"));
		if(title!=null&&title!="")((TextView)dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_msg"))).setText(title);
		
		if(content!=null&&content!="")((TextView)dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_content"))).setText(content);
		
		dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_cancel")).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(3);
			}
		});
		
		dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_ok")).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.what = 1;
				msg.obj = jsFunction;
				mHandler.sendMessage(msg);
			}
		});
		
		if(type!=null&&type!=""){
			int flag = Integer.parseInt(type);
			ImageView tipImg = (ImageView) dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_tipsimg"));
			if(flag%3==0){
				tipImg.setVisibility(View.GONE);
			}else if(flag%3==1){
				tipImg.setImageDrawable(getResources().getDrawable(Tools.getResourse(this,"drawable","cp_sdk_success")));
			}else{
				tipImg.setImageDrawable(getResources().getDrawable(Tools.getResourse(this,"drawable","cp_sdk_error")));
			}
			int topImgFlag = Integer.parseInt(titleAndMsg.split("&&")[4]);
			
			ImageView topImg = (ImageView) dialog.getWindow().findViewById(Tools.getResourse(this, "id", "dialog_topimg"));
			if(topImgFlag%3==0){
				topImg.setImageResource(R.drawable.cp_sdk_dialog_top_card);
			}else if(topImgFlag%3==1){
				topImg.setImageResource(R.drawable.cp_sdk_exchange_top);
			}else{
				topImg.setImageResource(R.drawable.cp_sdk_dialog_top_addr);
			}
		}

	}
	
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				webView.loadUrl("javascript:"+msg.obj.toString());
				dialog.dismiss();
				return;
			}
			
			if(msg.what == 2){
				loadHistory();
			}
			
			if(msg.what == 3){
				dialog.dismiss();
			}
		};
	};
	
	/**
	 * 将过滤后的url加入历史
	 * @param url
	 */
	private void addHistory(String url){
		if(url.contains("type=")||url.contains("result")){//过滤掉提交页面
			return;
		}
		if(loadHistoryUrls.contains(url)){
			return;
		}
		loadHistoryUrls.add(url);
	}
	
	/**
	 * 手动加载历史
	 */
	private void loadHistory(){
		int size = loadHistoryUrls.size();
		if(size>1){
			loadHistoryUrls.remove(loadHistoryUrls.size()-1);
//			Log.e("history", loadHistoryUrls.get(loadHistoryUrls.size()-1));
			webView.loadUrl(loadHistoryUrls.get(loadHistoryUrls.size()-1));
		}else{
//			String his = loadHistoryUrls.get(loadHistoryUrls.size()-1);
//			if(his.contains("changecard.jsp")||his.contains("addcoinresult.jsp")||his.contains("buycardresult.jsp")){
//				webView.goBack();
//				webView.goBack();
//				return;
//			}
			
			if(size==1){
				webView.loadUrl(Constants.BASE_URL_M+"index.jsp?mobile="+ZSSDK.getDefault().getMobile()+"&token="+SdkUtils.getToken(),head);
				loadHistoryUrls.remove(0);
				return;
			}
			
			if(webView.canGoBack()){
				webView.goBack();
			}
			
			if(size == 0){
				close();
			}
		}
	}
	
}
