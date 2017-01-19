package com.zeustel.top9.utils;

import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/20 14:46
 */
public class DelayAnimRunnable implements Runnable {
    private View view;
    private Handler handler;
    private boolean isLeftIn;
    private static final int DELAY_MILL = 250;

    public DelayAnimRunnable(View view,Handler handler,boolean isLeftIn) {
        this.view = view;
        this.handler = handler;
        this.isLeftIn = isLeftIn;
    }

    @Override
    public void run() {
        if (view != null && view instanceof ViewGroup) {
             ViewGroup viewGroup = ((ViewGroup) view);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                Tools.log_i(DelayAnimRunnable.class,"run"," i : "+ i);
                 View childView = viewGroup.getChildAt(i);
                handler.post(new AnimRunnable(childView,isLeftIn));
                try {
                    Thread.sleep(DELAY_MILL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
