package com.zeustel.top9.temp;


import android.support.v4.app.Fragment;

import com.zeustel.top9.base.WrapNotAndHandleFragment;

/**
 */
public class TempSkipFragment extends WrapNotAndHandleFragment {
    private OnSkipListener onSkipListener;

    public interface OnSkipListener {
        void onSkip(Fragment fragment);
    }

    public void setOnSkipListener(OnSkipListener onSkipListener) {
        this.onSkipListener = onSkipListener;
    }

    public OnSkipListener getOnSkipListener() {
        return onSkipListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onSkipListener = null;
    }

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }
}
