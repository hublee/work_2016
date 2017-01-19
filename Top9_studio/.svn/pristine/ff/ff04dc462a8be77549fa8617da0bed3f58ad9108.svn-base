package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.base.IAnimViewHolder;
import com.zeustel.top9.bean.MenuItem;
import com.zeustel.top9.utils.AnimUtils;

/**
 * 设置 我的 菜单列表
 *
 * @author NiuLei
 * @date 2015/8/9 19:47
 */
public class HolderSelfMenu extends OverallRecyclerHolder<MenuItem> implements IAnimViewHolder {
    private ImageView item_self_icon;
    private TextView item_self_title;
    private MenuItem mMenuItem;
    private int img;
    private String text;
    private AnimUtils animUtils;
    private View itemView;

    public HolderSelfMenu(Context context, View itemView) {
        super(context, itemView);
        animUtils = AnimUtils.getInstance(context);
    }

    @Override
    protected void initItemView(View itemView) {
        this.itemView = itemView;
        item_self_icon = (ImageView) itemView.findViewById(R.id.item_self_icon);
        item_self_title = (TextView) itemView.findViewById(R.id.item_self_title);
    }

    @Override
    public void set(OverallRecyclerAdapter<MenuItem> adapter, int position) {
        mMenuItem = adapter.getItem(position);
        img = mMenuItem.getImg();
        text = mMenuItem.getText();
        item_self_icon.setImageResource(img);
        item_self_title.setText(text);
    }

    @Override
    public void startAnim(int startOffset) {
        animUtils.setIn(itemView, AnimUtils.TranslateDirection.LEFT, false, startOffset);
    }

    @Override
    public void startUpAnim(int startOffset) {

    }

    @Override
    protected boolean isItemClickEnabled() {
        return false;
    }
}
