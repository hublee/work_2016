package com.zeustel.cp.views;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.zeustel.foxconn.cp_sdk.R;

/**
 * Created by Do on 2016/2/26.
 */
public class ExchangeOk extends CommonPopView{
    private Timer timer;
    private TimerTask timerTask;
    public ExchangeOk(Context context){
        super(context);
        addView();
    }

    private void addView() {
        inflate(getContext(), R.layout.view_exchangeok, layout);
     
        
        startTimer();
    }

    @Override
    public void onClick(View v) {
    	super.onClick(v);
    }
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
        timerTask = new TimerTask() {
            @Override
            public void run() {
               mHandler.sendEmptyMessage(1);
            }
        };

        timer.schedule(timerTask, 5000);
    }


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    close();
                    break;
            }
        }
    };

}
