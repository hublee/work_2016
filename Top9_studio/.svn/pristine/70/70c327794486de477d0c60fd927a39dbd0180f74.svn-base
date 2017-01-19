package com.zeustel.top9.fragments;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zeustel.top9.PublishTopicActivity;
import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderCommunity;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.base.WrapOneAndRefreshFragment;
import com.zeustel.top9.bean.Community;
import com.zeustel.top9.bean.CommunityInfo;
import com.zeustel.top9.bean.CommunityTopic;
import com.zeustel.top9.bean.HtmlPaper;
import com.zeustel.top9.result.CommunityInfoListResult;
import com.zeustel.top9.result.CommunityTopicListResult;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.NetHelper;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DBCommunityInfoImp;
import com.zeustel.top9.utils.operate.DBCommunityTopicImp;
import com.zeustel.top9.utils.operate.DataCommunityInfo;
import com.zeustel.top9.utils.operate.DataCommunityTopic;
import com.zeustel.top9.widgets.CustomRecyclerView;
import com.zeustel.top9.widgets.PopupMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

/**
 * 社区资讯 社区话题
 */
public class CommunityFragment extends WrapOneAndRefreshFragment<Community> implements View.OnClickListener, CustomRecyclerView.OnItemClickListener, AdapterView.OnItemClickListener {
    protected static final String EXTRA_NAME_ID = "gameId";
    protected static final String EXTRA_NAME_TYPE = "type";
    private int gameId;
    private int type;
    private String url;
    private TextView community_browse;
    private TextView community_release;
    private FrameLayout community_line;
    private PopupMenu mPopupMenu = null;
    private int tempType;
    private int length;
    private String[] browse = null;
    private OverallRecyclerAdapter<Community> adapter;
    private List<Community> animData = new ArrayList();
    private boolean isFirst;

