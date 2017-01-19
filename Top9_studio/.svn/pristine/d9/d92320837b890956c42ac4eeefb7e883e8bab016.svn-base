package com.zeustel.top9.utils;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

/**
 * 捕获程序崩溃异常
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/8 16:05
 */
public class UncaughtHandler implements Thread.UncaughtExceptionHandler {

    private static UncaughtHandler mUncaughtHandler = null;
    private Context context = null;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    //系统默认的UncaughtException处理类
    private UncaughtHandler(Context context) {
        this.context = context;
        //设置该CrashHandler为程序的默认处理器
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 懒汉式 单例模式
     * 特点 实例的延迟加载
     * 由于 多线程并发访问会出现安全问题 要加同步锁
     * 同步代码块和 同步关键字都行 <低效>
     * 用双层判断 可以解决 低效 问题
     */
    public static UncaughtHandler getInstance(Context context) {
        if (mUncaughtHandler == null) {
            synchronized (UncaughtHandler.class) {
                if (mUncaughtHandler == null)
                    mUncaughtHandler = new UncaughtHandler(context);
            }
        }
        return mUncaughtHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        saveLocal(ex);
        defaultUncaughtExceptionHandler.uncaughtException(thread, ex);
    }

    private void saveLocal(Throwable ex) {
        if (ex == null) {
            return;
        }
        PrintWriter mPrintWriter = null;
        try {
            File tempFile = File.createTempFile(formatter.format(System.currentTimeMillis()), ".log", Tools.getCacheDir(context));
            mPrintWriter = new PrintWriter(tempFile);
            ex.printStackTrace(mPrintWriter);
            mPrintWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (mPrintWriter != null) {
                mPrintWriter.close();
                mPrintWriter = null;
            }
        }
    }
}
