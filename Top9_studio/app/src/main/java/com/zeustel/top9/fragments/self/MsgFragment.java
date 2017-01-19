package com.zeustel.top9.fragments.self;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeustel.top9.R;

/**
 * 我的消息
 */
public class MsgFragment extends Fragment {


    public MsgFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_msg, container, false);
        return contentView;
    }


}
