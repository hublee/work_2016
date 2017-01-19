package com.zeustel.cp.utils;

import android.content.Context;
import android.util.Log;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/7/8 09:19
 */
public class UserManager {
    private Context context = null;
    private static UserManager mUserManager = null;
    private static boolean isOnline;
    private UserManager(Context context){
        this.context = context;
    }
    /**
     * 懒汉式 单例模式
     * 特点 实例的延迟加载
     * 由于 多线程并发访问会出现安全问题 要加同步锁
     * 同步代码块和 同步关键字都行 <低效>
     * 用双层判断 可以解决 低效 问题
     */
    public static UserManager getInstance(Context context){
        if(mUserManager == null){
            synchronized(UserManager.class){
                if(mUserManager == null)
                mUserManager = new UserManager(context);
            }
        }
        return mUserManager;
    }

    public boolean isOnline() {
        if (SdkUtils.getToken() == null || SdkUtils.getToken() == "") {
            Log.i("isOnline",  "isOnline  getToken (line 38): false");
            return false;
        }
            Log.i("isOnline",  "isOnline isOnline (line 11111): "+ String.valueOf(isOnline));
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
        Log.i("setOnline",  "setOnline (line 38): "+ String.valueOf(online));
    }


}
