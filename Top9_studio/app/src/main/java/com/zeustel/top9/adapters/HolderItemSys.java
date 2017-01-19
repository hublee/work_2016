package com.zeustel.top9.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.zeustel.top9.R;
import com.zeustel.top9.bean.html.SystemTemplateItem;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/6 10:06
 */
@Deprecated
public class HolderItemSys extends OverallRecyclerHolder<SystemTemplateItem> {
    private TextView sys_item_small_name;
    private TextView sys_item_small_title;
    private TextView sys_item_small_des;

    public HolderItemSys(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        sys_item_small_name = (TextView) itemView.findViewById(R.id.sys_item_small_name);
        sys_item_small_title = (TextView) itemView.findViewById(R.id.sys_item_small_title);
        sys_item_small_des = (TextView) itemView.findViewById(R.id.sys_item_small_des);
    }

    @Override
    public void set(OverallRecyclerAdapter<SystemTemplateItem> adapter, int position) {
        SystemTemplateItem item = adapter.getItem(position);
        if (item != null) {
            String name = item.getName();
            String icon = item.getIcon();
//            String themeColor = item.getThemeColor();
            String title = item.getTitle();
            String des = item.getDes();
            getImageLoader().loadImage(Constants.TEST_IMG + icon, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    sys_item_small_name.setCompoundDrawablesWithIntrinsicBounds(null, new BitmapDrawable(getContext().getResources(), loadedImage), null, null);
                }
            });
            sys_item_small_name.setText(Tools.isEmpty(name) ? "" : name);
            sys_item_small_title.setText(Tools.isEmpty(title) ? "" : title);
            try {
//                sys_item_small_title.setTextColor(Color.parseColor(themeColor));
            } catch (Exception e) {
                e.printStackTrace();
                sys_item_small_title.setTextColor(Color.RED);
            }
            sys_item_small_des.setText(des);
        }
    }

    @Override
    protected boolean isItemClickEnabled() {
        return false;
    }
}
