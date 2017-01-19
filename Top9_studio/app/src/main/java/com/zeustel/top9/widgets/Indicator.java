package com.zeustel.top9.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;

/**
 * 指示器
 *
 * @author NiuLei
 * @date 2015/8/2 14:12
 */
public class Indicator extends RadioGroup {
    public static final int ID_PREFIX = 20150805;
    private static final int DEF_SPACE = 5;
    private static final int DEF_SIZE = 20;
    private Context context;
    private Resources res;
    private int background = R.mipmap.indicator_background;
    private int itemDrawable = R.drawable.indicator_ietm_background;
    private int itemSpace;
    private int itemWidth;
    private int itemHeight;

    public Indicator(Context context) {
        super(context);
        init(context);

    }

    public Indicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        res = context.getResources();
        itemSpace = Tools.dip2px(context, DEF_SPACE);
        itemWidth = itemHeight = Tools.dip2px(context, DEF_SIZE);
        setBackgroundResource(background);
        setOrientation(RadioGroup.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
    }

    /**
     * 设置item图标
     *
     * @param itemDrawable 资源id
     */
    public void setItemDrawable(int itemDrawable) {
        this.itemDrawable = itemDrawable;
    }

    /**
     * 设置每项大小 、间距
     *
     * @param itemSize  宽高
     * @param itemSpace 间距
     */
    public void setItem(int itemSize, int itemSpace) {
        this.itemWidth = this.itemHeight = itemSize;
        this.itemSpace = itemSpace;
    }

    /**
     * 设置每项大小 、间距
     *
     * @param itemWidth  宽
     * @param itemHeight 高
     * @param itemSpace  间距
     */
    public void setItem(int itemWidth, int itemHeight, int itemSpace) {
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.itemSpace = itemSpace;
    }

    /**
     * 规定格式的RadioButton
     */
    public RadioButton newChildBtn() {
        RadioButton mRadioButton = new RadioButton(context);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(itemWidth, itemHeight);
        params.leftMargin = itemSpace / 2;
        params.rightMargin = itemSpace / 2;
        mRadioButton.setLayoutParams(params);
        return mRadioButton;
    }

    /**
     * 规定格式的RadioButton
     *
     * @param index   索引 作为id使用
     * @param normal  默认状态下颜色资源id
     * @param checked 选中状态下颜色资源id
     */
    public RadioButton newChildBtn(int index, int normal, int checked) {
        RadioButton mRadioButton = newChildBtn();
        mRadioButton.setId(ID_PREFIX + index);
        mRadioButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
        mRadioButton.setBackgroundDrawable(Tools.getCheckStateDrawable(getResources(), normal, checked));
        return mRadioButton;
    }

    /**
     * 准备数据
     *
     * @param count 数量
     */
    public void prepare(int count) {
        for (int i = 0; i < count; i++) {
            RadioButton item = newChildBtn();
            item.setId(ID_PREFIX + i);
            item.setButtonDrawable(itemDrawable);
            addView(item);
        }
    }

    @Override
    public void check(int id) {
        super.check(ID_PREFIX + id);
    }
}
