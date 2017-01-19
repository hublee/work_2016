package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.zeustel.top9.utils.Tools;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/12/2 11:19
 */
public class RecycleImageView extends ImageView {
    public RecycleImageView(Context context) {
        super(context);
    }

    public RecycleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RecycleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Tools.recycleImg(this);
        if (isAutoRecycle) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.gc();
                }
            }, 1000);
        }
    }

    //是否自动调用gc
    private boolean isAutoRecycle = false;

    public boolean isAutoRecycle() {
        return isAutoRecycle;
    }

    public void setIsAutoRecycle(boolean isAutoRecycle) {
        this.isAutoRecycle = isAutoRecycle;
    }
}
