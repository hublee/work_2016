package com.zeustel.top9.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.zeustel.top9.R;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;

/**
 * 图片展览组件
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/4 09:33
 */
public class ExhibitionView extends FrameLayout implements AnimViewFlipper.OnFlipperListener {
    private Context context;
    private LayoutInflater inflater = null;
    //视图滑动组件
    private AnimViewFlipper exhibition = null;
    //指示器
    private Indicator exhibition_indicator = null;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public ExhibitionView(Context context) {
        super(context);
        init(context);
    }

    public ExhibitionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExhibitionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        View contentView = inflater.inflate(R.layout.exhibition, null);
        exhibition = (AnimViewFlipper) contentView.findViewById(R.id.exhibition);
        exhibition_indicator = (Indicator) contentView.findViewById(R.id.exhibition_indicator);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
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
     */
    public void fillContent(String[] imgPaths) {
        fillContent(imgPaths, ImageDownloader.Scheme.HTTP);
    }

    /**
     * 填充显示内容
     *
     * @param imgPaths 数组
     * @param scheme   前缀
     */
    public void fillContent(String[] imgPaths, ImageDownloader.Scheme scheme) {
        if (imgPaths != null && imgPaths.length > 0) {
            for (int i = 0; i < imgPaths.length; i++) {
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                exhibition.addView(imageView, params);
                if (scheme.equals(ImageDownloader.Scheme.HTTP) || scheme.equals(ImageDownloader.Scheme.HTTPS)) {
                    imageLoader.displayImage(Constants.TEST_IMG + imgPaths[i], imageView, Tools.options);
                } else {
                    Tools.log_i(ExhibitionView.class, "fillContent", "scheme.wrap(imgPaths[i]) : " + scheme.wrap(imgPaths[i]));
                    imageLoader.displayImage(scheme.wrap(imgPaths[i]), imageView, Tools.options);
                }
            }
            exhibition_indicator.prepare(imgPaths.length);
            exhibition_indicator.check(0);
            if (imgPaths.length > 1) {
                /*单张图片隐藏指示器 禁止自动滑动*/
                exhibition_indicator.setVisibility(View.VISIBLE);
                exhibition.setAutoStart(true);
                exhibition.startFlipping();
            }
        } else {
            //..
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
