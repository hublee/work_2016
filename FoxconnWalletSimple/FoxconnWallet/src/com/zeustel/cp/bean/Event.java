package com.zeustel.cp.bean;

import java.util.Observable;

public class Event extends Observable{
	public void postMsg(Object obj){
		setChanged();
		notifyObservers(obj);
	}
}
