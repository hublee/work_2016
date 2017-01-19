package com.zeustel.top9;

import android.content.Intent;
import android.os.Bundle;

import com.zeustel.top9.adapters.AdapterFragmentPager;
import com.zeustel.top9.base.WrapNotViewPagerActivity;
import com.zeustel.top9.bean.Community;
import com.zeustel.top9.bean.Game;
import com.zeustel.top9.bean.HtmlComment;
import com.zeustel.top9.bean.HtmlPaper;
import com.zeustel.top9.fragments.CommunityFragment;
import com.zeustel.top9.fragments.GameDescribeFragment;
import com.zeustel.top9.fragments.HtmlDetailFragment;
import com.zeustel.top9.utils.Tools;

/*游戏详细*/
public class GameDetailActivity extends WrapNotViewPagerActivity implements HtmlDetailFragment.OnFragmentInteractionListener {
    public static final String EXTRA_NAME = "game";
    private Game game;
    private AdapterFragmentPager adapter;
    private CommunityFragment infoFragment;
    private CommunityFragment topicFragment;


    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            game = (Game) intent.getSerializableExtra(EXTRA_NAME);
        }
        if (game == null) {
            finish();
        }
        int gameId = game.getId();
        getIndicator().addView(getIndicator().newChildBtn(0/*position*/, R.color.black, R.color.yellow));
        topicFragment = CommunityFragment.newInstance(gameId, Community.CommunityType.communityTopic);
        getData().add(topicFragment);
        getIndicator().addView(getIndicator().newChildBtn(1/*position*/, R.color.black, R.color.red));
        getData().add(GameDescribeFragment.newInstance(game));
        getIndicator().addView(getIndicator().newChildBtn(2/*position*/, R.color.black, R.color.blue));
        infoFragment = CommunityFragment.newInstance(gameId, Community.CommunityType.communityInfo);
        getData().add(infoFragment);
        adapter = new AdapterFragmentPager(getSupportFragmentManager(), getData());
        getViewPager().setAdapter(adapter);
        //预加载所有页面
        //        getViewPager().setOffscreenPageLimit(getData().size());
        setCurrentItem(1/*position*/);
    }

    @Override
    public void updateCommentCount(HtmlPaper htmlPaper, int commentType) {
        Tools.log_i(GameDetailActivity.class, "updateCommentCount", "");
        if (HtmlComment.CommentType.TYPE_INFO == commentType) {
            infoFragment.updateCommentCount(htmlPaper);
        } else if (HtmlComment.CommentType.TYPE_TOPIC == commentType) {
            topicFragment.updateCommentCount(htmlPaper);
        }
    }
}
