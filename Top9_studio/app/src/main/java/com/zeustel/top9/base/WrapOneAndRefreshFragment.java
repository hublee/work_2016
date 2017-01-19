package com.zeustel.top9.base;

import com.umeng.analytics.MobclickAgent;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/31 17:38
 */
public abstract class WrapOneAndRefreshFragment<T> extends WrapHaveAndRefreshFragment<T> {
    @Override
    public void OnResumeCorrect() {
        super.OnResumeCorrect();
        //统计时长
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPauseCorrect() {
        //统计时长
        MobclickAgent.onPause(getActivity());
    }
}
