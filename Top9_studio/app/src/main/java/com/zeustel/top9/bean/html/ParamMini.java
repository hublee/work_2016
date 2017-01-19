package com.zeustel.top9.bean.html;

/**
 * 子元素
 */
@Deprecated
public class ParamMini {
    private int id;
    //类型
    private Type type;
    //值
    private String value;
    //如果为文字信息 设置 前面的图片
    private String flagImg;

    public String getFlagImg() {
        return flagImg;
    }

    public void setFlagImg(String flagImg) {
        this.flagImg = flagImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public enum Type {
        //文本信息
        TEXT,
        //图片
        IMG
    }
}