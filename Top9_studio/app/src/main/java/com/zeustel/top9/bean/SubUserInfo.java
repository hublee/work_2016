package com.zeustel.top9.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/25 10:48
 */
@Table(name = "SubUserInfo")
public class SubUserInfo implements Serializable {
    @Id
    @NoAutoIncrement
    private int id;
    //昵称
    @Column(column = "nickName")
    private String nickName;
    //头像
    @Column(column = "icon")
    private String icon;
    //性别
    @Column(column = "gender")
    private int gender = Gender.GENTLEMEN;
    //最后修改时间
    @Column(column = "lastModifyTime")
    private long lastModifyTime;
    //点赞总数
    @Column(column = "goodCount")
    private int goodCount;
    //回复总数
    @Column(column = "replyCount")
    private int replyCount;
    //粉丝总数
    @Column(column = "fansCount")
    private int fansCount;
    //关注总数
    @Column(column = "attentionCount")
    private int attentionCount;
    //主题图片
    @Column(column = "theme")
    private String theme;
    //当前等级
    @Column(column = "level")
    private UserLevel level;
    //当前等级的经验值
    @Column(column = "experience")
    private int experience;
    //个性签名
    @Column(column = "signature")
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getReplyCount() {
        return replyCount;
    }
    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getAttentionCount() {
        return attentionCount;
    }

    public void setAttentionCount(int attentionCount) {
        this.attentionCount = attentionCount;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    public UserLevel getLevel() {
        return level;
    }

    public void setLevel(UserLevel level) {
        this.level = level;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getGoodCount() {
        return goodCount;
    }

    public void setGoodCount(int goodCount) {
        this.goodCount = goodCount;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String toSubString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        buffer.append("\"id\":" + id);
        buffer.append(",");
        buffer.append("\"nickName\":" + "\"" + nickName + "\"");
        buffer.append(",");
        buffer.append("\"icon\":" + (icon == null ? null : "\"" + icon + "\""));
        buffer.append("}");
        return buffer.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof SubUserInfo) {
            return getId() == ((SubUserInfo) o).getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * 性别
     *
     * @author NiuLei
     * @date 2015/6/24 21:46
     */
    public class Gender {
        //男性
        public static final int GENTLEMEN = 0;
        //女性
        public static final int LADY = 1;
    }
}
