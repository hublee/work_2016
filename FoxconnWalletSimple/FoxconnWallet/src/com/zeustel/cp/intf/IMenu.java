package com.zeustel.cp.intf;

/**
 * 菜单操作接口
 * @author Do
 *
 */
public interface IMenu {
	/**
	 * 跳转到商城
	 */
	public void toShop();
	
	/**
	 * 跳转到页面
	 * @param viewId
	 */
	public void toView(int viewId);
	
	/**
	 * 跳转到游戏
	 */
	public void toGame();
	
	/**
	 * 跳转到礼包
	 */
	public void toGift();
	
	/**
	 * 跳转到社区
	 */
	public void toForum();
	
	/**
	 * 用户中心
	 */
	public void toUserCenter();
	
	/**
	 * 兑换
	 */
	public void toExchange();
}
