package com.zeustel.top9.assist.analytics;

import android.content.Context;
import android.text.TextUtils;

import com.umeng.analytics.MobclickAgent;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;

/**
 * 友盟统计相关工具类
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/30 09:59
 */
public class UmengHelper implements IAnalytics{
    private Context context = null;
    private static UmengHelper mUmengHelper = null;
    private UmengHelper(Context context){
        this.context = context;
    }
    /**
     * 懒汉式 单例模式
     * 特点 实例的延迟加载
     * 由于 多线程并发访问会出现安全问题 要加同步锁
     * 同步代码块和 同步关键字都行 <低效>
     * 用双层判断 可以解决 低效 问题
     */
    public static UmengHelper getInstance(Context context){
        if(mUmengHelper == null){
            synchronized(UmengHelper.class){
                if(mUmengHelper == null)
                mUmengHelper = new UmengHelper(context);
            }
        }
        return mUmengHelper;
    }
    /**
     * 获取设备信息，用于友盟统计 集成测试
     */
    public  void getDeviceInfo() {
        org.json.JSONObject json = new org.json.JSONObject();
        try{
            String device_id = Tools.getDeviceId(context);
            String mac = Tools.getMacAddress(context);
            json.put("mac", mac);
            if( TextUtils.isEmpty(device_id) ){
                device_id = mac;
            }
            if( TextUtils.isEmpty(device_id) ){
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
        }catch(Exception e){
            e.printStackTrace();
        }
        Tools.log_i(UmengHelper.class,"getDeviceInfo",json.toString());
    }

    @Override
    public void analyticsSDKVersion() {
        MobclickAgent.onEvent(context, EventId.ID_SDK_VERSION, Constants.SDK_VERSION);
        Tools.log_i(UmengHelper.class, "analyticsSDK_Version", Constants.SDK_VERSION);
    }

    @Override
    public void analyticsMainPage() {
        MobclickAgent.onEvent(context, EventId.ID_MAIN_PAGE);
        Tools.log_i(UmengHelper.class,"analyticsMainPage");
    }

    @Override
    public void analyticsNavigationPage() {
        MobclickAgent.onEvent(context, EventId.ID_NAVIGATION_PAGE);
        Tools.log_i(UmengHelper.class,"analyticsNavigationPage");
    }

    @Override
    public void analyticsGameDownload(String gameName) {
        MobclickAgent.onEvent(context, EventId.ID_GAME_DOWNLOAD,EventId.getGameParams(gameName));
        Tools.log_i(UmengHelper.class, "analyticsGameDownload", "gameName : " + gameName);
    }

    @Override
    public void analyticsGameInstall(String gameName) {
        MobclickAgent.onEvent(context, EventId.ID_GAME_INSTALL,EventId.getGameParams(gameName));
        Tools.log_i(UmengHelper.class, "analyticsGameInstall", "gameName : " + gameName);
    }
}
