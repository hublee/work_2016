package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Administrator on 2015/10/13 0013.
 */
public class BinderService extends Service {

    private MyBinder binder = new MyBinder();

    private static final String TAG = "BinderService";

    public class MyBinder extends Binder {
        public BinderService getService(){
            return BinderService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return binder;
    }

    public void MyMethod(){
        Log.i(TAG,"MyMethod()");
    }
}
