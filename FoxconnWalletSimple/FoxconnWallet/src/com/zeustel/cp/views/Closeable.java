package com.zeustel.cp.views;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.zeustel.cp.bean.PopView;
import com.zeustel.cp.utils.Tools;

/**
 * Created by Do on 2016/2/23.
 */
public class Closeable  extends PopView{
    protected LinearLayout layout;
    public Closeable(Context context){
        super(context);
    }

    @Override
    protected void initView() {
        inflate(getContext(), Tools.getResourse(getContext(), "layout", "view_closeable"), this);
        layout = (LinearLayout)findViewById(Tools.getResourse(getContext(), "id", "closeable_layout"));
    }

    @ Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
