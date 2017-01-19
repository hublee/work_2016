package com.zeustel.cp.intf;

import android.view.View;
import android.view.WindowManager;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/16 09:53
 */
public interface IFloat {
    /**
     * 添加view到WindowManager
     */
    void addView(View view, WindowManager.LayoutParams wmParams);

    /**
     * 添加view到WindowManager
     */
    void addView(View view, WindowManager.LayoutParams wmParams,boolean needMove);

    /**
     * 获取helper内view
     */
    View getView();

    /**
     * 删除helper内view
     */
    void removeView();

    /**
     * 设置helper内view显示
     * see {@link View  setVisibility}
     * @param visibility
     */
    void setVisibility(int visibility);
    /**
     * 获取helper内view显示
     *  see {@link View  getVisibility}
     */
    int getVisibility();
}
