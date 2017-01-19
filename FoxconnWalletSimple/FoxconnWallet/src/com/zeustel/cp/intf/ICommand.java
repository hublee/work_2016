package com.zeustel.cp.intf;

import android.content.Context;

import com.zeustel.cp.net.RequestManager;

public interface ICommand {
	
	public void updateContext(Context walletContext) ;
	
	public void setContext(Context context);

	/**
	 * 获取配置
	 * 
	 * @param requestManager
	 * @param appName
	 * @param callBack
	 */
	public void getConfig(RequestManager requestManager,
			final NextCallBack callBack);

	/**
	 * 登录
	 * 
	 * @param requestManager
	 * @param callBack
	 */
	public void login(RequestManager requestManager, NextCallBack callBack);

	/**
	 * 发验证码
	 * 
	 * @param requestManager
	 * @param phoneno
	 * @param callBack
	 */
	public void sendSMS(RequestManager requestManager, String phoneno,
			NextCallBack callBack);

	/**
	 * 校验
	 * 
	 * @param requestManager
	 * @param authcode
	 * @param phone
	 * @param code
	 * @param callBack
	 */
	public void checkCode(RequestManager requestManager, String phone,
			String code, NextCallBack callBack);

	/**
	 * 应用授权
	 * 
	 * @param requestManager
	 * @param callBack
	 */
	public void cpAuth(RequestManager requestManager, NextCallBack callBack);

	/**
	 * 兑换
	 * 
	 * @param requestManager
	 * @param authcode
	 * @param phoneno
	 * @param amount
	 * @param callBack
	 */
	public void exchange(RequestManager requestManager, String phoneno,
			int amount, final NextCallBack callBack);

	/**
	 * 校验token
	 * 
	 * @param requestManager
	 * @param callBack
	 */
	public void validToken(RequestManager requestManager,
			final NextCallBack callBack);

	/**
	 * 第三方登录
	 * @param openId
	 * @param platform
	 * @param userinfo
	 * @param requestManager
	 * @param callBack
	 */
	public void thirdLogin(String openId, String platform,String userinfo,
			RequestManager requestManager, final NextCallBack callBack);

	/**
	 * 邮箱注册
	 * @param email
	 * @param password
	 * @param requestManager
	 * @param callBack
	 */
	public void registViaEmail(String email, String password,
			RequestManager requestManager, final NextCallBack callBack);

	/**
	 * 邮箱登录
	 * @param email
	 * @param password
	 * @param requestManager
	 * @param callBack
	 */
	public void loginViaEmail(String email, String password,
			RequestManager requestManager, final NextCallBack callBack);

	/**
	 * 找回密码
	 * @param email
	 * @param password
	 * @param digitalCode
	 * @param requestManager
	 * @param callBack
	 */
	public void retrieveViaEmail(String email, String password,
			String digitalCode, RequestManager requestManager,
			final NextCallBack callBack);

	/**
	 * 邮箱获取验证码
	 * @param email
	 * @param requestManager
	 * @param callBack
	 */

	public void getCodeViaMail(String email, RequestManager requestManager,
			final NextCallBack callBack);

	/**
	 * 绑定第三方账号
	 * @param openid
	 * @param platform
	 * @param userId
	 * @param requestManager
	 * @param callBack
	 */
	 void bindThirdUser(String openid,String platform,String userId, RequestManager requestManager,
							  final NextCallBack callBack);

	/**
	 * 检查是否绑定
	 * @param openid
	 * @param platform
	 * @param requestManager
	 * @param callBack
	 */
	 void validBind(String openid,String platform,RequestManager requestManager,
							  final NextCallBack callBack);

	/**
	 * token登录
	 * @param requestManager
	 * @param callBack
	 */
	void tokenLogin(String token,RequestManager requestManager,
				   final NextCallBack callBack);
}
