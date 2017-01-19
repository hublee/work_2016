package com.zeustel.top9.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 社区话题
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/25 10:13
 */
@Table(name = "CommunityTopic")
public class CommunityTopic extends Community {
    //    //话题类型
    @Column(column = "topicType")
    private int topicType = TopicType.SYNTHESIS;
    //发表人信息
    @Foreign(column = "SubUserInfoId", foreign = "id")
    private SubUserInfo subUserInfo;
    //上传图片 ,分割
    @Column(column = "String")
    private String images;

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public SubUserInfo getSubUserInfo() {
        return subUserInfo;
    }

    public void setSubUserInfo(SubUserInfo subUserInfo) {
        this.subUserInfo = subUserInfo;
    }

    public int getTopicType() {
        return topicType;
    }

    public void setTopicType(int topicType) {
        if (TopicType.SYNTHESIS >= topicType) {
            this.topicType = TopicType.SYNTHESIS;
        } else if (TopicType.INTERFLOW <= topicType) {
            this.topicType = TopicType.INTERFLOW;
        } else {
            this.topicType = topicType;
        }
    }

    //话题类型
    public static  class TopicType {
        //综合讨论
        public static final int SYNTHESIS = 0;
        //活动公告
        public static final int NOTICE = 1;
        //攻略心得
        public static final int EXPERIENCE = 2;
        //问题咨询
        public static final int FEEDBACK = 3;
        //玩家交流
        public static final int INTERFLOW = 4;
        public static int getCount(){
            return 5;
        }
    }
}
