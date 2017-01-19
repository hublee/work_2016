package com.zeustel.top9.result;

import com.zeustel.top9.bean.SubUserInfo;
import com.zeustel.top9.bean.community.Reply;

import java.util.List;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/10 15:31
 */
public class ReplyListResult extends Result<List<Reply>> {
    //点赞人集合
    private List<SubUserInfo> goodGroup;

    public List<SubUserInfo> getGoodGroup() {
        return goodGroup;
    }

    public void setGoodGroup(List<SubUserInfo> goodGroup) {
        this.goodGroup = goodGroup;
    }
}
