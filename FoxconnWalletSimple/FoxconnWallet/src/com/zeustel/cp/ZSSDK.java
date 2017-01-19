package com.zeustel.cp;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.zeustel.cp.activity.WebActivity;
import com.zeustel.cp.bean.CPInfo;
import com.zeustel.cp.bean.ErrorMsg;
import com.zeustel.cp.bean.FmMenu;
import com.zeustel.cp.bean.HttpCommand;
import com.zeustel.cp.bean.PopView;
import com.zeustel.cp.bean.ServerInfo;
import com.zeustel.cp.bean.Users;
import com.zeustel.cp.bean.ViewFactory;
import com.zeustel.cp.intf.CPCallBack;
import com.zeustel.cp.intf.ExchangeCallBack;
import com.zeustel.cp.intf.ICommand;
import com.zeustel.cp.intf.ILocation;
import com.zeustel.cp.intf.IView;
import com.zeustel.cp.intf.NextCallBack;
import com.zeustel.cp.net.RequestManager;
import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.CrashHandler;
import com.zeustel.cp.utils.NetCache;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.Tools;
import com.zeustel.cp.utils.UserManager;
import com.zeustel.cp.views.AnimController;
import com.zeustel.cp.views.FMControlView;
import com.zeustel.cp.views.FloatFM;
import com.zeustel.cp.views.FloatHelper;
import com.zeustel.foxconn.cp_sdk.R;

import java.util.HashMap;

/**
 * 宙斯CP_SDK
 *
 * @author Do
 */
public class ZSSDK implements ILocation, IView, OnDismissListener {
    static volatile ZSSDK defaultInstance;

    private Activity activity;
    private ViewFactory viewFactory;// 视图工厂

    public static FloatFM floatFM;// 悬浮
    private FloatHelper mFloatHelper = null;

    private static int pointX;// 移动后的坐标x
    private static int pointY;// 移动后的坐标y

    private boolean isShow;// 菜单是否显示
    private static PopView controlMenuView;// 菜单

    private CPCallBack callBack;

    private RequestManager requestManager;

    private String authCode;

    private String mobile;

    private int location = 0;// 悬浮 左右位置

    private ExchangeCallBack exchangeCallBack;// 兑换回调

    private ICommand iCommand;

    private PopupWindow popupWindow;// 新的菜单
    // private Controller controller;//新菜单视图
    private AnimController controller;// 带动画的视图

    // private NewFmContrller controller;

    private boolean isUsercenterShow = false;

    private Users users = null;

    private ZSSDK() {
        iCommand = new HttpCommand();
        requestManager = new RequestManager();
    }

    public static ZSSDK getDefault() {
        if (defaultInstance == null) {
            synchronized (ZSSDK.class) {
                if (defaultInstance == null) {
                    defaultInstance = new ZSSDK();
                }
            }
        }
        return defaultInstance;
    }

    /**
     * 初始化sdk
     *
     * @param activity
     */
    public void initSDK(final Activity activity, boolean debugMode, String appId, String appKey, CPCallBack callBack) {
        if (this.activity != null) {
            return;
        }

        this.activity = activity;
        SdkUtils.DEBUGMODE = debugMode;
        this.callBack = callBack;

        if (appId == null || appKey == null) {
            SdkUtils.showTips(activity, ErrorMsg.CP_INFO_PARAMS_NULL);
            return;
        }

        if (callBack == null) {
            SdkUtils.showTips(activity, ErrorMsg.CP_CALLBACK_NULL);
            return;
        }

        registActivityLifecycleCallbacks(activity);
        // 缓存
        initCache();
        saveCpinfo(new CPInfo(appId, appKey));

        // 视图工厂
        initFactory(activity);
        // 悬浮
        initFloatView(activity);
        View decorView = activity.getWindow().getDecorView();
        if (decorView != null) {
            System.out.println("decorView != null");
            if (decorView instanceof ViewGroup) {
                if (((ViewGroup) decorView).getChildCount() > 0) {
                    System.out.println("count : " + ((ViewGroup) decorView).getChildCount());
                    decorView = ((ViewGroup) decorView).getChildAt(0);
                    if (decorView instanceof ViewGroup) {
                        System.out.println("count : " + ((ViewGroup) decorView).getChildCount());
                        decorView = ((ViewGroup) decorView).getChildAt(0);
                    }
                }
            }
        } else {
            System.out.println("decorView == null");
        }

        // FloatButton.getInstance().initButton(activity,decorView);

        // 错误处理
        // initCrash();
        controller = new AnimController(activity);

        iCommand.setContext(activity);

        getConfig(debugMode);
    }

