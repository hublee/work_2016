package com.zeustel.cp.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeustel.cp.intf.IFmMenu;
import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;


public class NewFmContrller extends LinearLayout{
	private IFmMenu iFmMenu;
	
	private ImageView playImageView;//播放
//	private ImageView programImageView;//节目单
//	private ImageView anchorImageView;//主播列表
//	private ImageView attentionImageView;//关注
//	private ImageView popImageView;//弹幕
	private ImageView closeImageView;//关闭
	private TextView nowPlayTextView;//当前节目
	
	public void setCallBack(IFmMenu iFmMenu){
		this.iFmMenu = iFmMenu;
	}

	public NewFmContrller(Context context) {
		super(context);
		
		init();
	}
	
	private void init(){
		inflate(getContext(), Tools.getResourse(getContext(), "layout", "view_newfmcontroller"), this);
		
		playImageView = (ImageView)findViewById(R.id.newfm_play);
//		programImageView = (ImageView)findViewById(R.id.newfm_program);
//		anchorImageView = (ImageView)findViewById(R.id.newfm_user);
//		attentionImageView = (ImageView)findViewById(R.id.newfm_attention);
//		popImageView = (ImageView)findViewById(R.id.newfm_pop);
		closeImageView = (ImageView)findViewById(R.id.newfm_close);
		nowPlayTextView = (TextView)findViewById(R.id.newfm_title);
		
		nowPlayTextView.setText("小M陪你玩游戏，赶快来加入我们吧！");
		
		closeImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iFmMenu.close();
			}
		});
		
		playImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iFmMenu.toPlay();
			}
		});
		
//		programImageView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				iFmMenu.toProgramView();
//			}
//		});
//		
//		anchorImageView.setOnClickListener(new OnClickListener() {		
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				iFmMenu.toAnchorView();
//			}
//		});
//		
//		attentionImageView.setOnClickListener(new OnClickListener() {		
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				iFmMenu.toAttentionView();
//			}
//		});
//		
//		popImageView.setOnClickListener(new OnClickListener() {		
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				iFmMenu.toPop();
//			}
//		});
		
		
	}

	/**
	 * 播放
	 */
	public void start(){
		playImageView.setImageResource(Tools.getResourse(getContext(), "drawable", "cp_sdk_newfm_stop"));
	}
	
	/**
	 * 暂停
	 */
	public void stop(){
		playImageView.setImageResource(Tools.getResourse(getContext(), "drawable", "cp_sdk_newfm_play"));
	}
	
	
}
