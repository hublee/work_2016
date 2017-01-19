package com.zeustel.cp.utils;

/**
 * Created by Do on 2016/2/23.
 */
public class Constants {

	// 第三方登录
	public static final String URL_LOGIN_OTHER = "/api/sv/thirdLogin";
	// 邮箱注册
	public static final String URL_REGISTER_EMAIL = "/api/sv/registViaEmail";
	// 邮箱登录
	public static final String URL_LOGIN_EMAIL = "/api/sv/loginViaEmail";
	// 找回密码
	public static final String URL_FORGET = "/api/sv/retrieveViaEmail";
	// 获取验证码
	public static final String URL_GET_VER = "/api/sv/getCodeViaMail";
	//绑定账号
	public static final String URL_BIND_USER = "/api/sv/bindThirdUser";
	//检查绑定
	public static final String URL_VALIDBIND = "/api/sv/validBind";
	//TOKEN 登录
	public static String URL_TOKEN_LOGIN = "/api/sv/login";

	public static boolean DEBUG = true;

	public static String TAG = "CP";

	// 视图id
	public final static int CONTROLLERCENTER_VIEWID = 0x1;// 控制中心
	public final static int EXCHANGE_VIEWID = 0x2;// 兑换
	public final static int EXCHANGE_VER = 0x22;// 兑换验证
	public final static int GIFT_VIEWID = 0x3;// 商城
	public final static int USERSET_VIEWID = 0x4;// 用户中心
	public final static int LOGIN_VIEWID = 0x5;// 登录
	public final static int CHECK_VIEWID = 0x6;// 验证
	public final static int EXCHANGE_OK_VIEWID = 0x7;// 兑换成功
	public final static int EXCHANGE_ERROR_VIEWID = 0x8;// 用户中心

	public final static int USERCENTER_VIEWID = 0x9;// 用户中心

	public final static int NEWPOP_VIEWID = 0x10;// 弹窗

	public static final int PAGE_WIDTH_PENCENT = 83;// 页面宽度占比
	public static final int PAGE_WIDTH_PENCENT_NOCONTROL = 73;// 页面宽度占比

	public static int PAGE_WIDTH_PENCENT_LITTLE = 93;// 页面宽度占比 小屏
	public static int PAGE_WIDTH_PENCENT_NOCONTROL_LITTLE = 83;// 页面宽度占比 小屏

	// 网络类型
	public static final int NETWORK_TYPE_WIFI = 1;
	public static final int NETWORK_TYPE_2G = 2;
	public static final int NETWORK_TYPE_3G = 3;
	public static final int NETWORK_TYPE_4G = 4;
	public static final int NETWORK_TYPE_OTHER = 10;
	public static final String USER_ID = "user_id";// 授权信息
	// 缓存标志
	public static final String USERINFO = "userinfo";// 用户信息
	public static final String COININFO = "coininfo";// 金币信息
	public static final String AUTHINFO = "authinfo";// 授权信息
	public static final String CPINFO = "cpinfo";// cp信息
	public static final String SERVERINFO = "serverinfo";// ip信息
	public static final String LASTACCESSTIME = "lastaccesstime";// 上次请求时间

	//public static final String PACKAGE_NAME_JFT = "com.zeustel.integralclient";// 积分通包名
	public static final String PACKAGE_NAME_JFT = "com.zeustel.icfoxconn";// 积分通包名
//	public static final String DOWNLOAD_URL_JFT = "http://www.pgyer.com/iIntegralClient";// 积分通的下载地址
	public static final String DOWNLOAD_URL_JFT = "http://beta.qq.com/m/pjhc";// 积分通的下载地址

	public static final String PACKAGE_NAME_TOP9 = "com.zeustel.top9";// Top9的包名
	public static final String DOWNLOAD_URL_TOP9 = "http://www.pgyer.com/iIntegralClient";// TOP9的下载地址

	public static final int CONTROLCENTER_WIDTH = 320;
	public static final int CONTROLCENTER_HEIGHT = 74;
	public static final int CONTROLLER_WIDTH = 64;

	public static boolean ISFULLSCREEN = true;

	// http
	public final static String GET = "GET";
	public final static String POST = "POST";

	// 参数对应名称
	public final static String SID = "sid";
	public final static String COIN_NAME = "currencyName";
	public final static String SCALE = "scale";
	public final static String SERVER = "server";
	public final static String msdk = "msdk";
	public final static String TESTSERVER = "testServer";
	// 测试
//	public static String BASE_URL = "http://115.29.5.34:8088/sdk";
//	public static String BASE_URL_M = "http://115.29.5.34:8088/msdk/";
	
	public static String BASE_URL = "http://120.76.189.57:8081/sdk";
	public static String BASE_URL_M = "http://120.76.189.57:8081/msdk/";
	
	//
	// 正式
	// public static String BASE_URL = "http://sdkapi.zeustel.com";
	// public static String BASE_URL_M =
	// "http://msdk.zeustel.com/community/community.jsp";

	public static final String BASE_URL_M_WEB = BASE_URL_M
			+ "community/community.jsp";

	public static String AUTH_URL = "/api/integral/exchange/sdkInit";// 授权地址

	public static String EXCHANGE_URL = "/api/integral/exchange/convert";// 兑换地址

	public static String SMS_URL = "/api/integral/exchange/getVerifyCode";// 短信地址

	public static String AUTH_TOKEN_URL = "/api/sv/authtoken";// token
	public static String AUTH_HAS_VER = "/sapi/user/isTwoLvPwd/v1";// 是否有数字密码
	public static String AUTH_VER_NUM_PWD = "/sapi/user/isCorrectTwoLvPwd/v1";// 数字密码验证

	public static String LOGIN_URL = "/api/sv/login";// 登录

	public static String CONFIG_URL = "/api/sv/getConfig";// 配置

	public static String VALID_TOKEN = "/api/sv/validToken";// 校验token

	public static String ZS_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQPXKWaQn/E8tU7X3dgaCF6ygbIpL81tNWKGctJisbWZM7a1nDQZAAhKXwVys0JFVUk5xUuHISTO0YvvzeC0h4hFj3+BjkzdYr2xn6ZSg77UqOxTqccfr+4gK/uZOEL10WPw5DZiBb6DdPLNYyijfFVv0dnvPLDu7mew8UbgbMyQIDAQAB";// 宙斯公钥

	// js 对应操作码
	public final static int TOSHOP = 0x1;// 跳转到商城
	public final static int TOLOGIN = 0x2;// 登录
	public final static int TOREFRESH = 0x3;// 刷新
	public final static int TOALERT = 0x4;// 弹窗

	public static void setBaseUrl(String url) {
		BASE_URL = url;
	}

	public static String getURL() {
		return BASE_URL.split("/")[2];
	}

	// token过期
	public final static int TOKEN_EXPIRE = 30008;
	public final static int TOKEN_EXPIRE2 = 10004;

	// sid非法
	public final static int SID_ERROR = 30004;

	public final static String APPNAME = "sdk";
}
