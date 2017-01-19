package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/13 12:40
 */
public class ScrollGroup extends ViewGroup {
    //阻尼比例
    private static final float DAMPING_PERCENT = 0.8F;
    //滑动距离 针对头
    private static final float DISTANCE_PERCENT = 0.3F;

    private float startY;

    public ScrollGroup(Context context) {
        super(context);
        initView(context, null, 0, 0);
    }

    public ScrollGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0, 0);
    }

    public ScrollGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int childCount = getChildCount();
        int maxWidth = 0;
        int childWidth = 0;
        int childTotalHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            childWidth = lp.leftMargin + lp.rightMargin + child.getMeasuredWidth();
            maxWidth = Math.max(childWidth, maxWidth);
            childTotalHeight = lp.topMargin + lp.bottomMargin + child.getMeasuredHeight();
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? sizeWidth : maxWidth, heightMode == MeasureSpec.EXACTLY ? sizeHeight : childTotalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int top = 0;
        int bottom = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            top = bottom + lp.topMargin;
            bottom = top + child.getMeasuredHeight();
            child.layout(lp.leftMargin, top, child.getMeasuredWidth() + lp.rightMargin, bottom);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        final float y = event.getY();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startY = y;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                scrollBy(0, (int) (startY - y));
//                startY = y;
//                Tools.log_i(ScrollGroup.class, "onTouchEvent", "" + getScrollY());
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return true;
//    }
}
