package com.zeustel.cp.intf;

import java.util.List;
import java.util.Map;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/7/8 13:55
 */
public interface IHttpCallBack {
    void callBack(int code,String msg,String string);
    void callBackWithHeads(int code, String msg, String string, Map<String, List<String>> heads);
}
