package com.zeustel.cp.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zeustel.cp.intf.IFloat;
import com.zeustel.cp.utils.Tools;


/**
 * 使view悬浮帮助类
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/16 09:46
 */
public final class FloatHelper implements IFloat, View.OnTouchListener{
    public Activity activity = null;
    private FloatType mFloatType;
    private static WindowManager mWindowManager = null;
    private WindowManager.LayoutParams wmParams = null;
    private View contentView = null;
    private PointF startPoint = new PointF();
    private PointF movePoint = new PointF();
    private Point center = new Point();
    //是否需要移动功能
    private boolean needMove;
    //暂时致残
    private boolean disabled;
    private OnMoveListener onMoveListener;

    //屏幕参数
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    
    private Window window;

    /**
     * 移动监听
     */
    public interface OnMoveListener {
        void onMove(int x, int y);
    }

    public void setOnMoveListener(OnMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

    public WindowManager.LayoutParams getWmParams() {
        return wmParams;
    }

    /**
     * 更新位置
     */
    public void updateLocation(int x, int y) {
        if (contentView != null) {
            this.wmParams.x = x;
            this.wmParams.y = y;
            //SdkUtils.LogI("x : " + x + ",y : " + y);
            mWindowManager.updateViewLayout(contentView, wmParams);
        }
    }

    public void updateLocation(WindowManager.LayoutParams wmParams) {
        if (contentView != null) {
            this.wmParams = wmParams;
            mWindowManager.updateViewLayout(contentView, this.wmParams);
        }
    }

    public Point getCenter() {
        return center;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isNeedMove() {
        return needMove;
    }

    public void setNeedMove(boolean needMove) {
        this.needMove = needMove;
    }

    @Override
    public void addView(View view, WindowManager.LayoutParams wmParams) {
        if (view == null)
            throw new NullPointerException();
//        if (view.getParent() != null) {
//            Tools.logE("view has parent already");
//            mWindowManager.removeView(view);
//        }

        this.contentView = view;
        this.wmParams = wmParams;
        if (needMove) {
            this.contentView.setOnTouchListener(this);
        }
//        Tools.logI("width : " + this.wmParams.width);
//        Tools.logI("height : " + this.wmParams.height);
        center.x = this.wmParams.width / 2;
        center.y = this.wmParams.height / 2;
        try {
            mWindowManager.addView(this.contentView, wmParams);
        }catch (Exception e){
//            Tools.logE("view has parent already");
        }

    }

    @Override
    public void addView(View view, WindowManager.LayoutParams wmParams,boolean needMove) {
        if (view == null)
            throw new NullPointerException();
//        if (view.getParent() != null) {
//            Tools.logE("view has parent already");
//            mWindowManager.removeView(view);
//        }

        this.contentView = view;
        this.wmParams = wmParams;
        if (needMove) {
            this.contentView.setOnTouchListener(this);
        }
//        Tools.logI("width : " + this.wmParams.width);
//        Tools.logI("height : " + this.wmParams.height);
        center.x = this.wmParams.width / 2;
        center.y = this.wmParams.height / 2;
        try {
            mWindowManager.addView(this.contentView, wmParams);
        }catch (Exception e){
//            Tools.logE("view has parent already");
        }

    }

    @Override
    public View getView() {
        return contentView;
    }

    @Override
    public void removeView() {
        if (contentView != null) {
            mWindowManager.removeView(contentView);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        if (contentView != null) {
            contentView.setVisibility(visibility);
        }
    }

    @Override
    public int getVisibility() {
        if (contentView != null) {
            return contentView.getVisibility();
        } else {
            return View.GONE;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (disabled) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startPoint.x = event.getRawX();
                startPoint.y = event.getRawY();
                System.out.print("startpoint:"+startPoint.x+","+startPoint.y);
                break;
            case MotionEvent.ACTION_MOVE:
            //case MotionEvent.ACTION_HOVER_MOVE:
                movePoint.x = event.getRawX() - startPoint.x;
                movePoint.y = event.getRawY() - startPoint.y;
                // 处理单击时移动
                if (Math.abs(movePoint.x) > 5 || Math.abs(movePoint.y) > 5) {

                    wmParams.x += (int)  movePoint.x;

                    wmParams.y += (int) movePoint.y;

                    mWindowManager.updateViewLayout(v, wmParams);
                    if (onMoveListener != null) {

                        Point tempPoint = Tools.dealOverFlow(activity,displayMetrics,contentView,wmParams.x,wmParams.y);
                        wmParams.x = wmParams.x<0?0:wmParams.x;
                        wmParams.y = wmParams.y<0?0:wmParams.y;
                        onMoveListener.onMove(tempPoint.x,tempPoint.y);//超过屏幕参数bug
                    }

                    this.startPoint.x = event.getRawX();
                    this.startPoint.y = event.getRawY();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(movePoint.x) > 5 || Math.abs(movePoint.y) > 5) {
                    return true;
                } else {
                    return false;
                }
        }
        return false;
    }

    // 悬浮类型
    public enum FloatType {
        // 只在单个activity悬浮
        TYPE_ACTIVITY,
        // 在所有界面悬浮
        TYPE_SERVICE
    }

    public FloatHelper(Activity activity, FloatType mFloatType) {
        this.activity = activity;
        this.mFloatType = mFloatType;
        this.needMove = false;
        mWindowManager = getWindowManager(activity, mFloatType);

        //保存屏幕参数
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    }

    public FloatHelper(Activity activity, FloatType mFloatType, boolean needMove) {
        this.activity = activity;
        this.mFloatType = mFloatType;
        this.needMove = needMove;
        mWindowManager = getWindowManager(activity, mFloatType);
      
        //保存屏幕参数
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    /**
     * 根据所传悬浮类型 获取WindowManager对象
     *
     * @param activity
     * @param mFloatType 悬浮类型
     * @return
     */
    public static WindowManager getWindowManager(Activity activity, FloatType mFloatType) {
        WindowManager mWindowManager = null;
        if (FloatType.TYPE_SERVICE == mFloatType) {
            mWindowManager = (WindowManager) activity.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        } else {
            mWindowManager = activity.getWindowManager();
        }
        return mWindowManager;
    }

    /**
     * 获取WindowManager.LayoutParams对象
     *
     * @param wmParamsX 初始x轴位置
     * @param wmParamsY 初始y轴位置
     * @param wmParamsW tag视图宽度
     * @param wmParamsH tag视图高度
     * @return
     */
    public static WindowManager.LayoutParams getWmParams(int wmParamsX, int wmParamsY, int wmParamsW, int wmParamsH, int flags,int type) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        // 设置window type
//        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE/*
//                                                 * | LayoutParams.
//												 * SOFT_INPUT_ADJUST_PAN
//												 */;
        wmParams.type = type;

        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        // wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE|
        // LayoutParams.FLAG_ALT_FOCUSABLE_IM
        // | LayoutParams.FLAG_NOT_TOUCH_MODAL
        // | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | flags;
//        wmParams.flags =WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM ;
//        wmParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = wmParamsX;
        wmParams.y = wmParamsY;
        // 设置悬浮窗口长宽数据
        wmParams.width = wmParamsW;
        wmParams.height = wmParamsH;
        return wmParams;
    }

    /**
     * 获取WindowManager.LayoutParams对象
     * 允许动画
     * @param wmParamsX 初始x轴位置
     * @param wmParamsY 初始y轴位置
     * @param wmParamsW tag视图宽度
     * @param wmParamsH tag视图高度
     * @return
     */
    public static WindowManager.LayoutParams getWmParamsAnimation(int wmParamsX, int wmParamsY, int wmParamsW, int wmParamsH, int flags,int type) {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        // 设置window type
//        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE/*
//                                                 * | LayoutParams.
//												 * SOFT_INPUT_ADJUST_PAN
//												 */;
        wmParams.type = type;

        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        // wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE|
        // LayoutParams.FLAG_ALT_FOCUSABLE_IM
        // | LayoutParams.FLAG_NOT_TOUCH_MODAL
        // | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | flags;
//        wmParams.flags =WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM ;
//        wmParams.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = wmParamsX;
        wmParams.y = wmParamsY;
        // 设置悬浮窗口长宽数据
        wmParams.width = wmParamsW;
        wmParams.height = wmParamsH;

        wmParams.windowAnimations=android.R.style.Animation_Translucent;

        return wmParams;
    }

    public void removeView(View view){
    	if(view == null){
    		return;
    	}
        try {
            mWindowManager.removeView(view);
        }catch (IllegalArgumentException e){
            //e.printStackTrace();
        }
    }

}
