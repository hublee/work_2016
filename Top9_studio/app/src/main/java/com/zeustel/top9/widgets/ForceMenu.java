package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 伸缩菜单
 *
 * @author NiuLei
 * @date 2015/11/22 19:17
 */
public class ForceMenu extends FrameLayout implements View.OnClickListener, RotateAnim.OnAnimUpdateListener, View.OnTouchListener {
    //默认持续时间
    private static final int DEF_DURATION = 300;
    //默认旋转角度
    private static final float DEF_DEGREE = 90F;
    //菜单集合
    private List<View> items = new ArrayList();
    //菜单控制器
    private CircleImage control;
    //菜单项容器
    private RelativeLayout itemGroup;
    //宽高
    private int item_dimen;
    //间距
    private int item_space;
    //持续时间
    private int duration = DEF_DURATION;
    //旋转动画
    private RotateAnim mRotateAnim;
    //菜单是否打开
    private boolean isOpened;
    //菜单容器高度
    private int item_group_height;
    //动画甩出的距离
    private int toss_len;
    //是否向上显示
    private boolean isUp;
    /*触摸起点*/
    private PointF startPoint = new PointF();
    /*触摸移动*/
    private PointF movePoint = new PointF();
    /*中点*/
    private Point centerPoint = new Point();
    /*状态栏高度*/
    private int statusBarHeight = -1;
    /*标题栏高度*/
    private int titleBarHeight = -1;
    /*父布局*/
    private RelativeLayout parent;
    /*control旋转角度*/
    private float degree = DEF_DEGREE;
    /*自动关闭*/
    private boolean autoClose;
    //处理parent_right
    private boolean process;
    private RelativeLayout.LayoutParams layoutParams;
    private OnForceMenuStateListener onForceMenuStateListener;
    private OnItemClickListener onItemClickListener;

    public void onDestroy() {
        if (items != null) {
            items.clear();
            items = null;
        }
        if (itemGroup != null) {
            itemGroup.removeAllViews();
            itemGroup = null;
        }
        if (mRotateAnim != null) {
            mRotateAnim.cancel();
            mRotateAnim = null;
        }
        onForceMenuStateListener = null;
        onItemClickListener = null;
        startPoint = null;
        movePoint = null;
        centerPoint = null;
    }
    /**
     * 状态监听
     */
    public interface OnForceMenuStateListener {
        /**
         * 打开
         */
        void onForceMenuOpened();

        /**
         * 关闭
         */
        void onForceMenuClosed();
    }

    /**
     * 项点击监听
     */
    public interface OnItemClickListener {
        void onItemClick(ForceMenu forceMenu, int position);
    }

    public ForceMenu(Context context) {
        super(context);
        initView();
    }

