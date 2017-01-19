package com.zeustel.top9.base;

/**
 * handle回调接口
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/31 16:48
 */
@Deprecated
public interface IOverallHandle {

    /**
     * 发表成功
     */
    void onHandlePublishSuccess();

    /**
     * 发表失败
     */
    void onHandlePublishFailed();

    /**
     * 单个数据
     */
    void onHandleSingle(Object obj);

    /**
     * 单个网络数据
     */
    void onHandleNetSingle(Object obj);

    /**
     * 单个本地数据
     */
    void onHandleNativeSingle(Object obj);

    /**
     * 没有数据
     */
    void onHandleNo();

    /**
     * 加载失败
     */
    void onHandleFailed();

    /**
     * 网络最新数据
     *
     * @param obj 数据
     */
    void onHandleNetUpdate(Object obj);

    /**
     * 网络历史
     */
    void onHandleNetHistory(Object obj);

    /**
     * 本地最新
     */
    void onHandleNativeUpdate(Object obj);

    /**
     * 本地历史
     */
    void onHandleNativeHistory(Object obj);

    /**
     * 最新数据
     */
    void onHandleUpdate(Object obj);

    /**
     * 历史数据
     */
    void onHandleHistory(Object obj);
}
