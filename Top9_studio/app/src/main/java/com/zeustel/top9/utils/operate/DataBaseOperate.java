package com.zeustel.top9.utils.operate;

import android.os.Bundle;
import android.os.Handler;

import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.zeustel.top9.base.IOverallHandleSupport;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.SimpleResponseHandler;
import com.zeustel.top9.utils.Tools;

import java.io.Serializable;
import java.util.List;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/30 16:14
 */
public abstract class DataBaseOperate<D> implements Serializable{
    //网络请求路径
    public static final String EXTRAS_NAME_URL = "url";
    //时间
    public static final String EXTRAS_NAME_TIME = "time";
    //是否为历史
    @Deprecated
    public static final String EXTRAS_NAME_IS_HISTORY = "isHistory";
    //更新 或者 历史
    public static final String EXTRAS_NAME_FLAG = "flag";
    //id
    public static final String EXTRAS_NAME_ID = "id";
    private Handler handler;
    private DBBaseOperate<D> dbBaseImp;
    /**
     * 发表回调
     */
    private SimpleResponseHandler publishResponseHandler = new SimpleResponseHandler() {
        @Override
        public void onCallBack(int code, String json) {
            super.onCallBack(code, json);
            if (200 == code) {
                Gson gson = new Gson();
                Result result = gson.fromJson(json, Result.class);
                if (result != null && Result.SUCCESS == result.getSuccess()) {
                    //发表成功
                    handler.sendEmptyMessage(NetHelper.MSG_PUBLISH_SUCCESS);
                    handler.obtainMessage(IOverallHandleSupport.MSG_PUBLISH_SUCCESS, result).sendToTarget();
                    return;
                }
                //发表失败
                handler.sendEmptyMessage(NetHelper.MSG_PUBLISH_FAILED);
                handler.obtainMessage(IOverallHandleSupport.MSG_PUBLISH_FAILED, result).sendToTarget();
            }else{
                //发表失败
                handler.sendEmptyMessage(NetHelper.MSG_PUBLISH_FAILED);
                handler.obtainMessage(IOverallHandleSupport.MSG_PUBLISH_FAILED).sendToTarget();
            }

        }
    };

    public DataBaseOperate(Handler handler, DBBaseOperate<D> dbBaseImp) {
        this.handler = handler;
        this.dbBaseImp = dbBaseImp;
    }

    public Handler getHandler() {
        return handler;
    }

    public DBBaseOperate<D> getDbBaseImp() {
        return dbBaseImp;
    }

    /**
     * 数据列表 临时参数
     *
     * @param url       网络请求路径
     * @param time      时间
     * @param isHistory 是否为历史
     * @return 临时参数
     */
    @Deprecated
    public Bundle createListBundle(final String url, final long time, final boolean isHistory) {
        Bundle extras = new Bundle();
        extras.putString(EXTRAS_NAME_URL, url);
        extras.putLong(EXTRAS_NAME_TIME, time);
        extras.putBoolean(EXTRAS_NAME_IS_HISTORY, isHistory);
        return extras;
    }

    public Bundle createListBundle(final String url, final long time, final int flag) {
        Tools.log_i(DataBaseOperate.class, "createListBundle", "url : " + url);
        Bundle extras = new Bundle();
        extras.putString(EXTRAS_NAME_URL, url);
        extras.putLong(EXTRAS_NAME_TIME, time);
        //      extras.putInt(EXTRAS_NAME_FLAG, flag);
        extras.putBoolean(EXTRAS_NAME_IS_HISTORY, NetHelper.Flag.HISTORY == flag ? true : false);
        return extras;
    }

    /**
     * 单个数据 临时参数
     *
     * @param url 网络请求路径
     * @param id  id
     * @return 临时参数
     */
    public Bundle createSingleBundle(final String url, final int id) {
        Bundle extras = new Bundle();
        extras.putString(EXTRAS_NAME_URL, url);
        extras.putInt(EXTRAS_NAME_ID, id);
        return extras;
    }

