package com.zeustel.cp.bean;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.zeustel.cp.views.FloatMenu;
import com.zeustel.foxconn.cp_sdk.R;

public class PopFactory {
	private static PopFactory popFactory = new PopFactory();
	
	private PopupWindow mainFloatWindow;
	private FloatMenu floatMenu;
	
	private int lastX;
	private int lastY;
	private int mScreenX;
	private int mScreenY;
	private int dx;
	private int dy;
	
	private PopFactory(){
		
	}
	
	public static PopFactory getInstance(){
		return popFactory;
	}
	
	/**
	 * 创建按钮
	 */
	public void createMainFloat(Activity activity,View view,int x,int y){
		if(mainFloatWindow != null && mainFloatWindow.isShowing()){
			mainFloatWindow.dismiss();
			return;
		}
		floatMenu = new FloatMenu(activity);
		mainFloatWindow = new PopupWindow(floatMenu,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
		mainFloatWindow.setTouchable(true);
		mainFloatWindow.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.newfm_center_bg));
		mainFloatWindow.setAnimationStyle(R.style.pop_window_style);
		mainFloatWindow.showAtLocation(view, 0, x, y);
		mainFloatWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
                switch (action) {
                case MotionEvent.ACTION_DOWN:
                    lastX = (int) event.getRawX();
                    lastY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    mScreenX = dx;
                    mScreenY = dy;
                    break;
                case MotionEvent.ACTION_MOVE:
                    dx = (int) event.getRawX() - lastX + mScreenX;
                    dy =  lastY - (int)event.getRawY() + mScreenY;
                    mainFloatWindow.update( dx,dy, -1, -1);
                    break;
                }
                return true;
			}
		});
		mainFloatWindow.update();
	}
	
	/**
	 * 销毁按钮
	 */
	public void destroyMainFloat(){
		if(mainFloatWindow != null && mainFloatWindow.isShowing()){
			mainFloatWindow.dismiss();
			return;
		}
	}
	
	public void showFloatMenu(){
		if(floatMenu!=null)
		floatMenu.setVisibility(View.VISIBLE);
	}
	
	public void hideFloatMenu(){
		if(floatMenu!=null)
		floatMenu.setVisibility(View.GONE);
	}
	
	public FloatMenu getFloatMenu(){
		return floatMenu;
	}
}
