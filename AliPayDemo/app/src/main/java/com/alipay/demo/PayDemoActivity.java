package com.alipay.demo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.demo.util.HttpRespons;
import com.alipay.demo.util.HttpUtil;
import com.alipay.demo.util.MD5;
import com.alipay.demo.util.OrderInfoUtil2_0;
import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.demo.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *  重要说明:
 *  
 *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
 *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
 *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
 */
public class PayDemoActivity extends FragmentActivity {
	
	/** 支付宝支付业务：入参app_id */
	public static final String APPID = "2016111002682850"; //捕鱼
//	public static final String APPID = "2016061601526391"; //游乐购

	/** 支付宝账户登录授权业务：入参pid值 */
	public static final String PID = "2088421263484864";
	/** 支付宝账户登录授权业务：入参target_id值 */
	public static final String TARGET_ID = "";

	/** 商户私钥，pkcs8格式 */
	// 捕鱼商户私钥
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMPyE0oyJXJicxtT\n" +
			"IvJaifzE9jHxwFEy2FZ3Q0fEXOGc533CYHH4mupIKm98H1x/07VpUvfQ6gwz1BLp\n" +
			"+Ffi3j98SaoJxtfxG9ye4VxJhSGafmLVDENMRHZ/5lKJrqdeoXFcCvsBuFCgFQAV\n" +
			"yMWbQchXwccJ6iEadcMOZlvxmkC9AgMBAAECgYBT38o8B00aMmQdPTAHV5QbW+Aa\n" +
			"ruXonAAYuwJUtQwhEv3QSiTohEcjo/JuOE5gVOM1kzkNlSKqSIEt+3Lvg9An0qNU\n" +
			"hhkEPTjGNUp4zgxquTspYcxnqY9yS4USaE+HDQfpAHXUXNSZBJfKDHrmuzFNs6b2\n" +
			"XjOVOrpb4BheDTivbQJBAO2X5ykPMgVmozRaxwuaVv6JMAvcVwskXRvdFWx0hLBu\n" +
			"o0ViM8xzVkrOv8E37XdIQ1JFzZHaFucMT0FdSH1h+RMCQQDTIDA+d/wnw2Rzp3rW\n" +
			"OJt854zPZQCPI5YjRxCyS0nL92paBEqHQQl4gD0PmFf2M1AaQdXQWBBr8IYTSC/r\n" +
			"oWjvAkEAtlB2uAIJ/yz4FSeHTzb6hEzfCmrkfA9GRyhJ1TQ/0WbEcGxflQX39GVb\n" +
			"rTduR7ayugIIMWApbNf8RQsAof248wJAcnjLb7OCxtSMjKDMW4aJ1+l8UZ8D9Jho\n" +
			"+Wu4w9NKigr2YSRefINo7Ssq8F+7ocQktHfpxHNbRAs/xwim4u27hQJAMdv05Gp/\n" +
			"p7AdDsmRWcyrYlM0DfpuCpslzuWAFiA4kbpZ9ZDG9a6GBr3TpvQJP/jeCvt1fD2e\n" +
			"iIueIYDaTLE/ig==";
	// 游乐购商户私钥
	public static final String RSA_PRIVATE_0 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOEkBsRWv08BlEkQ" + "XEXwk3qZECx2nb39MpH6HqrtUfyrJjarQJaiskvd+UMbAFrZaycfBpigycO1R+Ak" + "wKhAcbNfCQX4NxflRSR00iO08tLa4NyWK/Uyga9uNltONYprwlfTxYf3Csap5aeu" + "G8zv69u9tsZEC4aCtsQ4phG1iMWDAgMBAAECgYAEZa+8JfWWlXFP/AHRW+GFg3fN" + "ZoOuMkPKlhgDPp9bT/xvFOkuB/9ze+JibSSUoap2GSUH1hwatm+w8QbSQm/2B4Fx" + "Tqmf7VVw9Xsg2JaEMiYLy2b2bbM3QbfQN5+T5/pcePzQuXtvMx9mbAaNeE2MpPuH" + "BZ3fBigvCQykQhPgAQJBAPxIid+uoGVjjB7Xi44+O0kSgkWgQlJ9WrD7XJCv1QZO" + "/pv9E1FtdfPRbdOfyGo//90uWiJq4eaqD3XWEDPzIPMCQQDkdR9GWL+1NSLzj7qb" + "ogkJW5Bsy1UkfPCNapunKc18MnVZIBz8yGD/9YReSh5Z3hHFTGvnDIPNzt4dRvHD" + "KG0xAkEAu4n45AzeM0CTEwJMPctJKwLVUIjXhJam6lWQ9AWhp/TlFHRG/gsO2dbf" + "e6eVxlYZEAS7AEbP8zaME56zoMqlvQJBAJxYEukff0LRdHPeXsR3ZVvED9iEZmYs" + "IquE07TYhMmD9o7hzTSpJMjGCqpEavTxZpPxMr7R77l+4r6CeJ0oI2ECQQCslAnr" + "nUAawhoQ3MpovViCH1220fF9WCl9jqSKmi9OS6GWW6AzBtXQaAWhxkCElzvsuoQv" + "NWXYGHMW6rq8Le61";

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_AUTH_FLAG = 2;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				@SuppressWarnings("unchecked")
				PayResult payResult = new PayResult((Map<String, String>) msg.obj);
				/**
				 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为9000则代表支付成功
				if (TextUtils.equals(resultStatus, "9000")) {
					// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
					Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
				} else {
					// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
					Toast.makeText(PayDemoActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case SDK_AUTH_FLAG: {
				@SuppressWarnings("unchecked")
				AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
				String resultStatus = authResult.getResultStatus();

				// 判断resultStatus 为“9000”且result_code
				// 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
				if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
					// 获取alipay_open_id，调支付时作为参数extern_token 的value
					// 传入，则支付账户为该授权账户
					Toast.makeText(PayDemoActivity.this,
							"授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
							.show();
				} else {
					// 其他状态值则为授权失败
					Toast.makeText(PayDemoActivity.this,
							"授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

				}
				break;
			}
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_main);
	}
	
	/**
	 * 支付宝支付业务
	 * 
	 * @param v
	 */
	public void payV2(View v) {
		if (TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
							//
							finish();
						}
					}).show();
			return;
		}
	
		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * orderInfo的获取必须来自服务端；
		 */
		/*Map<String,Object> bizContent = new HashMap<>();
		bizContent.put("product_code","QUICK_MSECURITY_PAY");
		bizContent.put("total_amount","0.02");
		bizContent.put("subject","金币充值");
		bizContent.put("body", "测试");
		bizContent.put("out_trade_no", UUID.randomUUID().toString().substring(20));
		Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID,bizContent);
		String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
		String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
		final String orderInfo = orderParam + "&" + sign;*/





		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask alipay = new PayTask(PayDemoActivity.this);

				String url = "http://testjfapi.zeustel.com/api/money/alicharge/v1?";
				Map rtMap = new HashMap();
				try {
					Map<String,String> parm = new HashMap<>();
					parm.put("appId","7aea7fca095e46d29407b6dc7fd807d7");
					parm.put("money","0.02");
					parm.put("userId", "1017");
					parm.put("remark", "测试充值");
					String signParams = "appId=7aea7fca095e46d29407b6dc7fd807d7&money=0.02&remark=测试充值&userId=1017";
					String sign = MD5.getMessageDigest(signParams.getBytes());
					parm.put("sign",sign);
					HttpRespons respons = HttpUtil.sendPost(url, parm);
					rtMap = new ObjectMapper().readValue(respons.getContent(), Map.class);
					respons.getContent();
				} catch (IOException e) {
					e.printStackTrace();
				}
