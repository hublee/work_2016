package com.zeustel.top9.widgets;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;

import static android.view.WindowManager.LayoutParams;

/**
 * 悬浮标题
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/20 14:19
 */
@Deprecated
public class FloatTextView {
    private WindowManager mWindowManager = null;
    private LayoutParams wmParams = null;
    //悬浮textView
    private TextView floatText;

    public FloatTextView(Activity activity) {
        mWindowManager = activity.getWindowManager();
        floatText = new TextView(activity);
        floatText.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.getResources().getDimension(R.dimen.float_title_textsize));
        floatText.setTextColor(Color.WHITE);
        floatText.setGravity(Gravity.CENTER);
        LayoutParams wmParams = new LayoutParams();
        // 设置window type
        wmParams.type = LayoutParams.TYPE_PHONE;
        // 设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE|LayoutParams.FLAG_NOT_TOUCH_MODAL|LayoutParams.FLAG_NOT_TOUCH_MODAL;
        // LayoutParams.FLAG_ALT_FOCUSABLE_IM
        // | LayoutParams.FLAG_NOT_TOUCH_MODAL
        // | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // 调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = Tools.dip2px(activity,20);
        wmParams.y =  Tools.dip2px(activity,0);
        // 设置悬浮窗口长宽数据
        wmParams.width = Math.round(activity.getResources().getDimension(R.dimen.float_title_width));
        wmParams.height =Math.round(activity.getResources().getDimension(R.dimen.float_title_height));
        mWindowManager.addView(floatText, wmParams);
    }
    public void setText(String text){
        floatText.setText(text);
    }
    public void setText(String text,int color){
        setText(text);
        floatText.setTextColor(color);
    }
    public void setBackgroundColor(int backgroundColor){
        floatText.setBackgroundColor(backgroundColor);

    }
    public void setVisibility(int visibility){
        floatText.setVisibility(visibility);
    }
    public int getVisibility() {
       return floatText.getVisibility();
    }
    public void onDestroy(){
        mWindowManager.removeView(floatText);
        mWindowManager = null;
        wmParams = null;
    }
}
