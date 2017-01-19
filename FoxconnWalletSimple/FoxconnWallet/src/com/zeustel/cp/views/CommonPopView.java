package com.zeustel.cp.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zeustel.cp.bean.PopView;
import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;

public class CommonPopView extends PopView{
	protected LinearLayout layout;

//    private TextView title;
	private ImageView topImg;
    
    private ImageView close;
    
    private Context context;

    public CommonPopView(Context context){
        super(context);
        this.context = context;
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_commonpopview, this);
        layout = (LinearLayout)findViewById(R.id.closeable_layout);
//        title = (TextView)findViewById(R.id.title);
        close = (ImageView)findViewById(R.id.close);
        topImg = (ImageView)findViewById(R.id.commonpop_topimg);
        
        close.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
    	int id = v.getId();
    	if(id == Tools.getResourse(getContext(),"id","close")) {
            close();
        }
    }

    @Override
    public boolean isNeedMove() {
        return false;
    }
    
    /**
     * 验证顶图
     */
    public void setCheckViewTopImg(){
    	topImg.setImageDrawable(context.getResources().getDrawable(R.drawable.cp_sdk_check_top));
    }
    
}
