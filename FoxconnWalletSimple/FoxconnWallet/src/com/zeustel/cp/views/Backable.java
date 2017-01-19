package com.zeustel.cp.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeustel.cp.bean.PopView;
import com.zeustel.foxconn.cp_sdk.R;


/**
 * Created by Do on 2016/2/24.
 */
public class Backable extends PopView {
    protected LinearLayout layout;

    private ImageView back;
    private TextView title;

    public Backable(Context context){
        super(context);
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_backable, this);
        layout = (LinearLayout)findViewById(R.id.closeable_layout);
        title = (TextView)findViewById(R.id.title);
        back = (ImageView)findViewById(R.id.back);

        back.setOnClickListener(this);
    }

    /**
     * 设置标题
     * @param str
     */
    public void setTitle(String str){
        title.setText(str);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.back){
            back();
        }
    }

    public void back(){

    }

    @Override
    public boolean isNeedMove() {
        return false;
    }
    
}
