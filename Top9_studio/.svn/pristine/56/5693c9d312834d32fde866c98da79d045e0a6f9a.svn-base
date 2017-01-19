package com.zeustel.top9.assist.share;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.zeustel.top9.R;
import com.zeustel.top9.base.WrapNotActivity;
import com.zeustel.top9.bean.SubUserInfo;
import com.zeustel.top9.bean.UserInfo;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.result.UserInfoResult;
import com.zeustel.top9.utils.SimpleResponseHandler;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataUser;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/11 14:33
 */
public abstract class LoginBaseActivity extends WrapNotActivity implements PlatformActionListener {
    private static final int MSG_AUTH_CANCEL = 13;
    private static final int MSG_AUTH_ERROR = 14;
    private static final int MSG_AUTH_COMPLETE = 15;
    private static final int MSG_WECHAT_ERROR = 16;//微信登陆失败
    private ProgressDialog loginDialog;
    SimpleResponseHandler LoginSimpleResponseHandler = new SimpleResponseHandler() {
        @Override
        public void onCallBack(int code, final String json) {
            super.onCallBack(code, json);
            new Handler(Looper.myLooper()).post(new Runnable() {
                @Override
                public void run() {
                    endLogin();
                    Gson gson = new Gson();
                    UserInfoResult result = gson.fromJson(json, UserInfoResult.class);
                    if (result != null) {
                        if (Result.SUCCESS == result.getSuccess()) {
                            Tools.showToast(LoginBaseActivity.this, R.string.login_success);
                            endLogin(true);
                        } else {
                            Tools.showToast(LoginBaseActivity.this, result.getMsg());
                            endLogin(false);
                        }
                        return;
                    }
                    Tools.showToast(LoginBaseActivity.this, R.string.login_failed);
                    endLogin(false);
                }
            });
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUTH_CANCEL: {
                    Tools.showToast(LoginBaseActivity.this, R.string.auth_cancel);
                }
                break;
                case MSG_AUTH_ERROR: {
                    //                    Tools.showToast(LoginBaseActivity.this, R.string.auth_error);
                }
                break;
                case MSG_AUTH_COMPLETE: {
                    //                    Tools.showToast(LoginBaseActivity.this, R.string.auth_complete);
                }
                break;
                case MSG_WECHAT_ERROR: {
                    Tools.showToast(LoginBaseActivity.this, R.string.wechat_client_inavailable);
                }
                break;
            }

        }
    };

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    protected void startLogin() {
        if (loginDialog == null) {
            loginDialog = ProgressDialog.show(this, "", getString(R.string.login_ing));
        } else {
            loginDialog.show();
        }
    }

    protected void endLogin() {
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
    }

    protected abstract void endLogin(boolean success);

    protected void login(String account, String password) throws Exception {
        startLogin();
        DataUser.toLogin(this, account, password, LoginSimpleResponseHandler);
    }

    protected void login(Platform plat) {
        authorize(plat);
    }

    private void authorize(Platform plat) {
        if (plat == null) {
            return;
        }
        if (plat.isValid()) {
            plat.removeAccount();
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(true);
        plat.showUser(null);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> hashMap) {
        Tools.log_i(LoginBaseActivity.class,"onComplete","");
        if (action == Platform.ACTION_USER_INFOR && hashMap != null && platform != null) {
            Tools.log_i(LoginBaseActivity.class,"onComplete","ACTION_USER_INFOR");
            try {
                PlatformDb db = platform.getDb();
                String platformName = platform.getName();
                String userGender = db.getUserGender();
                String icon = db.getUserIcon();
                String nickName = db.getUserName();
                final String openId = hashMap.get("openid").toString();

                int gender = SubUserInfo.Gender.GENTLEMEN;
                int loginPlatform = UserInfo.LoginPlatform.DEFAULT;
                if (QQ.NAME.equals(platformName)) {
                    loginPlatform = UserInfo.LoginPlatform.QQ;
                    // qq 性别为 男女
                    if (userGender.equals(getString(R.string.regist_lady))) {
                        gender = SubUserInfo.Gender.LADY;
                    } else if (userGender.equals(getString(R.string.regist_gentlemen))) {
                        gender = SubUserInfo.Gender.GENTLEMEN;
                    }
                } else if (Wechat.NAME.equals(platformName)) {
                    loginPlatform = UserInfo.LoginPlatform.WECHAT;
                    // 因为微信该字段为空 所以用hashMap获取
                    Object obj = hashMap.get("sex");
                    if (obj != null) {
                        if (obj.toString().equals(String.valueOf(0))) {
                            gender = SubUserInfo.Gender.LADY;
                        } else {
                            gender = SubUserInfo.Gender.GENTLEMEN;
                        }
                    }
                }
                final UserInfo userInfo = new UserInfo();
                userInfo.setIcon(icon);
                userInfo.setNickName(nickName);
                userInfo.setLoginPlatform(loginPlatform);
                userInfo.setGender(gender);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        startLogin();
                        Tools.log_i(LoginBaseActivity.class, "onComplete", "");
                        DataUser.toLogin(LoginBaseActivity.this, userInfo, openId, LoginSimpleResponseHandler);
                    }
                });
                handler.sendEmptyMessage(MSG_AUTH_COMPLETE);
            } catch (Exception e) {
                e.printStackTrace();
                Tools.log_i(LoginBaseActivity.class,"onComplete","eooooooooo");
            }
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable throwable) {
        Tools.log_i(LoginBaseActivity.class,"onError","action :" +action);
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_ERROR);
            if (Wechat.NAME.equals(platform.getName())) {
                handler.sendEmptyMessage(MSG_WECHAT_ERROR);
            }
        }
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            handler.sendEmptyMessage(MSG_AUTH_CANCEL);
        }
    }
}
