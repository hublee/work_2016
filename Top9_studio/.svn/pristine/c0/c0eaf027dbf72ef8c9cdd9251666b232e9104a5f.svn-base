package com.zeustel.top9.bean;

import java.io.Serializable;

public class LoginNote implements Comparable<LoginNote>,Serializable{
    private String name;
    private long updateTime;
    private String token;

    public LoginNote() {
    }

    public LoginNote(String name, long updateTime, String token) {
        this.name = name;
        this.updateTime = updateTime;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int compareTo(LoginNote another) {
        return (int) (getUpdateTime() - another.getUpdateTime());
    }
}