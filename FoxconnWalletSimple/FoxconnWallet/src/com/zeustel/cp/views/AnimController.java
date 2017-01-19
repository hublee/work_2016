package com.zeustel.cp.views;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zeustel.cp.ZSSDK;
import com.zeustel.cp.ZSStatusCode;
import com.zeustel.cp.intf.IMenu;
import com.zeustel.cp.intf.NextCallBack;
import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.Tools;
import com.zeustel.cp.utils.UserManager;
import com.zeustel.cp.wallet.WalletActivity;
import com.zeustel.foxconn.cp_sdk.R;

/**
 * 自定义 带动画的 菜单
 * @author Do
 *
 */
public class AnimController extends LinearLayout implements IMenu{
	private ImageView image;
	private RelativeLayout layout;
	
	private ControllerImage imagebg;
	
	private ArrayList<Animator> animators;
	private AnimatorSet animatorSet;
	
	private boolean isInAniming = false;//是否在出场动画中
	
	private Handler handler;
	
	public AnimController(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public AnimController(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public AnimController(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		inflate(getContext(), Tools.getResourse(getContext(), "layout", "view_animcontroller"), this);
		layout = (RelativeLayout)findViewById(Tools.getResourse(getContext(), "id", "animlayout"));
		imagebg = (ControllerImage)findViewById(Tools.getResourse(getContext(), "id", "animimg"));
		handler = new Handler();
	}
	
	public void statrtAnim(){
		if(isInAniming)return;
		startFirstAnim();
	}
	
	private void startFirstAnim(){
		ScaleAnimation scaleAnimation;
        if(SdkUtils.isLeft){
        	scaleAnimation = new ScaleAnimation(0.1f, 1.0f,1.0f,1.0f);
        }else{
       
        	scaleAnimation = new ScaleAnimation(0.1f, 1.0f,1.0f,1.0f,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,1.0f);
        }
        scaleAnimation.setDuration(250);
        scaleAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				startSecondAnim();
			}
		});
        imagebg.startAnimation(scaleAnimation);
	}
	
	private void startSecondAnim(){
		animatorSet = new AnimatorSet();
		final UserManager instance = UserManager.getInstance(getContext());
		//兑换
        MenuItem exchangeItem = new MenuItem(getContext());
		if(!instance.isOnline()){
        	exchangeItem.setViewId(Constants.CHECK_VIEWID);
        }else{
        	exchangeItem.setViewId(Constants.EXCHANGE_VIEWID);
        }
        exchangeItem.displayImg(R.drawable.cp_sdk_controller_exchange);
        exchangeItem.setId(100);
        exchangeItem.registCallBack(this);
        layout.addView(exchangeItem,getParams(exchangeItem));
        addAnim(exchangeItem,SdkUtils.isLeft?0:3);
        

        //商城
        MenuItem shopItem = new MenuItem(getContext());
        shopItem.displayImg(R.drawable.cp_sdk_controller_shop);
        shopItem.setId(101);
        shopItem.registCallBack(this);
        shopItem.setViewFlag(MenuItem.SHOP_VIEW);
        layout.addView(shopItem,getParams(shopItem));
        addAnim(shopItem,SdkUtils.isLeft?1:2);

       /* //论坛
        MenuItem cp_sdk_community = new MenuItem(getContext());
        cp_sdk_community.displayImg(R.drawable.cp_sdk_community);
        cp_sdk_community.setId(102);
        cp_sdk_community.registCallBack(this);
        cp_sdk_community.setViewFlag(MenuItem.FORUM_VIEW);
        layout.addView(cp_sdk_community,getParams(cp_sdk_community));
        addAnim(cp_sdk_community,SdkUtils.isLeft?2:1);*/
        
      //论坛
        MenuItem cp_sdk_game = new MenuItem(getContext());
        cp_sdk_game.displayImg(R.drawable.cp_sdk_game);
        cp_sdk_game.setId(102);
        cp_sdk_game.registCallBack(this);
        cp_sdk_game.setViewFlag(MenuItem.cp_sdk_game);
        layout.addView(cp_sdk_game,getParams(cp_sdk_game));
        addAnim(cp_sdk_game,SdkUtils.isLeft?2:1);
        
      //论坛
        MenuItem cp_sdk_gift = new MenuItem(getContext());
        cp_sdk_gift.displayImg(R.drawable.cp_sdk_gift);
        cp_sdk_gift.setId(103);
        cp_sdk_gift.registCallBack(this);
        cp_sdk_gift.setViewFlag(MenuItem.cp_sdk_gift);
        layout.addView(cp_sdk_gift,getParams(cp_sdk_gift));
        addAnim(cp_sdk_gift,SdkUtils.isLeft?2:1);
        
        //我的
        MenuItem centerItem = new MenuItem(getContext());
        centerItem.displayImg(R.drawable.cp_sdk_controller_usercenter);
        centerItem.setId(104);

		if(!instance.isOnline()){
			centerItem.setTitle(getResources().getString(Tools.getResourse(getContext(), "string","controller_check_title1")));
		}else{
			centerItem.setTitle(getResources().getString(Tools.getResourse(getContext(), "string","controller_check_title2")));
		}
        centerItem.registCallBack(this);
        centerItem.setViewFlag(MenuItem.CENTER_VIEW);
        layout.addView(centerItem,getParams(centerItem));
        addAnim(centerItem,SdkUtils.isLeft?3:0);
	}
	
	private RelativeLayout.LayoutParams getParams(View view){
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		if(view.getId() > 100){
			params.addRule(RelativeLayout.RIGHT_OF,view.getId()-1);
		}
		
		return params;
	}
	
	/**
	 * 按钮入场
	 * @param view
	 */
	 private void addAnim(View view,int i){
    	TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 216, 0);
    	translateAnimation.setDuration(300);
    	translateAnimation.setStartOffset(i*100);
    	translateAnimation.setInterpolator(new OvershootInterpolator(2.5f));
    	view.startAnimation(translateAnimation);
	}
	
	public void forceClose(){
		animators = new ArrayList<Animator>();
		layout.removeAllViews();
		if(animatorSet!=null)animatorSet.end();
	}

	@Override
	public void toShop() {
		Bundle bundle = new Bundle();
		bundle.putString("mobile", ZSSDK.getDefault().getMobile());
		bundle.putString("token", SdkUtils.getToken());
		SdkUtils.openApp(getContext(), Constants.PACKAGE_NAME_JFT,bundle);
	}

	@Override
	public void toView(int viewId) {
		if (Constants.CHECK_VIEWID == viewId) {
			 Intent intent = new Intent(getContext(), WalletActivity.class);
			 getContext().startActivity(intent);
		}else{
			ZSSDK.getDefault().addView(viewId);
		}
	}

	@Override
	public void toUserCenter() {
		if (!checkOnline()) {
			return;
		}
		
		validToken();
	}

	@Override
	public void toExchange() {
		if (!checkOnline()) {
			Log.i("toExchange",  "toExchange (line 244):mei zai xian  ");
			return;
		}
		Log.i("toExchange",  "toExchange (line 244): toExchange ");
//		toView(Constants.EXCHANGE_VIEWID);

		validTokenSwitch(Constants.EXCHANGE_VIEWID);
	}

	@Override
	public void toForum() {
		if (!checkOnline()) {
			return;
		}
		
		validToken2();
	}
	

	@Override
	public void toGame() {
		if (!checkOnline()) {
			return;
		}
		validTokenSwitch(MenuItem.cp_sdk_game);
	}

	@Override
	public void toGift() {
		// TODO Auto-generated method stub
		if (!checkOnline()) {
			return;
		}
		validTokenSwitch(MenuItem.cp_sdk_gift);
	}

	private boolean checkOnline(){
		final boolean online = UserManager.getInstance(getContext()).isOnline();
		Log.i("online",  "checkOnline (line 277): "+String.valueOf(online));
		if (online) {
			return true;
		}
		Log.i("online",  "checkOnline (line 282): CHECK_VIEWID");
		toView(Constants.CHECK_VIEWID);
		return false;
	}
	private void validTokenSwitch(final int viewId){
		ZSSDK.getDefault().validToken(new NextCallBack() {
			@Override
			public void callBack(int code, String msg) {
				// TODO Auto-generated method stub
				switch(code){
				case ZSStatusCode.SUCCESS:
					if (viewId == MenuItem.cp_sdk_game) {
						ZSSDK.getDefault().toGame();
					}else if(viewId == MenuItem.cp_sdk_gift){
						ZSSDK.getDefault().toGift();
					} else if (viewId == Constants.EXCHANGE_VIEWID) {
						ZSSDK.getDefault().toInformation();
					}
					
				break;
				
				case Constants.SID_ERROR:
					//sid非法
					JSONObject json;
					try {
						json = new JSONObject(msg);
						JSONObject data = json.getJSONObject("data");
						if(data!=null){
							String str = data.getString(Constants.SID);
							ZSSDK.getDefault().setAuthCode(str);
							mHandler.sendEmptyMessage(1);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				break;
				
				default:
					if(code == Constants.TOKEN_EXPIRE || code == Constants.TOKEN_EXPIRE2){
						mHandler.sendEmptyMessage(1);
					}
					break;
				}
			}
		});
		
	}
	
	private void validToken(){
		ZSSDK.getDefault().validToken(new NextCallBack() {
			@Override
			public void callBack(int code, String msg) {
				// TODO Auto-generated method stub
				switch(code){
				case ZSStatusCode.SUCCESS:
					//Log.e("SUCCESS validToken", "code:"+code+",msg:"+msg);
					ZSSDK.getDefault().userCenter();
					
				break;
				
				case Constants.SID_ERROR:
					//sid非法
					JSONObject json;
					try {
						json = new JSONObject(msg);
						JSONObject data = json.getJSONObject("data");
						if(data!=null){
							String str = data.getString(Constants.SID);
							ZSSDK.getDefault().setAuthCode(str);
							mHandler.sendEmptyMessage(1);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				break;
				
				default:
					if(code == Constants.TOKEN_EXPIRE || code == Constants.TOKEN_EXPIRE2){
						mHandler.sendEmptyMessage(1);
					}
					break;
				}
			}
		});
		
	}
	
	
	private void validToken2(){
		ZSSDK.getDefault().validToken(new NextCallBack() {
			@Override
			public void callBack(int code, String msg) {
				// TODO Auto-generated method stub
				switch(code){
				case ZSStatusCode.SUCCESS:
					//Log.e("SUCCESS validToken", "code:"+code+",msg:"+msg);
					ZSSDK.getDefault().community();
					
				break;
				
				case Constants.SID_ERROR:
					//sid非法
					JSONObject json;
					try {
						json = new JSONObject(msg);
						JSONObject data = json.getJSONObject("data");
						if(data!=null){
							String str = data.getString(Constants.SID);
							ZSSDK.getDefault().setAuthCode(str);
							mHandler.sendEmptyMessage(1);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				break;
				
				default:
					if(code == Constants.TOKEN_EXPIRE || code == Constants.TOKEN_EXPIRE2){
						mHandler.sendEmptyMessage(1);
					}
					break;
				}
			}
		});
		
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1){
				Tools.tips(getContext(), "用户信息过期，请重新验证！");
				ZSSDK.getDefault().reCheck();
			}
		}
	};
	
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(MeasureSpec.makeMeasureSpec(Tools.dip2px(getContext(), Constants.CONTROLCENTER_WIDTH), MeasureSpec.EXACTLY), heightMeasureSpec);
	}

	
}
