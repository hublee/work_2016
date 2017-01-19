package com.zeustel.cp.bean;

import java.io.Serializable;

public class AccessInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 545435431654765783L;
	
	private String lastAccessTime;

	public String getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public AccessInfo(String lastAccessTime) {
		super();
		this.lastAccessTime = lastAccessTime;
	}
	
	
}
