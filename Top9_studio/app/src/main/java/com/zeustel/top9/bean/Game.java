package com.zeustel.top9.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 游戏
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/25 10:26
 */
@Table(name = "Game")
public class Game implements Serializable {
    @Id
    @NoAutoIncrement
    private int id;
    //30秒美女描述视频
    @Column(column = "describeUrl")
    private String describeUrl;
    //30秒美女描述视频封面
    @Column(column = "describeCover")
    private String describeCover;
    //30游戏秒游戏视频
    @Column(column = "videoUrl")
    private String videoUrl;
    //30游戏秒游戏视频封面
    @Column(column = "videoCover")
    private String videoCover;
    //游戏apk路径
    @Column(column = "apkUrl")
    private String apkUrl;
    //该游戏对应的评测id
    @Column(column = "evluatingId")
    private int evluatingId;
    //游戏对应截图 ,分割
    @Column(column = "screenshot")
    private String screenshot;
    //房间数目
    @Column(column = "roomCount")
    private int roomCount;
    //在线人数
    @Column(column = "onLineCount")
    private int onLineCount;
    //游戏简介
    @Column(column = "describe")
    private String describe;
    //游戏名称
    @Column(column = "name")
    private String name;
    //游戏英文名称
    @Column(column = "nameEn")
    private String nameEn;
    //游戏图标
    @Column(column = "banner")
    private String banner;
    //游戏小图标
    @Column(column = "icon")
    private String icon;
    @Column(column = "time")
    private long time;

    public String getDescribeCover() {
        return describeCover;
    }

    public void setDescribeCover(String describeCover) {
        this.describeCover = describeCover;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDescribeUrl() {
        return describeUrl;
    }

    public void setDescribeUrl(String describeUrl) {
        this.describeUrl = describeUrl;
    }

    public int getEvluatingId() {
        return evluatingId;
    }

    public void setEvluatingId(int evluatingId) {
        this.evluatingId = evluatingId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOnLineCount() {
        return onLineCount;
    }

    public void setOnLineCount(int onLineCount) {
        this.onLineCount = onLineCount;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }
}