    public ForceMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ForceMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ForceMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        item_space = getResources().getDimensionPixelSize(R.dimen.force_menu_item_space);// Tools.dip2px(getContext(), DEF_ITEM_SPCE);
        item_dimen = getResources().getDimensionPixelSize(R.dimen.force_menu_item_dimen);//Tools.dip2px(getContext(), DEF_ITEM_DIMEN);
        toss_len = item_dimen;
        View menuView = LayoutInflater.from(getContext()).inflate(R.layout.force_menu_layout, null);
        itemGroup = (RelativeLayout) menuView.findViewById(R.id.itemGroup);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) itemGroup.getLayoutParams();
        layoutParams.width = item_dimen;
        itemGroup.setLayoutParams(layoutParams);

        control = (CircleImage) menuView.findViewById(R.id.control);
        ViewGroup.LayoutParams layoutParams1 = control.getLayoutParams();
        layoutParams1.width = item_dimen;
        layoutParams1.height = item_dimen;
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        addView(menuView, params);
        control.setOnClickListener(this);
        control.setOnTouchListener(this);
        Tools.endCreateOperate(this, new Runnable() {
            @Override
            public void run() {
                centerPoint.x = getWidth() / 2;
                centerPoint.y = getHeight() / 2;
                statusBarHeight = Tools.measureStateBarHeight((Activity) getContext());
                titleBarHeight = Tools.measureTitleBarHeight((Activity) getContext());

                Tools.log_i(ForceMenu.class, "run", "statusBarHeight : " + statusBarHeight);
                Tools.log_i(ForceMenu.class, "run", "titleBarHeight : " + titleBarHeight);
                ViewParent parent = getParent();
                if (parent != null && parent instanceof RelativeLayout) {
                    ForceMenu.this.parent = (RelativeLayout) parent;
                }
            }
        });
    }

    /**
     * 触摸外部 关闭菜单
     */
    public boolean isOutSide(MotionEvent ev) {
        boolean outsideOf = Tools.isOutsideOf(this, ev);
        if (outsideOf && isOpened) {
            closeMenu();
        }
        return outsideOf;
    }

    /**
     * 设置ALIGN_PARENT_RIGHT 处理
     */
    private boolean processParentRight() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        int rightMargin = params.rightMargin;
        Tools.log_i(ForceMenu.class, "isUp", "getLeft : " + getLeft());
        if (rightMargin > 0) {
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            params.leftMargin = getLeft();
            setLayoutParams(params);
        }
        return true;
    }

    /**
     * 是否向上显示
     *
     * @return
     */
    private boolean isUp() {
        isUp = getTop() - item_group_height > 0;
        if (!process) {
            process = processParentRight();
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        RelativeLayout.LayoutParams controlParams = (RelativeLayout.LayoutParams) control.getLayoutParams();
        RelativeLayout.LayoutParams groupParams = (RelativeLayout.LayoutParams) itemGroup.getLayoutParams();
        if (isUp) {/*清除 ALIGN_PARENT_TOP 并设置 ALIGN_PARENT_BOTTOM*/
            controlParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            groupParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            controlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            groupParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
/*为了能在正确的方向上放大空间 供菜单展开*/
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.topMargin = 0;
            params.bottomMargin = parent.getHeight() - getTop() - item_dimen;
        } else {/*清除 ALIGN_PARENT_BOTTOM 并设置 ALIGN_PARENT_TOP*/

            controlParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            groupParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            controlParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            groupParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
/*为了能在正确的方向上放大空间 供菜单展开*/
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.topMargin = getTop();
            params.bottomMargin = 0;
        }
        setLayoutParams(params);
        control.setLayoutParams(controlParams);
        itemGroup.setLayoutParams(groupParams);
        return isUp;
    }

    /**
     * 添加item
     */
    public void addItemView(View view) {
        if (view != null) {
            RelativeLayout.LayoutParams itemParams = new RelativeLayout.LayoutParams(item_dimen, item_dimen);
            itemParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            itemGroup.addView(view, itemParams);
            view.setOnClickListener(this);
            items.add(view);
              /*计算菜单弹开后所需高度*/
            item_group_height = items.size() == 0 ? item_dimen : items.size() * (item_dimen + item_space) + toss_len;
        }
    }

    /**
     * 添加Item
     */
    public void addItemView(int imgId) {
        if (imgId != 0) {
            CircleImage circleImage = new CircleImage(getContext());
            circleImage.setImageResource(imgId);
            addItemView(circleImage);
        }
    }

    /**
     * 菜单项总数
     */
    public int size() {
        return items.size();
    }

    /**
     * 位移动画
     *
     * @param isOpened 是否打开
     * @param fromY    起点
     * @param toY      终点
     * @return
     */
    private TranslateAnimation getTranslateAnimation(boolean isOpened, float fromY, float toY) {
        TranslateAnimation mTranslateAnimation = new TranslateAnimation(0, 0, fromY, toY);
        mTranslateAnimation.setInterpolator(isOpened ? new OvershootInterpolator() : new AccelerateInterpolator());
        mTranslateAnimation.setDuration(duration);
        mTranslateAnimation.setFillAfter(true);
        return mTranslateAnimation;
    }

    /**
     * 旋转动画
     *
     * @param isOpened    是否打开
     * @param fromDegrees 起点角度
     * @param toDegrees   终点角度
     */
    private void initRotateAnimation(boolean isOpened, float fromDegrees, float toDegrees) {
        mRotateAnim = new RotateAnim(fromDegrees, toDegrees, isOpened);
        mRotateAnim.setFillAfter(true);
        mRotateAnim.setInterpolator(isOpened ? new OvershootInterpolator() : new AccelerateInterpolator());
        mRotateAnim.setDuration(duration);
        mRotateAnim.setListener(this);
    }

    /**
     * 是否动画进行中
     *
     * @return
     */
    private boolean isAniming() {
        if (mRotateAnim != null) {
            return mRotateAnim.isAniming();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (!isAniming()) {
            if (control.equals(v)) {
                toggle();
            } else {
                Tools.log_i(ForceMenu.class, "onClick", "index : " + items.indexOf(v));
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(this, items.indexOf(v));
                }
                if (autoClose) {
                    closeMenu();
                }
            }
        }
    }

    /**
     * 打开菜单
     */
    private void openMenu() {
        initRotateAnimation(true, 0.0F, isUp() ? degree : -degree);
        control.startAnimation(mRotateAnim);
        int item_need = 0;
        View child = null;
        RelativeLayout.LayoutParams params = null;
        for (int i = 0; i < items.size(); i++) {
            child = items.get(i);
            params = (RelativeLayout.LayoutParams) child.getLayoutParams();
            /*根据方向更新菜单item属性*/
            if (isUp) {
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            } else {
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            }
            item_need = (i + 1) * (control.getHeight() + item_space);
            child.setLayoutParams(params);
            itemGroup.updateViewLayout(child, params);
            TranslateAnimation translateAnimation = getTranslateAnimation(true, 0, isUp ? -item_need : item_need);
            translateAnimation.setAnimationListener(new AnimListener(child, true, isUp ? -item_need : item_need));
            child.startAnimation(translateAnimation);
        }
    }

    /**
     * 关闭菜单
     */
    private void closeMenu() {
        initRotateAnimation(false, isUp ? degree : -degree, 0.0F);
        control.startAnimation(mRotateAnim);
        int item_need = 0;
        View child = null;
        for (int i = 0; i < items.size(); i++) {
            child = items.get(i);
            item_need = (i + 1) * (control.getHeight() + item_space);
            TranslateAnimation translateAnimation = getTranslateAnimation(false, 0, isUp ? item_need : -item_need);
            translateAnimation.setAnimationListener(new AnimListener(child, false, 0));
            child.startAnimation(translateAnimation);
        }
    }

    /**
     * 开关
     */
    public void toggle() {
        if (isOpened) {
            closeMenu();
        } else {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = item_dimen + item_group_height;
            setLayoutParams(layoutParams);
            openMenu();
        }
    }


    @Override
    public void onAnimUpdate(float interpolatedTime) {
        if (isOpened) {
           /* int item_need = 0;
            View child = null;
            RelativeLayout.LayoutParams params = null;
            for (int i = 0; i < items.size(); i++) {
                child = items.get(i);
                params = (RelativeLayout.LayoutParams) child.getLayoutParams();
                item_need = (i + 1) * (control.getHeight() + item_space);

                //            child.startAnimation(getTranslateAnimation(false, isUp ? -item_need : item_need, 0));
                params.topMargin = 0;//isUp ? -item_need : item_need;
                itemGroup.updateViewLayout(child, params);
            }*/
        } else {
         /*   int item_need = 0;
            View child = null;
            RelativeLayout.LayoutParams params = null;
            for (int i = 0; i < items.size(); i++) {
                child = items.get(i);
                params = (RelativeLayout.LayoutParams) child.getLayoutParams();
                item_need = (i + 1) * (control.getHeight() + item_space);
                item_need = (int) (item_need * interpolatedTime);
                 *//*根据方向更新菜单item属性*//*
                if (isUp) {
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    params.bottomMargin = (int) (item_need * interpolatedTime);
                    params.topMargin = 0;
                } else {
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    params.bottomMargin = 0;
                    params.topMargin = (int) (item_need * interpolatedTime);
                }
                itemGroup.updateViewLayout(child, params);
            }*/
        }
    }

    @Override
    public void onAnimEnd(boolean isOpened) {
        this.isOpened = isOpened;
        if (!isOpened) {
            /*更新布局大小*/
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = item_dimen;
            setLayoutParams(layoutParams);
        }
        if (onForceMenuStateListener != null) {
            if (isOpened) {
                onForceMenuStateListener.onForceMenuOpened();
            } else {
                onForceMenuStateListener.onForceMenuClosed();
            }
        }
    }

    /*实现可移动*/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startPoint.x = event.getRawX();
                startPoint.y = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                movePoint.x = event.getRawX() - startPoint.x;
                movePoint.y = event.getRawY() - startPoint.y;
                if (!isAniming() && !isOpened) {/*动画时 打开时 不能移动*/
                    if (Math.abs(movePoint.x) > 5 || Math.abs(movePoint.y) > 5) {
                        onMove((int) event.getRawX() - centerPoint.x, (int) event.getRawY() - centerPoint.y - statusBarHeight - (titleBarHeight > 0 ? titleBarHeight : 0));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(movePoint.x) > 5 || Math.abs(movePoint.y) > 5) {
                    /*移动*/
                    return true;
                } else {
                    /*点击*/
                    return false;
                }
        }
        return false;
    }

    /**
     * 菜单移动
     *
     * @param x
     * @param y
     */
    private void onMove(int x, int y) {
        if (parent != null && xLimit(x) && yLimit(y)) {
            if (!process) {
                process = processParentRight();
            }
            layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);/*必须*/
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);/*必须*/
            layoutParams.leftMargin = x;
            layoutParams.topMargin = y;
            layoutParams.bottomMargin = 0;/*必须*/
            parent.updateViewLayout(this, layoutParams);
        }
    }

    /**
     * 选择
     */
    public void select(int position) {
        //to do..
    }

    /**
     * x轴移动限制
     *
     * @param x
     * @return
     */
    private boolean xLimit(int x) {
        return x >= 0 && x < parent.getWidth() - item_dimen;
    }

    /**
     * y轴移动限制
     *
     * @param y
     * @return
     */
    private boolean yLimit(int y) {
        return y >= 0 && y < parent.getHeight() - item_dimen;
    }

    private class AnimListener implements Animation.AnimationListener {
        private View view;
        private boolean isOpen;
        private int toY;

        public AnimListener(View view, boolean isOpen, int toY) {
            this.view = view;
            this.isOpen = isOpen;
            this.toY = Math.abs(toY);
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
            if (isOpen) {
                if (isUp) {
                    layoutParams.bottomMargin = toY;
                } else {
                    layoutParams.topMargin = toY;
                }
            } else {
                if (isUp) {
                    layoutParams.bottomMargin = 0;
                } else {
                    layoutParams.topMargin = 0;
                }
            }
            view.setLayoutParams(layoutParams);
            view.clearAnimation();
            Tools.log_i(AnimListener.class, "onAnimationEnd", "toY : " + toY);
            Tools.log_i(AnimListener.class, "onAnimationEnd", "index : " + items.indexOf(view));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * 设置控制器图标
     *
     * @param resId
     */
    public void setControl(int resId) {
        control.setImageResource(resId);
    }

    public int getItem_dimen() {
        return item_dimen;
    }

    public void setItem_dimen(int item_dimen) {
        this.item_dimen = item_dimen;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public int getItem_space() {
        return item_space;
    }

    public void setItem_space(int item_space) {
        this.item_space = item_space;
    }

    public int getToss_len() {
        return toss_len;
    }

    public void setToss_len(int toss_len) {
        this.toss_len = toss_len;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public boolean isAutoClose() {
        return autoClose;
    }

    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    public void setOnForceMenuStateListener(OnForceMenuStateListener onForceMenuStateListener) {
        this.onForceMenuStateListener = onForceMenuStateListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