    /**
     * 临时参数
     *
     * @param url 网络请求路径
     * @return 临时参数
     */
    public Bundle createSingleBundle(final String url) {
        Tools.log_i(DataBaseOperate.class, "createSingleBundle", "url : " + url);
        Bundle extras = new Bundle();
        extras.putString(EXTRAS_NAME_URL, url);
        return extras;
    }

    /**
     * 检查数据列表临时参数必备项
     *
     * @param extras
     */
    protected void checkListExtras(Bundle extras) {
        if (extras == null)
            throw new NullPointerException("need extras");
        if (!extras.containsKey(EXTRAS_NAME_URL))
            throw new NullPointerException("need extras" + EXTRAS_NAME_URL);
        if (!extras.containsKey(EXTRAS_NAME_TIME))
            throw new NullPointerException("need extras" + EXTRAS_NAME_TIME);
        if (!extras.containsKey(EXTRAS_NAME_IS_HISTORY))
            throw new NullPointerException("need extras" + EXTRAS_NAME_IS_HISTORY);
    }

    /**
     * 检查数据临时参数必备项
     *
     * @param extras
     */
    protected void checkSingleExtras(Bundle extras) {
        if (extras == null)
            throw new NullPointerException("need extras");
        if (!extras.containsKey(EXTRAS_NAME_URL))
            throw new NullPointerException("need extras" + EXTRAS_NAME_URL);
    }

    /**
     * 获取本地列表数据
     * 可重写适配
     *
     * @param dbBaseImp 数据操作类
     * @param extras    临时参数
     * @return 本地列表数据
     */
    protected List<D> executeNativeListData(DBBaseOperate<D> dbBaseImp, Bundle extras) throws Exception {
        final long time = extras.getLong(EXTRAS_NAME_TIME);
        final boolean isHistory = extras.getBoolean(EXTRAS_NAME_IS_HISTORY);
        return dbBaseImp.queueList(time, isHistory);
    }

    /**
     * 获取本地列表
     * 可重写适配
     *
     * @param dbBaseImp 数据操作类
     * @param extras    临时参数
     * @return 本地数据
     */
    protected D executeNativeSingleData(DBBaseOperate<D> dbBaseImp, Bundle extras) throws Exception {
        final int id = extras.getInt(EXTRAS_NAME_ID);
        return dbBaseImp.queueSingle(id);
    }

    /**
     * get网络获取数据
     * 可重写适配
     *
     * @param simpleResponseHandler 回调
     * @param extras                临时参数
     */
    protected void executeGetNetData(SimpleResponseHandler simpleResponseHandler, Bundle extras) {
        final String url = extras.getString(EXTRAS_NAME_URL);
        NetHelper.get(null/*Context*/, url, null/* RequestParams*/, simpleResponseHandler);
    }

    /**
     * post网络获取数据
     * 可重写适配
     *
     * @param simpleResponseHandler 回调
     * @param extras                临时参数
     */
    protected void executePostNetData(SimpleResponseHandler simpleResponseHandler, Bundle extras) {
        final String url = extras.getString(EXTRAS_NAME_URL);
        final long time = extras.getLong(EXTRAS_NAME_TIME);
        final boolean isHistory = extras.getBoolean(EXTRAS_NAME_IS_HISTORY);
        RequestParams params = new RequestParams();
        params.put("userId", Constants.USER.getId());
        params.put("time", time);
        params.put("flag", isHistory ? NetHelper.Flag.HISTORY : NetHelper.Flag.UPDATE);
        NetHelper.post(url, params, simpleResponseHandler);
    }

    /**
     * 网络获取单个数据
     * 可重写适配
     *
     * @param simpleResponseHandler 回调
     * @param extras                临时参数
     */
    protected void executeNetSingleData(SimpleResponseHandler simpleResponseHandler, Bundle extras) {
        executeGetNetData(simpleResponseHandler, extras);
    }

