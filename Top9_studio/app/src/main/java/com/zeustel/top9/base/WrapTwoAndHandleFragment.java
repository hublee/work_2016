package com.zeustel.top9.base;

import com.umeng.analytics.MobclickAgent;

/**
 * 有被viewpager包裹2次 并且含有handle实现
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/31 17:21
 */
@Deprecated
public abstract  class WrapTwoAndHandleFragment extends WrapHaveAndHandleFragment{
    @Override
    public void OnResumeCorrect() {
        super.OnResumeCorrect();
        //统计页面
        MobclickAgent.onPageStart(getClassName());
    }

    @Override
    public void onPauseCorrect() {
        //统计页面
        MobclickAgent.onPageEnd(getClassName());
    }
}
