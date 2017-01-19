package com.zeustel.top9.adapters;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.SpeakUser;
import com.zeustel.top9.bean.VoiceRoom;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.CustomRecyclerView;
import com.zeustel.top9.widgets.FullyGridLayoutManager;

import java.util.List;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/8 15:19
 */
public class AdapterVoiceRoom extends BaseExpandableListAdapter {
    private Context context;
    //房间集合
    private List<VoiceRoom> data;
    private LayoutInflater inflater;
    //加入房间回调
    private OnItemVoiceJoinListener listener;
    //加入房间索引
    private int joinPosition = -1;

    public AdapterVoiceRoom(Context context, List<VoiceRoom> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    public void setListener(OnItemVoiceJoinListener listener) {
        this.listener = listener;
    }

    /**
     * 加入 更新按钮状态
     *
     * @param groupPosition
     */
    public void join(int groupPosition) {
        this.joinPosition = groupPosition;
    }

    @Override
    public int getGroupCount() {
        return data == null ? 0 : data.size();
    }

    //固定1项 用recyclerview替换
    @Override
    public int getChildrenCount(int groupPosition) {
        if (!Tools.isEmpty(data) && data.get(groupPosition) != null) {
            List<SpeakUser> members = data.get(groupPosition).getMembers();
            if (!Tools.isEmpty(members)) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public VoiceRoom getGroup(int groupPosition) {
        return Tools.isEmpty(data) ? null : data.get(groupPosition);
    }

    public List<SpeakUser> getChildData(int groupPosition) {
        return getGroup(groupPosition) == null ? null : getGroup(groupPosition).getMembers();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder mGroupViewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(GroupViewHolder.LAYOUT_ID, null);
            mGroupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(mGroupViewHolder);
        } else {
            mGroupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        mGroupViewHolder.set(groupPosition, isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder mChildViewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(ChildViewHolder.RECYCLER_LAYOUT_ID, null);
            mChildViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(mChildViewHolder);
        } else {
            mChildViewHolder = (ChildViewHolder) convertView.getTag();
        }
        mChildViewHolder.set(groupPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 点击加入房间回调
     */
    public interface OnItemVoiceJoinListener {
        void onItemVoiceJoinListener(int position, VoiceRoom voiceRoom);
    }

    class GroupViewHolder implements View.OnClickListener {
        static final int LAYOUT_ID = R.layout.list_item_voice_group;
        private TextView voice_group_name;
        private TextView voice_group_count;
        private ImageView voice_group_join;
        private ImageView voice_group_toggle;
        private FrameLayout voice_group_line;
        private int position;
        private VoiceRoom voiceRoom;

        GroupViewHolder(View groupView) {
            voice_group_name = (TextView) groupView.findViewById(R.id.voice_group_name);
            voice_group_count = (TextView) groupView.findViewById(R.id.voice_group_count);
            voice_group_join = (ImageView) groupView.findViewById(R.id.voice_group_join);
            voice_group_toggle = (ImageView) groupView.findViewById(R.id.voice_group_toggle);
            voice_group_line = (FrameLayout) groupView.findViewById(R.id.voice_group_line);
            voice_group_join.setOnClickListener(this);
        }

        void set(int position, boolean isExpaned) {
            this.position = position;
            voiceRoom = getGroup(position);
            if (voiceRoom != null) {
                voice_group_name.setText(voiceRoom.getName());
                voice_group_count.setText("(" + (voiceRoom.getMembers() == null ? 0 : voiceRoom.getMembers().size()) + ")");
            }
            /*切换指示器*/
            voice_group_toggle.setSelected(isExpaned);
            /*奇幻加入图标*/
            if (joinPosition != -1) {
                if (joinPosition == position) {
                    voice_group_join.setSelected(true);
                } else {
                    voice_group_join.setSelected(false);
                }
            }
            /*打开的组隐藏分割线*/
            if (getGroupCount() - 1 != position) {
                voice_group_line.setVisibility(isExpaned ? View.INVISIBLE : View.VISIBLE);
            } else {
                voice_group_line.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemVoiceJoinListener(position, voiceRoom);
            }
        }
    }

    public class ChildViewHolder {
        static final int LAYOUT_ID = R.layout.list_item_voice_child;
        static final int RECYCLER_LAYOUT_ID = R.layout.list_item_voice_child_recycler;
        static final int SPAN_COUNT = 5;
        public AdapterSpeakUser adapter;
        private CustomRecyclerView childView;
        private FrameLayout voice_child_recycler_line;

        public ChildViewHolder(View childView) {
            this.childView = (CustomRecyclerView) childView.findViewById(R.id.voice_child_recycler);
            this.voice_child_recycler_line = (FrameLayout) childView.findViewById(R.id.voice_child_recycler_line);
            FullyGridLayoutManager gridLayoutManager = new FullyGridLayoutManager(context, SPAN_COUNT);
            this.childView.setLayoutManager(gridLayoutManager);
            //设置默认动画
            this.childView.setItemAnimator(new DefaultItemAnimator());
            this.childView.setHasFixedSize(true);
            this.childView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_alpha_in));
        }

        public void set(int position) {
            if (!Tools.isEmpty(getChildData(position))) {
                try {
                    adapter = new AdapterSpeakUser(getChildData(position), LAYOUT_ID);
                    childView.setAdapter(adapter);
                    if (getGroupCount() - 1 != position) {
                        voice_child_recycler_line.setVisibility(View.VISIBLE);
                    } else {
                        voice_child_recycler_line.setVisibility(View.INVISIBLE);
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
