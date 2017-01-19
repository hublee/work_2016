package com.zeustel.top9;

import android.content.Intent;
import android.os.Bundle;

import com.zeustel.top9.adapters.AdapterFragmentPager;
import com.zeustel.top9.base.WrapNotViewPagerActivity;
import com.zeustel.top9.fragments.SettingsFragment;
import com.zeustel.top9.fragments.SupportFragment;
import com.zeustel.top9.fragments.self.FractionFragment;
import com.zeustel.top9.fragments.self.MsgFragment;
import com.zeustel.top9.fragments.self.RankingContainerFragment;

public class SelfDetailActivity extends WrapNotViewPagerActivity {
    public static final String EXTRA_NAME_POSITION = "position";
    private AdapterFragmentPager adapter;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            position = intent.getIntExtra(EXTRA_NAME_POSITION, position);
        }
        if (position < 0/*position*/ || position >= 4/*position*/) {
            finish();
        }
        getIndicator().addView(getIndicator().newChildBtn(0/*position*/, R.color.black, R.color.yellow));
        getData().add(new SettingsFragment());
        getIndicator().addView(getIndicator().newChildBtn(1/*position*/, R.color.black, R.color.red));
        getData().add(new RankingContainerFragment()/*排行榜*/);

//        getData().add(AmountFragment.newInstance(AmountFragment.NOTE_TYPE_CURRENCY/*货币记录*/));
        getIndicator().addView(getIndicator().newChildBtn(2/*position*/, R.color.black, R.color.blue));
        getData().add(new FractionFragment()/*积分*/);
//        getData().add(AmountFragment.newInstance(AmountFragment.NOTE_TYPE_CONVERSION/*兑换记录*/));
        getIndicator().addView(getIndicator().newChildBtn(3/*position*/, R.color.black, R.color.yellow));
        getData().add(new MsgFragment());
        getIndicator().addView(getIndicator().newChildBtn(3/*position*/, R.color.black, R.color.red));
        getData().add(new SupportFragment());
        adapter = new AdapterFragmentPager(getSupportFragmentManager(), getData());
        getViewPager().setAdapter(adapter);
        //预加载所有页面
        setCurrentItem(position);
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
