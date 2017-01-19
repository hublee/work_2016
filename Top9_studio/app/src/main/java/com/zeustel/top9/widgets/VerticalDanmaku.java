package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.zeustel.top9.utils.Tools;

import java.lang.ref.WeakReference;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/19 20:01
 */
public class VerticalDanmaku extends ViewGroup {
    /*持续时间*/
    private static final int durationMillis = 1000;
    private int startOffset;
    private int itemHeight;
    private int height;

    public VerticalDanmaku(Context context) {
        super(context);
        initView();
    }

    public VerticalDanmaku(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public VerticalDanmaku(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerticalDanmaku(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /*偏移量 用于更新新view高度*/
    private int offset = 0;
    /*新view高度*/
    private int offsetBy = 0;

    private void initView() {
        Tools.endCreateOperate(this, new Runnable() {
            @Override
            public void run() {
                height = getHeight();
                Tools.log_i(VerticalDanmaku.class, "run", "height : " + height);
            }
        });
    }

    ViewGroup.MarginLayoutParams mMarginLayoutParams;

    /**
     * 发送弹幕
     *
     * @param view
     */

        public void send(View view) {
            if (isEnabled() && height != 0) {
                mMarginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                addView(view, mMarginLayoutParams);
                if (this.itemHeight == 0) {
                    view.measure(0, 0);
                    this.itemHeight = view.getMeasuredHeight();
                    updateStartOffset();
                }
                ScaleInAnim scaleInAnim = new ScaleInAnim();
                scaleInAnim.setDuration(durationMillis);
                scaleInAnim.setAnimationListener(new ScaleListener(view));
                view.startAnimation(scaleInAnim);
            } else {
                view = null;
            }

        }
/*    public void send(View view) {
        if (isEnabled() && height != 0) {
            mMarginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            addView(view, mMarginLayoutParams);
            if (this.itemHeight == 0) {
                view.measure(0, 0);
                this.itemHeight = view.getMeasuredHeight();
                updateStartOffset();
            }
            ScaleInAnim scaleInAnim = new ScaleInAnim();
            scaleInAnim.setDuration(durationMillis);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(durationMillis * 2);
            alphaAnimation.setInterpolator(new DecelerateInterpolator());
            alphaAnimation.setStartOffset(startOffset);
            AnimationSet set = new AnimationSet(false);
            set.addAnimation(scaleInAnim);
            set.addAnimation(alphaAnimation);
            set.setAnimationListener(new AnimEndListener(view));
            view.startAnimation(set);
        } else {
            view = null;
        }

    }*/

    /**
     * 更新消失时间偏移量
     */
    private void updateStartOffset() {
        if (itemHeight == 0) {
            return;
        }
        int multiple = height / this.itemHeight;
        startOffset = durationMillis * multiple;
        Tools.log_i(VerticalDanmaku.class, "updateStartOffset", "startOffset : " + startOffset);
    }

    /**
     * 发送弹幕
     *
     * @param view       弹幕item view
     * @param itemHeight item view 高度
     */
  /*  public void send(View view, int itemHeight) {
        if (isEnabled() && height != 0) {
            if (this.itemHeight == 0) {
                this.itemHeight = itemHeight;
                updateStartOffset();
            }
            mMarginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, itemHeight);
            addView(view, mMarginLayoutParams);
            ScaleInAnim scaleInAnim = new ScaleInAnim();
            scaleInAnim.setDuration(durationMillis);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            alphaAnimation.setDuration(durationMillis);
            alphaAnimation.setInterpolator(new DecelerateInterpolator());
            alphaAnimation.setStartOffset(startOffset);
            AnimationSet set = new AnimationSet(false);
            set.addAnimation(scaleInAnim);
            set.addAnimation(alphaAnimation);
            set.setAnimationListener(new AnimEndListener(view));
            view.startAnimation(set);
        } else {
            view = null;
        }

    }*/
    public void send(View view, int itemHeight) {
        if (isEnabled() && height != 0) {
            if (this.itemHeight == 0) {
                this.itemHeight = itemHeight;
                updateStartOffset();
            }
            mMarginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, itemHeight);
            addView(view, mMarginLayoutParams);
            ScaleInAnim scaleInAnim = new ScaleInAnim();
            scaleInAnim.setDuration(durationMillis);
            scaleInAnim.setAnimationListener(new ScaleListener(view));
            view.startAnimation(scaleInAnim);
        } else {
            view = null;
        }

    }

    public void resetView() {
        for (int i = 0; i < getChildCount(); i++) {
            final View childView = getChildAt(i);
            childView.clearAnimation();
            removeView(childView);
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT);
    }

    protected LayoutParams generateLayoutParams(LayoutParams params) {
        return new MarginLayoutParams(params);
    }

    /**
     * 测量group大小  未具体完善
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    /**
     * 主要实现 view 从下到上排列
     * 并且实现模拟 上方view被新出现view慢慢顶上去的动画效果
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount > 0) {
            int lastIndex = childCount - 1;
            int cWidth;
            int cHeight;
            int cTop = 0;
            int offsetBy;
            View childView = null;
            MarginLayoutParams params;
            int bottom = getHeight();
            for (int i = lastIndex; i >= 0 && i < childCount; i--) {
                childView = getChildAt(i);
                cHeight = childView.getMeasuredHeight();

                if (cTop < 0) {
                    removeViewsInLayout(0, i + 1);/*实时删除 规定外的view*/
                    break;
                } else {
                    cWidth = childView.getMeasuredWidth();

                    params = (MarginLayoutParams) childView.getLayoutParams();
                    offsetBy = cHeight + params.bottomMargin + params.topMargin;
                    if (lastIndex == i) {/*新出现的view */
                        this.offsetBy = offsetBy;
                        cTop = getHeight() - offset;/*为了防止 新添加view 一瞬间闪烁  差异感问题  <慢慢顶上去>*/
                        childView.layout(params.leftMargin, cTop, cWidth + params.leftMargin + params.rightMargin, getHeight());
                    } else {
                        cTop = bottom - offsetBy;
                        childView.layout(params.leftMargin, cTop, cWidth + params.leftMargin + params.rightMargin, bottom);
                    }
                    bottom = cTop;
                }
            }
            offset = 0;
        }
    }

    /**
     * x轴缩放动画
     */
    public class ScaleInAnim extends ScaleAnimation {
        public ScaleInAnim() {
            super(0.0F, 1.0F, 1.0F, 1.0F);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0D) {
                offset = (int) (interpolatedTime * offsetBy);/*更新offset 用于慢慢顶上去动画*/
                requestLayout();
            }
        }
    }

    private static class AnimEndListener implements Animation.AnimationListener {
        private final WeakReference<View> viewRef;

        public AnimEndListener(View view) {
            viewRef = new WeakReference<View>(view);
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            final View view = viewRef.get();
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
                view.clearAnimation();
                if (animation != null) {
                    animation.cancel();
                    animation = null;
                }
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Deprecated
    private class ScaleListener implements Animation.AnimationListener {
        private View view;

        public ScaleListener(View view) {
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
            if (isEnabled()) {
                ViewPropertyAnimator.animate(view).alpha(0.0f)/*.alphaBy(1.0f)*/.setStartDelay(startOffset).setInterpolator(new DecelerateInterpolator()).setDuration(durationMillis * 2).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //                        Tools.log_i(ScaleListener.class, "onAnimationEnd", "");
                        view.setVisibility(View.INVISIBLE);
                        view.clearAnimation();
                        if (animator != null) {
                            animator.cancel();
                            animator = null;
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                        //                        Tools.log_i(ScaleListener.class, "onAnimationCancel", "");
                        view.setVisibility(View.INVISIBLE);
                        view.clearAnimation();
                        if (animator != null) {
                            animator.cancel();
                        }
                        animator = null;
                        if (view != null) {
                            removeView(view);
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            } else {
                if (view != null) {
                    removeView(view);
                }
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
