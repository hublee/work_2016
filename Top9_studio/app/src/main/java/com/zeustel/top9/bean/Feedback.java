package com.zeustel.top9.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 反馈意见
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/8 11:30
 */
public class Feedback implements Serializable {
    private int id;

    private long feedbackTime; //反馈时间

    private String feedbackContent; //反馈内容

    private int status; //回复状态

    private List<FeedbackReply> replyList;//回复列表

    private String imgs;//反馈图片
    //发表人
    private SubUserInfo subUserInfo;

    public SubUserInfo getSubUserInfo() {
        return subUserInfo;
    }

    public void setSubUserInfo(SubUserInfo subUserInfo) {
        this.subUserInfo = subUserInfo;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public long getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(long feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<FeedbackReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<FeedbackReply> replyList) {
        this.replyList = replyList;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
