package com.zeustel.cp.views;

import android.content.Context;
import android.view.View;

import com.zeustel.foxconn.cp_sdk.R;


/**
 * Created by Do on 2016/2/23.
 */
public class Gift extends Closeable{
    public Gift(Context context){
        super(context);
        addView();
    }

    private void addView() {
        inflate(getContext(), R.layout.view_gift, layout);
    }

    @Override
    public void onClick(View v) {

    }
}
