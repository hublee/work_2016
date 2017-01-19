package com.zeustel.top9.bean;

import com.lidroid.xutils.db.annotation.Column;

/**
 * 社区话题
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/25 09:43
 */
public class Community extends HtmlPaper {
    //社区类型
    public class CommunityType {
        //社区资讯
        public static final int communityInfo = 0;
        //社区话题
        public static final int communityTopic = 1;
    }

    //话题类型
    @Column(column = "communityType")
    private int communityType = CommunityType.communityInfo;

    public int getCommunityType() {
        return communityType;
    }

    public void setCommunityType(int communityType) {
        this.communityType = communityType;
    }
}
