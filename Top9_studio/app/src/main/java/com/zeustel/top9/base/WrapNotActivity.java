package com.zeustel.top9.base;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

public abstract class WrapNotActivity extends AppCompatActivity implements IOverall{
    @Override
    public void onResume() {
        super.onResume();
        //统计页面
        MobclickAgent.onPageStart(getClassName());
        //统计时长
        MobclickAgent.onResume(getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        //统计页面
        MobclickAgent.onPageEnd(getClassName());
        //统计时长
        MobclickAgent.onPause(getApplicationContext());
    }
    @Override
    public String getClassName() {
        return getClass().getName();
    }
}
