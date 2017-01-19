package com.zeustel.top9.utils;

import android.view.View;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/20 14:38
 */
public class AnimRunnable implements Runnable {
    private View view;
    private boolean isLeft;

    public AnimRunnable(View view, boolean isLeft) {
        this.view = view;
        this.isLeft = isLeft;
    }

    @Override
    public void run() {
        if (view != null) {
            Tools.log_i(AnimRunnable.class,"run","...");
            view.startAnimation(isLeft ? Tools.animLeftIn : Tools.animRightIn);
        }
    }
}
