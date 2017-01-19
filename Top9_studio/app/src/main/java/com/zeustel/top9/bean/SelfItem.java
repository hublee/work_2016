package com.zeustel.top9.bean;

import android.support.v4.app.Fragment;

/**
 * 我的  菜单项
 * @author NiuLei
 * @date 2015/8/9 20:41
 */
public class SelfItem extends MenuItem {
    private Fragment fragment;

    public SelfItem(Fragment fragment) {
        this.fragment = fragment;
    }

    public SelfItem(int img, String text, Fragment fragment) {
        super(img, text);
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
