package com.zeustel.top9.widgets;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderII;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片展览组件
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/4 09:33
 */
public class ExhibitionFaceView extends FrameLayout implements AnimViewFlipper.OnFlipperListener {
    private Context context;
    private LayoutInflater inflater = null;
    //视图滑动组件
    private AnimViewFlipper exhibition = null;
    //指示器
    private Indicator exhibition_indicator = null;
    private static final int MAX_SPANCOUNT = 5;
    private static final int MAX_LineCOUNT = 4;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public ExhibitionFaceView(Context context) {
        super(context);
        init(context);
    }

    public ExhibitionFaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExhibitionFaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.exhibition, null);
        exhibition = (AnimViewFlipper) contentView.findViewById(R.id.exhibition);
        exhibition_indicator = (Indicator) contentView.findViewById(R.id.exhibition_indicator);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(contentView, params);
        exhibition_indicator.setBackgroundDrawable(null);
        exhibition_indicator.setVisibility(View.INVISIBLE);
        exhibition.addOnFlipperListener(this);
    }

    /**
     * 为AnimViewFlipper添加item点击事件
     *
     * @param mOnItemClickCallBack
     */
    public void addOnItemClickCallBack(AnimViewFlipper.OnItemClickCallBack mOnItemClickCallBack) {
        exhibition.addOnItemClickCallBack(mOnItemClickCallBack);
    }

    /**
     * 为AnimViewFlipper设置父视图是否具有滚动功能
     *
     * @param scrollable
     */
    public void setParentScrollAble(boolean scrollable) {
        exhibition.setParentScrollAble(scrollable);
    }


    /**
     * 获取当前显示视图索引
     *
     * @return 当前显示视图索引
     */
    public int getDisplayedChild() {
        if (exhibition != null) {
            return exhibition.getDisplayedChild();
        }
        return -1;
    }

    /**
     * 设置当前显示视图
     *
     * @param position 索引
     */
    public void setDisplayedChild(int position) {
        exhibition.setDisplayedChild(position);
        exhibition_indicator.check(position);
    }

    /**
     * 填充显示内容
     *
     * @param imgPaths 集合
     */
    public void fillContent(List<String> imgPaths) {
        if (!Tools.isEmpty(imgPaths)) {
            int max_count = MAX_LineCOUNT * MAX_SPANCOUNT;
            int count = imgPaths.size();
            int multiple = count / max_count;
            int remainder = count % max_count;
            List<List<String>> multiplePaths = new ArrayList();
            if (multiple > 0) {
                for (int i = 0; i < multiple; i++) {
                    multiplePaths.add(imgPaths.subList(i * max_count, (i + 1) * max_count));
                }
            }
            if (remainder > 0) {
                multiplePaths.add(imgPaths.subList(imgPaths.size() - remainder, imgPaths.size()));
            }

            if (!Tools.isEmpty(multiplePaths)) {
                for (int i = 0; i < multiplePaths.size(); i++) {
                    List<String> strings = multiplePaths.get(i);
                    CustomRecyclerView recyclerView = new CustomRecyclerView(getContext());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), MAX_SPANCOUNT);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    try {
                        recyclerView.setAdapter(new OverallRecyclerAdapter(strings, R.layout.list_item_ii, HolderII.class));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    exhibition.addView(recyclerView, params);
                }
                exhibition_indicator.prepare(multiplePaths.size());
                exhibition_indicator.check(0);
                if (multiplePaths.size() > 1) {
                    exhibition_indicator.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void showPrevioused() {
        exhibition_indicator.check(exhibition.getDisplayedChild());
    }

    @Override
    public void showNexted() {
        exhibition_indicator.check(exhibition.getDisplayedChild());
    }
}
