package com.zeustel.top9.bean;

import java.io.Serializable;

/**
 * 评论
 *
 * @author NiuLei
 * @date 2015/6/24 22:47
 */
public class HtmlComment implements Serializable {
    private int id;
    //评论内容
    private String content;
    //评论时间
    private long time;
    //评论对象id
    private int whichId;
    //评论人信息
    private SubUserInfo subUserInfo;
    //评论类型
    private int commentType;

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SubUserInfo getSubUserInfo() {
        return subUserInfo;
    }

    public void setSubUserInfo(SubUserInfo subUserInfo) {
        this.subUserInfo = subUserInfo;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getWhichId() {
        return whichId;
    }

    public void setWhichId(int whichId) {
        this.whichId = whichId;
    }

    public static class CommentType{
        public static final int TYPE_EVALUATING = 0;
        public static final int TYPE_INFO = 1;
        public static final int TYPE_TOPIC = 2;
    }
}
