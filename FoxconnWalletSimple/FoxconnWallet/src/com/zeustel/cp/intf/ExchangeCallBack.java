package com.zeustel.cp.intf;

/**
 * 兑换接口
 * @author Do
 *
 */
public interface ExchangeCallBack {
	/**
	 * 回调
	 * @param mobile 手机号
	 * @param amount 兑换的积分数
	 * @param sid sid
	 */
	public void callBack(String mobile,int amount,String sid);
}
