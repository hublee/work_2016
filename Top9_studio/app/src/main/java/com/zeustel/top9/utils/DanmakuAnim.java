package com.zeustel.top9.utils;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/11 17:27
 */
public class DanmakuAnim extends TranslateAnimation implements Animation.AnimationListener {
    public DanmakuAnim(float fromYDelta, float toYDelta) {
        super(0, 0, fromYDelta, toYDelta);
    }

    public interface OnAnimListener {
        void onAnimIng(float interpolatedTime);

        void onAnimStart(Animation animation);

        void onAnimEnd(Animation animation);
    }

    private OnAnimListener onAnimListener;

    public void setOnAnimListener(OnAnimListener onAnimListener) {
        this.onAnimListener = onAnimListener;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        Tools.log_i(DanmakuAnim.class, "applyTransformation", "interpolatedTime : " + interpolatedTime);
        Tools.log_i(DanmakuAnim.class, "applyTransformation", "interpolatedTime ???????????? : " + interpolatedTime);
        if (onAnimListener != null) {
            onAnimListener.onAnimIng(interpolatedTime);
        }

    }


    @Override
    public void onAnimationStart(Animation animation) {
        if (onAnimListener != null) {
            onAnimListener.onAnimStart(animation);
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        animation.cancel();
        if (onAnimListener != null) {
            onAnimListener.onAnimEnd(animation);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
