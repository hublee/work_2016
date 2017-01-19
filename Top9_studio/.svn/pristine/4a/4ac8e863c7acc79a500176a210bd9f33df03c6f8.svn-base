package com.zeustel.top9.widgets;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;

/**
 * 自身旋转动画
 */
public class RotateAnim extends RotateAnimation implements Animation.AnimationListener {
    private OnAnimUpdateListener listener;
    /*是否动画进行中*/
    private boolean isAniming;
    /*是否打开*/
    private boolean isOpened;
    /*所用时间*/
    private float interpolatedTime;

    @Override
    public void onAnimationStart(Animation animation) {
        isAniming = true;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        isAniming = false;
        if (listener != null) {
            listener.onAnimEnd(isOpened);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public boolean isAniming() {
        return isAniming;
    }

    /**
     * 动画监听
     */
    public interface OnAnimUpdateListener {
        /**
         * 动画进行中
         *
         * @param interpolatedTime 时间
         */
        void onAnimUpdate(float interpolatedTime);

        /**
         * 动画完成
         *
         * @param isOpened 是否为打开动画
         */
        void onAnimEnd(boolean isOpened);
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setListener(OnAnimUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * 构造方法
     *
     * @param fromDegrees 起点角度
     * @param toDegrees   终点角度
     * @param isOpened    是否打开
     */
    public RotateAnim(float fromDegrees, float toDegrees, boolean isOpened) {
        super(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        this.isOpened = isOpened;
        setAnimationListener(this);
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        if (interpolatedTime <= 1.0D && this.interpolatedTime != interpolatedTime) {
            this.interpolatedTime = interpolatedTime;
            if (listener != null) {
                listener.onAnimUpdate(interpolatedTime);
            }
        }
    }
}