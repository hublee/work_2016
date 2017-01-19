package com.zeustel.top9.temp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.NativeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class TempNativeFragment extends Fragment implements View.OnClickListener {


    public TempNativeFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_native, container, false);
        contentView.findViewById(R.id.exportDatabase).setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exportDatabase:
                try {
                    NativeUtils.exportDatabase2Cache(getActivity().getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
