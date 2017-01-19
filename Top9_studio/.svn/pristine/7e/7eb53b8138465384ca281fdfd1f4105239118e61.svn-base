package com.zeustel.top9.fm;

import android.content.Context;

import com.zeustel.net.NetSDK;
import com.zeustel.net.event.EnterRoomResultEvent;
import com.zeustel.net.event.HandOkEvent;
import com.zeustel.net.event.LoginResultEvent;
import com.zeustel.top9.utils.Tools;

import de.greenrobot.event.EventBus;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/12/1 20:10
 */
public class FMHelper {
    public static final NetSDK netSDK = new NetSDK();
    private Context context = null;
    private static FMHelper mFMHelper = null;
    private static final String FM_HOST = "115.29.5.34";
    private static final String TEST_ACCOUNT_AND_PASSWORD = "qwe";
    private static final int TEST_ROOM_ID = 769;

    private FMHelper(Context context) {
        this.context = context;
        netSDK.setHost(FM_HOST);
        netSDK.initSDK(context);
        EventBus.getDefault().register(this);
    }

    /**
     * 懒汉式 单例模式
     * 特点 实例的延迟加载
     * 由于 多线程并发访问会出现安全问题 要加同步锁
     * 同步代码块和 同步关键字都行 <低效>
     * 用双层判断 可以解决 低效 问题
     */
    public static FMHelper getInstance(Context context) {
        if (mFMHelper == null) {
            synchronized (FMHelper.class) {
                if (mFMHelper == null)
                    mFMHelper = new FMHelper(context);
            }
        }
        return mFMHelper;
    }

    //电台握手成功事件
    public void onEvent(HandOkEvent event) {
        Tools.log_i(FMHelper.class, "onEvent", "fm hand success ");
        netSDK.userLogin(TEST_ACCOUNT_AND_PASSWORD, TEST_ACCOUNT_AND_PASSWORD);
    }

    //登录成功事件
    public void onEvent(LoginResultEvent event) {
        if (event.isLogin()) {
            Tools.log_i(FMHelper.class, "onEvent", "fm login success ");
            netSDK.userEnterRoom(TEST_ROOM_ID);
        }
    }

    //登录房间成功事件
    public void onEvent(EnterRoomResultEvent event) {
        if (event.isEntered()) {
            Tools.log_i(FMHelper.class, "onEvent", "fm entry success .");
            //            netSDK.mic(true);
        }
    }
}
