package com.zeustel.top9.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zeustel.top9.LoginActivity;
import com.zeustel.top9.R;
import com.zeustel.top9.SelfDetailActivity;
import com.zeustel.top9.adapters.HolderSelfMenu;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.base.IAnimFragment;
import com.zeustel.top9.base.WrapOneFragment;
import com.zeustel.top9.bean.MenuItem;
import com.zeustel.top9.bean.UserLevel;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.CircleImage;
import com.zeustel.top9.widgets.CustomRecyclerView;

import java.util.ArrayList;

/**
 * 我的页面
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/6/26
 */
public class SelfFragment extends WrapOneFragment implements CustomRecyclerView.OnItemClickListener, View.OnClickListener, IAnimFragment {


    private static final int REQUESTCODE = 1000;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    //主题
    private ImageView self_theme;
    //头像
    private CircleImage self_icon;
    //名字
    private TextView self_name;
    //top9币总数·
    private TextView self_integral;
    //个性签名
    private TextView self_signature;
    //个人等级
    private TextView self_level;
    //个人经验进度
    private ProgressBar self_experience_progress;
    //关注
    private TextView self_attention;
    //粉丝
    private TextView self_fans;
    //个人经验
    private TextView self_experience_text;
    //需隐藏布局
    private LinearLayout self_user_ab1;
    private LinearLayout self_user_ab2;
    //菜单列表
    private CustomRecyclerView self_recyler;
    private ArrayList<MenuItem> data = new ArrayList();
    private OverallRecyclerAdapter<MenuItem> adapter;
    private View contentView;
    private String signature;

    @Override
    public void onDestroyView() {
        if (self_recyler != null) {
            self_recyler.setAdapter(null);
        }
        super.onDestroyView();
        if (data != null) {
            data.clear();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        data = null;
    }

    public SelfFragment() {

    }

    @Override
    public void OnResumeCorrect() {
        super.OnResumeCorrect();
        updateUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_self, container, false);
        self_theme = (ImageView) contentView.findViewById(R.id.self_theme);
        self_icon = (CircleImage) contentView.findViewById(R.id.self_icon);
        self_name = (TextView) contentView.findViewById(R.id.self_name);
        self_integral = (TextView) contentView.findViewById(R.id.self_integral);
        self_recyler = (CustomRecyclerView) contentView.findViewById(R.id.self_recyler);

        self_signature = (TextView) contentView.findViewById(R.id.self_signature);
        self_level = (TextView) contentView.findViewById(R.id.self_level);
        self_experience_progress = (ProgressBar) contentView.findViewById(R.id.self_experience_progress);
        self_experience_text = (TextView) contentView.findViewById(R.id.self_experience_text);
        self_attention = (TextView) contentView.findViewById(R.id.self_attention);
        self_fans = (TextView) contentView.findViewById(R.id.self_fans);
        self_user_ab1 = (LinearLayout) contentView.findViewById(R.id.self_user_ab1);
        self_user_ab2 = (LinearLayout) contentView.findViewById(R.id.self_user_ab2);

        self_recyler.setItemAnimator(new DefaultItemAnimator());
        self_recyler.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        self_recyler.setLayoutManager(mLayoutManager);
        data.clear();
        /*添加菜单项*/
        data.add(new MenuItem(R.mipmap.self_settings, getString(R.string.self_settings)));
        //        data.add(new MenuItem(R.mipmap.self_note, getString(R.string.self_note)));
        //        data.add(new MenuItem(R.mipmap.self_exchange, getString(R.string.self_exchange)));
        //        data.add(new MenuItem(R.mipmap.self_support, getString(R.string.self_support)));
        data.add(new MenuItem(R.mipmap.self_note, getString(R.string.self_ranking)));
        data.add(new MenuItem(R.mipmap.self_exchange, getString(R.string.self_currency)));
        data.add(new MenuItem(R.mipmap.self_support, getString(R.string.self_msg)));
        data.add(new MenuItem(R.mipmap.self_search, getString(R.string.self_search)));//, new SearchFragment()

        try {
            adapter = new OverallRecyclerAdapter(data, R.layout.list_item_self, HolderSelfMenu.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (adapter != null) {
            self_recyler.setAdapter(adapter);
        }
        self_recyler.setOnItemClickListener(this);
        self_icon.setOnClickListener(this);
        return contentView;
    }

    private void updateUI() {
        if (contentView != null) {
            if (Constants.USER.isOnline()) {
                self_user_ab1.setVisibility(View.VISIBLE);
                self_user_ab2.setVisibility(View.VISIBLE);
                self_name.setText(Constants.USER.getNickName());
                self_integral.setText(getString(R.string.self_integral, Constants.USER.getIntegralAmount()));
                self_attention.setText(getString(R.string.self_attention, Constants.USER.getAttentionCount()));
                self_fans.setText(getString(R.string.self_fans, Constants.USER.getFansCount()));
                signature = Constants.USER.getSignature();
                self_signature.setText(signature == null ? "" : signature);
                UserLevel level = Constants.USER.getLevel();
                int max = 0;
                if (level != null) {
                    String levelName = level.getLevelName();
                    self_level.setText(Tools.isEmpty(levelName) ? "" : levelName);
                    max = level.getExperience();
                }
                self_experience_progress.setMax(max);
                self_experience_progress.setProgress(Constants.USER.getExperience());
                self_experience_text.setText(Constants.USER.getExperience() + "/" + max);
                self_integral.setText(getString(R.string.self_integral, Constants.USER.getIntegralAmount()));
                imageLoader.displayImage(Constants.TEST_IMG + Constants.USER.getIcon(), self_icon, Tools.options);
            } else {
                self_user_ab1.setVisibility(View.INVISIBLE);
                self_user_ab2.setVisibility(View.INVISIBLE);
                self_name.setText("");
                self_integral.setText("");
                self_attention.setText("");
                self_fans.setText("");
                self_signature.setText("");
                self_experience_progress.setProgress(0);
                self_experience_progress.setMax(0);
                imageLoader.displayImage(null, self_icon, Tools.options);
            }
        }
    }

    @Override
    public String getClassName() {
        return SelfFragment.class.getSimpleName();
    }

    @Override
    public String getFloatTitle() {
        return getString(R.string.title_self);
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public boolean onItemClick(RecyclerView parent, RecyclerView.ViewHolder viewHolder, View itemView, int viewType, int position) {
        if (position >= 0 && position < adapter.getItemCount()) {
            Intent intent = null;
            if (position < adapter.getItemCount() - 1/*最后一项*/) {
                if (Constants.USER.isOnline()) {
                    intent = new Intent(getContext(), SelfDetailActivity.class);
                    intent.putExtra(SelfDetailActivity.EXTRA_NAME_POSITION, position);
                } else {
                    goToLogin();
                }
            } else {
                getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.container, new SearchFragment()).commit();
            }
            if (intent != null) {
                startActivity(intent);
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (self_icon.equals(v)) {
            if (Constants.USER.isOnline()) {

            } else {
                goToLogin();
            }
        }/* else if (self_exit.equals(v)) {
            DataUser.toLoginOut();
            updateUI();
        }*/
    }

    private void goToLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateUI();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return self_recyler;
    }
}
