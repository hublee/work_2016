package com.zeustel.top9.bean.community;

import com.zeustel.top9.bean.SubUserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 评论 回复 基类
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/9 16:01
 */
public class Mutual implements Serializable {
    private int id;
    //行为人
    private SubUserInfo publishUser;
    //等级
    private Level level = Level.NORMAL;
    //标签
    private List<Medal> medals;
    //是否获得奖励
    private boolean isGoodluck;
    //目标Id
    private int tagId;
    //时间
    private long time;
    private int goodCount;
    //内容
    private Content content;

    private boolean isGooded;

    public boolean isGooded() {
        return isGooded;
    }

    public void setIsGooded(boolean isGooded) {
        this.isGooded = isGooded;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    //点赞总数
    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public SubUserInfo getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(SubUserInfo publishUser) {
        this.publishUser = publishUser;
    }

    public Content getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isGoodluck() {
        return isGoodluck;
    }

    public void setIsGoodluck(boolean isGoodluck) {
        this.isGoodluck = isGoodluck;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<Medal> getMedals() {
        return medals;
    }

    public void setMedals(List<Medal> medals) {
        this.medals = medals;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Mutual mutual = (Mutual) o;
        return id == mutual.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
