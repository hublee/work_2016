package com.zeustel.top9.bean.html;

import java.io.Serializable;

/**
 * 定制 如主系统 副系统上分别为图片 和视频
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/6 08:59
 */
public class TopItem implements Serializable{
    private Type type;
    //封面图片路径 ，展览第一图片
    private String url;
    //视频为视频播放地址 ，展览为9宫格样式 路径
    private String dataUrl;

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public enum Type {
        VIDEO,//视频
        EXHIBITION//展览
    }

    @Override
    public String toString() {
        return "TopItem{" +
                "dataUrl='" + dataUrl + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                '}';
    }
}
