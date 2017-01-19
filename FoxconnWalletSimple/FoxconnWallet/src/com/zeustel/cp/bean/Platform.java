package com.zeustel.cp.bean;

public enum Platform {
	FACEBOOK("facebook"),
	ZEUSTEL("zeustel"),
	OTHER("other");

	private String platform;
	Platform(String platform){
		this.platform = platform;
	}
	public String platform(){
		return platform;
	}
}
