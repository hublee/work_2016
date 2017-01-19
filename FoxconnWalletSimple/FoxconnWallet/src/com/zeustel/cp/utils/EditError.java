package com.zeustel.cp.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/7/6 17:12
 */
public enum EditError {
    ACCOUNT_EMPTY("Account is null"),
    ACCOUNT_FORMAT("Email format is wrong"),
    VER_EMPTY("Verification code is null"),
    PWD_EMPTY("Password is null"),
    PWD_DIFF("Passwords don't match"),
    NONE("SUCCESS"),
    PWD_FORMAT("Password format for 6 ~ 20 letters, Numbers, underscores");

    private String msg;

    EditError(String msg) {
        this.msg = msg;
    }
    public String errMsg(){
        return msg;
    }
    public void alert(Context context){
        Toast.makeText(context,errMsg(), Toast.LENGTH_SHORT).show();
    }
}
