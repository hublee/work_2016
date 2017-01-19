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
public class HolderRankingReply extends OverallRecyclerHolder<SubUserInfo> {
    private CircleImage item_ranking_icon;
    private TextView item_ranking_count;
    private SubUserInfo item;

    public HolderRankingReply(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        item_ranking_icon = (CircleImage) itemView.findViewById(R.id.item_ranking_icon);
        item_ranking_count = (TextView) itemView.findViewById(R.id.item_ranking_count);
        item_ranking_count.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.comment_reply, 0, 0, 0);
    }

    @Override
    public void set(OverallRecyclerAdapter<SubUserInfo> adapter, int position) {
        item = adapter.getItem(position);
        if (item != null) {
            getImageLoader().displayImage(Constants.TEST_IMG + item.getIcon(), item_ranking_icon, Tools.options);
            item_ranking_count.setText(String.valueOf(item.getReplyCount()));
        }
    }
}
