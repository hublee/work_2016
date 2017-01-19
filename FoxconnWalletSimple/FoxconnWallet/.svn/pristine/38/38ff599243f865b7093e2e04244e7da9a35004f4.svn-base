package com.zeustel.cp.bean;

//import com.zeustel.cp.intf.ClickEvent;

import android.view.GestureDetector;
import android.view.MotionEvent;


/**
 * Created by Administrator on 2015/12/3.
 * 处理多个点击事件
 */
public class OnMultiClick extends GestureDetector.SimpleOnGestureListener {
    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);
//        EventBus.getDefault().post(new ClickEvent(ClickEvent.LONGCLICK));

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
//        EventBus.getDefault().post(new ClickEvent(ClickEvent.SINGLECLICK));
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
//        EventBus.getDefault().post(new ClickEvent(ClickEvent.DOUBLECLICK));
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }
}
