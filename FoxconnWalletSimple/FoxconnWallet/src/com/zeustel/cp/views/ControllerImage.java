package com.zeustel.cp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ControllerImage extends ImageView{

	public ControllerImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ControllerImage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ControllerImage(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public void setImageWidth(int width){
		if(width<0){
			width = -width;
		}
		ViewGroup.LayoutParams layoutParams =  (ViewGroup.LayoutParams)this.getLayoutParams();
		layoutParams.width = width;
		this.setLayoutParams(layoutParams);
		Log.e("setImageWidth", "width:"+width);
	}
	
	public int getImageWidth(){
		return this.getWidth();
	}

}
