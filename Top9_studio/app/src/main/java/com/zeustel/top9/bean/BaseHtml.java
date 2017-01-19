package com.zeustel.top9.bean;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

import java.io.Serializable;
import java.util.Comparator;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/27 15:08
 */
public class BaseHtml implements Serializable, Comparator<BaseHtml> {
    //id
    @Id
    @NoAutoIncrement
    private int id;
    //游戏Id
    @Column(column = "gameId")
    //    @Foreign(column = "gameId", foreign = "id")
    private int gameId;
    //标题
    @Column(column = "title")
    private String title;
    //小标题
    @Column(column = "subTitle")
    private String subTitle;
    //发布时间
    @Column(column = "time")
    private long time;
    //评论总数
    @Column(column = "commentAmount")
    private int commentAmount;  //回复总数

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getCommentAmount() {
        return commentAmount;
    }

    public void setCommentAmount(int commentAmount) {
        this.commentAmount = commentAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compare(BaseHtml lhs, BaseHtml rhs) {
        return String.valueOf(lhs.getTime()).compareTo(String.valueOf(rhs.getTime()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BaseHtml html = (BaseHtml) o;
        return this.id == html.getId();
    }

    @Override
    public int hashCode() {
        return id;
    }
}
