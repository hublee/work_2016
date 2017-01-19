package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2015/10/13 0013.
 */
public class ExampleService  extends Service{

    private static final int  COOLSZY = 1;
    private static final int KUKA = 2;

    private static final String TAG = "ExampleService";

    private ServiceHandler handler;

    private class ServiceHandler extends Handler{
        public ServiceHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case COOLSZY :
                    Log.i(TAG,"handlerMessage-->> " + msg.obj);
                    break;
                case KUKA:
                    Log.i(TAG,"handlerMessage-》》 " + msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "ExampleService-->onCreate");
        Looper looper = Looper.getMainLooper();
        handler = new ServiceHandler(looper);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"ExampleService-->onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"ExampleService-->onStartCommand");
        Message msg = handler.obtainMessage();
        msg.what = KUKA;
        msg.arg1 = startId;
        msg.obj = "kuka";
        handler.sendMessage(msg);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
