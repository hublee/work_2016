package com.zeustel.top9.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zeustel.top9.R;

/**
 * 带删除键的edittext
 *
 * @author NiuLei
 * @date 2015/8/9 13:26
 */
public class WithDeleteEditText<T extends EditText> extends TextInputLayout {
    private T editText;
    private Drawable closeDrawable;
    private boolean isNeedDelete;

    public WithDeleteEditText(Context context) {
        super(context);
    }

    public WithDeleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {
        editText = (T) new EditText(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(editText, params);
        closeDrawable = getResources().getDrawable(R.mipmap.ic_delete);

    }

    public void setDeleteVisibility(boolean isVisibility) {
 /*画的drawable的宽高是按drawable.setBound()设置的宽高，
所以才有The Drawables must already have had setBounds(Rect) called.
使用之前必须使用Drawable.setBounds设置Drawable的长宽。*/
        // editText.setCompoundDrawables();
  /*是画的drawable的宽高是按drawable固定的宽高，
所以才有The Drawables' bounds will be set to their intrinsic bounds.
即通过getIntrinsicWidth()与getIntrinsicHeight()获得，*/
        //        editText.setCompoundDrawablesWithIntrinsicBounds();
//        editText.setCompoundDrawablesWithIntrinsicBounds();
    }

    @Override
    public T getEditText() {
        return editText;
    }
}
