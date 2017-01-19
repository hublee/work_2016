package com.zeustel.cp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.zeustel.cp.bean.Users;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/16 10:01
 */
public class Tools {
    private static int STATE_BAR_HEIGHT = -1;
    private static int TITLE_BAR_HEIGHT = -1;

    //账号格式匹配 6-16字母 数字 下划线
    public static final String REGEX_ACCOUNT = "^(\\w){6,16}$";
    //密码格式匹配 6-20字母 数字 下划线
    public static final String REGEX_PASSWORD = "^(\\w){6,20}$";
    //邮箱格式匹配
    public static final String REGEX_EMAIL = "^(\\w)+@(\\w)+.(\\w)+$";

    /**
     * 检查是否允许注册
     * @param account
     * @param pwd
     * @param pwdAgain
     * @return
     */
    public static EditError allowRegister(String account, String pwd, String pwdAgain) {
        final EditError editError = allowLogin(account, pwd);
        if (editError != EditError.NONE) {
            return editError;
        }
        if (pwdAgain == null || !pwdAgain.equals(pwd)) {
            return EditError.PWD_DIFF;
        }
        return EditError.NONE;
    }

    /**
     * 检查是否允许登录
     * @param account
     * @param pwd
     * @return
     */
    public static EditError allowLogin(String account, String pwd) {
        final EditError editError = checkAccount(account);
        if (editError != EditError.NONE) {
            return editError;
        }
        final EditError editError1 = checkPwd(pwd);
        if (editError1 != EditError.NONE) {
            return editError1;
        }
        return EditError.NONE;
    }

    /**
     * 检查账号是否符合邮箱格式
     * @param account
     * @return
     */
    public static EditError checkAccount(String account) {
        if (TextUtils.isEmpty(account)) {
            return EditError.ACCOUNT_EMPTY;
        }
        if (!account.matches(Tools.REGEX_EMAIL)) {
            return EditError.ACCOUNT_FORMAT;
        }
        return EditError.NONE;
    }

    /**
     * 检查账号是否符合格式
     * @param pwd
     * @return
     */
    public static EditError checkPwd(String pwd){
        if (TextUtils.isEmpty(pwd)) {
            return EditError.PWD_EMPTY;
        }
        if (!pwd.matches(Tools.REGEX_PASSWORD)) {
            return EditError.PWD_FORMAT;
        }
        return EditError.NONE;
    }

    //屏幕参数
    private static DisplayMetrics displayMetrics = new DisplayMetrics();

    /**
     * 获取状态栏高度
     */
    public static int getStateBarHeight(Activity activity) {
        if (activity == null) {
            return -1;
        }
        if (STATE_BAR_HEIGHT == -1) {
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            STATE_BAR_HEIGHT = frame.top > 0 ? frame.top : -1;
        }
        return STATE_BAR_HEIGHT;
    }

