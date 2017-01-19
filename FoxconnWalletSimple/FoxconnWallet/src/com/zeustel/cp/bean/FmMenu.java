package com.zeustel.cp.bean;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.zeustel.cp.intf.IFmMenu;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.views.AnchorList;
import com.zeustel.cp.views.NewFmContrller;
import com.zeustel.foxconn.cp_sdk.R;

@Deprecated
public class FmMenu implements IFmMenu{
	private static FmMenu fmMenu = new FmMenu();
	
	private Activity activity;
	
    private PopupWindow fmWindow;//电台控制
    //private FmController fmController;//电台控制 视图
    private NewFmContrller fmController;
    
    private PopupWindow anchorWindow;//主播
    private AnchorList anchorList;//主播界面
    
    private PopupWindow programWindow;//节目单
    
    
    
	
	private FmMenu(){
		
	}
	
	public static FmMenu getInstance(){
		return fmMenu;
	}
	
	/**
	 * 创建电台控制界面
	 * @param activity
	 * @param view
	 * @param pointX
	 * @param pointY
	 */
	public void createFmMenu(Activity activity,View view,int pointX,int pointY){
		this.activity = activity;
		if(fmWindow != null && fmWindow.isShowing()){
    		fmWindow.dismiss();
			return;
		}
		fmController = new NewFmContrller(activity);
		if(!SdkUtils.isPlay())fmController.stop();;//没播放状态
		fmController.setCallBack(this);
    	//fmController.setNowPlayProgram("<font color=\"#ff0000\">【正在播放】</font>"+"小M陪你玩游戏，速来围观");
    	fmWindow = new PopupWindow(fmController,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
    	fmWindow.setTouchable(true);
    	fmWindow.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.newfm_center_bg));
    	fmWindow.setOutsideTouchable(true);
		fmWindow.setAnimationStyle(R.style.pop_window_style);
//		fmWindow.showAtLocation(view, 0, -pointX,-pointY+50);
//		fmWindow.showAtLocation(view, 0, (Tools.getScreenWidth(activity)-760)/2,-pointY+100);
		fmWindow.showAtLocation(view, 0, 0,200);
		fmWindow.update();
	}
	
	/**
	 * 销毁电台控制界面
	 */
	public void destroyFmMenu(){
		if(fmWindow != null && fmWindow.isShowing()){
			fmWindow.dismiss();
			return;
		}
	}

	@Override
	public void toAnchorView() {
		// TODO Auto-generated method stub
		createAnchorView();
	}

	@Override
	public void toProgramView() {
		// TODO Auto-generated method stub
		Log.e("toProgramView", "toProgramView");
	}

	@Override
	public void toPlay() {
		// TODO Auto-generated method stub
		if(SdkUtils.isPlay()){
//			NetSDK.getInstance().stopPlay();
			fmController.stop();
			SdkUtils.setIsPlay(false);
		}else{
//			NetSDK.getInstance().startPlay();
			fmController.start();
			SdkUtils.setIsPlay(true);
		}
	}

	@Override
	public void toAttentionView() {
		// TODO Auto-generated method stub
		Log.e("toAttentionView", "toAttentionView");
	}

	@Override
	public void toPop() {
		// TODO Auto-generated method stub
		Log.e("toPop", "toPop");
	}
	
	/**
	 * 创建主播界面
	 */
	private void createAnchorView(){
//		if(anchorWindow != null && anchorWindow.isShowing()){
//			anchorWindow.dismiss();
//			return;
//		}
//		anchorList = new AnchorList(activity);
//    	anchorWindow = new PopupWindow(fmController,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,true);
//    	anchorWindow.setTouchable(true);
//    	anchorWindow.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.fm_center_bg));
//    	anchorWindow.setOutsideTouchable(true);
//    	anchorWindow.setAnimationStyle(R.style.pop_window_style);
//    	anchorWindow.showAtLocation(fmController, 0, 0,0);
//    	anchorWindow.update();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
//		ZSSDK.getDefault().distroyFmController();
	}
	
	
	
}
