package com.zeustel.top9.bean;

import java.io.Serializable;

/**
 * 主播
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/24 16:38
 */
public class Compere extends UserInfo implements Serializable {
    //宣传视频
    private String video;
    //视频封面
    private String videoCover;
    //血型
    private String bloodType;
    //英文名字
    private String nameEN;
    //星座
    private String constellation;
    //爱好
    private String hobby;
    //宣言
    private String manifesto;
    //相册
    private String photos;
    //生日
    private String birthday;

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getManifesto() {
        return manifesto;
    }

    public void setManifesto(String manifesto) {
        this.manifesto = manifesto;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