    public CommunityFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (animData != null) {
            animData.clear();
            animData = null;
        }
    }

    public static CommunityFragment newInstance(int gameId, int type) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_NAME_ID, gameId);
        args.putInt(EXTRA_NAME_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameId = getArguments().getInt(EXTRA_NAME_ID);
            type = getArguments().getInt(EXTRA_NAME_TYPE);
        }
        if (gameId <= 0 || type < 0) {
            getFragmentManager().popBackStack();
        }
        isFirst = true;
    }

    @Override
    public View onBeforeCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_community, container, false);
        community_browse = (TextView) contentView.findViewById(R.id.community_browse);
        community_release = (TextView) contentView.findViewById(R.id.community_release);
        community_line = (FrameLayout) contentView.findViewById(R.id.community_line);
        mPopupMenu = new PopupMenu(getActivity());

        Resources res = getResources();
        /*社区和资讯布局不同之处*/
        if (Community.CommunityType.communityInfo == type) {
            community_release.setVisibility(View.GONE);
            community_line.setVisibility(View.GONE);
            url = Constants.URL_TOP9_INFO_LIST;
            browse = res.getStringArray(R.array.infoType);
        } else {
            community_release.setVisibility(View.VISIBLE);
            community_line.setVisibility(View.VISIBLE);
            community_release.setOnClickListener(this);
            url = Constants.URL_TOP9_TOPIC_LIST;
            browse = res.getStringArray(R.array.topicType);
        }
        tempType = length = browse.length - 1;
        mPopupMenu.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.popup_menu_item, R.id.popup_menu_item, Arrays.asList(browse)));
        mPopupMenu.getContent().setOnItemClickListener(this);
        community_browse.setOnClickListener(this);
        return contentView;
    }

    @Override
    protected void onAfterCreateView(View contentView) {
        setDbOperate(Community.CommunityType.communityInfo == type ? new DBCommunityInfoImp(getActivity()) : new DBCommunityTopicImp(getActivity()));
        setDataOperate(Community.CommunityType.communityInfo == type ? new DataCommunityInfo(getHandler(), getDbOperate()) : new DataCommunityTopic(getHandler(), getDbOperate()));
        try {
            setAdapter(adapter = new OverallRecyclerAdapter<Community>(getData(), R.layout.list_item_community, HolderCommunity.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        getRecyler().setOnItemClickListener(this);
    }

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public void onPullDownRefresh() {
        Community community = Tools.getFirstData(getData());
        long time = 0;
        if (community != null) {
            time = community.getTime();
        }
        try {
            getDataOperate().getListData(getDataOperate().createListBundle(url + "?flag=" + NetHelper.Flag.UPDATE + "&time=" + time + "&gameId=" + gameId, time, NetHelper.Flag.UPDATE), Community.CommunityType.communityInfo == type ? CommunityInfoListResult.class : CommunityTopicListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPullUpRefresh() {
        Community community = Tools.getLastData(getData());
        long time = 0;
        if (community != null) {
            time = community.getTime();
        }
        try {
            getDataOperate().getListData(getDataOperate().createListBundle(url + "?flag=" + NetHelper.Flag.HISTORY + "&time=" + time + "&gameId=" + gameId, time, NetHelper.Flag.HISTORY), Community.CommunityType.communityInfo == type ? CommunityInfoListResult.class : CommunityTopicListResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (community_browse.equals(v)) {
            if (!mPopupMenu.isShowing()) {
                mPopupMenu.showAsDropDown(community_browse);
            } else {
                mPopupMenu.dismiss();
            }
        } else if (community_release.equals(v)) {
           /* getFragmentManager().beginTransaction().add(R.id.selfDetail, PublishTopicFragment
                    .newInstance(gameId)).commit();*/
            Intent intent = new Intent(getContext(), PublishTopicActivity.class);
            intent.putExtra(PublishTopicActivity.EXTRA_NAME, gameId);
            startActivityForResult(intent, PublishTopicActivity.REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Community.CommunityType.communityTopic == type && PublishTopicActivity.REQUEST_CODE == requestCode) {
            onPullDownRefresh();//退出发表话题页面 刷新列表
        }
    }

    @Override
    public boolean onItemClick(RecyclerView parent, RecyclerView.ViewHolder viewHolder, View itemView, int viewType, int position) {
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.selfDetail, HtmlDetailFragment.newInstance(adapter.getItem(position))).commit();
        Tools.log_i(CommunityFragment.class, "onItemClick", "size : " + getData().size());
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (this.tempType == position) {
            //重复
            return;
        }
        if (this.tempType == length) {
            //all - single
            this.tempType = position;
            all2Single(adapter, getData());
        } else {
            if (position == length) {
                //single - all
                this.tempType = position;
                single2All(adapter, animData, getData());
            } else {
                //single - single
                this.tempType = position;
                single2Single(adapter, animData, getData());
            }
        }

        mPopupMenu.dismiss();
    }
  /*  @Override
    public void onHandleListUpdate(Result result, Object obj) {
        getLoading().loadSuccess();
        cancelRefresh();
        List<Community> tempList = (List<Community>) obj;
        if (!Tools.isEmpty(tempList)) {
            animData.addAll(0, tempList);
            Tools.log_i(CommunityFragment.class, "onHandleListUpdate", "tempList.size : " + tempList.size());
            if (!animData.contains(tempList)) {
                animData.addAll(0, tempList);
            }
            if (isFirst || this.tempType == length) {
                if (!getData().contains(tempList)) {
                    getData().addAll(0, tempList);
                    Tools.log_i(CommunityFragment.class, "onHandleListUpdate", "  getData().addAll(0, tempList)");
                    getAdapter().notifyItemRangeInserted(0, tempList.size());
                }
                isFirst = false;
            } else {
                int tempType = length;
                int index = 0;
                for (int i = 0; i < tempList.size(); i++) {
                    Community temp = tempList.get(i);
                    if (temp instanceof CommunityTopic) {
                        tempType = ((CommunityTopic) temp).getTopicType();
                    } else {
                        tempType = ((CommunityInfo) temp).getInfoType();
                    }
                    if (this.tempType == tempType) {
                        if (!getData().contains(temp)) {
                            getData().add(index, temp);
                            Tools.log_i(CommunityFragment.class, "onHandleListUpdate", "  getData().add(index, temp);");
                            getAdapter().notifyItemInserted(index);
                            index++;
                        }
                    }
                }
            }
        }
    }*/

/*    @Override
    public void onHandleListHistory(Result result, Object obj) {
        Tools.log_i(CommunityFragment.class, "onHandleListHistory", "");
        getLoading().loadSuccess();
        cancelRefresh();
        List<Community> tempList = (List<Community>) obj;
        if (!Tools.isEmpty(tempList)) {
            animData.addAll(tempList);

            if (this.tempType == length) {
                getData().addAll(tempList);
                getAdapter().notifyItemRangeInserted(getData().size() - 1, tempList.size());
            } else {
                int tempType = length;
                for (int i = 0; i < tempList.size(); i++) {
                    Community temp = tempList.get(i);
                    if (temp instanceof CommunityTopic) {
                        tempType = ((CommunityTopic) temp).getTopicType();
                    } else {
                        tempType = ((CommunityInfo) temp).getInfoType();
                    }
                    if (this.tempType == tempType) {
                        getData().add(getData().size(), temp);
                        getAdapter().notifyItemInserted(getData().size() - 1);
                    }
                }
            }
        }
    }*/

    public void all2Single(RecyclerView.Adapter adapter, List<Community> source) {
        Tools.log_i(CommunityFragment.class, "all2Single", "");
        ListIterator<Community> lis = source.listIterator();
        int tempType = length;
        if (lis != null) {
            while (lis.hasNext()) {
                Community next = lis.next();
                if (next instanceof CommunityTopic) {
                    tempType = ((CommunityTopic) next).getTopicType();
                } else {
                    tempType = ((CommunityInfo) next).getInfoType();
                }
                if (tempType != this.tempType) {
                    int index = source.indexOf(next);
                    lis.remove();
                    adapter.notifyItemRemoved(index);
                }
            }
        }
    }

    public void single2Single(RecyclerView.Adapter adapter, List<Community> source, List<Community> target) {
        Tools.log_i(CommunityFragment.class, "single2Single", "");
        target.clear();
        ListIterator<Community> lis = source.listIterator();
        int tempType = length;
        if (lis != null) {
            while (lis.hasNext()) {
                Community next = lis.next();
                if (next instanceof CommunityTopic) {
                    Tools.log_i(CommunityFragment.class, "single2Single", "CommunityTopic");
                    tempType = ((CommunityTopic) next).getTopicType();
                } else {
                    Tools.log_i(CommunityFragment.class, "single2Single", "CommunityInfo");
                    tempType = ((CommunityInfo) next).getInfoType();
                }
                Tools.log_i(CommunityFragment.class, "single2Single", "tempType ; " + tempType + ",this. : tempType");
                if (tempType == this.tempType) {
                    Tools.log_i(CommunityFragment.class, "single2Single", "add...");
                    target.add(next);
                }
            }
            adapter.notifyDataSetChanged();
        } else {
            Tools.log_i(CommunityFragment.class, "single2Single", "lis == null");
        }

    }

    public void single2All(RecyclerView.Adapter adapter, List<Community> source, List<Community> target) {
        Tools.log_i(CommunityFragment.class, "single2All", "");
        ListIterator<Community> lis = source.listIterator();
        if (lis != null) {
            int sourceIndex = -1;
            int targetIndex = -1;
            while (lis.hasNext()) {
                Community next = lis.next();
                sourceIndex = source.indexOf(next);
                targetIndex = target.indexOf(next);
                if (targetIndex == -1 && sourceIndex != -1) {
                    if (sourceIndex >= 0 && sourceIndex < target.size() - 1) {
                        target.add(sourceIndex, next);
                        adapter.notifyItemInserted(sourceIndex);
                    }
                }
            }
            sourceIndex = -1;
            targetIndex = -1;
        }
    }

    public void updateCommentCount(HtmlPaper htmlPaper) {
        if (htmlPaper != null && htmlPaper instanceof Community) {
            int index = getData().indexOf(htmlPaper);
            Tools.log_i(CommunityFragment.class, "updateCommentCount", "index : " + index);
            if (index != -1) {
                getData().set(index, (Community) htmlPaper);
                getAdapter().notifyItemChanged(index);
            }
        }
    }
}
