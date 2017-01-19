package com.zeustel.top9;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gotye.api.voichannel.ChannelInfo;
import com.gotye.api.voichannel.ErrorType;
import com.gotye.api.voichannel.LoginInfo;
import com.zeustel.top9.adapters.AdapterVoiceRoom;
import com.zeustel.top9.assist.gotye.BaseVoiceActivity;
import com.zeustel.top9.bean.SpeakUser;
import com.zeustel.top9.bean.VoiceRoom;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.result.VoiceRoomListResult;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.LoadProgress;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataVoiceRoom;
import com.zeustel.top9.widgets.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class VoiceActivity extends BaseVoiceActivity implements View.OnClickListener, AdapterVoiceRoom.OnItemVoiceJoinListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String EXTRA_NAME = "gameId";
    //测试数据
    //    String[] roomIds = {"719347", "719344", "719342", "628148", "eyJjaGFubmVsSWQiOiIxMzQiLCJyb29tSWQiOjg5Mzk1LCJzZXJ2ZXJJZCI6NzE4NiwidHlwZSI6MX0=",};
    //折叠视图
    private ExpandableListView voice_list;
    //聊天开关
    private TextView voice_toggle;
    //扬声器\麦克 开关
    private TextView voice_hf_toggle;
    private AdapterVoiceRoom adapter;
    //房间容器
    private List<VoiceRoom> data = new ArrayList();
    private Gson gson = new Gson();
    private LoadProgress mLoadProgress;
    //是否按过返回键
    private boolean isbackPressed;
    //加入房间索引
    private int joinPosition = -1;
    //临时房间索引
    private int tempPosition = -1;
    //退出房间索引
    private int exitPosition = -1;
    //是否禁用 扬声器\麦克
    private boolean muted;
    private int gameId;
    private DataVoiceRoom dataVoiceRoom;
    private SwipeRefreshLayout voice_list_refresh;

    @Override
    protected void onDestroy() {
        if (data != null) {
            data.clear();
            data = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            gameId = getIntent().getIntExtra(EXTRA_NAME, -1);
        }
        if (gameId == -1) {
            finish();
        }
        setContentView(R.layout.activity_voice);
        mLoadProgress = new LoadProgress(getApplicationContext());
        dataVoiceRoom = new DataVoiceRoom(getHandler());
        init();
        onPullDownRefresh();
    }

    @Override
    public void onHandleListFailed(Result result) {
        super.onHandleListFailed(result);
        mLoadProgress.dismiss();
        if (voice_list_refresh.isRefreshing())
            voice_list_refresh.setRefreshing(false);
    }

    @Override
    public void onHandleListUpdate(Result result, Object obj) {
        if (obj != null)
            data.clear();
        data.addAll((List<VoiceRoom>) obj);
        adapter.notifyDataSetChanged();
        mLoadProgress.dismiss();
        if (voice_list_refresh.isRefreshing())
            voice_list_refresh.setRefreshing(false);
    }

    @Override
    public void onHandleListNo(Result result) {
        super.onHandleListNo(result);
        mLoadProgress.dismiss();
        if (voice_list_refresh.isRefreshing())
            voice_list_refresh.setRefreshing(false);
    }

    /*
                *返回键
                * 没有进入过房间 直接关闭页面
                *
                * 如果正在摸个房间 先退出房间 再关闭页面
                * */
    @Override
    public void onBackPressed() {
        if (joinPosition != -1) {
            isbackPressed = true;
            mLoadProgress.show(getString(R.string.load_exit_channel));
            voiChannelAPI.exitChannel();
        } else {
            finish();
        }
    }

    @Override
    protected LoginInfo getLoginInfo() {
         /*亲加语音登录*/
        return new LoginInfo(Constants.USER.toSubString(), Constants.USER.getNickName(), null);
    }

    @Override
    protected void talking(boolean isTalking, String uId) {
        Tools.log_i(VoiceActivity.class, "talking", "isTalking : " + isTalking + ",uId:" + uId);
        SpeakUser subUserInfo = gson.fromJson(uId, SpeakUser.class);
        if (Constants.USER.equals(subUserInfo)) {
            //如果是本人 则更新下方语音开关状态
            voice_toggle.setSelected(isTalking);
        }
        /*更新当前房间内 isTalking 状态*/
        VoiceRoom voiceRoom = data.get(joinPosition);
        List<SpeakUser> members = voiceRoom.getMembers();
        if (members.contains(subUserInfo)) {
            SpeakUser speakUser = members.get(members.indexOf(subUserInfo));
            speakUser.setIsSpeak(isTalking);
        }
        adapter.notifyDataSetChanged();
        mLoadProgress.dismiss();
    }

    /**
     * 正在登录用户转换 SpeakUser对象
     */
    private SpeakUser toSpeakUser() {
        SpeakUser mSpeakUser = new SpeakUser();
        mSpeakUser.setId(Constants.USER.getId());
        mSpeakUser.setNickName(Constants.USER.getNickName());
        mSpeakUser.setIcon(Constants.USER.getIcon());
        mSpeakUser.setGender(Constants.USER.getGender());
        mSpeakUser.setLastModifyTime(Constants.USER.getLastModifyTime());
        mSpeakUser.setIsSpeak(false);
        return mSpeakUser;
    }

    /*进入房间回调*/
    @Override
    public void onJoinChannel(boolean b) {
        super.onJoinChannel(b);
        mLoadProgress.dismiss();
        if (b) {
            //            进入房间后标记
            joinPosition = tempPosition;
            //更新界面
            adapter.join(joinPosition);
/*当前用户添加到用户组第一项 并更新界面*/
            VoiceRoom voiceRoom = data.get(joinPosition);
            List<SpeakUser> members = null;
            if (voiceRoom.getMembers() != null) {
                members = voiceRoom.getMembers();
                members.add(0, toSpeakUser());
            } else {
                members = new ArrayList();
                members.add(0, toSpeakUser());
                voiceRoom.setMembers(members);
            }
            adapter.notifyDataSetChanged();
/*标记要退出的房间索引*/
            exitPosition = joinPosition;
            /*进入房间后 展开该组*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                voice_list.expandGroup(exitPosition, true);
            } else {
                voice_list.expandGroup(exitPosition);
            }
            dataVoiceRoom.listenEnterRoom(voiceRoom.getId());
        }
        Tools.log_i(VoiceActivity.class, "onJoinChannel", "进入了 exitPosition : " + exitPosition);
    }

    /*退出房间*/
    @Override
    public void onExitChannel(boolean b) {
        super.onExitChannel(b);
        //        exitPosition  通知服务器
        if (b) {
            /*更新上次房间界面*/
            VoiceRoom voiceRoom = data.get(exitPosition);
            List<SpeakUser> members = voiceRoom.getMembers();
            if (members.contains(Constants.USER)) {
                members.remove(Constants.USER);
                adapter.notifyDataSetChanged();
            }
            dataVoiceRoom.listenExitRoom(voiceRoom.getId());
        }
        Tools.log_i(VoiceActivity.class, "onExitChannel", "退出了 exitPosition : " + exitPosition);
        if (isbackPressed) {
            /*退出房间后 关闭界面*/
            mLoadProgress.dismiss();
            finish();
        }
    }

    @Override
    public void onError(ErrorType errorType) {
        mLoadProgress.dismiss();
        if (isbackPressed) {
             /*退出房间失败后 关闭界面*/
            finish();
            return;
        }
        super.onError(errorType);
    }

    @Override
    public void onGetChannelMember(String userId) {
        super.onGetChannelMember(userId);
        if (userId != null) {
            SpeakUser temp = gson.fromJson(userId, SpeakUser.class);
            VoiceRoom voiceRoom = data.get(joinPosition);
            List<SpeakUser> members = voiceRoom.getMembers();
            if (!members.contains(temp)) {
                members.add(temp);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onRemoveChannelMember(String userId) {
        super.onRemoveChannelMember(userId);
        if (userId != null) {
            SpeakUser temp = gson.fromJson(userId, SpeakUser.class);
            VoiceRoom voiceRoom = data.get(joinPosition);
            List<SpeakUser> members = voiceRoom.getMembers();
            if (members.contains(temp)) {
                members.remove(temp);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onMuteStateChanged(boolean muted) {
        super.onMuteStateChanged(muted);
        this.muted = muted;
        voice_hf_toggle.setSelected(this.muted);
        mLoadProgress.dismiss();
        Tools.log_i(VoiceActivity.class, "onMuteStateChanged", "b : " + muted);
    }

    private void init() {
        voice_list_refresh = (SwipeRefreshLayout) findViewById(R.id.voice_list_refresh);
        voice_list = (ExpandableListView) findViewById(R.id.voice_list);
        voice_toggle = (TextView) findViewById(R.id.voice_toggle);
        voice_hf_toggle = (TextView) findViewById(R.id.voice_hf_toggle);
        voice_list_refresh.setOnRefreshListener(this);
        voice_toggle.setOnClickListener(this);
        voice_hf_toggle.setOnClickListener(this);
        adapter = new AdapterVoiceRoom(getApplicationContext(), data);
        adapter.setListener(this);
        voice_list.setAdapter(adapter);
        voice_list.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //只打开一个组
                if (adapter.getGroupCount() > 0) {
                    for (int i = 0; i < adapter.getGroupCount(); i++) {
                        if (groupPosition == i) {
                            continue;
                        }
                        voice_list.collapseGroup(i);
                    }
                }
            }
        });
        voice_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                groupClick(parent, groupPosition);
                return true;
            }
        });
    }

    /**
     * 组开关
     */
    private void groupClick(ExpandableListView parent, int groupPosition) {
        if (parent.isGroupExpanded(groupPosition)) {
            parent.collapseGroup(groupPosition);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                parent.expandGroup(groupPosition, true);
            } else {
                parent.expandGroup(groupPosition);
            }
        }
    }

    /**
     * 测试数据
     */
  /*  private List<VoiceRoom> getData() {
        List<VoiceRoom> voiceRooms = new ArrayList();
        for (int i = 0; i < roomIds.length; i++) {
            String id = roomIds[i];
            VoiceRoom mVoiceRoom = new VoiceRoom();
            mVoiceRoom.setName("我是第 " + i + "--房间");
            mVoiceRoom.setSerialNum(id);
            List<SpeakUser> subUserInfos = new ArrayList();
            for (int j = 0; j < i; j++) {
                SpeakUser mSubUserInfo = new SpeakUser();
                mSubUserInfo.setNickName("昵称" + j);
                mSubUserInfo.setIcon(null);
                subUserInfos.add(mSubUserInfo);
            }
            mVoiceRoom.setMembers(subUserInfos);
            voiceRooms.add(mVoiceRoom);
        }
        return voiceRooms;
    }*/
    @Override
    public void onClick(View v) {
        if (joinPosition == -1) {
            return;
        }
        mLoadProgress.show(R.string.load_toggle_state);
        if (voice_toggle.equals(v)) {
            if (voice_toggle.isSelected()) {
                voiChannelAPI.stopTalking();
            } else {
                voiChannelAPI.startTalking();
            }
        } else if (voice_hf_toggle.equals(v)) {
            if (muted) {
                voiChannelAPI.restore();
            } else {
                voiChannelAPI.mute();
            }
        }
    }

    /**
     * adapter加入回调
     *
     * @param position  索引
     * @param voiceRoom 房间
     */
    @Override
    public void onItemVoiceJoinListener(int position, VoiceRoom voiceRoom) {
        if (voiceRoom != null) {
            if (joinPosition == position) {
                groupClick(voice_list, joinPosition);
                return;
            }
            tempPosition = position;
            ChannelInfo channelInfo = new ChannelInfo(voiceRoom.getSerialNum(), null);
            voiChannelAPI.joinChannel(channelInfo);
            mLoadProgress.show(getString(R.string.load_join_channel));
        }
    }

    @Override
    public void onPullDownRefresh() {
        mLoadProgress.show(R.string.pull_room_list);
        try {
            dataVoiceRoom.getListData(dataVoiceRoom.createListBundle(Constants.URL_TOP9_CHART_LIST + "?gameId=" + gameId, 0, NetHelper.Flag.UPDATE), VoiceRoomListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPullUpRefresh() {

    }
}
