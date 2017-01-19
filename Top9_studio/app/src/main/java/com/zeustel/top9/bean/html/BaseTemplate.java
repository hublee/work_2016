package com.zeustel.top9.bean.html;

import java.io.Serializable;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/27 15:16
 */
public class BaseTemplate implements Serializable {
    private int id;
    //可以理解为评测id,资讯id
    private int bannerId;
    //   所属游戏id
    private int gameId;
    //详细html地址
    private String detailUrl;
    //    语音地址
    private String voiceUrl;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getVoiceUrl() {
        return voiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        this.voiceUrl = voiceUrl;
    }
}
