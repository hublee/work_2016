package com.zeustel.cp.utils;

import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//import android.app.AlertDialog;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler{
	private static final String CRASHSERVER = "http://10.1.1.204:8080/Crash/index.jsp";
	
	private static CrashHandler handler = new CrashHandler();
	
	private UncaughtExceptionHandler defaultHandler;
	
	private Context context;
	
	private String key;
	
	private CrashHandler(){
		
	}
	
	public static CrashHandler getInstance(){
		return handler;
	}
	
	public void init(Context context){
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		this.context = context;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		if(!handleException(ex)&&defaultHandler!=null){
			defaultHandler.uncaughtException(thread, ex);
		}else{
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	private boolean handleException(final Throwable ex){
		if(ex == null){
			return false;
		}
		
		saveCrashToDisk(ex);
		
		new Thread(){
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				Looper.loop();
			};
		}.start();
		
		sendCrashToServer(ex);
		return true;
	}
	
//	/**
//	 * 提示错误信息
//	 * @param info
//	 */
//	private void showTips(String info){
//		Log.e("异常", info);
//		new AlertDialog.Builder(context).setTitle("异常")
//		.setMessage(info)
//		.setPositiveButton("确定", null)
//		.show();
//	}
	
	/**
	 * 发送错误到服务器
	 */
	private void sendCrashToServer(Throwable ex){
		sendPost(CRASHSERVER,getPostData(key,ex.toString()));
	}
	
	/**
	 * 把本地所有日志发到服务器
	 */
	public void sendAllCrashToServer(){
		NetCache.getInstance().openCrashCache(context);
		//所有的key
		String[] keyArray = NetCache.getInstance().getCrashKeyList();
		for(String key:keyArray){
			Object obj = NetCache.getInstance().getObjectCache(key);
			sendPost(CRASHSERVER,getPostData(key,obj.toString()));
		}
	}
	

	private String getPostData(String key,String value){
		StringBuffer buffer = new StringBuffer();
		buffer.append("key="+key);
		buffer.append("&devicetype=1");//安卓
		buffer.append("&clienttype=1");//电台
		buffer.append("&appversion="+Tools.getAppVersionInfo(context));//应用版本
		buffer.append("&osversion="+android.os.Build.VERSION.SDK_INT);//系统版本
		buffer.append("&networktype="+Tools.networkType(context));//网络类型
		buffer.append("&devicemodel="+android.os.Build.MODEL);
		buffer.append("&memoryinfo="+Tools.getAvailMemory(context)+"/"+Tools.getTotalMemory(context));
		
		buffer.append("&info="+value);
		return buffer.toString();
	}
	
	private void sendPost(final String urls, final String param) {
		new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO Auto-generated method stub
                HttpURLConnection connection=null;
                 
                try {
                    URL url=new URL(urls);
                    connection =(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(param.getBytes());
                    
                    int response = connection.getResponseCode();            //获得服务器的响应码
                    if(response == HttpURLConnection.HTTP_OK) {
                    	SdkUtils.LogE("提示", "错误已发送到服务器");
                    }
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
//                    e.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
             
        }).start();
    }    
	
	/**
	 * 保存错误到本地
	 * @param ex
	 */
	private void saveCrashToDisk(Throwable ex){
		getCrashKey();
		NetCache.getInstance().openCrashCache(context);
		NetCache.getInstance().addCrash(key, ex);
		NetCache.getInstance().flushCache();
	}
	
	/**
	 * 生成唯一标识
	 * @return
	 */
	private void getCrashKey(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS",Locale.CHINA);
		key = "at"+format.format(new Date());
	}
	
}
