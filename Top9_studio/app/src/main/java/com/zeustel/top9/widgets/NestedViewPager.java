package com.zeustel.top9.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/22 16:40
 */
public class NestedViewPager extends CustomViewPager {
    public NestedViewPager(Context context) {
        super(context);
        init();
    }

    public NestedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        ViewParent mParent = getParent();
        if (mParent != null && mParent instanceof CustomViewPager) {
            mParent.requestDisallowInterceptTouchEvent(true);
        }
    }
}
