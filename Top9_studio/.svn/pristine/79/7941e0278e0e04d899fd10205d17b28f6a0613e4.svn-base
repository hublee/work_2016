package com.zeustel.top9.bean;

import java.io.Serializable;

/**
 * 记录基类
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/10 11:23
 */
public class Note implements Serializable {
    private int id;
    //标题 描述
    private String title;
    //时间
    private long time;
    //数量
    private int noteType;

    //记录类型
    public class NoteType {
        //节目
        public static final int program = 0;
        //积分
        public static final int integral = 1;
        //鲜花
        public static final int flower = 2;
    }

    public int getNoteType() {
        return noteType;
    }

    public void setNoteType(int noteType) {
        this.noteType = noteType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
