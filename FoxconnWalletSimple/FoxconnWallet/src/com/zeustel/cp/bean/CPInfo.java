package com.zeustel.cp.bean;

import java.io.Serializable;

import android.text.TextUtils;

public class CPInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 167656435434376656L;
	private String appId;//appid
	private String appKey;//appkey
	
	/**
	 * 获取appid
	 * @return
	 */
	public String getAppId() {
		return appId;
	}
	
	/**
	 * 设置appid
	 * @return
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	/**
	 *获取appkey
	 * @return
	 */
	public String getAppKey() {
		return appKey;
	}
	
	/**
	 * 设置appkey
	 * @param appKey
	 */
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	
	public CPInfo(){
		
	}
	
	public CPInfo(String appId, String appKey) {
		super();
		this.appId = appId;
		this.appKey = appKey;
	}
	
	public boolean isEmpty(){
		return TextUtils.isEmpty(appId)||TextUtils.isEmpty(appKey);
	}
	
}
