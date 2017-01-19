package com.zeustel.top9.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 单选RecyclerView
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/5 15:15
 */
@Deprecated
public class RadioRecyclerView extends CustomRecyclerView {
    private int mCheckedPosition = -1;

    public RadioRecyclerView(Context context) {
        super(context);
    }

    public RadioRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RadioRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 选择某项
     *
     * @param position 索引
     */
    public void check(int position) {
        if (position != -1 && (position == mCheckedPosition)) {
            return;
        }
        if (mCheckedPosition != -1) {
            setCheckedStateForViewHodler(mCheckedPosition, false);
        }

        if (position != -1) {
            setCheckedStateForViewHodler(position, true);
        }
        setCheckedPosition(position);
    }

    /**
     * 选择某项
     *
     * @param position 索引
     */
    private void setCheckedPosition(int position) {
        mCheckedPosition = position;
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged(this, mCheckedPosition);
        }
    }

    /**
     * 设置选择状态
     *
     * @param position 索引
     * @param checked  是否选择
     */
    private void setCheckedStateForViewHodler(int position, boolean checked) {
        View itemView = getChildAt(position);
        if (itemView != null) {
            ViewHolder viewHolder = getChildViewHolder(itemView);
            if (viewHolder != null) {
                if (viewHolder instanceof IRadioViewHolder) {
                    ((IRadioViewHolder) viewHolder).check(position, checked);
                }
            }
        }
    }

    private OnCheckedChangeListener onCheckedChangeListener;

    /**
     * 添加监听
     *
     * @param onCheckedChangeListener
     */
    public void addOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    /**
     * 选择改变监听
     */
    public static interface OnCheckedChangeListener {
        public void onCheckedChanged(RadioRecyclerView radioRecyclerView, int position);
    }

    /**
     * 应用这个组件 必须使用实现IRadioViewHolder的viewholder
     */
    public static interface IRadioViewHolder {
        public void check(int position, boolean checked);
    }
}