//				String zhparam = "charset=utf-8&biz_content=%7B%22out_trade_no%22%3A%22ZS1478784721967194542%22%2C%22total_amount%22%3A1.0%2C%22subject%22%3A%22%E9%87%91%E5%B8%81%E5%85%85%E5%80%BC%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22body%22%3Anull%7D&method=alipay.trade.app.pay&format=json&notify_url=http%3A%2F%2Ftestjfapi.zeustel.com%2Fapi%2Falicharge%2Fcallback%2Fv1&app_id=2016111002682850&sign_type=RSA&version=1.0&timestamp=2016-11-10+21%3A32%3A01&sign=ngSNLnE2cek5PwBFjrEDdSbPp8lI302SDwQLy83CpREbFZF9D6aaSSIyCqDF6t1zTJme9SqnpQQ1yl09A8lsD3zOp27qxjef8cEXs2seBVWrH3nKf1zcGVplQKmAR4daGdGoRHnfN1p42k5zG%2Fvcf0r%2B3uG77a2k99sMJHLrIJs%3D";
				Map<String, String> result = alipay.payV2(rtMap.get("data")+"", true);
//				Map<String, String> result = alipay.payV2(orderInfo, true);
				Log.i("msp", result.toString());
				
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 支付宝账户授权业务
	 * 
	 * @param v
	 */
	public void authV2(View v) {
		if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID) || TextUtils.isEmpty(RSA_PRIVATE)
				|| TextUtils.isEmpty(TARGET_ID)) {
			new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialoginterface, int i) {
						}
					}).show();
			return;
		}

		/**
		 * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
		 * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
		 * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险； 
		 * 
		 * authInfo的获取必须来自服务端；
		 */
		Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID);
		String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);
		String sign = OrderInfoUtil2_0.getSign(authInfoMap, RSA_PRIVATE);
		final String authInfo = info + "&" + sign;
		Runnable authRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造AuthTask 对象
				AuthTask authTask = new AuthTask(PayDemoActivity.this);
				// 调用授权接口，获取授权结果
				Map<String, String> result = authTask.authV2(authInfo, true);

				Message msg = new Message();
				msg.what = SDK_AUTH_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread authThread = new Thread(authRunnable);
		authThread.start();
	}
	
	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
	 * 
	 * @param v
	 */
	public void h5Pay(View v) {
		Intent intent = new Intent(this, H5PayDemoActivity.class);
		Bundle extras = new Bundle();
		/**
		 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
		 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
		 * 商户可以根据自己的需求来实现
		 */
		String url = "http://m.taobao.com";
		// url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
		extras.putString("url", url);
		intent.putExtras(extras);
		startActivity(intent);
	}

}
