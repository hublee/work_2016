package com.zeustel.top9.bean;

import java.io.Serializable;

/**
 * 反馈回复
 */
public class FeedbackReply implements Serializable {
    private int id;

    private int feedbackId; //反馈id

    private long replyTime; //回复时间

    private String replyContent; //回复内容

    private SubUserInfo subUserInfo; //回复人信息

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public long getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(long replyTime) {
        this.replyTime = replyTime;
    }

    public SubUserInfo getSubUserInfo() {
        return subUserInfo;
    }

    public void setSubUserInfo(SubUserInfo subUserInfo) {
        this.subUserInfo = subUserInfo;
    }
}