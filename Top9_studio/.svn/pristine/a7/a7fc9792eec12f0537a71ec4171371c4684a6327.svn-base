package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.html.ItemInfo;
import com.zeustel.top9.utils.Tools;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/6 10:06
 */
public class HolderItemInfo extends OverallRecyclerHolder<ItemInfo> {
    private TextView info_key;
    private TextView info_value;
    private String key;
    private String value;
    private ItemInfo item;

    public HolderItemInfo(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        info_key = (TextView) itemView.findViewById(R.id.info_key);
        info_value = (TextView) itemView.findViewById(R.id.info_value);
    }

    @Override
    public void set(OverallRecyclerAdapter<ItemInfo> adapter, int position) {
        item = adapter.getItem(position);
        if (item != null) {
            key = item.getKey();
            value = item.getValue();
            info_key.setText(Tools.isEmpty(key) ? "" : key);
            info_value.setText(Tools.isEmpty(value) ? "" : value);

        }
    }

    @Override
    protected boolean isItemClickEnabled() {
        return false;
    }
}
