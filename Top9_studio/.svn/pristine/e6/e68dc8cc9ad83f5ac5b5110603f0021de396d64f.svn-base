package com.zeustel.top9.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/9 10:37
 */
public class HolderSupport extends OverallRecyclerHolder<String> {
    /*根据图片*/
    public static final int MODE_IMG = 0;
    /*根据布局*/
    public static final int MODE_LAYOUT = 1;
    private ImageView imageView;
    private View itemView;
    private int itemWidth;
    private int itemHeight;
    private int mode = MODE_IMG;
    /*是否来源于网络*/
    private boolean isNet;
    private String path;

    public HolderSupport(Context context, View itemView) {
        super(context, itemView);
    }

    public boolean isNet() {
        return isNet;
    }

    public void setIsNet(boolean isNet) {
        this.isNet = isNet;
    }

    @Override
    protected void initItemView(View itemView) {
        this.itemView = itemView;
        this.imageView = (ImageView) itemView.findViewById(R.id.support_item);
    }

    @Override
    public void set(OverallRecyclerAdapter<String> adapter, int position) {
        if (MODE_LAYOUT == mode) {
            if (itemWidth != 0 | itemHeight != 0) {
                if (itemView != null) {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
                    params.width = itemWidth;
                    params.height = itemHeight;
                }
            }
        } else {
            if (itemWidth != 0 | itemHeight != 0) {
                if (imageView != null) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                    params.width = itemWidth;
                    params.height = itemHeight;
                }
            }
        }
        path = adapter.getItem(position);
        if (isNet) {
            Tools.log_i(HolderSupport.class, "set", "img : " + Constants.TEST_IMG + path);
            getImageLoader().displayImage(Constants.TEST_IMG + path, imageView, Tools.options);
        } else {
            getImageLoader().displayImage("file://" + path, imageView, Tools.options);
        }
    }

    public void setItemLayoutParams(int itemWidth, int itemHeight, int mode) {
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.mode = mode;
    }

    @Override
    protected boolean isItemClickEnabled() {
        return false;
    }
}
