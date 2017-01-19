package com.zeustel.top9.base;

import com.umeng.analytics.MobclickAgent;

/**
 * 有被viewpager包裹1次 并且含有handle实现
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/31 17:21
 */
public abstract class WrapOneAndHandleFragment extends WrapHaveAndHandleFragment {
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
