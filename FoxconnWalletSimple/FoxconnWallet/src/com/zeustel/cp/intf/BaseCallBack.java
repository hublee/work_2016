package com.zeustel.cp.intf;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/7/8 17:25
 */
public class BaseCallBack {
    public void callBack(int code,String msg){

    }

    public  void callBack(int code,String msg,String token){
        callBack(code,msg);

    }
}
