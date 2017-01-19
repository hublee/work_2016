package com.zeustel.top9;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.TextView;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.zeustel.top9.adapters.AdapterJazzy;
import com.zeustel.top9.base.IAnimFragment;
import com.zeustel.top9.base.IAnimViewHolder;
import com.zeustel.top9.bean.GameEvaluating;
import com.zeustel.top9.fragments.EvaluatingFragment;
import com.zeustel.top9.fragments.FMFragment;
import com.zeustel.top9.fragments.SelfFragment;
import com.zeustel.top9.fragments.StoreFragment;
import com.zeustel.top9.temp.TempFMPropertyFragment;
import com.zeustel.top9.utils.AnimUtils;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataUser;
import com.zeustel.top9.widgets.ForceMenu;
import com.zeustel.top9.widgets.Indicator;

import java.util.ArrayList;

/**
 * 主页面
 */
public class ContainerActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, AdapterView.OnItemClickListener {

    //悬浮标题
    public TextView container_title;
    //主题内容
    private JazzyViewPager container_viewPager;
    //    private CustomViewPager container_viewPager;
    private AdapterJazzy adapter;
    //    private AdapterFragmentPager adapter;
    //悬浮菜单
    private ForceMenu container_menu;
    //    private AdapterFloatMenu menuAdapter;
    //指示器
    private Indicator container_indicator;
    //Fragment容器
    private ArrayList<Fragment> data;
    //页面总数与菜单项总数差
    private int diff;
    private Animation titleScale;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            container_title.setVisibility(View.VISIBLE);
            container_title.startAnimation(titleScale);
        }
    };
    private RecyclerScrollListener mRecyclerScrollListener = new RecyclerScrollListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        titleScale = AnimUtils.getInstance(getApplicationContext()).getScaleIn();
        titleScale.setStartOffset(500);
        try {
            DataUser.toLogin(getApplicationContext(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initContentView();
    }

    private void initContentView() {
        container_viewPager = (JazzyViewPager) findViewById(R.id.container_viewPager);
        container_viewPager.setTransitionEffect(JazzyViewPager.TransitionEffect.CubeOut);
        container_viewPager.setCubeDegrees(45f);
        container_title = (TextView) findViewById(R.id.container_title);
        container_menu = (ForceMenu) findViewById(R.id.container_menu);
        container_indicator = (Indicator) findViewById(R.id.container_indicator);
        /*填充内容*/
        data = new ArrayList<>();
        data.add(EvaluatingFragment.newInstance(GameEvaluating.EvaluatingType.OFFLINE_GAME, R.color.red));
        data.add(EvaluatingFragment.newInstance(GameEvaluating.EvaluatingType.ONLINE_GAME, R.color.yellow));
        data.add(EvaluatingFragment.newInstance(GameEvaluating.EvaluatingType.HAND_GAME, R.color.blue));
        data.add(new TempFMPropertyFragment());
//        data.add(new FMContainerFragment());
        //        if (Constants.GAME_ENABLE) {
        //            if (Constants.SHOW_HOW) {
        //                data.add(new Grid9Fragment());
        //            } else {
        //                data.add(new NewTop9Fragment());
        //            }
        //        }
        data.add(new StoreFragment());
        data.add(new SelfFragment());
        adapter = new AdapterJazzy(container_viewPager, getSupportFragmentManager(), data);
        container_viewPager.setAdapter(adapter);
        //添加页面切换监听
        container_viewPager.addOnPageChangeListener(this);
        /*初始化指示器*/
        container_indicator.setGravity(Gravity.CENTER);
        container_indicator.setBackgroundDrawable(null);
        container_indicator.setItem(Tools.getDimension(getResources(), R.dimen.viewpager_indicator_item_width), Tools.getDimension(getResources(), R.dimen.viewpager_indicator_height), Tools.getDimension(getResources(), R.dimen.viewpager_indicator_item_margin));
        container_indicator.addView(container_indicator.newChildBtn(0/*index*/, R.color.black, R.color.red));
        container_indicator.addView(container_indicator.newChildBtn(1/*index*/, R.color.black, R.color.yellow));
        container_indicator.addView(container_indicator.newChildBtn(2/*index*/, R.color.black, R.color.blue));

        container_menu.addItemView(R.mipmap.force_menu_home);
        container_menu.addItemView(R.mipmap.force_menu_fm);
        container_menu.addItemView(R.mipmap.force_menu_store);
        container_menu.addItemView(R.mipmap.force_menu_self);
        container_menu.setControl(R.mipmap.force_menu_control);
        container_menu.setDegree(45F);
        container_menu.setAutoClose(true);
        container_menu.setOnItemClickListener(new ForceMenu.OnItemClickListener() {
            @Override
            public void onItemClick(ForceMenu forceMenu, int position) {
                container_menu.select(position);
                container_viewPager.setCurrentItem(position == 0 ? 0 : position + diff);
            }
        });
        diff = data.size() - container_menu.size();
        Tools.endCreateOperate(container_indicator, new Runnable() {
            @Override
            public void run() {
                container_viewPager.setCurrentItem(0/*index*/);
                container_indicator.check(0/*index*/);//默认选择第一个
                onPageSelected(0);
                onPageScrolled(0, 0.51f, 0);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        /*点击外部取消*/
        //        if (Tools.isOutsideOf(container_menu, event)) {
        //            container_menu.hide();
        //        }
        container_menu.isOutSide(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        if (data != null) {
            data.clear();
            data = null;
        }
        if (container_indicator != null) {
            container_indicator.removeAllViews();
            container_indicator = null;
        }
        if (container_menu != null) {
            container_menu.onDestroy();
            container_menu = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        DataUser.toLoginOut();
        super.onDestroy();

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0.5) {
            if (View.VISIBLE == container_title.getVisibility()) {
                container_title.setVisibility(View.INVISIBLE);
            }

            if (View.VISIBLE != container_title.getVisibility()) {
                handler.removeMessages(1);
                handler.sendEmptyMessageDelayed(1, 150);
            }
        }
    }

    //页面切换到
    @Override
    public void onPageSelected(int position) {
        startPositionAnim(position);
        Tools.log_i(ContainerActivity.class, "onPageSelected", "position : " + position);
            /*滑动后后同时切换菜单 、指示器*/
        position = position % data.size();
        if (position >= 0 && position < container_indicator.getChildCount()) {
            if (View.VISIBLE != container_indicator.getVisibility()) {
                container_indicator.setVisibility(View.VISIBLE);
            }
            container_indicator.check(position);
            container_menu.select(0/*position*/);
        } else {
            container_indicator.setVisibility(View.INVISIBLE);
            container_menu.select(position - diff);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        container_viewPager.setCurrentItem(position);
    }

    private void startPositionAnim(int position) {
        Fragment fragment = data.get(position);
        if (fragment instanceof IAnimFragment) {
            RecyclerView recyclerView = ((IAnimFragment) fragment).getRecyclerView();
            if (recyclerView == null) {

                if (fragment instanceof FMFragment) {
                    ((FMFragment) fragment).onAnimIn();
                }
                return;
            }
            recyclerView.removeOnScrollListener(mRecyclerScrollListener);
            recyclerView.addOnScrollListener(mRecyclerScrollListener);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int first = layoutManager.findFirstVisibleItemPosition();
            int last = layoutManager.findLastVisibleItemPosition();
            for (int i = first; i < last + 1; i++) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForLayoutPosition(i);
                //                View item = layoutManager.findViewByPosition(i);
                if (viewHolder instanceof IAnimViewHolder) {
                    ((IAnimViewHolder) viewHolder).startAnim((i - first) * AnimUtils.DEF_OFFSET);
                }
            }
        }
    }

    private static final class RecyclerScrollListener extends OnScrollListener {
        private int last = -1;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView != null) {
                final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                final int last = layoutManager.findLastVisibleItemPosition();
                if (-1 != this.last) {
                    if (this.last < last) {
                        final RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForLayoutPosition(last);
                        if (viewHolder instanceof IAnimViewHolder) {
                            ((IAnimViewHolder) viewHolder).startUpAnim(0);
                        }
                    }
                }
                this.last = last;
            }

        }
    }

}
