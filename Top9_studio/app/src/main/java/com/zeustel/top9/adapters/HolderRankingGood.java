package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.SubUserInfo;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.CircleImage;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/12/4 16:32
 */
public class HolderRankingGood extends OverallRecyclerHolder<SubUserInfo> {
    private CircleImage item_ranking_icon;
    private TextView item_ranking_count;
    private SubUserInfo item;
    private int position;

    public HolderRankingGood(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        item_ranking_icon = (CircleImage) itemView.findViewById(R.id.item_ranking_icon);
        item_ranking_count = (TextView) itemView.findViewById(R.id.item_ranking_count);
    }

    @Override
    public void set(OverallRecyclerAdapter<SubUserInfo> adapter, int position) {
        item = adapter.getItem(position);
        if (item != null) {
            getImageLoader().displayImage(Constants.TEST_IMG + item.getIcon(), item_ranking_icon, Tools.options);
            item_ranking_count.setText(String.valueOf(item.getGoodCount()));
        }
        if (this.position == position) {
            item_ranking_count.setSelected(true);
        } else {
            item_ranking_count.setSelected(false);
        }
    }

    public void select(int position) {
        this.position = position;
        if (position == getAdapterPosition()) {
            item_ranking_count.setSelected(true);
        }
    }
}
