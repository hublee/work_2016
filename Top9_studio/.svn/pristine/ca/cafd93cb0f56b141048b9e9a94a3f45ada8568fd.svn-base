package com.zeustel.top9.widgets;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.AdapterSupportPicture;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/26 09:23
 */
public class ImgPreview extends CustomRecyclerView {
    /*最大显示条目*/
    public static final int MAX_COUNT = 6;
    /*列*/
    public static final int SPAN_COUNT = 3;
    private AdapterSupportPicture adapterSupportPicture;

    public ImgPreview(Context context) {
        super(context);
        init();
    }

    public ImgPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImgPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setItemAnimator(new DefaultItemAnimator());
        setHasFixedSize(true);
       /* GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), SPAN_COUNT);
        setLayoutManager(mLayoutManager);*/
        FullyGridLayoutManager gridLayoutManager = new FullyGridLayoutManager(getContext(), SPAN_COUNT);
        setLayoutManager(gridLayoutManager);
        setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_anim_alpha_in));
    }
}
