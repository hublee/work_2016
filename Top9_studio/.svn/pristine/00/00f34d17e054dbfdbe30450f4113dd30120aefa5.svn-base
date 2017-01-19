package com.zeustel.top9.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;

/**
 * 悬浮菜单
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/22 09:41
 */
@Deprecated
public class FloatMenu extends PopupWindow {
    private Context context;
    private LayoutInflater inflater;
    private ListView float_menu_list;
    private View floatView;
    private Resources res;
    private Animation translate;

    public FloatMenu(Context context) {
        initContent(context);
    }
    private void initContent(Context context) {
        this.context = context;
        this.res = context.getResources();
        setAnimationStyle(R.style.FloatMenu);
        inflater = LayoutInflater.from(this.context);
        floatView = inflater.inflate(R.layout.float_menu_list, null);
        float_menu_list = (ListView) floatView.findViewById(R.id.float_menu_list);
        //设置选择颜色
        float_menu_list.setSelector(res.getDrawable(R.color.red));
        //设置背景
        setBackgroundDrawable(res.getDrawable(R.color.black));
        int float_btn_width = Tools.getDimension(res, R.dimen.float_menu_width);
        setContentView(floatView);
        if (getWidth() <= 0) {
            setWidth(float_btn_width);
        }
    }

    /**
     * 为Listview添加适配器 并计算其默认高度
     *
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        if (adapter == null) {
            throw new NullPointerException();
        }
        if (float_menu_list != null) {
            float_menu_list.setAdapter(adapter);
            if (getHeight() <= 0) {
                int count = float_menu_list.getAdapter().getCount();
                float_menu_list.measure(0,0);
                setHeight(count * float_menu_list.getMeasuredHeight());
            }
        }
    }

    /**
     * 对外提供Listview 以便监听
     *
     * @return
     */
    public ListView getContent() {
        return float_menu_list;
    }

    /**
     * 打开菜单时 使其能获取焦点
     *
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {
        if (float_menu_list.getAdapter() == null)
            throw new NullPointerException("need ListAdapter");
        super.showAsDropDown(anchor,Tools.dip2px(context,10),-20);
        setFocusable(true);
        update();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setFocusable(false);
    }
}
