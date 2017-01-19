package com.zeustel.top9.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;

import com.zeustel.top9.R;
import com.zeustel.top9.widgets.Rotate3dAnimation;

import java.lang.ref.WeakReference;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/18 15:46
 */
public class AnimUtils {
    public static final int DEF_OFFSET = 100;
    private AnticipateOvershootInterpolator mAnticipateOvershootInterpolator;

    public enum TranslateDirection {
        LEFT(R.anim.anim_left_in), TOP(R.anim.anim_top_in), RIGHT(R.anim.anim_right_in), BOTTOM(R.anim.anim_bottom_in);
        private int animId;

        TranslateDirection(int animId) {
            this.animId = animId;
        }

        public int getAnimId() {
            return animId;
        }
    }

    private Context context = null;
    private static AnimUtils mAnimIn = null;

    private AnimUtils(Context context) {
        this.context = context;
        mAnticipateOvershootInterpolator = new AnticipateOvershootInterpolator();
    }

    /**
     * 懒汉式 单例模式
     * 特点 实例的延迟加载
     * 由于 多线程并发访问会出现安全问题 要加同步锁
     * 同步代码块和 同步关键字都行 <低效>
     * 用双层判断 可以解决 低效 问题
     */
    public static AnimUtils getInstance(Context context) {
        if (mAnimIn == null) {
            synchronized (AnimUtils.class) {
                if (mAnimIn == null)
                    mAnimIn = new AnimUtils(context);
            }
        }
        return mAnimIn;
    }

    public void translateIn(View v, TranslateDirection direction, boolean overshoot, int offset) {
        if (v != null) {
            Animation animation = getTranslate(direction, overshoot);
            animation.setStartOffset(offset);
            v.startAnimation(animation);
        }
    }

    public void translateIn(View v, TranslateDirection direction, boolean overshoot, int offset, int durationMillis) {
        if (v != null) {
            Animation animation = getTranslate(direction, overshoot);
            animation.setStartOffset(offset);
            animation.setDuration(durationMillis);
            v.startAnimation(animation);
        }
    }

    public void setIn(View v, TranslateDirection direction, boolean overshoot, int offset) {
        if (v != null) {
            AnimationSet animationSet = new AnimationSet(false);
            animationSet.setStartOffset(offset);
            Animation translate = getTranslate(direction, overshoot, false);
            animationSet.addAnimation(translate);
            animationSet.addAnimation(new AlphaAnimation(0.0F, 1.0F));
            animationSet.setDuration(translate.getDuration() + 300);
            animationSet.setAnimationListener(listener);
            v.startAnimation(animationSet);
        }
    }

    public void translateYIn(View v, float fromYDelta, float toYDelta, int startOffset) {
        if (v != null) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, fromYDelta, toYDelta);
            translateAnimation.setDuration(500);
            translateAnimation.setStartOffset(startOffset);
            translateAnimation.setAnimationListener(listener);
            v.startAnimation(translateAnimation);
        }
    }

    public void translateIn(View v, TranslateDirection direction, boolean overshoot) {
        if (v != null) {
            Animation animation = getTranslate(direction, overshoot);
            v.startAnimation(animation);
        }
    }

    public Animation getRotate(int startOffset) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
        animation.setStartOffset(startOffset);
        return animation;
    }

    public void rotate(View v, int startOffset) {
        if (v != null) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_rotate);
            animation.setStartOffset(startOffset);
            animation.setAnimationListener(listener);
            v.startAnimation(animation);
        }
    }

    public Animation getTranslate(TranslateDirection direction, boolean overshoot) {
        return getTranslate(direction, overshoot, true);
    }

    public Animation getTranslate(TranslateDirection direction, boolean overshoot, boolean needListener) {
        Animation animation = AnimationUtils.loadAnimation(context, direction.getAnimId());
        if (needListener) {
            animation.setAnimationListener(listener);
        }
        if (overshoot && mAnticipateOvershootInterpolator != null) {
            animation.setInterpolator(mAnticipateOvershootInterpolator);
        }
        return animation;
    }

    public void scaleLineIn(View v, int startOffset) {
        if (v != null) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.sacle_line_in);
            animation.setAnimationListener(new CancelListener(context, v));
            animation.setStartOffset(startOffset);
            v.startAnimation(animation);
        }
    }

    public void Rotate3dAnimation(View v, float fromDegrees, float toDegrees, float depthZ, boolean reverse, int startOffset) {
        if (v != null) {
            float centerX = v.getWidth() / 2f;
            float centerY = v.getHeight() / 2f;
            // 构建3D旋转动画对象，旋转角度为0到90度，这使得ListView将会从可见变为不可见
            final Rotate3dAnimation rotation = new Rotate3dAnimation(fromDegrees, toDegrees, centerX, centerY, depthZ, reverse);
            // 动画持续时间500毫秒
            rotation.setDuration(500);
            // 动画完成后保持完成的状态
            rotation.setInterpolator(new BounceInterpolator());
            // 设置动画的监听器
            rotation.setStartOffset(startOffset);
            v.startAnimation(rotation);
        }
    }

    public Animation getScaleIn() {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_sacle_in);
        return animation;
    }


    private Animation.AnimationListener listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (animation != null) {
                animation.cancel();
                animation = null;
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private static final class CancelListener implements Animation.AnimationListener {
        private WeakReference<View> viewRef;
        private WeakReference<Context> contextRef;

        public CancelListener(Context context, View view) {
            contextRef = new WeakReference<Context>(context);
            viewRef = new WeakReference<View>(view);
        }

        @Override
        public void onAnimationStart(Animation animation) {
            final View view = viewRef.get();
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            final Context context = contextRef.get();
            final View view = viewRef.get();
            if (context != null && view != null) {
                Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.sacle_line_out);
                animation1.setAnimationListener(new Cancel2Listener(view));
                view.startAnimation(animation1);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private static final class Cancel2Listener implements Animation.AnimationListener {
        private WeakReference<View> viewRef;

        public Cancel2Listener(View view) {
            viewRef = new WeakReference<View>(view);
        }

        @Override
        public void onAnimationStart(Animation animation) {
            final View view = viewRef.get();
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            final View view = viewRef.get();
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
            if (animation != null) {
                animation.cancel();
                animation = null;
            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
