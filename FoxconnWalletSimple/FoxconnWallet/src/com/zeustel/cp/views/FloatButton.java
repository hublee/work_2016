package com.zeustel.cp.views;

import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.Tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

public class FloatButton {
	private static FloatButton floatButton = new FloatButton();
	private PopupWindow popupWindow;
	private AnimController controller;
	
	private PointF startPoint = new PointF();
    private PointF movePoint = new PointF();
    private Point center = new Point();
    
    private final static int MSG_MOVE = 1;
    private final static int MSG_POP = 2;
    
    private View contentView;
	
	private FloatButton(){
		
	}
	
	public static FloatButton getInstance(){
		return floatButton;
	}
	
	/**
	 * 初始化悬浮按钮
	 * @param context
	 * @param view
	 */
	public void initButton(final Activity activity,final View view){
		controller = new AnimController(activity);
		if(popupWindow != null && popupWindow.isShowing()){
			return;
		}
		
		LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = layoutInflater.inflate(Tools.getResourse(activity, "layout", "fm_control"), null);
		
    	popupWindow = new PopupWindow(contentView);
    	popupWindow.setContentView(controller);
    	popupWindow.setTouchable(true);
		popupWindow.setWidth(Tools.dip2px(activity,Constants.CONTROLCENTER_WIDTH));
		popupWindow.setHeight(Tools.dip2px(activity,Constants.CONTROLLER_WIDTH));
		popupWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
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
	                	
	                    Message message = new Message();
	                    message.what = MSG_MOVE;
	                    message.arg1 = (int)movePoint.x;
	                    message.arg2 = (int)movePoint.y;
	                    mHandler.sendMessage(message);

	                    startPoint.x = event.getRawX();
	                    startPoint.y = event.getRawY();
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
		});
		
//		activity.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				if (!activity.isFinishing()){
					popupWindow.showAtLocation(view,0,500, 100);
//				}
//			}
//		});
					
//		popupWindow.showAtLocation(view,0,500, 100);
//		popupWindow.update();
	}
	
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_MOVE:
				popupWindow.update(msg.arg1,msg.arg2);
				break;
			case MSG_POP:
				
				break;
			default:
				break;
			
			}
		}
	};
		
	
	
}
