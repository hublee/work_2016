package com.zeustel.top9.base;

import com.umeng.analytics.MobclickAgent;

/**
 * 被1个Viewpager包含的基类
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/23 16:08
 */
public abstract class WrapOneFragment extends WrapHaveFragment {
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
