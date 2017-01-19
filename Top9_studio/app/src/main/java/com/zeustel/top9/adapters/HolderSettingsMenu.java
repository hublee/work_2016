package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.SelfItem;

/**
 * settings界面item
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/10 09:33
 */
public class HolderSettingsMenu extends OverallRecyclerHolder<SelfItem> {
    private TextView item_settings_title;
    private SelfItem item;
    private String text;

    public HolderSettingsMenu(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        item_settings_title = (TextView) itemView.findViewById(R.id.item_settings_title);
    }

    @Override
    public void set(OverallRecyclerAdapter<SelfItem> adapter, int position) {
        item = adapter.getItem(position);
        if (item == null) {
            return;
        }
        text = item.getText();
        item_settings_title.setText(text);
    }

    @Override
    protected boolean isItemClickEnabled() {
        return false;
    }
}
