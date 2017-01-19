package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.AdapterFloatMenu;
import com.zeustel.top9.utils.Tools;

/**
 * 主页菜单
 *
 * @author NiuLei
 * @date 2015/8/23 13:24
 */
public class ExpandMenu extends RelativeLayout implements View.OnClickListener {

    public ExpandMenu(Context context) {
        super(context);
        initView(context);
    }

    public ExpandMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ExpandMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExpandMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private Context context;
    private LayoutInflater inflater;
    private CustomListView expand_menu_content;
    private RelativeLayout expand_menu_content_layout;
    private RelativeLayout expand_menu_control_layout;
    private ImageView expand_menu_control;
    private Resources res;
    private int menu_item_width;
    private TranslateAnimation openAnim, closeAnim;
    private boolean isShowing;
    private boolean isAniming;
    private OnMenuStatusListener onMenuStatusListener;
    private interface OnMenuStatusListener {
        void onOpened();

        void onColsed();
    }

    public void setOnMenuStatusListener(OnMenuStatusListener onMenuStatusListener) {
        this.onMenuStatusListener = onMenuStatusListener;
    }

    @Override
    public void onClick(View v) {
        if (expand_menu_control.equals(v)) {
            Tools.log_i(ExpandMenu.class, "onClick", "");
            if (isShowing()) {
                hide();
            } else {
                show();
            }
        }
    }


    private class AnimListener implements Animation.AnimationListener {
        private boolean isOpen;

        public AnimListener(boolean isOpen) {
            this.isOpen = isOpen;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            isAniming = true;
            if (isOpen) {
                expand_menu_control.setSelected(true);
                expand_menu_control_layout.setBackgroundResource(R.color.float_menu_bg);
            } else {

            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isAniming = false;
            if (isOpen) {
                isShowing = true;
                update(true);
                if (onMenuStatusListener != null) {
                    onMenuStatusListener.onOpened();
                }
            } else {
                isShowing = false;
                update(false);
                expand_menu_control.setSelected(false);
                expand_menu_control_layout.setBackgroundColor(Color.TRANSPARENT);
                if (onMenuStatusListener != null) {
                    onMenuStatusListener.onColsed();
                }

            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private void initView(Context context) {
        this.context = context;
        res = context.getResources();
        inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.expand_menu_layout, null);
        expand_menu_content = (CustomListView) contentView.findViewById(R.id.expand_menu_content);
        expand_menu_content_layout = (RelativeLayout) contentView.findViewById(R.id.expand_menu_content_layout);
        expand_menu_control_layout = (RelativeLayout) contentView.findViewById(R.id.expand_menu_control_layout);
        expand_menu_control = (ImageView) contentView.findViewById(R.id.expand_menu_control);
        expand_menu_control.setSelected(false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(contentView, params);
        expand_menu_control.setOnClickListener(this);
    }

    public CustomListView getExpandContent() {
        return expand_menu_content;
    }

    public void setContentAdapter(AdapterFloatMenu adapter) {
        if (adapter != null) {
            expand_menu_content.setAdapter(adapter);
            prepare();
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        expand_menu_content.setOnItemClickListener(onItemClickListener);
    }

    int controlHeight;
    int contentHeight;

    private void prepare() {
        expand_menu_content.measure(0, 0);
        expand_menu_control.measure(0, 0);
        contentHeight = expand_menu_content.getMeasuredHeight() + expand_menu_content.getChildCount() * expand_menu_content.getDividerHeight();
        controlHeight = expand_menu_control.getMeasuredHeight();
        update(false);
        openAnim = new TranslateAnimation(0, 0, 0, -contentHeight);
        openAnim.setDuration(300);
        closeAnim = new TranslateAnimation(0, 0, 0, contentHeight);
        closeAnim.setDuration(300);
        openAnim.setAnimationListener(new AnimListener(true));
        closeAnim.setAnimationListener(new AnimListener(false));
    }

    private void update(boolean isOpen) {
        updateMenu((isOpen ? contentHeight : 0) + controlHeight);
        updateContent(isOpen ? 0 : contentHeight);
    }

    private void updateContent(int height) {
        RelativeLayout.LayoutParams content_params = (LayoutParams) expand_menu_content.getLayoutParams();
        content_params.topMargin = height;
        expand_menu_content.setLayoutParams(content_params);
    }

    private void updateMenu(int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = height;
        setLayoutParams(params);
    }

    public void show() {
        if (!isShowing && !isAniming) {
            updateMenu(contentHeight + controlHeight);
            expand_menu_content.clearAnimation();
            expand_menu_content.startAnimation(openAnim);
        }
    }

    public void hide() {
        if (isShowing && !isAniming) {
            expand_menu_content.clearAnimation();
            expand_menu_content.startAnimation(closeAnim);
        }
    }

    public boolean isShowing() {
        return isShowing;
    }
}
