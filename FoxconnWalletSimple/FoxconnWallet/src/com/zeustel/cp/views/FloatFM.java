package com.zeustel.cp.views;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zeustel.cp.bean.Angle;
import com.zeustel.cp.bean.AngleFactory;
import com.zeustel.cp.intf.ILocation;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/17 15:08
 */
public class FloatFM implements View.OnClickListener, FloatHelper.OnMoveListener {
    /*延迟时间*/
    private static final int DELAY_TIME = 100;
    /*角度*/
    private static final int DEF_ANGLE = 30;
    private static final int DEF_HALF_ANGLE = 28;

    /*控制器*/
    private static FloatHelper controlHelper;
    private Activity activity;
    /*是否动画中*/
    private boolean isAniming;
    /*是否打开*/
    private boolean isOpend;
    /*菜单项*/
    private List<HashMap<FloatHelper, Animation>> menuItens = new ArrayList<HashMap<FloatHelper, Animation>>();
    /*菜单项宽高*/
    private int ic_fm_menu_item_dimen;
    /*控制器宽高*/
    private int ic_fm_layout_dimen;
    /*间距*/
    private int control_item_space;

    //屏幕参数
    private DisplayMetrics displayMetrics = new DisplayMetrics();

    private OnItemClickListener onItemClickListener;

    private ILocation iLocation;

    private boolean isShow = false;

    public void setLocation(ILocation iLocation){
        this.iLocation = iLocation;
    }

    public void setOnFmControlListener(FMControlView.OnFmControlListener onFmControlListener) {
        this.onFmControlListener = onFmControlListener;
    }

    private FMControlView.OnFmControlListener onFmControlListener;

    private FMControlView mFMControlView;

    @Override
    public void onMove(int x, int y) {
        //Tools.logI("onMove   x : " + x + ", y : " + y);

        if(x<0)x=0;
        if(y<0)y=0;
        iLocation.setLocation(x,y);

        if(isOpend) {
            hideItems();
        }

    }

    /**
     * 项点击事件
     */
    public interface OnItemClickListener {
        void onItemClick(int index);
    }

