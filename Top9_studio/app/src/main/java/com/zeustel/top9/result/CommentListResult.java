package com.zeustel.top9.result;

import com.zeustel.top9.bean.SubUserInfo;
import com.zeustel.top9.bean.community.Comment;

import java.util.List;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/10 11:43
 */
public class CommentListResult extends Result<List<Comment>> {
    //以平台为基础 (非单个评测)评论被点赞最多的3个人
    private List<SubUserInfo> best;

    public List<SubUserInfo> getBest() {
        return best;
    }

    public void setBest(List<SubUserInfo> best) {
        this.best = best;
    }
}
