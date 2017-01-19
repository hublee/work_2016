package com.zeustel.cp.views;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zeustel.cp.ZSSDK;
import com.zeustel.cp.ZSStatusCode;
import com.zeustel.cp.intf.NextCallBack;
import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.Tools;


/**
 * Created by Do on 2016/2/24.
 */
public class Check extends CommonPopView{
    private TextView submit;
    private EditText phoneNoEditText;
    private EditText checkCodeText;
    private TextView sendCodeTextView;

    private Timer timer;
    private TimerTask timerTask;

    private int leftTime = 0;

    public Check(Context context){
        super(context);
        addView();
    }

    private void addView(){
        inflate(getContext(), Tools.getResourse(getContext(), "layout", "view_check"),layout);

        submit = (TextView)findViewById(Tools.getResourse(getContext(),"id","check_submit"));
        phoneNoEditText = (EditText)findViewById(Tools.getResourse(getContext(),"id","check_phone"));
        checkCodeText = (EditText)findViewById(Tools.getResourse(getContext(),"id","check_code"));
        sendCodeTextView = (TextView)findViewById(Tools.getResourse(getContext(),"id","check_sendcode"));

        submit.setOnClickListener(this);
        sendCodeTextView.setOnClickListener(this);

        initData();
    }

    private void initData(){
//        setTitle(getResources().getString(Tools.getResourse(getContext(),"string","check_title")));
    	setCheckViewTopImg();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        System.out.println("onClick   id");
        if(id == Tools.getResourse(getContext(),"id","check_submit")) {
            submit();
        }else if(id == Tools.getResourse(getContext(),"id","check_sendcode")){
            sendCode();
        }
    }

//    @Override
//    public void back() {
//        super.back();
////        iView.addView(Constants.EXCHANGE_VIEWID);
//        close();
//    }

    private void submit(){
    	
    	  System.out.println("submit");
    	  
    	  
        if(TextUtils.isEmpty(phoneNoEditText.getText().toString())){
            Tools.tips(getContext(),"请输入手机号");
            phoneNoEditText.requestFocus();
            return;
        }
        
        if(!SdkUtils.checkPhoneno(phoneNoEditText.getText().toString())){
        	Tools.tips(getContext(),"手机号码不正确，请重新输入！");
        	phoneNoEditText.requestFocus();
        	return;
        }

        if(TextUtils.isEmpty(checkCodeText.getText().toString())){
            Tools.tips(getContext(),"请输入验证码");
            checkCodeText.requestFocus();
            return;
        }
        
        
        submit.setClickable(false);
        System.out.println("验证");
        ZSSDK.getDefault().checkCode(phoneNoEditText.getText().toString(),checkCodeText.getText().toString(),new NextCallBack() {	
			@Override
			public void callBack(int code,String msg) {
				// TODO Auto-generated method stub
				System.out.println("code : "+code);
				if(code==ZSStatusCode.SUCCESS){
					mHandler.sendEmptyMessage(4);
				}else{
					Message message = new Message();
					message.what = 6;
					message.obj = msg;
					mHandler.sendMessage(message);
				}
			}
		});
        
        
        
    }

    private void sendCode(){
        if(leftTime>0){
            return;
        }
        
        if(!SdkUtils.checkPhoneno(phoneNoEditText.getText().toString())){
        	Tools.tips(getContext(),"手机号码不正确，请重新输入！");
        	phoneNoEditText.requestFocus();
        	return;
        }
        
        sendCodeTextView.setClickable(false);
        
        sendSms(phoneNoEditText.getText().toString());
    }
    
    private void sendSms(final String text){
    	 ZSSDK.getDefault().sendSMS(text,new NextCallBack() {	
 			@Override
 			public void callBack(int code,String msg) {
 				// TODO Auto-generated method stub
 				if(code==ZSStatusCode.SUCCESS){
 					mHandler.sendEmptyMessage(3);
 				}else{
 					if(code == Constants.SID_ERROR){//sid非法，保存新的sid,重新发验证码
 						try {
 							JSONObject json = new JSONObject(msg);
 							JSONObject data = json.getJSONObject("data");
 							if(data!=null){
 								String str = data.getString(Constants.SID);
 								ZSSDK.getDefault().setAuthCode(str);
 								sendSms(text);
 							}
 						} catch (JSONException e) {
 							// TODO Auto-generated catch block
 							e.printStackTrace();
 						}
 						return;
 					}
 					
 					Message message = new Message();
 					message.what = 5;
 					message.obj = msg;
 					mHandler.sendMessage(message);
 				}
 			}
 		});
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                	
                    sendCodeTextView.setText("("+leftTime + "秒)重发");
                    break;
                case 2:
                    sendCodeTextView.setText("获取验证码");
                    sendCodeTextView.setClickable(true);
//                    sendCodeTextView.setBackgroundDrawable(getContext().getResources().getDrawable(Tools.getResourse(getContext(), "drawable", "common_btn")));
                    break;
                case 3:
                	Tools.tips(getContext(),"验证码已发送！");
					startTimer();
//					sendCodeTextView.setBackgroundDrawable(getContext().getResources().getDrawable(Tools.getResourse(getContext(), "drawable", "common_btn_enable")));
					break;
                case 4:
//                	iView.addView(Constants.EXCHANGE_VIEWID);
                	Tools.tips(getContext(), "验证成功！");
                    close();
                    break;
                case 5:
                	Tools.tips(getContext(), msg.obj.toString());
                	sendCodeTextView.setClickable(false);
                	break;
                case 6:
                	Tools.tips(getContext(), msg.obj.toString());
                	submit.setClickable(true);
                	break;
            }
        }
    };

    /**
     * 启动定时器
     */
    private void startTimer(){
        if(timer != null){
            timer.cancel();
        }
        if(timerTask!=null){
            timerTask.cancel();
        }
        timer = new Timer();
        leftTime = 60;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(leftTime>0){
                    leftTime--;
                    mHandler.sendEmptyMessage(1);
                }else{
                    mHandler.sendEmptyMessage(2);
                }
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

    


}
