package com.zeustel.cp.bean;

import java.io.Serializable;

public class CoinInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 547684535443254326L;
	private int exchangeRate = 1000;
	private String coinName = "金币";
	public int getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(int exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public String getCoinName() {
		return coinName;
	}
	public void setCoinName(String coinName) {
		this.coinName = coinName;
	}
	
	public String getTips(){
		return "1通币/"+exchangeRate+coinName;
	}
	
	public CoinInfo(){
		
	}
	
	public CoinInfo(int exchangeRate, String coinName) {
		super();
		this.exchangeRate = exchangeRate;
		this.coinName = coinName;
	}

}
