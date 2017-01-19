package com.zeustel.top9.temp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeustel.top9.R;

/**
 */
public class TempGameFragment extends TempSkipFragment {


    public TempGameFragment() {
    }


    private OnSkipListener mOnSkipListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_temp_game, container, false);
        contentView.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSkipListener == null) {
                    mOnSkipListener = getOnSkipListener();
                }
                if (mOnSkipListener != null) {
                    mOnSkipListener.onSkip(TempGameFragment.this);
                }
            }
        });
        return contentView;
    }
}
