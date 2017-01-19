package com.zeustel.top9.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.Community;
import com.zeustel.top9.bean.CommunityInfo;
import com.zeustel.top9.bean.CommunityTopic;
import com.zeustel.top9.bean.SubUserInfo;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/13 09:52
 */
public class HolderCommunity extends OverallRecyclerHolder<Community> {
    private static String[] topicType = null;
    private static String[] infoType = null;
    private ImageView community_item_icon;
    private TextView community_item_name;
    private TextView community_item_title;
    private TextView community_item_subTitle;
    private TextView community_item_time;
    private TextView community_item_comment;
    private String title;

    public HolderCommunity(Context context, View itemView) {
        super(context, itemView);
        if (topicType == null) {
            topicType = context.getResources().getStringArray(R.array.topicType);
        }
        if (infoType == null) {
            infoType = context.getResources().getStringArray(R.array.infoType);
        }
    }

    @Override
    protected void initItemView(View itemView) {
        community_item_icon = (ImageView) itemView.findViewById(R.id.community_item_icon);
        community_item_name = (TextView) itemView.findViewById(R.id.community_item_name);
        community_item_title = (TextView) itemView.findViewById(R.id.community_item_title);
        community_item_subTitle = (TextView) itemView.findViewById(R.id.community_item_subTitle);
        community_item_time = (TextView) itemView.findViewById(R.id.community_item_time);
        community_item_comment = (TextView) itemView.findViewById(R.id.community_item_comment);
    }

    @Override
    public void set(OverallRecyclerAdapter<Community> adapter, int position) {
        Community community = adapter.getItem(position);
        if (community != null) {
            title = community.getTitle();
            if (community instanceof CommunityInfo) {
                getImageLoader().displayImage(Constants.TEST_IMG + ((CommunityInfo) community)
                        .getIcon(), community_item_icon, Tools.newOptions(community_item_icon
                        .getWidth() / 2));
                community_item_name.setVisibility(View.GONE);
                community_item_title.setText(infoType[((CommunityInfo) community).getInfoType()]);
            } else if (community instanceof CommunityTopic) {
                SubUserInfo subUserInfo = ((CommunityTopic) community).getSubUserInfo();
                if (subUserInfo != null) {
                    getImageLoader().displayImage(Constants.TEST_IMG + subUserInfo.getIcon(),
                            community_item_icon, Tools.newOptions(community_item_icon.getWidth()
                                    / 2));
                    community_item_name.setText(subUserInfo.getNickName());
                }
                community_item_title.setText(topicType[((CommunityTopic) community).getTopicType
                        ()]);
            }
            community_item_subTitle.setText(community.getSubTitle());
            community_item_time.setText(Tools.getTimeFormatformatyyyyDDNN(community.getTime()));
            community_item_comment.setText(String.valueOf(community.getCommentAmount()));
            if (getLightText() != null) {
                SpannableStringBuilder spannableStringBuilder = Tools.highLight(title,
                        getLightText(), ContextCompat.getColor(getContext(), R.color.red));
                if (spannableStringBuilder != null) {
                    community_item_title.append(spannableStringBuilder);
                    return;
                }
            }
            community_item_title.append(title == null ? "" : title);
        }
    }

    @Override
    protected boolean isItemClickEnabled() {
        return false;
    }
}
