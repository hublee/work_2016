package com.zeustel.cp.bean;

import java.io.Serializable;

public class ServerInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 156426845648438745L;
	private String server;
	private String testServer;
	private String msdk;
	
	public String getMsdk() {
		return msdk;
	}
	public void setMsdk(String msdk) {
		this.msdk = msdk;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getTestServer() {
		return testServer;
	}
	public void setTestServer(String testServer) {
		this.testServer = testServer;
	}
	
	public ServerInfo(){
		
	}
	
	public ServerInfo(String server, String testServer,String msdk) {
		super();
		this.server = server;
		this.testServer = testServer;
		this.msdk = msdk;
	}
	
	

}
