package com.zeustel.cp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zeustel.cp.bean.AccessInfo;
import com.zeustel.cp.bean.CPInfo;
import com.zeustel.cp.bean.CoinInfo;
import com.zeustel.cp.bean.ServerInfo;
import com.zeustel.cp.net.Parameter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 帮助类
 * 
 * @author Do
 *
 */
public class SdkUtils {
	public static boolean DEBUGMODE = false;

	public static int total = 0;// 游戏币总数

	public static CoinInfo coinInfo;

	private static boolean isPlay = true;// 是否播放

	public static boolean isLeft = true;

	/**
	 * 菜单x坐标
	 * 
	 * @return
	 */
	public static int getControlMenuX(Activity activity, int x) {
		int baseX = x + Tools.dip2px(activity, Constants.CONTROLLER_WIDTH / 2);
		int screenWidth = Tools.getScreenWidth(activity);
		if (baseX > screenWidth / 2) {
			isLeft = false;
			Log.e("iSLEFT:", isLeft + "");
			return x - Tools.dip2px(activity, Constants.CONTROLCENTER_WIDTH);
		} else {
			isLeft = true;
			Log.e("iSLEFT:", isLeft + "");
			return baseX
					+ Tools.dip2px(activity, Constants.CONTROLLER_WIDTH / 2);
		}

	}

	/**
	 * 菜单x坐标
	 * 
	 * @return
	 */
	public static int getControllerMenuX(Activity activity, int x) {
		int baseX = x + Tools.dip2px(activity, Constants.CONTROLLER_WIDTH / 2);
		int screenWidth = Tools.getScreenWidth(activity);
		if (baseX > screenWidth / 2) {
			isLeft = false;
			Log.e("iSLEFT:", isLeft + "");
			return -Tools.dip2px(activity, Constants.CONTROLCENTER_WIDTH);
		} else {
			isLeft = true;
			Log.e("iSLEFT:", isLeft + "");
			return Tools.dip2px(activity, Constants.CONTROLLER_WIDTH);
		}
	}

	/**
	 * 菜单宽度
	 * 
	 * @return
	 */
	public static int getControlMenuWidth(Context context) {
		return Tools.dip2px(context, Constants.CONTROLCENTER_WIDTH);
	}

	/**
	 * 菜单高度
	 * 
	 * @return
	 */
	public static int getControlMenuHeight(Context context) {
		return Tools.dip2px(context, Constants.CONTROLCENTER_HEIGHT);
	}

	/**
	 * 打开应用
	 * 
	 * @return
	 */
	public static void openApp(Context context, String pack) {
		Intent intent = Tools.packageIntent(context, pack);
		if (intent == null) {// 检查是否安装包
			// 下载应用
			Tools.openUrl(
					context,
					pack == Constants.PACKAGE_NAME_TOP9 ? Constants.DOWNLOAD_URL_TOP9
							: Constants.DOWNLOAD_URL_JFT);
		} else {
			// 打开应用
			context.startActivity(intent);
		}
	}

	/**
	 * 打开应用
	 * 
	 * @return
	 */
	public static void openApp(Context context, String pack, Bundle bundle) {
		Intent intent = Tools.packageIntent(context, pack);
		if (intent == null) {// 检查是否安装包
			// 下载应用
			Tools.openUrl(
					context,
					pack == Constants.PACKAGE_NAME_TOP9 ? Constants.DOWNLOAD_URL_TOP9
							: Constants.DOWNLOAD_URL_JFT);
		} else {
			// 打开应用
			intent.putExtras(bundle);
			context.startActivity(intent);
		}
	}

	/**
	 * 提示
	 * 
	 * @param context
	 * @param tips
	 */
	public static void showTips(final Context context, final String tips) {
		if (DEBUGMODE) {
			new Thread() {
				@Override
				public void run() {
					Looper.prepare();
					Toast toast = Toast.makeText(context, tips,
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM, 0, 10);
					toast.show();
					Looper.loop();
				};
			}.start();
		}
	}

	/**
	 * 打印日志
	 * 
	 * @param msg
	 */
	public static void LogE(String msg) {
		if (DEBUGMODE) {
			LogE("CP_DEBUG", msg);
		}
	}

