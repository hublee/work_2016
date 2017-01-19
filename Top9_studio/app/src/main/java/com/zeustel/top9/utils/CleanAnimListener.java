package com.zeustel.top9.utils;

import android.view.View;
import android.view.animation.Animation;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/25 16:15
 */
public class CleanAnimListener implements Animation.AnimationListener {
    private View view;

    public CleanAnimListener(View view) {
        this.view = view;
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        view.clearAnimation();
        animation.cancel();
        animation = null;
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
