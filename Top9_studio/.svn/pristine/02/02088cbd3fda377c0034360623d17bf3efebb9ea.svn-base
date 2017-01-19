package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.zeustel.top9.R;

/**
 * ListVIEW单选模式父布局
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/25 10:33
 */
public class ChoiceRelativeLayout extends RelativeLayout implements Checkable {
    private RadioButton radioBtn;
    private int radioBtnId;

    public ChoiceRelativeLayout(Context context) {
        super(context);
        initView(context, null, 0, 0);
    }

    public ChoiceRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0, 0);
    }

    public ChoiceRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChoiceRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChoiceRelativeLayout, defStyleAttr, defStyleRes);
        radioBtnId = a.getResourceId(R.styleable.ChoiceRelativeLayout_radioBtn, 0);
        if (radioBtnId == 0) {
            throw new IllegalArgumentException("The radioBtnId attribute is required and must refer " + "to a valid child.");
        }
        final boolean checked = a.getBoolean(R.styleable.ChoiceRelativeLayout_radioCheck, false);
        setChecked(checked);
        a.recycle();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        radioBtn = (RadioButton) findViewById(radioBtnId);
        if (radioBtn == null) {
            throw new IllegalArgumentException("The radioBtnId attribute is must refer to an" + " existing child.");
        }
    }

    @Override
    public void setChecked(boolean checked) {
        if (radioBtn != null) {
            radioBtn.setChecked(checked);
        }
    }

    @Override
    public boolean isChecked() {
        if (radioBtn != null) {
            return radioBtn.isChecked();
        }
        return false;
    }

    @Override
    public void toggle() {
        if (radioBtn != null) {
            radioBtn.toggle();
        }
    }
}
