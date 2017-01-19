package com.zeustel.top9.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.Note;
import com.zeustel.top9.widgets.CircleImage;

/**
 *
 */
public abstract class NoteFragment<T extends Note> extends WrapOneAndRefreshFragment<T>{
    private CircleImage note_icon;
    private TextView note_title;
    public NoteFragment() {

    }

    @Override
    public View onBeforeCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_note, container, false);
        note_icon = (CircleImage) contentView.findViewById(R.id.note_icon);
        note_title = (TextView) contentView.findViewById(R.id.note_title);
        return contentView;
    }

    public ImageView getNote_icon() {
        return note_icon;
    }

    public TextView getNote_title() {
        return note_title;
    }
}
