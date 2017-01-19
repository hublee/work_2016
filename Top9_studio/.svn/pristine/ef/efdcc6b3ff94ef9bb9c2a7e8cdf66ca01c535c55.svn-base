package com.zeustel.top9.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.io.File;

/**
 * 字体帮助类
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/19 11:25
 */
public class FontHelper {
    private static FontHelper mTypefaceHelper = null;
    private Typeface typeface = null;
    private Context context = null;

    private FontHelper(Context context) {
        this.context = context;
        if (typeface == null) {
//            typeface = Typeface.createFromAsset(this.context.getAssets(), "fonts" + File.separator + "mini.TTF");
            typeface = Typeface.createFromAsset(this.context.getAssets(), "fonts" + File.separator + "BellMT-Italic.otf");
        }
    }

    /**
     * 懒汉式 单例模式
     * 特点 实例的延迟加载
     * 由于 多线程并发访问会出现安全问题 要加同步锁
     * 同步代码块和 同步关键字都行 <低效>
     * 用双层判断 可以解决 低效 问题
     */
    public static FontHelper getInstance(Context context) {
        if (mTypefaceHelper == null) {
            synchronized (FontHelper.class) {
                if (mTypefaceHelper == null)
                    mTypefaceHelper = new FontHelper(context);
            }
        }
        return mTypefaceHelper;
    }

    public Typeface getTypeface() {
        return typeface;
    }
}
