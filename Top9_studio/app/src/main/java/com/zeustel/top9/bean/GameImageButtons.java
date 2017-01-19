package com.zeustel.top9.bean;

import java.io.Serializable;

/**
 * 截图演示 每个图片
 */
public class GameImageButtons implements Serializable {

	private int id;
	private int imageId;
	private int left;
	private int nextImageId;
	private int top;
	private int w;
	private int h;
	private int in;
	private int out;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public int getLeft() {
		return left;
	}

	public void setLeft(int left) {
		this.left = left;
	}

	public int getNextImageId() {
		return nextImageId;
	}

	public void setNextImageId(int nextImageId) {
		this.nextImageId = nextImageId;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getIn() {
		return in;
	}

	public void setIn(int in) {
		this.in = in;
	}

	public int getOut() {
		return out;
	}

	public void setOut(int out) {
		this.out = out;
	}

}
