package com.zeustel.cp.views;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zeustel.cp.intf.IClick;
import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;

public class FloatMenu extends LinearLayout{
	private ImageView menuImageView;
	private IClick iClick;

	public FloatMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		initView();
	}
	
	private void initView(){
		inflate(getContext(), Tools.getResourse(getContext(), "layout", "view_floatmenu"), this);
		
		menuImageView = (ImageView)findViewById(R.id.floatmenu_image);
		menuImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(iClick!=null)iClick.onFloatMenuClick();
			}
		});
	}
	
	/**
	 * 动画
	 */
	public void startAnim(){
		List<Animator> animators = new ArrayList<Animator>();
		AnimatorSet set = new AnimatorSet();
      	animators.add(ObjectAnimator.ofFloat(menuImageView, "scaleX", 1, 0.6F,1));
      	animators.add(ObjectAnimator.ofFloat(menuImageView, "scaleY", 1, 0.6F,1));
		set.playTogether(animators);
		set.setDuration(280);
		set.start();
	}
	

}
