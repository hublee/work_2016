package com.zeustel.top9.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.SearchActivity;
import com.zeustel.top9.base.WrapNotAndHandleFragment;
import com.zeustel.top9.bean.Search;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.result.SearchResult;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.LoadProgress;
import com.zeustel.top9.utils.SharedPreferencesUtils;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataSearch;
import com.zeustel.top9.widgets.KeywordsFlow;

import java.util.Iterator;
import java.util.Set;

/**
 * 搜索
 */
public class SearchFragment extends WrapNotAndHandleFragment implements View.OnClickListener {
    //输入
    private TextInputLayout search_input;
    //返回按键
    private ImageButton search_back;
    //搜索
    private Button search_go;
    //记录显示
    private KeywordsFlow search_flow;

    private DataSearch dataOperate;

    private LoadProgress loadProgress;
    private String text;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_search, container, false);
        search_input = (TextInputLayout) contentView.findViewById(R.id.search_input);
        search_back = (ImageButton) contentView.findViewById(R.id.search_back);
        search_go = (Button) contentView.findViewById(R.id.search_go);
        search_flow = (KeywordsFlow) contentView.findViewById(R.id.search_flow);
        search_flow.setDuration(1000);
        /*获取本地搜索记录添加至显示组件*/
        Set<String> keys = SharedPreferencesUtils.getSearch(getActivity());
        Tools.log_i(SearchFragment.class, "onCreateView", "keys.size : " + keys.size());
        Iterator<String> iterator = keys.iterator();
        if (iterator != null) {
            while (iterator.hasNext()) {
                search_flow.feedKeyword(iterator.next());
            }
        }
        search_go.setOnClickListener(this);
        search_back.setOnClickListener(this);
        search_flow.setOnClickListener(this);
        /*监听软键盘搜索键*/
        Tools.listenSoftInput(search_input.getEditText(), EditorInfo.IME_ACTION_SEARCH, false, new Runnable() {
            @Override
            public void run() {
                search();
            }
        });
        dataOperate = new DataSearch(getHandler(), null);
        loadProgress = new LoadProgress(getActivity());
        Tools.log_i(SearchFragment.class, "onCreateView", "");
        return contentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*显示记录*/
        search_flow.go2Show(KeywordsFlow.ANIMATION_IN);
    }

    @Override
    public void onClick(View v) {
        if (search_back.equals(v)) {
            getFragmentManager().popBackStack();
        } else if (search_go.equals(v)) {
            search();
        } else {
            if (v instanceof TextView) {
                String text = ((TextView) v).getText().toString();
                Tools.log_i(SearchFragment.class, "onClick", "text : " + text);
                if (!Tools.isEmpty(text)) {
                    search_input.getEditText().setText(text);
                }
            }
        }
    }

    private void search() {
        //隐藏软键盘
        Tools.hideSoftInput(getActivity(), search_input.getEditText().getWindowToken());
        text = search_input.getEditText().getText().toString();
        if (Tools.isEmpty(text)) {
            Tools.showToast(getActivity(), R.string.search_empty);
        } else {
            search_input.getEditText().setText("");
            //添加并显示记录
            search_flow.feedKeyword(text);
            search_flow.go2Show(KeywordsFlow.ANIMATION_OUT);
            //本地保存该条记录
            SharedPreferencesUtils.saveSearch(getActivity(), text);
            loadProgress.show(R.string.search_ing);
            try {
                dataOperate.getSingleData(dataOperate.createSearchBundle(Constants.URL_SEARCH_LIST, text), SearchResult.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onHandleSingleSuccess(Result result, Object obj) {
        loadProgress.dismiss();
        if (obj != null) {
            Search search = (Search) obj;
            search.setText(text);
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra(SearchActivity.EXTRA_NAME, search);
            startActivity(intent);
        }
    }

    @Override
    public void onHandleSingleFailed(Result result) {
        super.onHandleSingleFailed(result);
        loadProgress.dismiss();
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
