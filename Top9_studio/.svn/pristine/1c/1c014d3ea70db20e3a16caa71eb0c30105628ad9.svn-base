package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.View;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.SubUserInfo;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.CircleImage;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/12 16:34
 */
public class HolderGG extends OverallRecyclerHolder<SubUserInfo> {
    private CircleImage item_cir;
    private SubUserInfo item;
    private String icon;

    public HolderGG(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        item_cir = (CircleImage) itemView.findViewById(R.id.item_cir);
    }

    @Override
    public void set(OverallRecyclerAdapter<SubUserInfo> adapter, int position) {
        if (adapter != null) {
            item = adapter.getItem(position);

            if (item != null && item_cir != null) {
                icon = item.getIcon();
                getImageLoader().displayImage(Constants.TEST_IMG + icon, item_cir, Tools.options);
            }
        }
    }

    @Override
    protected boolean isItemClickEnabled() {
        return false;
    }
}
