package com.zeustel.top9.assist.analytics;

import com.zeustel.top9.base.WrapNotFrament;

import java.util.HashMap;

/**
 * 自定义统计项
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/30 10:15
 */
public interface IAnalytics {
    class EventId {
        //系统版本
        @Deprecated
        protected static final String ID_SDK_VERSION = "sdk_version";
        //主页访问量
        protected static final String ID_MAIN_PAGE = "mainPage";
        //导航页访问量
        @Deprecated
        protected static final String ID_NAVIGATION_PAGE = "navigationPage";
        //游戏下载量
        protected static final String ID_GAME_DOWNLOAD = "gameDownload";
        //游戏安装量
        protected static final String ID_GAME_INSTALL = "gameInstall";
        //key 游戏名称
        protected static final String KEY_GAME_NAME = "gameName";

        /**
         * 获取游戏相关统计参数
         * @param gameName 游戏名称
         * @return HashMap
         */
        protected static HashMap<String,String> getGameParams(String gameName) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put(KEY_GAME_NAME, gameName);
            return params;
        }
    }

    /**
     * 统计系统版本
     *  @Deprecated Use Umeng default method
     */
    @Deprecated
    void analyticsSDKVersion();

    /**
     * 统计主页访问量
     */
    void analyticsMainPage();

    /**
     * 统计导航页访问量
     *
     * @deprecated Use extends {@link WrapNotFrament} instead.
     */
    @Deprecated
    void analyticsNavigationPage();

    /**
     * 统计游戏下载量
     * @param gameName 游戏名称
     */
    void analyticsGameDownload(String gameName);

    /**
     * 统计游戏安装量
     * @param gameName 游戏名称
     */
    void analyticsGameInstall(String gameName);
}