	/**
	 * 打印日志
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void LogE(String tag, String msg) {
		if (DEBUGMODE) {
			Log.e(tag, msg);
		}
	}

	/**
	 * 打印日志
	 * 
	 * @param msg
	 */
	public static void LogD(String msg) {
		if (DEBUGMODE) {
			LogD("CP_DEBUG", msg);
		}
	}

	/**
	 * 打印日志
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void LogD(String tag, String msg) {
		if (DEBUGMODE) {
			Log.d(tag, msg);
		}
	}

	/**
	 * 打印日志
	 * 
	 * @param msg
	 */
	public static void LogI(String msg) {
		if (DEBUGMODE) {
			LogI("CP_DEBUG", msg);
		}
	}

	/**
	 * 打印日志
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void LogI(String tag, String msg) {
		if (DEBUGMODE) {
			Log.i(tag, msg);
		}
	}

	/**
	 * 返回总游戏币
	 * 
	 * @return
	 */
	public static int getTotal() {
		return total;
	}

	/**
	 * 检查手机号是否合法
	 * 
	 * @param phoneno
	 * @return
	 */
	public static boolean checkPhoneno(String phoneno) {
		Pattern pattern = Pattern.compile("1[0-9]{10}");
		Matcher matcher = pattern.matcher(phoneno);
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 金币信息
	 * 
	 * @return
	 */
	public static CoinInfo getCoinInfo() {
		if (coinInfo != null) {
			return coinInfo;
		}
		coinInfo = (CoinInfo) NetCache.getInstance().getObjectCache(
				Constants.COININFO);
		return coinInfo;
	}

	/**
	 * 保存金币信息
	 */
	public static void saveCoinInfo(String coinName, int rate) {
		coinInfo = new CoinInfo(rate, coinName);
		NetCache.getInstance().addCache(Constants.COININFO, coinInfo);
	}

	/**
	 * 保存服务器ip信息
	 */
	public static void saveServerInfo(String server, String testServer,String msdk) {
		Log.e("saveServerInfo", server + ":" + testServer);
		NetCache.getInstance().addCache(Constants.SERVERINFO,
				new ServerInfo(server, testServer,msdk));
	}
	public static ServerInfo getServerInfo() {
		ServerInfo mServerInfo = null;
		Object obj = NetCache.getInstance()
				.getObjectCache(Constants.SERVERINFO);
		if (obj == null) {
			return null;
		}
		mServerInfo = (ServerInfo) obj;
		return mServerInfo;
	}

	public static void setuserId(String userId){
		NetCache.getInstance().addCache(Constants.USER_ID,
				new Parameter(Constants.USER_ID, userId));
	}
	public static String getUserId(){
		Parameter mParameter = (Parameter) NetCache.getInstance().getObjectCache(Constants.USER_ID);
		return mParameter.getValue();
	}
	
	
	/**
	 * 设置token
	 * 
	 * @param string
	 */
	public static void SetToken(String string) {
		NetCache.getInstance().addCache(Constants.AUTHINFO,
				new Parameter(Constants.AUTHINFO, string));
	}

	/**
	 * 获取token
	 * 
	 * @return
	 */
	public static String getToken() {
		String saveToken = "";
		Object obj = NetCache.getInstance().getObjectCache(Constants.AUTHINFO);
		if (obj == null) {
			return null;
		}
		saveToken = ((Parameter) obj).getValue();

		return saveToken;
	}

	/**
	 * 设置上次请求时间
	 * 
	 * @param string
	 */
	public static void setLastAccessTime(String string) {
		NetCache.getInstance().addCache(Constants.LASTACCESSTIME,
				new AccessInfo(string));
	}

	/**
	 * 获取上次请求时间
	 * 
	 * @return
	 */
	public static String getLastAccessTime() {
		Object obj = NetCache.getInstance().getObjectCache(
				Constants.LASTACCESSTIME);
		if (obj == null) {
			return "";
		}
		return ((AccessInfo) obj).getLastAccessTime();
	}

	/**
	 * 获取根视图
	 * 
	 * @param context
	 * @return
	 */
	public static View getRootView(Activity context) {
		return ((ViewGroup) context.findViewById(android.R.id.content))
				.getChildAt(0);
	}

	public static void setIsPlay(boolean play) {
		isPlay = play;
	}

	public static boolean isPlay() {
		return isPlay;
	}

	/**
	 * cp信息
	 * 
	 * @return
	 */
	public static CPInfo getCp() {
		Object obj = NetCache.getInstance().getObjectCache(Constants.CPINFO);
		if (obj == null) {
			return null;
		}
		return (CPInfo) obj;
	}

	/**
	 * 设备信息
	 * 
	 * @return
	 */
	public static String getDevicesInfo(Context context) {

		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);//
			JSONObject obj = new JSONObject();
			if(Tools.getAndroidSDKVersion() < 23) {
				obj.put("DeviceId(IMEI)", tm.getDeviceId());
				obj.put("DeviceSoftwareVersion", tm.getDeviceSoftwareVersion());
				obj.put("Line1Number", tm.getLine1Number());
				obj.put("NetworkCountryIso", tm.getNetworkCountryIso());
				obj.put("NetworkOperator", tm.getNetworkOperator());
				obj.put("NetworkOperatorName", tm.getNetworkOperatorName());
				obj.put("NetworkType", tm.getNetworkType());
				obj.put("PhoneType", tm.getPhoneType());
				obj.put("SimCountryIso", tm.getSimCountryIso());
				obj.put("SimOperator", tm.getSimOperator());
				obj.put("SimOperatorName", tm.getSimOperatorName());
				obj.put("SimSerialNumber", tm.getSimSerialNumber());
				obj.put("SimState", tm.getSimState());
				obj.put("SubscriberId(IMSI)", tm.getSubscriberId());
				obj.put("VoiceMailNumber", tm.getVoiceMailNumber());
			}
			obj.put("Osversion", android.os.Build.VERSION.SDK_INT);// 系统版本
			obj.put("Networktype", networkType(context));// 网络类型
			obj.put("Devicemodel", android.os.Build.MODEL);
			obj.put("Memoryinfo", getAvailMemory(context) + "/"
					+ getTotalMemory(context));
			obj.put("MacAddress", getMacAddress(context));
			obj.put("AllApps", getAllApp(context));
			return Base64.encode(obj.toString().getBytes());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	private static String getAllApp(Context context) {
		String result = "";
		List<PackageInfo> packages = context.getPackageManager()
				.getInstalledPackages(0);
		for (PackageInfo i : packages) {
			if ((i.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				result += i.applicationInfo.loadLabel(
						context.getPackageManager()).toString()
						+ ",";
			}
		}
		return result.substring(0, result.length() - 1);
	}

	public static String[] getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" }; // 1-cpu型号 //2-cpu频率
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return cpuInfo;
	}

	public static String getMacAddress(Context context) {
		String result = "";
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		result = wifiInfo.getMacAddress();
		return result;
	}

	/**
	 * 网络类型
	 * 
	 * @return
	 */
	public static String networkType(Context context) {
		NetworkInfo networkInfo = (NetworkInfo) ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return "WIFI";
			} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				String _strSubTypeName = networkInfo.getSubtypeName();

				// TD-SCDMA networkType is 17
				int networkType = networkInfo.getSubtype();
				switch (networkType) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_EDGE:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by
															// 11
					return "2G";

				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_EVDO_A:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by
															// 14
				case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by
															// 12
				case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by
															// 15
					return "3G";

				case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by
														// 13
					return "4G";

				default:
					// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
					if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
							|| _strSubTypeName.equalsIgnoreCase("WCDMA")
							|| _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
						return "3G";
					} else {
						Log.e("未知网络类型", _strSubTypeName);
						return "OTHER";
					}
				}

			}
		}
		return "NULL";
	}

	/**
	 * 可用内存
	 * 
	 * @param context
	 * @return
	 */
	public static String getAvailMemory(Context context) {// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存
		return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
	}

	/**
	 * 总内存
	 * 
	 * @param context
	 * @return
	 */
	public static String getTotalMemory(Context context) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

}
