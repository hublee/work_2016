package com.zeustel.top9.temp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.AdapterChoice;
import com.zeustel.top9.base.WrapOneAndHandleFragment;
import com.zeustel.top9.bean.Program;
import com.zeustel.top9.utils.Tools;

import java.util.ArrayList;

/**
 * 节目单
 */
public class TempProgramFragment extends WrapOneAndHandleFragment {
    private static final String EXTRA_NAME = "ProgramList";

    private TextView note_title;
    private ListView note_list;
    private ArrayList<Program> data;
//    private AdapterProgram adapterProgram;
    private AdapterChoice<Program> adapterChoice;
    public TempProgramFragment() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (data != null) {
            data.clear();
            data = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (ArrayList<Program>) getArguments().getSerializable(EXTRA_NAME);
        }
        if (Tools.isEmpty(data)) {
            getFragmentManager().popBackStack();
        }
//        adapterProgram = new AdapterProgram(data);
        adapterChoice = new AdapterChoice<>(data);
    }

    public static TempProgramFragment newInstance(ArrayList<Program> data) {
        TempProgramFragment fragment = new TempProgramFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, data);
        fragment.setArguments(args);
        return fragment;
    }

    public void startAnim() {
        if (adapterChoice != null && note_list != null) {
            adapterChoice.anim(note_list.getFirstVisiblePosition(), note_list.getLastVisiblePosition());
        }
//        if (adapterProgram != null && note_list != null) {
//            adapterProgram.anim(note_list.getFirstVisiblePosition(), note_list.getLastVisiblePosition());
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_program, container, false);
        note_title = (TextView) contentView.findViewById(R.id.note_title);
        note_list = (ListView) contentView.findViewById(R.id.note_list);
        note_list.setAdapter(adapterChoice);
//        note_list.setAdapter(adapterProgram);
        note_title.setText(Tools.getTimeFormatYMD(getResources(), System.currentTimeMillis()));
        note_title.setTextColor(getResources().getColor(R.color.red));
        note_list.setItemChecked(2,true);
        return contentView;
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
