package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.FaceLib;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.gif.GifData;

import java.util.ArrayList;
import java.util.List;

/**
 * 多功能输入
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/17 15:41
 */
public class MultifunctionInput extends FrameLayout implements View.OnClickListener {

    private View inputView;
    private LayoutParams inputParams;

    public MultifunctionInput(Context context) {
        super(context);
        initView();
    }

    public MultifunctionInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MultifunctionInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultifunctionInput(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public interface OnSoftKeyBoardListener {

        /**
         * 软键盘监听
         *
         * @param isShow 是否打开
         * @param height 高度
         */
        void onSoftKeyBoardStateChange(boolean isShow, int height);
    }

    private LayoutInflater inflater;
    private ImageButton input_switch;//语音 文本输入 切换
    private Button input_voice; //语音输入入口
    private LinearLayout input_edit_layout;//文本编辑布局
    private EditText input_edit;//文本编辑区域
    private ImageButton input_face;//表情控制
    private Button input_send;//发送
    private ImageButton input_add;//添加??
    private ImageButton input_settings;//设置??
    private TabLayout input_tab;//表情库切换
    private LinearLayout input_height_view;//输入一样高的view
    private LinearLayout input_switch_top;
    private ViewPager input_pager;//表情库界面
    private Indicator mIndicator;//指示器
    private List<FaceLib> faceLibs = new ArrayList();//表情库集合
    private List<OnSoftKeyBoardListener> onSoftKeyBoardListeners = new ArrayList();
    private int topHeight;
    private int keyBoardHeight;

    //根据行列 拆分 成多个
    private void initView() {
        inflater = LayoutInflater.from(getContext());
        inputView = inflater.inflate(R.layout.multifunction_input, null);
        input_switch = (ImageButton) inputView.findViewById(R.id.input_switch);
        input_voice = (Button) inputView.findViewById(R.id.input_voice);
        input_edit_layout = (LinearLayout) inputView.findViewById(R.id.input_edit_layout);
        input_edit = (EditText) inputView.findViewById(R.id.input_edit);
        input_send = (Button) inputView.findViewById(R.id.input_send);
        input_face = (ImageButton) inputView.findViewById(R.id.input_face);
        input_add = (ImageButton) inputView.findViewById(R.id.input_add);
        input_settings = (ImageButton) inputView.findViewById(R.id.input_settings);
        input_height_view = (LinearLayout) inputView.findViewById(R.id.input_height_view);
        input_switch_top = (LinearLayout) inputView.findViewById(R.id.input_switch_top);
        input_tab = (TabLayout) inputView.findViewById(R.id.input_tab);
        input_pager = (ViewPager) inputView.findViewById(R.id.input_pager);
        input_switch.setOnClickListener(this);
        input_send.setOnClickListener(this);
        input_face.setSelected(false);
        input_face.setOnClickListener(this);
        input_add.setOnClickListener(this);
        input_settings.setOnClickListener(this);
        input_tab.setTabMode(TabLayout.MODE_SCROLLABLE);
        input_tab.setTabTextColors(getResources().getColor(R.color.def_font), getResources().getColor(R.color.red));
        setData();
        input_tab.setTabsFromPagerAdapter(new FacePagerAdapter());
        input_pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(input_tab));
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                // 取得这个布局动态的显示区域
                getWindowVisibleDisplayFrame(r);
                // 取得这个布局所在根布局的高度,补充些知识点,我们所有的布局都是放在一层根布局里(好像是framelayout),
                // 我们平常的输入法之类的是放在这个根布局里的,所以我们的思路是取得根布局的高度,再减去动态变化的这个RelativeLayout的高度,
                // 得到的就是我们输入法的布局。拓展出去，获取状态栏的高度同理
                int screenHeight = getRootView().getHeight();
                int heightDifference = screenHeight - (r.bottom - r.top);
                if (heightDifference < 100/*小于100px 看做隐藏*/) {
                    if (keyBoardHeight != 0) {
                        keyBoardHeight = 0;
                        if (onSoftKeyBoardListeners != null && !onSoftKeyBoardListeners.isEmpty()) {
                            for (int i = 0; i < onSoftKeyBoardListeners.size(); i++) {
                                OnSoftKeyBoardListener listener = onSoftKeyBoardListeners.get(i);
                                listener.onSoftKeyBoardStateChange(false, keyBoardHeight);
                            }
                        }
                    }
                } else {
                    if (keyBoardHeight != heightDifference) {
                        keyBoardHeight = heightDifference;
                        if (onSoftKeyBoardListeners != null && !onSoftKeyBoardListeners.isEmpty()) {
                            for (int i = 0; i < onSoftKeyBoardListeners.size(); i++) {
                                OnSoftKeyBoardListener listener = onSoftKeyBoardListeners.get(i);
                                listener.onSoftKeyBoardStateChange(true, keyBoardHeight);
                            }
                        }
                    }
                }
            }
        });
        input_switch_top.measure(0, 0);
        topHeight = input_switch_top.getMeasuredHeight();
        inputParams = new LayoutParams(LayoutParams.MATCH_PARENT, topHeight);
        addView(inputView, inputParams);
        addOnSoftKeyBoardListener(new OnSoftKeyBoardListener() {

            @Override
            public void onSoftKeyBoardStateChange(boolean isKeyBoardShow, int height) {
                MultifunctionInput.this.isKeyBoardShow = isKeyBoardShow;
                updateUI(height < topHeight ? topHeight : height);
                Tools.log_i(MultifunctionInput.class, "OnSoftKeyBoardListener", "height : " + height);
            }
        });
    }

    private boolean isKeyBoardShow;

    public void addOnSoftKeyBoardListener(OnSoftKeyBoardListener onSoftKeyBoardListener) {
        if (onSoftKeyBoardListener == null) {
            return;
        }
        this.onSoftKeyBoardListeners.add(onSoftKeyBoardListener);
    }

    public void setData() {
        //区分
        Tools.log_i(MultifunctionInput.class,"setData","size : "+GifData.gifFacePaths.size());
        faceLibs.add(new FaceLib("cover", GifData.gifFacePaths));
    }

    private class FacePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return faceLibs == null ? 0 : faceLibs.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {//tab 所需
            if (faceLibs == null) {
                return null;
            }
            FaceLib faceLib = faceLibs.get(position);
            return faceLib == null ? null : faceLib.getCover();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(container.getChildAt(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ExhibitionFaceView mExhibitionFaceView = new ExhibitionFaceView(getContext());
            mExhibitionFaceView.fillContent(faceLibs.get(position).getPaths());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(mExhibitionFaceView, params);
            return mExhibitionFaceView;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.input_switch:
                if (input_voice.getVisibility() == View.VISIBLE) {
                    input_voice.setVisibility(View.GONE);
                    input_edit_layout.setVisibility(View.VISIBLE);
                    input_send.setEnabled(true);
                } else {
                    input_edit_layout.setVisibility(View.GONE);
                    input_voice.setVisibility(View.VISIBLE);
                    input_send.setEnabled(false);
                }
                break;
            case R.id.input_send:
                break;
            case R.id.input_face:
                if (input_face.isSelected()) {
                    input_height_view.setVisibility(View.INVISIBLE);
                    input_face.setSelected(false);
                } else {
                    Tools.hideSoftInput(getContext(), input_edit.getWindowToken());
                    input_height_view.setVisibility(View.VISIBLE);
                    input_face.setSelected(true);
                }
                break;
            case R.id.input_add:
                break;
            case R.id.input_settings:
                break;
        }
    }

    private void updateUI(int height) {
        inputParams.height = height;
        inputView.setLayoutParams(inputParams);
    }
}
