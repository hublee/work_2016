package com.zeustel.cp.views;

import android.content.Context;
import android.view.View;

import com.zeustel.cp.bean.PopView;
import com.zeustel.foxconn.cp_sdk.R;

/**
 * Created by Do on 2016/2/23.
 */
public class Music extends PopView{
    public Music(Context context){
        super(context);
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_set, this);
    }

    @Override
    public void onClick(View v) {

    }
}
