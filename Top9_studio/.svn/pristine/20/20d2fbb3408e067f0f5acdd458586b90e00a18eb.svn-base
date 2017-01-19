package com.zeustel.top9.bean;

import android.content.res.Resources;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.zeustel.top9.R;

/**
 * 游戏评测
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/24 15:45
 */
@Table(name = "GameEvaluating")
public class GameEvaluating extends HtmlPaper {
    //游戏名称 add
    @Column(column = "gameName")
    private String gameName;
    //横幅图片
    @Column(column = "banner")
    private String banner;
    //评测类型
    @Column(column = "evaluatingType")
    private int evaluatingType = EvaluatingType.OFFLINE_GAME;
    //apk路径
    @Column(column = "apkUrl")
    private String apkUrl;
    //视频地址
    @Column(column = "videoUrl")
    private String videoUrl;
    //视频截图
    @Column(column = "videoIcon")
    private String videoIcon;


    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public int getEvaluatingType() {
        return evaluatingType;
    }

    public void setEvaluatingType(int evaluatingType) {
        if (GameEvaluating.EvaluatingType.OFFLINE_GAME >= evaluatingType) {
            this.evaluatingType = GameEvaluating.EvaluatingType.OFFLINE_GAME;
        } else if (GameEvaluating.EvaluatingType.HAND_GAME <= evaluatingType) {
            this.evaluatingType = GameEvaluating.EvaluatingType.HAND_GAME;
        } else {
            this.evaluatingType = evaluatingType;
        }
    }

    public String getVideoIcon() {
        return videoIcon;
    }

    public void setVideoIcon(String videoIcon) {
        this.videoIcon = videoIcon;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public static class EvaluatingType {
        //单机
        public static final int OFFLINE_GAME = 0;
        //网游
        public static final int ONLINE_GAME = 1;
        //手游
        public static final int HAND_GAME = 2;

        /**
         * 根据游戏类型返回对应名称
         *
         * @param res  Resources
         * @param type 游戏类型
         * @return 对应名称
         */
        public static String getName(Resources res, int type) {
            if (GameEvaluating.EvaluatingType.OFFLINE_GAME >= type) {
                return res.getString(R.string.title_home_offline);
            } else if (GameEvaluating.EvaluatingType.ONLINE_GAME == type) {
                return res.getString(R.string.title_home_online);
            } else {
                return res.getString(R.string.title_home_hand);
            }
        }
        /**
         * 根据游戏类型返回对应名称
         *
         * @param res  Resources
         * @param type 游戏类型
         * @return 对应名称
         */
        public static String getNameEN(Resources res, int type) {
            if (GameEvaluating.EvaluatingType.OFFLINE_GAME >= type) {
                return "offline";
            } else if (GameEvaluating.EvaluatingType.ONLINE_GAME == type) {
                return "online";
            } else {
                return "hand";
            }
        }
    }

}
