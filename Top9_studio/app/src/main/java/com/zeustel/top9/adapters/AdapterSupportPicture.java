package com.zeustel.top9.adapters;

import android.view.ViewGroup;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;

import java.util.List;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/9 11:25
 */
public class AdapterSupportPicture extends OverallRecyclerAdapter<String> {
    private int itemWidth, itemHeight;
    private boolean isNet;
    private int mode = HolderSupport.MODE_IMG;

    public AdapterSupportPicture(List<String> data) throws NoSuchMethodException {
        super(data, R.layout.list_item_support, HolderSupport.class);
    }

    public void setSpanCount(int maxCount, int spanCount, int width, int height) {
        if (width != 0 && height != 0) {
            int rows = maxCount / spanCount;
            int lastRow = maxCount % spanCount;
            rows = rows + lastRow;
            float itemWidth = width / spanCount;
            float itemHeight = height / rows;
            this.itemWidth = this.itemHeight = (int) Math.min(itemWidth, itemHeight);
        } else {
            if (width == 0 && height == 0) {
                Tools.log_i(AdapterSupportPicture.class, "setSpanCount", "all 0");
                return;
            }
            int max = Math.max(width, height);
            if (max == width) {
                float itemWidth = width / spanCount;
                this.itemWidth = this.itemHeight = (int) itemWidth;
            } else {
                //...
            }
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public boolean isNet() {
        return isNet;
    }

    public void setIsNet(boolean isNet) {
        this.isNet = isNet;
    }

    @Override
    public OverallRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderSupport viewHolder = (HolderSupport) super.onCreateViewHolder(parent, viewType);
        viewHolder.setItemLayoutParams(itemWidth, itemHeight, mode);
        viewHolder.setIsNet(isNet);
        return viewHolder;
    }
}
