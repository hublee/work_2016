package com.zeustel.cp.views;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeustel.cp.intf.IFmMenu;
import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;

public class FmController extends LinearLayout{
	private TextView nowPlayTextView;//正在播放
	
	private ImageView anchorImageView;//主播
	private ImageView programImageView;//节目单
	private ImageView playImageView;//播放
	private ImageView attentionImageView;//关注
	private ImageView popImageView;//弹幕
	
	private IFmMenu iFmMenu;
	
	public FmController(Context context){
		super(context);
		initView();
	}
	
	private void initView(){
		inflate(getContext(), Tools.getResourse(getContext(), "layout", "view_fmcontroller"), this);
		
		nowPlayTextView = (TextView)findViewById(R.id.fm_center_nowplay);
		anchorImageView = (ImageView)findViewById(R.id.fm_controller_anchor);
		programImageView = (ImageView)findViewById(R.id.fm_controller_program);
		playImageView = (ImageView)findViewById(R.id.fm_controller_play);
		attentionImageView = (ImageView)findViewById(R.id.fm_controller_attention);
		popImageView = (ImageView)findViewById(R.id.fm_controller_pop);
		
		anchorImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iFmMenu.toAnchorView();
			}
		});
		
		programImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iFmMenu.toProgramView();
			}
		});
		
		playImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iFmMenu.toPlay();
			}
		});
		
		attentionImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iFmMenu.toAttentionView();
			}
		});
		
		popImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iFmMenu.toPop();
			}
		});
	}
	
	/**
	 * 设置回调
	 * @param callBack
	 */
	public void setCallBack(IFmMenu callBack){
		iFmMenu = callBack;
	}
	
	/**
	 * 设置当前节目
	 * @param programName
	 */
	public void setNowPlayProgram(String programName){
		nowPlayTextView.setText(Html.fromHtml(programName));
	}
}
