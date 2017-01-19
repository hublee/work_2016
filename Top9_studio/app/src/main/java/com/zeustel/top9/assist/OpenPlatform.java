package com.zeustel.top9.assist;

import android.content.Context;

import com.gotye.api.voichannel.VoiChannelAPI;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import cn.sharesdk.framework.ShareSDK;

/**
 * 第三方开放平台帮助类
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/23 16:28
 */
public class OpenPlatform {
    /**
     * 友盟统计sdk初始化
     * @param context
     * @param debug 调试模式
     */
    public static void initUmengSDK(Context context,boolean debug){
        //        友盟sdk debug模式
        MobclickAgent.setDebugMode(debug);
        //        禁用原有页面统计
        MobclickAgent.openActivityDurationTrack(false);
        /**
         * 发送策略
         *
         * Android平台的数据发送策略有两种方式：
         * 启动时发送：APP启动时发送当次启动数据和上次的使用时长等缓存数据，当次使用过程中产生的自定义事件数据缓存在客户端，下次启动时发送
         * 按间隔发送：按特定间隔发送数据，间隔时长介于90秒与1天之间。您可以在后台自定义发送间隔。
         * 在没有取到在线配置的发送策略的情况下，会使用默认的发送策略：启动时发送。
         * 你可以在友盟后台“设置->发送策略”页面自定义数据发送的频率。
         *
         * 注意：打开调试模式（debug）后，在集成测试模式中，数据实时发送，不受发送策略控制
         */
        MobclickAgent.updateOnlineConfig(context);
        //         日志加密设置
        AnalyticsConfig.enableEncrypt(true);
        //      MobclickAgent.setSessionContinueMillis(1000);
    }

    public static void initSharedSDK(Context context) {
        ShareSDK.initSDK(context);
    }

    public static void initGotyeSDK(Context context) {
        VoiChannelAPI api = VoiChannelAPI.getInstance();
        api.init(context, "587ac548-6e6b-443b-8629-90cb903f1382");
    }
}
