package com.zeustel.cp.bean;

import java.io.Serializable;

/**
 * Created by Do on 2016/2/23.
 */
public class Users implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 145435432543256328L;
	private String userName;
    private String userPass;
    private String userId;
    private long userPoint;
    private String phoneno;

    public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getUserPoint() {
        return userPoint;
    }

    public void setUserPoint(long userPoint) {
        this.userPoint = userPoint;
    }

    public Users(){
    	
    }
    
    public Users(String userName, String userPass, String userId, long userPoint) {
        this.userName = userName;
        this.userPass = userPass;
        this.userId = userId;
        this.userPoint = userPoint;
    }

	@Override
	public String toString() {
		return "Users [userName=" + userName + ", userPass=" + userPass
				+ ", userId=" + userId + ", userPoint=" + userPoint
				+ ", phoneno=" + phoneno + "]";
	}
    
}
