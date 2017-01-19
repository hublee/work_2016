package com.zeustel.cp.net;

import java.io.Serializable;

public class Parameter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 220255729376376234L;
	private String key;
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public Parameter(){
		
	}
	
	public Parameter(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Parameter [key=" + key + ", value=" + value + "]";
	}
	

}
