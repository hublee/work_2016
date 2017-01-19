package com.zeustel.top9.fragments.self;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;

import com.zeustel.top9.adapters.AdapterWitchTitle;
import com.zeustel.top9.fragments.ChoiceContainerFragment;
import com.zeustel.top9.fragments.RankingFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 排行榜
 */
public class RankingContainerFragment extends ChoiceContainerFragment {


    public RankingContainerFragment() {
    }

    private Fragment rankingGood = RankingFragment.newInstance(RankingFragment.RANKING_GOOD);
    private Fragment rankingReply = RankingFragment.newInstance(RankingFragment.RANKING_REPLY);
    private List<Fragment> data = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data.clear();
        data.add(rankingGood);
        data.add(rankingReply);
    }

    @Override
    protected CharSequence getTitle() {
        return "排行榜";
    }


    @Override
    protected PagerAdapter getAdapterWithPageTitles() {
        return new AdapterWitchTitle(getFragmentManager(), data, new String[]{"点赞排行榜", "回复排行榜"});
    }
}
