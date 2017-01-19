package com.zeustel.top9.widgets;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LayoutAnimationController;

import com.zeustel.top9.utils.Tools;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/31 11:35
 */
public class CustomRecyclerView extends RecyclerView {
    private GestureDetectorCompat gestureDetector;
    private boolean anim = true;

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onItemClickListener = null;
        onItemLongClickListener = null;
        gestureDetector = null;
        anim = false;
    }

    public CustomRecyclerView(Context context, boolean anim) {
        super(context);
        this.anim = anim;
        init(context);
    }

    public CustomRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (Tools.animLeftIn != null && anim) {
            LayoutAnimationController lac = new LayoutAnimationController(Tools.animLeftIn);
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            lac.setDelay(0.8f);
            setLayoutAnimation(lac);
        }
        addOnItemTouchListener(new ItemTouchListener());
        gestureDetector = new GestureDetectorCompat(context, new GestureListener());
    }

    /**
     * 每项触摸监听
     */
    private class ItemTouchListener extends SimpleOnItemTouchListener {
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            //交给手势监听处理
            gestureDetector.onTouchEvent(e);
            return false;
        }
    }

    /**
     * 手势监听
     */
    private class GestureListener extends android.view.GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            //通过触摸位置获取子view
            View itemView = findChildViewUnder(e.getX(), e.getY());
            if (itemView != null) {
                ViewHolder viewHolder = getChildViewHolder(itemView);
                if (viewHolder != null) {
                    int position = viewHolder.getAdapterPosition();
                    int viewType = viewHolder.getItemViewType();
                    if (NO_POSITION != position) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(CustomRecyclerView.this, viewHolder, itemView, viewType, position);
                        }
                    }
                }
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //通过触摸位置获取子view
            View itemView = findChildViewUnder(e.getX(), e.getY());
            if (itemView != null) {
                ViewHolder viewHolder = getChildViewHolder(itemView);
                if (viewHolder != null) {
                    int position = viewHolder.getAdapterPosition();
                    int viewType = viewHolder.getItemViewType();
                    if (NO_POSITION != position) {
                        if (onItemLongClickListener != null) {
                            onItemLongClickListener.onItemLongClick(CustomRecyclerView.this, viewHolder, itemView, viewType, position);
                        }
                    }
                }
            }
            super.onLongPress(e);
        }
    }

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener {
        boolean onItemClick(RecyclerView parent, ViewHolder viewHolder, View itemView, int viewType, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(RecyclerView parent, ViewHolder viewHolder, View itemView, int viewType, int position);
    }
}
