package com.example.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.util.Log;

import com.example.hlb.myapplication.MainActivity;

/**
 * Created by Administrator on 2015/10/13 0013.
 */
public class StatusService extends IntentService {

    private static final String TAG = "StatusService";
    private static final int KUKA = 0;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public StatusService(String name) {
        super("StatusService");
    }

    public StatusService() {
        super("StatusService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG,"开始下载......");
        showNotification();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"程序下载完毕");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification() {
//        Notification notification = new Notification(android.R.drawable.dark_header,"哈哈",System.currentTimeMillis());
        Notification notification = new Notification.Builder(getBaseContext()).setContentTitle("正在下载")
                .setContentText("下载中...").setSmallIcon(android.R.drawable.ic_dialog_email).build();
//        PendingIntent intent = PendingIntent.getActivity(getBaseContext(),0,0);

        notification.contentIntent = null;

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(KUKA,notification);
    }
}
