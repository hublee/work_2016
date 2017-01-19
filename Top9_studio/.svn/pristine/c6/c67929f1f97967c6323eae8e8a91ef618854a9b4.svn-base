package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/12 16:34
 */
public class HolderII extends OverallRecyclerHolder<String> {
    private ImageView item_ii;
    private String item;

    public HolderII(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        item_ii = (ImageView) itemView.findViewById(R.id.item_ii);
    }

    @Override
    public void set(OverallRecyclerAdapter<String> adapter, int position) {
        if (adapter != null) {
            item = adapter.getItem(position);
            if (item != null && item_ii != null) {
                getImageLoader().displayImage("file://" + item, item_ii, Tools.options);
            }
        }
    }

    @Override
    protected boolean isItemClickEnabled() {
        return false;
    }
}
