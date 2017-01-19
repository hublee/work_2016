package com.zeustel.top9.base;

import com.zeustel.top9.result.Result;

/**
 * handle回调接口
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/31 16:48
 */
public interface IOverallHandleSupport {

    int MSG_LIST_SUCCESS = 21111;
    int MSG_LIST_FAILED = 21123;

    int MSG_PUBLISH_SUCCESS = 21114;
    int MSG_PUBLISH_FAILED = 21125;

    int MSG_SINGLE_SUCCESS = 21116;
    int MSG_SINGLE_FAILED = 21127;

    int ARG2_UPDATE = 21118;
    int ARG2_HISTORY = 21119;

    int ARG1_NATIVE = 21110;
    int ARG1_NET = 21120;

    /**
     * 列表最新数据
     */
    void onHandleListUpdate(Result result, Object obj);

    /**
     * 列表历史数据
     */
    void onHandleListHistory(Result result, Object obj);

    /**
     * 列表没有数据
     */
    void onHandleListNo(Result result);

    /**
     * 列表请求失败
     */
    void onHandleListFailed(Result result);

    /**
     * 发表成功
     */
    void onHandlePublishSuccess(Result result);

    /**
     * 发表失败
     */
    void onHandlePublishFailed(Result result);

    /**
     * 单个数据
     */
    void onHandleSingleSuccess(Result result, Object obj);

    /**
     * 单个数据失败
     */
    void onHandleSingleFailed(Result result);
}
