package com.zeustel.top9.base;

import com.umeng.analytics.MobclickAgent;

/**
 * 被viewpager + viewpager 内部包含的Fragment的基类
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/23 16:08
 */
@Deprecated
public abstract  class WrapTwoFragment extends WrapHaveFragment {
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