    /**
     * 网络获取列表数据
     * 可重写适配
     *
     * @param simpleResponseHandler 回调
     * @param extras                临时参数
     */
    protected void executeNetListData(SimpleResponseHandler simpleResponseHandler, Bundle extras) {
        executeGetNetData(simpleResponseHandler, extras);
    }

    /**
     * 获取单个数据
     *
     * @param extras 临时参数
     * @param cls    Gson解析所需
     */
    public void getSingleData(final Bundle extras, final Class<? extends Result<D>> cls) throws Exception {
        checkSingleExtras(extras);
        D nativeData = null;
        if (dbBaseImp != null) {
            try {
                nativeData = executeNativeSingleData(dbBaseImp, extras);
            } catch (Exception e) {
                e.printStackTrace();
                nativeData = null;
            }
        }
        if (nativeData != null) {
            handler.obtainMessage(NetHelper.MSG_REQUEST_SUCCESS, NetHelper.ARG1_NATIVE, NetHelper.ARG2_SINGLE, nativeData).sendToTarget();
            handler.obtainMessage(IOverallHandleSupport.MSG_SINGLE_SUCCESS, IOverallHandleSupport.ARG1_NATIVE, -1, nativeData).sendToTarget();
        } else {
            SimpleResponseHandler simpleResponseHandler = new SimpleResponseHandler() {
                @Override
                public void onCallBack(int code, String json) {
                    super.onCallBack(code, json);
                    if (200 == code) {
                        Gson gson = new Gson();
                        Result<D> result = gson.fromJson(json, cls);
                        if (result != null && Result.SUCCESS == result.getSuccess()) {
                            D netData = result.getData();
                            if (netData != null) {
                                //网络最新
                                handler.obtainMessage(NetHelper.MSG_REQUEST_SUCCESS, NetHelper.ARG1_NET, NetHelper.ARG2_SINGLE, netData).sendToTarget();
                                handler.obtainMessage(IOverallHandleSupport.MSG_SINGLE_SUCCESS, IOverallHandleSupport.ARG1_NET, -1, result).sendToTarget();
                                if (dbBaseImp != null) {
                                    try {
                                        dbBaseImp.insertSingle(netData);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                return;
                            }
                        }
                       /*可以添加args参数用于区分 是列表请求没有更多 还是 单个数据没有请求到数据*/
                        handler.sendEmptyMessage(NetHelper.MSG_REQUEST_NO);
                        handler.obtainMessage(IOverallHandleSupport.MSG_SINGLE_FAILED, result).sendToTarget();
                        return;
                    } else {
                        handler.sendEmptyMessage(NetHelper.MSG_REQUEST_FAILED);
                        handler.obtainMessage(IOverallHandleSupport.MSG_SINGLE_FAILED).sendToTarget();
                    }
                    handler.sendEmptyMessage(IOverallHandleSupport.MSG_SINGLE_FAILED);
                    handler.obtainMessage(IOverallHandleSupport.MSG_SINGLE_FAILED).sendToTarget();
                }
            };
            executeGetNetData(simpleResponseHandler, extras);
        }
    }

    private void listData(final String method, final Bundle extras, final Class<? extends
            Result<List<D>>> cls) throws Exception {
        checkListExtras(extras);

        final long time = extras.getLong(EXTRAS_NAME_TIME);
        final boolean isHistory = extras.getBoolean(EXTRAS_NAME_IS_HISTORY);
        List<D> nativeList = null;
        if (dbBaseImp != null) {
            try {
                nativeList = executeNativeListData(dbBaseImp, extras);
            } catch (Exception e) {
                e.printStackTrace();
                nativeList = null;
            }
        }
        if (!Tools.isEmpty(nativeList)) {
            if (Tools.isHistory(time, isHistory)) {//本地历史
                handler.obtainMessage(NetHelper.MSG_REQUEST_SUCCESS, NetHelper.ARG1_NATIVE, NetHelper.ARG2_HISTORY, nativeList).sendToTarget();
                handler.obtainMessage(IOverallHandleSupport.MSG_LIST_SUCCESS, IOverallHandleSupport.ARG1_NATIVE, IOverallHandleSupport.ARG2_HISTORY, nativeList).sendToTarget();
            } else {
                //本地最新
                handler.obtainMessage(NetHelper.MSG_REQUEST_SUCCESS, NetHelper.ARG1_NATIVE, NetHelper.ARG2_UPDATE, nativeList).sendToTarget();
                handler.obtainMessage(IOverallHandleSupport.MSG_LIST_SUCCESS, IOverallHandleSupport.ARG1_NATIVE, IOverallHandleSupport.ARG2_UPDATE, nativeList).sendToTarget();
            }
        } else {
            SimpleResponseHandler simpleResponseHandler = new SimpleResponseHandler() {
                @Override
                public void onCallBack(int code, String json) {
                    super.onCallBack(code, json);
                    if (200 == code) {
                        Gson gson = new Gson();
                        Result<List<D>> result = gson.fromJson(json, cls);
                        if (result != null && Result.SUCCESS == result.getSuccess()) {
                            List<D> netList = result.getData();
                            if (!Tools.isEmpty(netList)) {
                                if (Tools.isHistory(time, isHistory)) {
                                    //网络历史
                                    handler.obtainMessage(NetHelper.MSG_REQUEST_SUCCESS, NetHelper.ARG1_NET, NetHelper.ARG2_HISTORY, netList).sendToTarget();
                                } else {
                                    //网络最新
                                    handler.obtainMessage(NetHelper.MSG_REQUEST_SUCCESS, NetHelper.ARG1_NET, NetHelper.ARG2_UPDATE, netList).sendToTarget();
                                }
                                if (dbBaseImp != null) {
                                    try {
                                        dbBaseImp.insertList(netList);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            } else {
                                handler.sendEmptyMessage(NetHelper.MSG_REQUEST_NO);
                            }
                            if (Tools.isHistory(time, isHistory)) {
                                //网络历史
                                handler.obtainMessage(IOverallHandleSupport.MSG_LIST_SUCCESS, IOverallHandleSupport.ARG1_NET, IOverallHandleSupport.ARG2_HISTORY, result).sendToTarget();
                            } else {
                                //网络最新
                                handler.obtainMessage(IOverallHandleSupport.MSG_LIST_SUCCESS, IOverallHandleSupport.ARG1_NET, IOverallHandleSupport.ARG2_UPDATE, result).sendToTarget();
                            }
                            return;
                        }
                        handler.obtainMessage(IOverallHandleSupport.MSG_LIST_FAILED, result);
                        handler.sendEmptyMessage(NetHelper.MSG_REQUEST_FAILED);
                        return;
                    }
                    handler.sendEmptyMessage(NetHelper.MSG_REQUEST_FAILED);
                    handler.sendEmptyMessage(IOverallHandleSupport.MSG_LIST_FAILED);
                }
            };
            if (method.equals("POST")) {
                executePostNetData(simpleResponseHandler, extras);
            } else {
                executeGetNetData(simpleResponseHandler, extras);
            }
        }
    }

    /**
     * GET获取数据列表
     *
     * @param extras 临时参数
     * @param cls    Gson解析所需
     */
    public void getListData(final Bundle extras, final Class<? extends Result<List<D>>> cls) throws Exception {
        listData("GET", extras, cls);
    }

    /**
     * POST获取数据列表
     *
     * @param extras 临时参数
     * @param cls    Gson解析所需
     */
    public void postListData(final Bundle extras, final Class<? extends Result<List<D>>> cls) throws Exception {
        listData("POST", extras, cls);
    }

    public SimpleResponseHandler getPublishResponseHandler() {
        return publishResponseHandler;
    }

    /**
     * 发表数据
     *
     * @param data
     */
    public void publishData(D data) {
    }
}
