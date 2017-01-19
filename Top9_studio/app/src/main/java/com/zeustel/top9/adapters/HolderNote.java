package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.AmountNote;
import com.zeustel.top9.bean.Note;
import com.zeustel.top9.utils.Tools;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/10 11:21
 */
public class HolderNote extends OverallRecyclerHolder<Note> {
    private RadioButton note_item_title;
    private TextView note_item_time;
    private TextView note_item_amount;
    private Note note;
    private String title;
    private int type;
    private long time;

    public HolderNote(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        note_item_title = (RadioButton) itemView.findViewById(R.id.note_item_title);
        note_item_time = (TextView) itemView.findViewById(R.id.note_item_time);
        note_item_amount = (TextView) itemView.findViewById(R.id.note_item_amount);
    }

    @Override
    public void set(OverallRecyclerAdapter<Note> adapter, int position) {
        note = adapter.getItem(position);
        if (note == null) {
            return;
        }
        title = note.getTitle();
        note_item_title.setText(title);
        type = note.getNoteType();
        time = note.getTime();
        switch (type) {
            case Note.NoteType.program:
                note_item_time.setText(Tools.getTimeFormatformatHHmm(time));
                note_item_amount.setVisibility(View.GONE);
                break;
            default:
                if (note instanceof AmountNote) {
                    note_item_time.setText(Tools.getTimeFormatMMddHHmm(time));
                    note_item_amount.setVisibility(View.VISIBLE);
                    note_item_amount.setText(String.valueOf(((AmountNote) note).getAmount()));
                }
                break;
        }

    }

    @Override
    protected boolean isItemClickEnabled() {
        return false;
    }
}