    /**
     * 获取标题栏高度
     */
    public static int getTitleBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        if (TITLE_BAR_HEIGHT == -1) {
            int contentTop = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
            TITLE_BAR_HEIGHT = contentTop - getStateBarHeight(activity);
        }
        return TITLE_BAR_HEIGHT;
    }

    /**
     * md5加密字符串
     */
    public static String md5Encrypt(String string) throws NoSuchAlgorithmException {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = string.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase(Locale.CHINA);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取Mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifi.getConnectionInfo().getMacAddress();
    }

    // 获取mac地址
    public static String getMac() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return macSerial;
    }

    private final static int BYTEMASK = 0xFF; // 8 bits

    public static int encodeLittleEndian(byte[] dst, long val, int offset, int size) {
        for (int i = 0; i < size; i++) {
            dst[offset++] = (byte) (val >> ((size - i - 1) * Byte.SIZE));
        }
        return offset;
    }

    public static long decodeLittleEndian(byte[] val, int offset, int size) {
        long rtn = 0;
        for (int i = 0; i < size; i++) {
            rtn = (rtn << Byte.SIZE) | ((long) val[offset + i] & BYTEMASK);
        }
        return rtn;
    }

    public static int decodeEndian(byte[] val, int offset, int size) {
        int rtn = 0;
        for (int i = 0; i < size; i++) {
            rtn = (rtn << Byte.SIZE) | ((int) val[offset + i] & BYTEMASK);
        }
        return rtn;
    }

    public static int encodeBigEndian(byte[] dst, long val, int offset, int size) {
        int len = offset + size - 1;
        for (int i = 0; i < size; i++) {
            dst[len--] = (byte) (val >> ((size - i - 1) * Byte.SIZE));
        }
        return offset + size;
    }


    public static long decodeBigEndian(byte[] val, int offset, int size) {
        long rtn = 0;
        int len = offset + size - 1;
        for (int i = 0; i < size; i++) {
            if (len - i < val.length) {
                rtn = (rtn << Byte.SIZE) | ((long) val[len - i] & BYTEMASK);
            }
        }
        return rtn;
    }

    public static int decodeBigEndianInt(byte[] val, int offset, int size) {
        int rtn = 0;
        int len = offset + size - 1;
        for (int i = 0; i < size; i++) {
            if (len - i < val.length) {
                rtn = (rtn << Byte.SIZE) | ((int) val[len - i] & BYTEMASK);
            }
        }
        return rtn;
    }

    public static byte[] shortArray2byteArray(short[] array, boolean big) {
        byte[] byteArray = new byte[array.length * 2];
        byte[] temp = new byte[2];
        int index = 0;
        for (int i = 0; i < array.length; i++) {
            short item = array[i];
            if (big) {
                encodeBigEndian(temp, item, 0, 2);
            } else {
                encodeLittleEndian(temp, item, 0, 2);
            }
            byteArray[index] = temp[0];
            byteArray[index + 1] = temp[1];
            index += 2;
        }
        return byteArray;
    }

    public static short[] byteArray2shortArray(byte[] array, boolean big) {
        short[] shortArray = new short[array.length / 2];
        int index = 0;
        byte[] buf = new byte[2];
        for (int i = 0; i < array.length; ) {
            buf[0] = array[i];
            buf[1] = array[i + 1];
            short st = 0;
            if (big) {
                st = (short) decodeBigEndian(buf, 0, 2);
            } else {
                st = (short) decodeLittleEndian(buf, 0, 2);
            }
            shortArray[index] = st;
            index++;
            i += 2;
        }
        return shortArray;
    }


    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    // 是否触摸到这个View
    public static boolean isMotionInView(View v, MotionEvent ev) {
        Rect rect = new Rect();
        v.getGlobalVisibleRect(rect);
        if (rect.contains((int) ev.getX(), (int) ev.getY()))
            return true;
        return false;
    }


    /**
     * 隐藏键盘
     *
     * @param context
     * @param windowToken
     */
    public static void hideSoftInput(Context context, IBinder windowToken) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 显示键盘
     *
     * @param context
     * @param windowToken
     */
    public static void showSoftInput(Context context, IBinder windowToken) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInputFromInputMethod(windowToken, InputMethodManager.RESULT_SHOWN);
        //		im.showSoftInput(view, 0);
        // im.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
    }


    //    /**
    //     * 开始裁剪图片意图
    //     */
    //    public static void startTailoringPicture(Uri imageUri, Fragment fragment, int requestCode) {
    //        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    //        intent.setType("image/*");
    //        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
    //        intent.putExtra("crop", "true");
    //        // aspectX aspectY 是宽高的比例
    //        intent.putExtra("aspectX", 1);
    //        intent.putExtra("aspectY", 1);
    //        // outputX outputY 是裁剪图片宽高
    //        intent.putExtra("outputX", 400);
    //        intent.putExtra("outputY", 400);
    //        intent.putExtra("scale", true);
    //        intent.putExtra("scaleUpIfNeeded", true);
    //        intent.putExtra("return-data", false);
    //        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    //        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    //        intent.putExtra("noFaceDetection", true);
    //        fragment.startActivityForResult(intent, requestCode);
    //    }
    //
    //
    //    /**
    //     * 获取缓存目录
    //     *
    //     * @param context
    //     * @return
    //     */
    //    public static File getCacheDir(Context context) {
    //        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
    //            File[] dirs = ContextCompat.getExternalCacheDirs(context);
    //            if (!Tools.isEmpty(dirs)) {
    //                for (int i = 0; i < dirs.length; i++) {
    //                    try {
    //                    	SdkUtils.LogI("getCacheDir" + "dirs : " + dirs[i].getAbsolutePath());
    //                    } catch (Exception e) {
    //                        e.printStackTrace();
    //                        SdkUtils.LogI("getCacheDir" + "sdcard error");
    //                        return null;
    //                    }
    //                }
    //                return dirs[0];
    //            }
    //        }
    //        return context.getCacheDir();
    //    }

    /**
     * 数组是否为空
     *
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean isEmpty(T[] array) {
        return !(array != null && array.length > 0);
    }


    /**
     * 根据url得到bitmap
     *
     * @param url
     * @return
     */
    public static Bitmap getBitmapFromURL(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * 粉丝数显示方式
     *
     * @param fans 粉丝数
     * @return
     */
    public static String getFansDescribe(int fans) {
        if (fans < 10000) {
            return fans + "";
        } else {
            return String.format(Locale.CHINA, "粉丝 %.1f万", ((double) fans) / 10000);
        }
    }

    /**
     * 检查某个包是否已安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isPacageInstall(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }


    public static Intent packageIntent(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }


    /**
     * 打开地址
     *
     * @param url
     */
    public static void openUrl(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 处理参数越界
     *
     * @param x 横坐标
     * @param y 纵坐标
     */
    public static Point dealOverFlow(Context context, DisplayMetrics displayMetrics, View contentView, int x, int y) {
        int tempx = 0;
        int tempy = 0;

        int trueWidth = 0;//手机真实宽度
        int trueHeight = 0;//真实高度
        if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
            trueWidth = displayMetrics.heightPixels;
            trueHeight = displayMetrics.widthPixels;
        } else {
            trueWidth = displayMetrics.widthPixels;
            trueHeight = displayMetrics.heightPixels;
        }


        int statusBarHeight;
        if (Constants.ISFULLSCREEN) {
            statusBarHeight = 0;
        } else {
            statusBarHeight = Tools.getStatusBarHeight(context);//状态栏高度
        }
        //获取屏幕方向
        Configuration configuration = context.getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {//横屏
            if (x < 0) {
                tempx = 0;
            } else if (x < trueHeight - contentView.getWidth()) {
                tempx = x;
            } else {
                tempx = trueHeight - contentView.getWidth();
            }
            if (y < 0) {
                tempy = 0;
            } else if (y < (trueWidth - contentView.getHeight() - statusBarHeight)) {
                tempy = y;
            } else {
                tempy = trueWidth - contentView.getHeight() - statusBarHeight;
            }
        } else {//竖屏
            if (x < 0) {
                tempx = 0;
            } else if (x < trueWidth - contentView.getWidth()) {
                tempx = x;
            } else {
                tempx = trueWidth - contentView.getWidth();
            }
            if (y < 0) {
                tempy = 0;
            } else if (y < (trueHeight - contentView.getHeight() - statusBarHeight)) {
                tempy = y;
            } else {
                tempy = trueHeight - contentView.getHeight() - statusBarHeight;
            }
        }

        return new Point(tempx, tempy);
    }


    /**
     * 根据uri获取文件
     *
     * @param uri
     * @param mContext
     * @return
     */
    public static Bitmap getBitmapFromUri(Uri uri, Context mContext) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取版本号
     *
     * @return
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
        }
        return version;
    }


    /**
     * 网络类型
     *
     * @return
     */
    public static int networkType(Context context) {
        NetworkInfo networkInfo = (NetworkInfo) ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return Constants.NETWORK_TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        return Constants.NETWORK_TYPE_2G;

                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        return Constants.NETWORK_TYPE_3G;

                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        return Constants.NETWORK_TYPE_4G;

                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            return Constants.NETWORK_TYPE_3G;
                        } else {
                            SdkUtils.LogE("未知网络类型", _strSubTypeName);
                            return Constants.NETWORK_TYPE_OTHER;
                        }
                }


            }
        }
        return 0;
    }

    /**
     * 应用版本
     *
     * @param context
     */
    public static String getAppVersionInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);

            String versionName = pi.versionName;
            int versionCode = pi.versionCode;

            if (versionName == null || versionName.length() <= 0) {
                return "";
            }

            return versionName + versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 可用内存
     *
     * @param context
     * @return
     */
    public static String getAvailMemory(Context context) {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
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
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                SdkUtils.LogI(str2, num + "\t");
            }

            initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }


    /**
     * 保存用户信息
     */
    public static void SaveUserInfo(Users user) {
        NetCache.getInstance().addCache(Constants.USERINFO, user);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static Users getUserInfo() {
        Object object = NetCache.getInstance().getObjectCache(Constants.USERINFO);
        if (object != null) {
            return (Users) object;
        }
        return null;
    }

    /**
     * 显示提示
     *
     * @param tip
     */
    public static void tips(Context context, String tip) {
        Toast toast = Toast.makeText(context, tip, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.show();
    }


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param context
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        Log.e("scale", scale + "");
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 是否横屏
     *
     * @return
     */
    public static boolean isLandscape(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取屏幕参数
     *
     * @param activity
     */
    public static void getDisplayParams(Activity activity) {
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        getDisplayParams(activity);
        //    	int width = 0;
        //    	Log.e("TOOLS", displayMetrics.heightPixels+","+displayMetrics.widthPixels);
        //    	if(isLandscape(activity)){
        //    		width =  displayMetrics.heightPixels;
        //    	}else{
        //    		width =  displayMetrics.widthPixels;
        //    	}
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Activity activity) {
        getDisplayParams(activity);
        //    	if(isLandscape(activity)){
        //    		return displayMetrics.widthPixels;
        //    	}else{
        //    		return displayMetrics.heightPixels;
        //    	}
        return displayMetrics.heightPixels;
    }

    /**
     * 菜单默认位置Y坐标
     *
     * @param activity
     * @return
     */
    public static int getControlMenuY(Activity activity) {
        return getScreenHeight(activity) - dip2px(activity, Constants.CONTROLLER_WIDTH);//默认放在左下角
    }

    /**
     * 屏幕参数
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics() {
        return displayMetrics;
    }

    public static int getResourse(Context context, String type, String value) {
        return context.getResources().getIdentifier(value, type, context.getPackageName());
    }

}