    /**
     * 获取配置信息
     */
    private void getConfig(final boolean debugMode) {
        Log.e("getConfig SUCCESS", "debugMode : " + debugMode);
        iCommand.getConfig(requestManager, new NextCallBack() {
            @Override
            public void callBack(int code, String msg) {

                // 处理结果
                switch (code) {
                    case ZSStatusCode.SUCCESS:
                        Log.e("getConfig SUCCESS", "code:" + code + ",msg:" + msg);
                        init(debugMode);
                        break;
                    default:
                        Log.e("getConfig", "code:" + code + ",msg:" + msg);
                        break;
                }
            }
        });
    }

    public ICommand getiCommand() {
        return iCommand;
    }

    public void setiCommand(ICommand iCommand) {
        this.iCommand = iCommand;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Users getUsers() {
        return users;
    }

    /**
     * 初始化
     */
    private void init(boolean debugMode) {
        Log.e("init : ", "init");
        // 服务器ip
        initServerIp(debugMode);
        // 验证信息
        cpAuth();

    }

    /**
     * token登录
     * @param callBack
     */
    public void tokenLogin(String token, NextCallBack callBack) {
        Log.i("tokenLogin",  "tokenLogin (line 228): "+token);
        iCommand.tokenLogin(token, requestManager, callBack);
    }
    public void getCodeViaMail(String email,  final NextCallBack callBack) {
        iCommand.getCodeViaMail(email,requestManager,callBack);
    }
    public void retrieveViaEmail(String email, String password, String digitalCode, final NextCallBack callBack){
        iCommand.retrieveViaEmail(email,password,digitalCode,requestManager,callBack);
    }
    // ////////

    public void thirdLogin(String openId, String platform, String userinfo, NextCallBack mNextCallBack) {
        iCommand.thirdLogin(openId, platform, userinfo, requestManager, mNextCallBack);
    }

    public void bindThirdUser(String openid, String platform, String userId, NextCallBack callBack) {
        iCommand.bindThirdUser(openid, platform, userId, requestManager, callBack);
    }

    public void registViaEmail(String email, String password, NextCallBack mNextCallBack) {
        iCommand.registViaEmail(email, password, requestManager, mNextCallBack);
    }

    public void loginViaEmail(String email, String password, NextCallBack mNextCallBack) {
        iCommand.loginViaEmail(email, password, requestManager, mNextCallBack);

    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    //
    // public void retrieveViaEmail(String email, String password,
    // String digitalCode) {
    // iCommand.retrieveViaEmail(email, password, digitalCode, requestManager,
    // new NextCallBack() {
    // @Override
    // public void callBack(int code, String msg) {
    // // TODO Auto-generated method stub
    // // 处理结果
    // Log.e("retrieveViaEmail", "code:" + code + ",msg:"
    // + msg);
    // switch (code) {
    // case ZSStatusCode.SUCCESS:
    // break;
    // default:
    // Log.e("getConfig", "code:" + code + ",msg:" + msg);
    // break;
    // }
    // }
    // });
    // }
    //
    // public void getCodeViaMail(String email) {
    // iCommand.getCodeViaMail(email, requestManager, new NextCallBack() {
    // @Override
    // public void callBack(int code, String msg) {
    // // TODO Auto-generated method stub
    // // 处理结果
    // Log.e("getCodeViaMail", "code:" + code + ",msg:" + msg);
    // switch (code) {
    // case ZSStatusCode.SUCCESS:
    // break;
    // default:
    // Log.e("getConfig", "code:" + code + ",msg:" + msg);
    // break;
    // }
    // }
    // });
    // }

    // /////////

    /**
     * 设置服务器ip
     *
     * @param debugMode
     */
    private void initServerIp(boolean debugMode) {
        ServerInfo info = (ServerInfo) NetCache.getInstance().getObjectCache(Constants.SERVERINFO);

        Constants.setBaseUrl(debugMode ? info.getTestServer() : info.getServer());
    }

    /**
     * 保存cp信息
     *
     * @param info
     */
    private void saveCpinfo(CPInfo info) {
        NetCache.getInstance().addCache(Constants.CPINFO, info);
    }

    /**
     * 初始化视图工厂
     *
     * @param context
     */
    private void initFactory(Context context) {
        viewFactory = ViewFactory.getInstance();
        viewFactory.setContext(context);
        viewFactory.addViewListener(this);// 添加监听
    }

    /**
     * 初始化悬浮
     */
    private void initFloatView(Activity activity) {
        pointX = 0;
        pointY = Tools.getControlMenuY(activity);// 悬浮默认Y坐标

        if (mFloatHelper == null) {
            mFloatHelper = new FloatHelper(activity, FloatHelper.FloatType.TYPE_SERVICE, true);
            floatFM = new FloatFM(activity);
            floatFM.setLocation(this);

            floatFM.setOnFmControlListener(new FMControlView.OnFmControlListener() {
                @Override
                public void onClick() {
                    // TODO Auto-generated method stub
                    controllerClick();
                }

                @Override
                public void onDoubleClick() {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onLongPress() {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onOutSide() {
                    // TODO Auto-generated method stub

                }
            });

        }
    }

    /**
     * 初始错误处理
     */
    private void initCrash() {
        CrashHandler.getInstance().init(activity);
    }

    /**
     * 初始缓存
     */
    private void initCache() {
        NetCache.getInstance().openObjectCache(activity);
    }

    /**
     * 点击菜单
     */
    private void controllerClick() {
        addPopWindow();

        controller.statrtAnim();
    }

    /**
     * 添加菜单
     */
    private void addController() {
        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        mFloatHelper.addView(controlMenuView, FloatHelper.getWmParamsAnimation(SdkUtils.getControlMenuX(activity, pointX), pointY, SdkUtils.getControlMenuWidth(activity), SdkUtils.getControlMenuHeight(activity), 1, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT), false);
        controlMenuView.startAnimation(animation);
    }

    @Override
    public void setLocation(int x, int y) {
        pointX = x;
        pointY = y;

        /**
         * 移动图标时菜单消失
         */
        // distroyControlMenu();

        distroyController();
    }

    @Override
    public void dropView(final View view) {
        // TODO Auto-generated method stub
        new Handler().post(new Runnable() {
            public void run() {
                mFloatHelper.removeView(view);
            }
        });
    }

    private PopView popView;

    @Override
    public View addView(int viewId) {
        // TODO Auto-generated method stub
        popView = viewFactory.createView(viewId);// 创建视图
        if (popView != null) {
            popView.addViewListener(this);
            addView(popView, viewId, popView.getViewWidth(), popView.getViewHeight(), popView.isNeedMove());
        }
        distroyController();// 关闭菜单
        return popView;
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 添加视图
     *
     * @param view
     */
    private void addView(View view, int viewId, int width, int height, boolean needMove) {
        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        if (Tools.getAndroidSDKVersion() > 18) {
            mFloatHelper.addView(view, FloatHelper.getWmParamsAnimation((Tools.getDisplayMetrics().widthPixels - width) / 2, 1, width, height, 1, WindowManager.LayoutParams.TYPE_TOAST), needMove);
        } else {
            mFloatHelper.addView(view, FloatHelper.getWmParamsAnimation((Tools.getDisplayMetrics().widthPixels - width) / 2, 1, width, height, 1, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT), needMove);
        }
        view.startAnimation(animation);
    }

    // /**
    // * 移除旧菜单
    // */
    // public void distroyControlMenu(){
    // if (isShow){
    // ViewFactory.getInstance().closeController(controlMenuView);
    // isShow = false;
    // }
    // }

    /**
     * 获取授权码
     */
    private void cpAuth() {
        iCommand.cpAuth(requestManager, new NextCallBack() {
            @Override
            public void callBack(int code, String msg) {
                // TODO Auto-generated method stub
                // 处理结果
                switch (code) {
                    case ZSStatusCode.SUCCESS:
                        callBack.callBack(ZSStatusCode.SUCCESS, authCode);
                        break;
                    default:
                        callBack.callBack(code, msg);
                        break;
                }
            }
        });
    }

    /**
     * 设置用户游戏币总数
     *
     * @param total
     */
    public void setTotal(int total) {
        /*
		 * // 未获取到授权码 if (TextUtils.isEmpty(authCode)) {
		 * callBack.callBack(ZSStatusCode.CP_INFO_ERROR,
		 * ZSStatusCode.CP_INFO_ERROR_MSG); return; }
		 * 
		 * if (total < 0) { callBack.callBack(ZSStatusCode.TOTAL_ERROR,
		 * ZSStatusCode.TOTAL_ERROR_MSG); return; }
		 */
        SdkUtils.total = total;
    }

    // /**
    // * 兑换接口，直接调用发起兑换
    // * @param authCode 授权码
    // * @param total 用户游戏币总数
    // */
    // private void exchange(String authCode,int total){
    // if(TextUtils.isEmpty(authCode)){
    // callBack.callBack(ZSStatusCode.AUTH_CODE_NULL,
    // ZSStatusCode.AUTH_CODE_NULL_MSG);
    // return;
    // }
    // if(total<0){
    // callBack.callBack(ZSStatusCode.TOTAL_ERROR,
    // ZSStatusCode.TOTAL_ERROR_MSG);
    // return;
    // }
    // SdkUtils.total = total;
    // addView(Constants.EXCHANGE_VIEWID);
    // }

    // /**
    // * 兑换接口，直接调用发起兑换
    // * @param authCode 授权码
    // */
    // public void exchange(String authCode){
    // if(TextUtils.isEmpty(authCode)){
    // callBack.callBack(ZSStatusCode.AUTH_CODE_NULL,
    // ZSStatusCode.AUTH_CODE_NULL_MSG);
    // return;
    // }
    // addView(Constants.EXCHANGE_VIEWID);
    // }

    /**
     * 发送验证码
     *
     * @param phoneno
     */
    public void sendSMS(String phoneno, NextCallBack callBack) {
        iCommand.sendSMS(requestManager, phoneno, callBack);
    }

    // /**
    // * 兑换
    // * @param phoneno
    // * @param callBack
    // */
    // public void exchange(String phoneno,int amount,NextCallBack callBack){
    // // iCommand.exchange(requestManager, phoneno, amount, callBack);
    // }

    /**
     * 验证
     *
     * @param phoneno
     * @param callBack
     */
    public void checkCode(String phoneno, String checkCode, NextCallBack callBack) {
        iCommand.checkCode(requestManager, phoneno, checkCode, callBack);
    }

    /**
     * 设置授权码
     *
     * @param authCode
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    /**
     * 获取授权码
     *
     * @return
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * 登出，删除缓存
     */
    public void logOut() {
        NetCache.getInstance().remove(Constants.AUTHINFO);
        NetCache.getInstance().remove(Constants.COININFO);
        NetCache.getInstance().remove(Constants.USERINFO);
    }

    // /**
    // * 用户信息
    // * @param callBack
    // */
    // public void getUserInfo(NextCallBack callBack){
    // iCommand.login(requestManager, callBack);
    // }

    /**
     * 用户中心
     */
    public void userCenter() {
        // Intent intent = new Intent();
        // intent.setClassName(activity,
        // "com.zeustel.cp.activity.UserInfoActivity");
        // activity.startActivity(intent);
        // isUsercenterShow = true;
//        getWebIntent("/index.jsp");
        getWebIntent("/seasdk/ACCOUNT.html");
    }

    public void community() {
        // Intent intent = new Intent();
        // intent.setClassName(activity,
        // "com.zeustel.cp.activity.ForumActivity");
        // activity.startActivity(intent);
        // isUsercenterShow = true;
        getWebIntent("/community/community.jsp");
    }

    public void toGame() {
        // Intent intent = new Intent();
        // intent.setClassName(activity, "com.zeustel.cp.activity.WebActivity");
        // intent.putExtra("url",
        // WebActivity.generateUrl(SdkUtils.getServerInfo()
        // .getMsdk() + "/seasdk/Game.html", SdkUtils.getUserId(),
        // getServer()));
        // activity.startActivity(intent);
        // isUsercenterShow = true;
        getWebIntent("/seasdk/Game.html");
    }

    public void toGift() {
        // Intent intent = new Intent();
        // intent.setClassName(activity, "com.zeustel.cp.activity.WebActivity");
        // ServerInfo serverInfo = SdkUtils.getServerInfo();
        //
        // intent.putExtra("url",
        // WebActivity.generateUrl(SdkUtils.getServerInfo()
        // .getMsdk() + "/seasdk/GiftListing.html", SdkUtils.getUserId(),
        // serverInfo.getServer()));
        // activity.startActivity(intent);
        //
        getWebIntent("/seasdk/GiftListing.html");

    }


    /**
     * 资讯
     */
    public void toInformation() {
        getWebIntent("/seasdk/InformationPage.html");
    }

    private void getWebIntent(String url) {
        Object obj = NetCache.getInstance().getObjectCache(Constants.CPINFO);
        if (obj == null) {
            return;
        }
        CPInfo cpInfo = (CPInfo) obj;
        // 公共头
        HashMap<String, String> head = new HashMap<String, String>();
        head.put("token", SdkUtils.getToken());
        head.put("baseurl", SdkUtils.getServerInfo().getServer());
        head.put("appId", cpInfo.getAppId());
        head.put("deviceInfo", SdkUtils.getDevicesInfo(activity));
        head.put("osType", "0");// 安卓

        Intent intent = new Intent();
        intent.setClassName(activity, "com.zeustel.cp.activity.WebActivity");
        intent.putExtra("url", WebActivity.generateUrl(SdkUtils.getServerInfo().getMsdk() + url, SdkUtils.getUserId(), getServer()));
        activity.startActivity(intent);
        isUsercenterShow = true;
    }

    private String getServer() {
        ServerInfo serverInfo = SdkUtils.getServerInfo();
        String server = serverInfo.getServer();
        String http = "http://";
        server = server.substring(http.length(), server.length());
        return server;
    }

    /**
     * 设置悬浮按钮位置
     *
     * @param location
     */
    public void setFloatButtonLocation(int location) {
        if (location < 0 || location != 0 || location != 1) {
            location = 0;
        }

        this.location = location;
    }

    /**
     * 设置手机号
     *
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
        Users user = new Users();
        user.setPhoneno(mobile);
        NetCache.getInstance().addCache(Constants.USERINFO, user);
    }

    /**
     * 获取手机号
     *
     * @return
     */
    public String getMobile() {
        Object obj = NetCache.getInstance().getObjectCache(Constants.USERINFO);
        if (obj == null) {
            return null;
        }
        return ((Users) obj).getPhoneno();
    }

    /**
     * 显示兑换结果
     *
     * @param exchangeSuccess true,兑换成功;false,兑换失败
     */
    public void showExchangeResult(boolean exchangeSuccess) {
        if (exchangeSuccess) {
            addView(Constants.EXCHANGE_OK_VIEWID);
        } else {
            addView(Constants.EXCHANGE_ERROR_VIEWID);
        }
    }
    public void unRegister(){

    }
    public void loginOut(){
        UserManager.getInstance(activity).setOnline(false);
    }
    /**
     * 注册兑换回调
     *
     * @param callBack
     */
    public void registExchangeCallBack(ExchangeCallBack callBack) {
        exchangeCallBack = callBack;
    }

    public ExchangeCallBack getExchangeCallBack() {
        return exchangeCallBack;
    }

    /**
     * 销毁
     */
    public void destroyFloatButton() {
        if (floatFM != null) {
            floatFM.destroy();
        }

        if (isShow) {
            // distroyControlMenu();
            distroyController();
        }

        logOut();

        ViewFactory.getInstance().destroy();
    }

    /**
     * 隐藏悬浮
     */
    public void hideFloatButton() {
        floatFM.hide();
    }

    /**
     * 显示悬浮
     */
    public void showFloatButton() {
        floatFM.show();
    }

    private void registActivityLifecycleCallbacks(final Activity act) {
        act.getApplication().registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStopped(Activity activity) {
                // Log.e("registActivityLifecycleCallbacks",
                // "onActivityStopped"+","+activity.getLocalClassName());
                distroyController();
                if (activity.getLocalClassName().equals("com.zeustel.cp.activity.UserInfoActivity") || activity.getLocalClassName().equals("com.zeustel.cp.activity.WebActivity")) {
                    return;
                }
                hideFloatButton();

                hidePopViews();
            }

            @Override
            public void onActivityStarted(Activity activity) {
                // Log.e("registActivityLifecycleCallbacks",
                // "onActivityStarted"+","+activity.getLocalClassName());
                distroyController();

                if (activity.getLocalClassName().equals("com.zeustel.cp.activity.UserInfoActivity") || activity.getLocalClassName().equals("com.zeustel.cp.activity.WebActivity")) {
                    return;
                }
                showFloatButton();
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                // Log.e("registActivityLifecycleCallbacks",
                // "onActivitySaveInstanceState"+","+activity.getLocalClassName());
                distroyController();
                if (activity.getLocalClassName().equals("com.zeustel.cp.activity.UserInfoActivity") || activity.getLocalClassName().equals("com.zeustel.cp.activity.WebActivity")) {
                    return;
                }
                hideFloatButton();
            }

            @Override
            public void onActivityResumed(Activity activity) {
                // Log.e("registActivityLifecycleCallbacks",
                // "onActivityResumed"+","+activity.getLocalClassName());
                distroyController();
            }

            @Override
            public void onActivityPaused(Activity activity) {
                // Log.e("registActivityLifecycleCallbacks",
                // "onActivityPaused"+","+activity.getLocalClassName());
                distroyController();
                if (activity.getLocalClassName().equals("com.zeustel.cp.activity.UserInfoActivity") || activity.getLocalClassName().equals("com.zeustel.cp.activity.WebActivity")) {
                    return;
                }

                hideFloatButton();
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                // Log.e("registActivityLifecycleCallbacks",
                // "onActivityDestroyed"+","+activity.getPackageName());
                distroyController();
                if (activity.getLocalClassName().equals("com.zeustel.cp.wallet.WalletActivity")) {
                    showFloatButton();
                    return;
                }
                if (activity.getLocalClassName().equals("com.zeustel.cp.activity.UserInfoActivity") || activity.getLocalClassName().equals("com.zeustel.cp.activity.WebActivity")) {
                    return;
                }
                hideFloatButton();
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                // Log.e("registActivityLifecycleCallbacks",
                // "onActivityCreated"+","+activity.getPackageName());
                distroyController();
            }

        });
    }

    /**
     * 新的菜单
     */
    private synchronized void addPopWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            isShow = false;
            return;
        }

        popupWindow = new PopupWindow(activity);
        popupWindow.setContentView(controller);
        popupWindow.setTouchable(true);
        popupWindow.setClippingEnabled(false);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setWidth(Tools.dip2px(activity, Constants.CONTROLCENTER_WIDTH));
        popupWindow.setHeight(Tools.dip2px(activity, Constants.CONTROLLER_WIDTH));
        // popupWindow.setAnimationStyle(R.style.pop_window_style);
        // popupWindow.showAtLocation(floatFM.getFMControlView(), 0,
        // SdkUtils.isLeft?tempX+28:tempX-28, 0);
        SdkUtils.getControllerMenuX(activity, pointX);
        int controlWidth = Tools.dip2px(activity, Constants.CONTROLLER_WIDTH);
        popupWindow.showAtLocation(floatFM.getFMControlView(), SdkUtils.isLeft ? Gravity.START : Gravity.END, 28 + controlWidth, 0);
        popupWindow.setOnDismissListener(this);
        popupWindow.update();
        isShow = true;
    }

    /**
     * 移除新的菜单
     */
    public void distroyController() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            isShow = false;
            return;
        }
    }

    /**
     * 电台菜单
     */
    public void addFmController() {
        FmMenu.getInstance().createFmMenu(activity, floatFM.getFMControlView(), pointX, pointY);
        // addView(Constants.NEWPOP_VIEWID);
        distroyController();
    }

    /**
     * 移除新的菜单
     */
    public void distroyFmController() {
        // if(popupWindow != null && popupWindow.isShowing()){
        // popupWindow.dismiss();
        // isShow = false;
        // return;
        // }
        FmMenu.getInstance().destroyFmMenu();
    }

    @Override
    public void onDismiss() {
        // TODO Auto-generated method stub
        controller.forceClose();
        popupWindow = new PopupWindow(controller, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
    }


    public void validBind(String openid, String platform, NextCallBack callBack) {
        iCommand.validBind(openid, platform, requestManager, callBack);
    }


    /**
     * 校验token
     *
     * @param callBack
     */
    public void validToken(NextCallBack callBack) {
        iCommand.validToken(requestManager, callBack);
    }

    /**
     * 重新验证
     */
    public void reCheck() {
        logOut();
        addView(Constants.CHECK_VIEWID);
    }

    /**
     * 关闭悬浮
     */
    private void hidePopViews() {
        if (popView != null) {
            popView.close();
        }
    }

}
