package com.zeustel.top9.bean;

/**
 * 用户信息
 * @author NiuLei
 * @date 2015/6/24 21:38
 */
public class UserInfo extends SubUserInfo{

    //用户名
    private String username;
    //邮箱
    private String email = null;
    //手机号
    private String phone = null;
    //积分总数
    private int integralAmount = 0;
    //鲜花总数
    private int flowerAmount = 0;
    //登录平台
    private int loginPlatform = LoginPlatform.DEFAULT;
    //注册时间
    private long time;
    //是否在线
    private boolean isOnline = false;
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIntegralAmount() {
        return integralAmount;
    }

    public void setIntegralAmount(int integralAmount) {
        this.integralAmount = integralAmount;
    }

    public int getFlowerAmount() {
        return flowerAmount;
    }

    public void setFlowerAmount(int flowerAmount) {
        this.flowerAmount = flowerAmount;
    }

    public int getLoginPlatform() {
        return loginPlatform;
    }

    public void setLoginPlatform(int loginPlatform) {
        this.loginPlatform = loginPlatform;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    /**
     * 登陆平台
     *
     * @author NiuLei
     * @date 2015/6/24 21:42
     */
    public class LoginPlatform {
        //Top9注册
        public static final int DEFAULT = 0;
        //QQ用户注册
        public static final int QQ = 1;
        //微信用户注册
        public static final int WECHAT = 2;
    }
}