    @Override
    public void onClick(View v) {
        if (v != null && v.getTag() != null) {
            int index = (Integer) v.getTag();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(index);
            }
        }
    }

    /*菜单显示动画*/
    /*动画回调*/
    private class OpenListener implements Animation.AnimationListener {
        private int index;

        public OpenListener(int index) {
            this.index = index;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            /*最后一下*/
            if (index == menuItens.size() - 1) {
            	SdkUtils.LogI("anim end...");
                setIsAniming(false);
                setIsOpend(true);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public FloatFM(Activity activity) {
        this.activity = activity;
        control_item_space = activity.getResources().getDimensionPixelSize(R.dimen.common_space);
        ic_fm_menu_item_dimen = activity.getResources().getDimensionPixelSize(R.dimen.fm_menu_item_dimen);
        ic_fm_layout_dimen = activity.getResources().getDimensionPixelSize(R.dimen.fm_layout_dimen);
        controlHelper = new FloatHelper(activity, FloatHelper.FloatType.TYPE_SERVICE, true);
        controlHelper.setOnMoveListener(this);
        mFMControlView = FMControlView.getInstance(activity);
        mFMControlView.setOnFmControlListener(new FMControlView.OnFmControlListener() {

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				if (onFmControlListener != null) {
                  onFmControlListener.onClick();
				}
			}

			@Override
			public void onDoubleClick() {
				// TODO Auto-generated method stub
				if (onFmControlListener != null) {
					onFmControlListener.onDoubleClick();
				}
			}

			@Override
			public void onLongPress() {
				// TODO Auto-generated method stub
				if (onFmControlListener != null) {
					onFmControlListener.onLongPress();
				}
			}

			@Override
			public void onOutSide() {
				// TODO Auto-generated method stub
				onFmControlListener.onOutSide();
			}
        });

        //获取屏幕参数
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        //默认放在左下角
        int y = Tools.getControlMenuY(activity);

//        if (Tools.getAndroidSDKVersion() > 18) {
//            controlHelper.addView(mFMControlView, FloatHelper.getWmParams(0, y, ic_fm_layout_dimen, ic_fm_layout_dimen, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.TYPE_TOAST));
//        }else{
//            controlHelper.addView(mFMControlView, FloatHelper.getWmParams(0, y, ic_fm_layout_dimen, ic_fm_layout_dimen, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
//        }
        
        if (Tools.getAndroidSDKVersion() > 18) {
            controlHelper.addView(mFMControlView, FloatHelper.getWmParams(500, 100, ic_fm_layout_dimen, ic_fm_layout_dimen, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.TYPE_TOAST));
        }else{
            controlHelper.addView(mFMControlView, FloatHelper.getWmParams(500, 100, ic_fm_layout_dimen, ic_fm_layout_dimen, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
        }
        
        

//        if (Tools.getAndroidSDKVersion() > 18) {
//            controlHelper.addView(mFMControlView, FloatHelper.getWmParams((displayMetrics.widthPixels - ic_fm_layout_dimen) / 2, (displayMetrics.heightPixels - ic_fm_layout_dimen) / 2, ic_fm_layout_dimen, ic_fm_layout_dimen, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.TYPE_TOAST));
//        }else{
//            controlHelper.addView(mFMControlView, FloatHelper.getWmParams((displayMetrics.widthPixels - ic_fm_layout_dimen) / 2, (displayMetrics.heightPixels - ic_fm_layout_dimen) / 2, ic_fm_layout_dimen, ic_fm_layout_dimen, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
//        }
    }

    public boolean isAniming() {
        return isAniming;
    }

    private void setIsAniming(boolean isAniming) {
        this.isAniming = isAniming;
        if (controlHelper != null) {
            if (!isOpend) {
                controlHelper.setDisabled(this.isAniming);
            }
        }
    }

    public boolean isOpend() {
        return isOpend;
    }

    private void setIsOpend(boolean isOpend) {
        this.isOpend = isOpend;
        if (controlHelper != null) {
//            controlHelper.setDisabled(this.isOpend);//2015年11月26日14:02:36 注销 菜单打开能拖动
        }
    }

    public void addItem(int iconId) {
        if (isAniming()) {
            //
            return;
        }
        if (isOpend()) {
            //
            return;
        }
        int size = menuItens.size();

        FrameLayout frameLayout = addView(iconId,size);

        Animation scaleAnimation = AnimationUtils.loadAnimation(activity, R.anim.sacle_anim_in);
        scaleAnimation.setStartOffset(size * DELAY_TIME);
        OpenListener mOpenListener = new OpenListener(size);
        scaleAnimation.setAnimationListener(mOpenListener);

        FloatHelper mFloatHelper = new FloatHelper(activity, FloatHelper.FloatType.TYPE_SERVICE);
        mFloatHelper.addView(frameLayout, FloatHelper.getWmParams(0, 0, ic_fm_menu_item_dimen, ic_fm_menu_item_dimen, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));

        HashMap<FloatHelper, Animation> item = new HashMap();
        item.put(mFloatHelper, scaleAnimation);
        menuItens.add(item);
    }

    private FrameLayout addView(int iconId,int size){
        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(iconId);

        FrameLayout frameLayout = new FrameLayout(activity);
//        frameLayout.addView(imageView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        int width = activity.getResources().getDimensionPixelSize(R.dimen.float_item_img_size);
        frameLayout.addView(imageView, new FrameLayout.LayoutParams(width, width));
        frameLayout.setVisibility(View.GONE);
        frameLayout.setTag(size);
        frameLayout.setOnClickListener(this);

        return frameLayout;
    }

    public FMControlView getFMControlView() {
        return (FMControlView) controlHelper.getView();
    }

    public void toggle() {
        if (!isAniming()) {
            if (!menuItens.isEmpty()) {
                setIsAniming(true);
                if (isOpend()) {
                    hideItems();
                } else {
                    showItems();
                }
            }
        }
    }


    /**
     * 隐藏
     */
    public void hideItems(){
        for (int i = 0; i < menuItens.size(); i++) {
            FloatHelper key = null;
            HashMap<FloatHelper, Animation> itemC = menuItens.get(i);
            for (HashMap.Entry<FloatHelper, Animation> item : itemC.entrySet()) {
                key = item.getKey();
                break;
            }
            key.setVisibility(View.GONE);
            setIsOpend(false);
            setIsAniming(false);
        }

        isShow = false;
    }


    /**
     * 显示菜单项
     * 根据位置显示
     */
    private void showItems(){
        //2016年1月29日11:47:42 处理contentView
        FMControlView controlView = getFMControlView();
        if(controlView==null){
            return;
        }

        int statusBarHeight = Tools.getStatusBarHeight(activity);//状态栏高度
        int tempWidth = displayMetrics.widthPixels>displayMetrics.heightPixels?displayMetrics.heightPixels:displayMetrics.widthPixels;
        int tempHeight = displayMetrics.widthPixels>displayMetrics.heightPixels?displayMetrics.widthPixels:displayMetrics.heightPixels;

        //屏幕的宽高
        int screenWidth = 0;
        int screenHeight = 0;
        if(Tools.isLandscape(activity)) {//横屏
            screenWidth = tempHeight;
            screenHeight = tempWidth;
        }else{
            screenWidth = tempWidth;
            screenHeight = tempHeight;
        }


        //视图宽高
        int viewWidth = controlView.getWidth();
        int viewHeight = controlView.getHeight();

        //当前位置
        int[] location = new int[2];
        getFMControlView().getLocationOnScreen(location);
        //菜单默认显示在上面
        boolean leftFlag = false;//左边标记
        boolean rightFlag = false;//右边标记
        boolean upFlag = true;//上边标记
        boolean downFlag = false;//下边标记

        //x<110 只能显示在右边
        if(location[0]<120){
            rightFlag = true;
            leftFlag = false;

            if(location[1]+viewHeight+viewWidth/2>screenHeight){
                upFlag = true;
            }else{
                upFlag = false;
            }
        }
        //y<210 显示在下边
        if(location[1]<viewWidth/1.4){
            downFlag = true;
            upFlag = false;
        }

        //x+width+110>屏width 显示在左边
        if(location[0]+viewWidth+120>screenWidth){
            leftFlag = true;
            rightFlag = false;

            if(location[1]+viewHeight+viewWidth/2>screenHeight){
                upFlag = true;
            }else{
                upFlag = false;
            }
        }

        //位置标记
        int locationsign = 0;

        if(upFlag&&!leftFlag&&!rightFlag){//上
            locationsign = 1;
        }

        if(downFlag&&!leftFlag&&!rightFlag){//下
            locationsign = 2;
        }

        if(leftFlag&&!upFlag&&!downFlag){//左
            locationsign = 3;
        }

        if(rightFlag&&!upFlag&&!downFlag){//右
            locationsign = 4;
        }

        if(upFlag&&leftFlag){//左上
            locationsign = 5;
        }

        if(upFlag&&rightFlag){//右上
            locationsign = 6;
        }

        if(leftFlag&&downFlag){//左下
            locationsign = 7;
        }

        if(rightFlag&&downFlag){//右下
            locationsign = 8;
        }


        int x = 0;
        int y = 0;
        Angle angle = null;
        for (int i = 0; i < menuItens.size(); i++) {
            FloatHelper key = null;
            Animation value = null;
            HashMap<FloatHelper, Animation> itemC = menuItens.get(i);
            for (HashMap.Entry<FloatHelper, Animation> item : itemC.entrySet()) {
                key = item.getKey();
                value = item.getValue();
                break;
            }
            if(controlHelper.getWmParams()==null){//2015年11月27日10:09:22 按住home键切换任务 bug
                return;
            }

//            Point tempPoint = dealOverFlow(controlHelper.getWmParams().x,controlHelper.getWmParams().y);
            Point tempPoint = Tools.dealOverFlow(activity, displayMetrics, mFMControlView, controlHelper.getWmParams().x, controlHelper.getWmParams().y);

            x = tempPoint.x + controlHelper.getCenter().x;
            y = tempPoint.y + controlHelper.getCenter().y;

            if(locationsign>4){
                angle = AngleFactory.getInstance(DEF_HALF_ANGLE * i - 10, control_item_space, x, y, locationsign);
            }else {
                angle = AngleFactory.getInstance(DEF_ANGLE * i + 35, control_item_space, x, y, locationsign);
            }

            key.updateLocation(angle.getTagX() - (ic_fm_menu_item_dimen / 2), angle.getTagY() - (ic_fm_menu_item_dimen / 2));
            key.setVisibility(View.VISIBLE);
            ((FrameLayout) key.getView()).getChildAt(0).startAnimation(value);
        }

        isShow = true;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void hide(){
        hideItems();
        mFMControlView.setVisibility(View.GONE);
    }

    public void show(){
        mFMControlView.setVisibility(View.VISIBLE);
    }

    public boolean isControlViewVisible(){
        return mFMControlView.getVisibility() == View.VISIBLE;
    }
    
    public void destroy(){
    	if(mFMControlView!=null){
    		controlHelper.removeView(mFMControlView);
    	}
    }
}
