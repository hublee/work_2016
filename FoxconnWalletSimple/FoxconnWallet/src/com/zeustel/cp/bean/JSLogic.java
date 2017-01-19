package com.zeustel.cp.bean;

import android.content.Context;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.zeustel.cp.intf.IJs;

public class JSLogic {
	private Context context;
	
	private IJs iJs;
	
    public JSLogic(Context context,IJs iJs){
        this.context = context;
        this.iJs = iJs;
    }

    @JavascriptInterface
    public void showMsg(String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    
    @JavascriptInterface
    public void showMsg(int code,String msg){
        iJs.deal(code, msg);
    }

    @JavascriptInterface
    public void close(){
        iJs.close();
    }
    
    @JavascriptInterface
    public void back(){
        if (context != null) {
            new Handler(context.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    iJs.back();
                }
            });
        }

    }
    @JavascriptInterface
    public void copy(String val){
        iJs.copy(val);
    }

    @JavascriptInterface
    public void pay(){
        iJs.pay();
    }
}
