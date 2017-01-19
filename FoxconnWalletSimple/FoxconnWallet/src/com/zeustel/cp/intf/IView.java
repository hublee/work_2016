package com.zeustel.cp.intf;

import android.view.View;

/**
 * Created by Administrator on 2015/11/23.
 */
public interface IView {
    public void dropView(View view);
    public View addView(int viewId);
    public void close();
}
