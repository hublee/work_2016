package com.zeustel.cp.wallet.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/7/1 13:43
 */
public abstract class WalletFrameLayout extends FrameLayout {
    public WalletFrameLayout(Context context) {
        super(context);
        initView();
    }

    public WalletFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WalletFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected abstract void initView();

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }
}
