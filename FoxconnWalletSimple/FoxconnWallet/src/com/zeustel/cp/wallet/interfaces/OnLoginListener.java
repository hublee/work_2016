package com.zeustel.cp.wallet.interfaces;

import android.view.View;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/7/1 13:01
 */
public interface OnLoginListener {
    /**
     * 登录类型
     */
    enum LoginType {
        /**
         * 快速
         */
        LOGIN_QUICK(1),
        /**
         * 会员
         */
        LOGIN_MEMBER(2),
        /**
         * facebook
         */
        LOGIN_FACEBOOK(3),
        /**
         * ....
         */
        LOGIN_MIGME(3),;
        private int type;

        LoginType(int type) {
            this.type = type;
        }
        int type() {
            return type;
        }
    }

    void onLogin(View view, LoginType loginType,Object ...args);
}
