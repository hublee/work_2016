package com.zeustel.top9.fragments.self;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.text.Spanned;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.AdapterWitchTitle;
import com.zeustel.top9.fragments.AmountChoiceFragment;
import com.zeustel.top9.fragments.ChoiceContainerFragment;
import com.zeustel.top9.fragments.ChoiceFragment;
import com.zeustel.top9.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的积分
 */
public class FractionFragment extends ChoiceContainerFragment {


    public FractionFragment() {
    }
    private ChoiceFragment currencyFragment = AmountChoiceFragment.newInstance(AmountChoiceFragment.NOTE_TYPE_CURRENCY);
    private ChoiceFragment shopFragment = AmountChoiceFragment.newInstance(AmountChoiceFragment.NOTE_TYPE_CONVERSION);
    private List<Fragment> data = new ArrayList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data.clear();
        data.add(currencyFragment);
        data.add(shopFragment);
    }

    @Override
    protected CharSequence getTitle() {
        final Spanned spanned = Html.fromHtml(getContext().getString(R.string.html_font, "#484D57"/*color*/, "Top9积分") +
                getContext().getString(R.string.space) +
                getContext().getString(R.string.html_font, "#FD4C3C"/*color*/, Constants.USER.getIntegralAmount()));
        return spanned;
    }

    @Override
    protected PagerAdapter getAdapterWithPageTitles() {
        return new AdapterWitchTitle(getFragmentManager(), data, new String[]{"积分记录", "商城记录"});
    }
}
