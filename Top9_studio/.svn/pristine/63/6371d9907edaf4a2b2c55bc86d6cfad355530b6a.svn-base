package com.zeustel.top9.fragments;


import android.os.Bundle;
import android.view.View;

import com.zeustel.top9.R;
import com.zeustel.top9.adapters.HolderNote;
import com.zeustel.top9.adapters.OverallRecyclerAdapter;
import com.zeustel.top9.base.NoteFragment;
import com.zeustel.top9.bean.Program;
import com.zeustel.top9.utils.Tools;

import java.util.ArrayList;

/**
 * 节目单
 */
public class ProgramFragment extends NoteFragment<Program> {
    private static final String EXTRA_NAME = "ProgramList";
    private ArrayList<Program> data;

    public ProgramFragment() {
    }

    public static ProgramFragment newInstance(ArrayList<Program> data) {
        ProgramFragment fragment = new ProgramFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, data);
        fragment.setArguments(args);
        return fragment;
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
    }

    @Override
    protected void onAfterCreateView(View contentView) {
        try {
            setAdapter(new OverallRecyclerAdapter<Program>(data, R.layout.list_item_note, HolderNote.class));//, evaluating_refresh
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        getLoading().loadSuccess();
        getNote_icon().setImageResource(R.mipmap.program_title_icon);
        getNote_title().setText(Tools.getTimeFormatYMD(getResources(), System.currentTimeMillis()));
        getNote_title().setTextColor(getResources().getColor(R.color.red));
    }


    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public void onPullDownRefresh() {
        getRefresh().setRefreshing(false);
    }

    @Override
    public void onPullUpRefresh() {
        getRefresh().setRefreshing(false);

    }
}
