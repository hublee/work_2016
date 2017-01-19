package com.zeustel.top9.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 社区资讯
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/25 10:14
 */
@Table(name = "CommunityInfo")
public class CommunityInfo extends Community {
    //资讯类型
    public static class InfoType {
        //资讯新闻
        public static final int NEWS = 0;
        //资讯公告
        public static final int NOTICE = 1;
        //资讯心得
        public static final int EXPERIENCE = 2;
        //资讯活动
        public static final int ACTIVITIES = 3;
        //资讯访谈
        public static final int INTERVIEW = 4;

        public static int getCount(){
            return 5;
        }
    }

    //资讯类型
    @Column(column = "infoType")
    private int infoType = InfoType.NEWS;
    //小图标
    @Column(column = "icon")
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getInfoType() {
        return infoType;
    }

    public void setInfoType(int infoType) {
        if (InfoType.NEWS >= infoType) {
            this.infoType = InfoType.NEWS;
        } else if (InfoType.INTERVIEW <= infoType) {
            this.infoType = InfoType.INTERVIEW;
        } else {
            this.infoType = infoType;
        }
    }
}
