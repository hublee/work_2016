package com.zeustel.top9.widgets;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/16 17:27
 */
public class TextInputHorizontalLayout extends TextInputLayout {
    public TextInputHorizontalLayout(Context context) {
        super(context);
        init();
    }

    public TextInputHorizontalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextInputHorizontalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(TextInputLayout.HORIZONTAL);
    }
}
