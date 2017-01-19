package com.zeustel.top9.base;

import android.os.Handler;
import android.os.Message;

import com.zeustel.top9.R;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;

import java.util.List;

/**
 * 没有被viewpager包裹 并且含有handle实现
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/31 17:21
 */
public abstract class WrapNotAndHandleFragment extends WrapNotFrament implements
        IOverallHandle, IOverallHandleSupport {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NetHelper.MSG_REQUEST_SUCCESS:
                    /*根据本地 网络 区分*/
                    if (NetHelper.ARG1_NATIVE == msg.arg1) {//本地数据
                        if (NetHelper.ARG2_HISTORY == msg.arg2) {//历史
                            onHandleNativeHistory(msg.obj);
                        } else if (NetHelper.ARG2_UPDATE == msg.arg2) {//本地最新
                            onHandleNativeUpdate(msg.obj);
                        } else {
                            onHandleNativeSingle(msg.obj);
                        }
                    } else {//网络数据
                        if (NetHelper.ARG2_HISTORY == msg.arg2) {//历史
                            onHandleNetHistory(msg.obj);
                        } else if (NetHelper.ARG2_UPDATE == msg.arg2) {//本地最新
                            onHandleNetUpdate(msg.obj);
                        } else {
                            onHandleNetSingle(msg.obj);
                        }
                    }
                    /*不区分本地 网络*/
                    if (NetHelper.ARG2_HISTORY == msg.arg2) {//历史
                        onHandleHistory(msg.obj);
                    } else if (NetHelper.ARG2_UPDATE == msg.arg2) {//最新
                        onHandleUpdate(msg.obj);
                    } else {
                        onHandleSingle(msg.obj);
                    }
                    break;
                case NetHelper.MSG_REQUEST_FAILED:
                    onHandleFailed();
                    /*Tools.showToast(getActivity(),R.string.load_request_failed);*/
                    break;
                case NetHelper.MSG_REQUEST_NO:
                    onHandleNo();
                    Tools.showToast(getActivity(), R.string.load_request_no);
                    break;
                case NetHelper.MSG_PUBLISH_SUCCESS:
                    onHandlePublishSuccess();
                    break;
                case NetHelper.MSG_PUBLISH_FAILED:
                    onHandlePublishFailed();
                    break;


                case IOverallHandleSupport.MSG_LIST_SUCCESS:
                    if (msg.arg1 == IOverallHandleSupport.ARG1_NATIVE) {//本地
                        if (msg.arg2 == IOverallHandleSupport.ARG2_HISTORY) {//历史
                            onHandleListHistory(null, msg.obj);
                        } else {//更新
                            onHandleListUpdate(null, msg.obj);
                        }
                    } else {//网络
                        Result result = (Result) msg.obj;
                        Object obj = result.getData();
                        List data = null;
                        if (obj != null && obj instanceof List) {
                            data = (List) obj;
                            if (!Tools.isEmpty(data)) {
                                if (msg.arg2 == IOverallHandleSupport.ARG2_HISTORY) {//历史
                                    onHandleListHistory(result, data);
                                } else {//更新
                                    onHandleListUpdate(result, data);
                                }
                            } else {
                                onHandleListNo(result);
                            }
                        }
                    }
                    break;
                case IOverallHandleSupport.MSG_LIST_FAILED:
                    onHandleListFailed(msg.obj == null ? null : (Result) msg.obj);
                    break;
                case IOverallHandleSupport.MSG_PUBLISH_SUCCESS:
                    onHandlePublishSuccess(msg.obj == null ? null : (Result) msg.obj);
                    break;
                case IOverallHandleSupport.MSG_PUBLISH_FAILED:
                    onHandlePublishFailed(msg.obj == null ? null : (Result) msg.obj);
                    break;
                case IOverallHandleSupport.MSG_SINGLE_SUCCESS:
                    if (msg.arg1 == IOverallHandleSupport.ARG1_NATIVE) {
                        onHandleSingleSuccess(null,msg.obj);
                    } else {
                        Result result = (Result) msg.obj;
                        onHandleSingleSuccess(result,result.getData());
                    }
                    break;
                case IOverallHandleSupport.MSG_SINGLE_FAILED:
                    onHandleSingleFailed(msg.obj == null ? null : (Result) msg.obj);
                    break;
            }
        }
    };

    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onHandlePublishSuccess() {
//        Tools.showToast(getContext(), R.string.publish_success);
    }

    @Override
    public void onHandlePublishFailed() {
//        Tools.showToast(getContext(), R.string.load_request_failed);
    }

    @Override
    public void onHandleFailed() {

    }

    @Override
    public void onHandleHistory(Object obj) {

    }

    @Override
    public void onHandleNativeHistory(Object obj) {

    }

    @Override
    public void onHandleNativeSingle(Object obj) {

    }

    @Override
    public void onHandleNativeUpdate(Object obj) {

    }

    @Override
    public void onHandleNetHistory(Object obj) {

    }

    @Override
    public void onHandleNetSingle(Object obj) {

    }

    @Override
    public void onHandleNetUpdate(Object obj) {

    }

    @Override
    public void onHandleNo() {

    }

    @Override
    public void onHandleSingle(Object obj) {

    }

    @Override
    public void onHandleUpdate(Object obj) {

    }

    @Override
    public void onHandleListUpdate(Result result, Object obj) {

    }

    @Override
    public void onHandleListHistory(Result result, Object obj) {

    }

    @Override
    public void onHandleListNo(Result result) {

    }

    @Override
    public void onHandleListFailed(Result result) {

    }

    @Override
    public void onHandlePublishSuccess(Result result) {
        Tools.showToast(getContext(), R.string.publish_success);
    }

    @Override
    public void onHandlePublishFailed(Result result) {
        Tools.showToast(getContext(), R.string.load_request_failed);
    }

    @Override
    public void onHandleSingleSuccess(Result result, Object obj) {

    }

    @Override
    public void onHandleSingleFailed(Result result) {
        Tools.showToast(getContext(), R.string.load_request_failed);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
