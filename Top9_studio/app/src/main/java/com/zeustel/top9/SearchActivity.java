package com.zeustel.top9;

import android.os.Bundle;

import com.zeustel.top9.adapters.AdapterFragmentPager;
import com.zeustel.top9.base.WrapNotViewPagerActivity;
import com.zeustel.top9.bean.CommunityInfo;
import com.zeustel.top9.bean.CommunityTopic;
import com.zeustel.top9.bean.GameEvaluating;
import com.zeustel.top9.bean.Search;
import com.zeustel.top9.fragments.SearchPagerFragment;

import java.util.ArrayList;

/**
 * 搜索
 */
public class SearchActivity extends WrapNotViewPagerActivity {
    public static final String EXTRA_NAME = "Search";
    private Search mSearch = null;
    private AdapterFragmentPager adapter;
    private ArrayList<GameEvaluating> gameEvaluatings;
    private ArrayList<CommunityInfo> communityInfos;
    private ArrayList<CommunityTopic> communityTopics;
    private String lightText;

    @Override
    protected void onDestroy() {
        if (gameEvaluatings != null) {
            gameEvaluatings.clear();
            gameEvaluatings = null;
        }
        if (communityInfos != null) {
            communityInfos.clear();
            communityInfos = null;
        }
        if (communityTopics != null) {
            communityTopics.clear();
            communityTopics = null;
        }
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mSearch = (Search) getIntent().getSerializableExtra(EXTRA_NAME);
        }
        if (mSearch == null) {
            finish();
        }
        gameEvaluatings = mSearch.getEvaluateList();
        communityInfos = mSearch.getNewsList();
        communityTopics = mSearch.getTopicList();
        lightText = mSearch.getText();

        getIndicator().addView(getIndicator().newChildBtn(0/*position*/, R.color.black, R.color.yellow));
        getData().add(SearchPagerFragment.newInstance(gameEvaluatings, Search.Type.EVALUATING, lightText));
        getIndicator().addView(getIndicator().newChildBtn(1/*position*/, R.color.black, R.color.red));
        getData().add(SearchPagerFragment.newInstance(communityInfos, Search.Type.INFO, lightText));
        getIndicator().addView(getIndicator().newChildBtn(2/*position*/, R.color.black, R.color.blue));
        getData().add(SearchPagerFragment.newInstance(communityTopics, Search.Type.TOPIC, lightText));
        adapter = new AdapterFragmentPager(getSupportFragmentManager(), getData());
        getViewPager().setAdapter(adapter);
        setCurrentItem(0/*position*/);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }
}
