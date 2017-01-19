package com.zeustel.cp.intf;

public interface IFmMenu {
	/**
	 * 主播界面
	 */
	public void toAnchorView();
	
	/**
	 * 节目单
	 */
	public void toProgramView();
	
	/**
	 * 播放
	 */
	public void toPlay();
	
	/**
	 * 关注
	 */
	public void toAttentionView();
	
	/**
	 * 弹幕
	 */
	public void toPop();
	
	/**
	 * 关闭
	 */
	public void close();
}
