package com.zeustel.top9.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/13 14:27
 */
public class PopupMenu extends PopupWindow {

    private Context context;
    private LayoutInflater inflater;
    private ListView listView;
    private View contentView;
    private Resources res;

    public PopupMenu(Context context) {
        initContent(context);
    }

    private void initContent(Context context) {
        this.context = context;
        this.res = context.getResources();
        inflater = LayoutInflater.from(this.context);
        contentView = inflater.inflate(R.layout.popup_menu, null);
        listView = (ListView) contentView.findViewById(R.id.listView);
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        int popup_btn_width = Tools.getDimension(res, R.dimen.popup_menu_width);
        setContentView(contentView);
        if (getWidth() <= 0) {
            setWidth(popup_btn_width);
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
        if (listView != null) {
            listView.setAdapter(adapter);
            if (getHeight() <= 0) {
                int totalHeight = 0;
                for (int i = 0; i < listView.getAdapter().getCount(); i++) {
                    View listItem = listView.getAdapter().getView(i, null, listView);
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
                totalHeight = totalHeight + (listView.getDividerHeight() * (listView.getAdapter().getCount() - 1));
                totalHeight = totalHeight + (Tools.dip2px(context, 10) * 2);/*距上距下*/
                setHeight(totalHeight);
            }
        }
    }

    /**
     * 对外提供Listview 以便监听
     *
     * @return
     */
    public ListView getContent() {
        return listView;
    }

    /**
     * 打开菜单时 使其能获取焦点
     *
     * @param anchor
     */
    @Override
    public void showAsDropDown(View anchor) {
        if (listView.getAdapter() == null)
            throw new NullPointerException("need ListAdapter");
        super.showAsDropDown(anchor, -anchor.getWidth() / 2, Tools.dip2px(context, 5));
        setFocusable(true);
        update();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setFocusable(false);
    }
}
