package com.zeustel.top9.fmcontrol;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zeustel.top9.R;


/**
 * 少动画进行监听
 * Created by Administrator on 2015/11/24.
 */
public class MediaControl extends FrameLayout implements View.OnClickListener {
    private static final int DEF_TIME = 500;//默认持续时间
    private static final float DEF_STARTOFFSET = 0.3f;//默认时间偏移量百分比
    private ImageView pause;
    private ImageView play;
    private boolean isPlay;
    private boolean isAniming;

    private int animTime = DEF_TIME;
    private float startOffset = DEF_STARTOFFSET;
    private OnMediaControlListener onMediaControlListener;

    @Override
    protected void onDetachedFromWindow() {
        if (pause != null) {
            pause.clearAnimation();
        }
        if (play != null) {
            play.clearAnimation();
        }
        isPlay = false;
        isAniming = false;
        onMediaControlListener = null;
        removeAllViews();
        super.onDetachedFromWindow();

    }

    public interface OnMediaControlListener {
        void onPlay();

        void onPause();
    }

    public void setOnMediaControlListener(OnMediaControlListener onMediaControlListener) {
        this.onMediaControlListener = onMediaControlListener;
    }

    public int getAnimTime() {
        return animTime;
    }

    public void setAnimTime(int animTime) {
        this.animTime = animTime;
    }

    public float getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(float startOffset) {
        this.startOffset = startOffset;
    }

    public MediaControl(Context context) {
        super(context);
        initView();
    }

    public MediaControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MediaControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MediaControl(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        pause = new ImageView(getContext());
        pause.setImageResource(R.mipmap.temp_fm_pause);
        pause.setVisibility(View.INVISIBLE);
        play = new ImageView(getContext());
        play.setImageResource(R.mipmap.temp_fm_play);
        ViewGroup.LayoutParams playParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ViewGroup.LayoutParams pauseParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(play, playParams);
        addView(pause, pauseParams);
        setOnClickListener(this);
    }

    private void start() {
        play.startAnimation(animOut(play));
        Animation animIn = animIn(pause);
        animIn.setStartOffset((int) (animTime * startOffset));
        pause.startAnimation(animIn);

    }

    private void pause() {
        pause.startAnimation(animOut(pause));
        Animation animIn = animIn(play);
        animIn.setStartOffset((int) (animTime * startOffset));
        play.startAnimation(animIn);
    }

    public void toggle() {
        if (!isAniming) {
            if (isPlay) {
                pause();
            } else {
                start();
            }
        }

    }


    /**
     * 退出动画
     *
     * @param view 目标view
     * @return 退出动画
     */
    public Animation animOut(View view) {
        return getAnimation(view, false);
    }

    /**
     * 进入动画
     *
     * @param view 目标view
     * @return 进入动画
     */
    public Animation animIn(View view) {
        return getAnimation(view, true);
    }

    private Animation getAnimation(View view, boolean isIn) {
        float from = isIn ? 0.0f : 1.0f;
        float to = isIn ? 1.0f : 0.0f;
        AnimationSet anim = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(from, to, from, to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.addAnimation(scale);
        anim.addAnimation(rotate);
        anim.setDuration(animTime);
        anim.setAnimationListener(new AnimListener(view, isIn));
        return anim;
    }

    @Override
    public void onClick(View view) {
        toggle();
    }

    /**
     * 用于动画后操作view
     */
    private class AnimListener implements AnimationListener {
        private View view;
        private boolean isIn;

        public AnimListener(View view, boolean isIn) {

            this.view = view;
            this.isIn = isIn;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            view.setVisibility(View.VISIBLE);
            isAniming = true;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (view != null) {
                view.setVisibility(isIn ? View.VISIBLE : View.INVISIBLE);
                view.clearAnimation();
                animation.cancel();
                animation = null;
            }
            isAniming = false;
            //播放键 进入动画结束 代表播放

            if (isIn && view.equals(pause)) {
                isPlay = true;
                if (onMediaControlListener != null) {
                    onMediaControlListener.onPlay();
                }
            } else {
                isPlay = false;
                if (onMediaControlListener != null) {
                    onMediaControlListener.onPause();
                }
            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
