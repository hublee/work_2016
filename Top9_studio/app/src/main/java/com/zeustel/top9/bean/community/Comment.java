package com.zeustel.top9.bean.community;

/**
 * 评论
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/9 15:37
 */
public class Comment extends Mutual {
    //回复数
    private int replyCount;
    //tag id 为评测

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }
}
