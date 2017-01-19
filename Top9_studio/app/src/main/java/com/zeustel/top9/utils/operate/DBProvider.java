package com.zeustel.top9.utils.operate;

import android.content.Context;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/28 16:00
 */
@Deprecated
public class DBProvider {
    private SQLiteHelper sqLiteHelper;
    private Context context = null;
    private static DBProvider mDBProvider = null;
    private DBProvider(Context context){
        this.context = context;
        sqLiteHelper = new SQLiteHelper(context);
    }
    /**
     * 懒汉式 单例模式
     * 特点 实例的延迟加载
     * 由于 多线程并发访问会出现安全问题 要加同步锁
     * 同步代码块和 同步关键字都行 <低效>
     * 用双层判断 可以解决 低效 问题
     */
    public static DBProvider getInstance(Context context){
        if(mDBProvider == null){
            synchronized(DBProvider.class){
                if(mDBProvider == null)
                mDBProvider = new DBProvider(context);
            }
        }
        return mDBProvider;
    }
    // ===================== user start ===================

    // ===================== user end ===================
}
