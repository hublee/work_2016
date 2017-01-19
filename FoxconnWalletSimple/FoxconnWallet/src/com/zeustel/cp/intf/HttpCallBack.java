package com.zeustel.cp.intf;

import java.util.List;
import java.util.Map;

public class HttpCallBack implements IHttpCallBack {
    public void callBack(int code, String msg, String string) {

    }

    @Override
    public void callBackWithHeads(int code, String msg, String string, Map<String, List<String>> heads) {
        callBack(code,msg,string);

    }
}
