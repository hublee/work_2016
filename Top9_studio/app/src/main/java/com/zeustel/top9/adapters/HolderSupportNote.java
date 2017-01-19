package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.Feedback;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/10 11:21
 */
public class HolderSupportNote extends OverallRecyclerHolder<Feedback> {
    private RadioButton support_note_item_title;
    private TextView support_note_item_reply;
    private TextView support_note_item_detail;
    private Feedback feedback;
    private String feedbackContent;
    private boolean replyed;

    public HolderSupportNote(Context context, View itemView) {
        super(context, itemView);
    }

    @Override
    protected void initItemView(View itemView) {
        support_note_item_title = (RadioButton) itemView.findViewById(R.id.support_note_item_title);
        support_note_item_reply = (TextView) itemView.findViewById(R.id.support_note_item_reply);
        support_note_item_detail = (TextView) itemView.findViewById(R.id.support_note_item_detail);
    }

    @Override
    public void set(OverallRecyclerAdapter<Feedback> adapter, int position) {
        feedback = adapter.getItem(position);
        feedbackContent = feedback.getFeedbackContent();
        support_note_item_title.setText(feedbackContent == null ? "" :feedbackContent);
        replyed = feedback.getStatus() == 1;
        support_note_item_reply.setText(replyed ? getContext().getString(R.string.self_support_reply_already) : getContext().getString(R.string.self_support_reply_not));
        support_note_item_reply.setSelected(replyed);
    }

    @Override
    protected boolean isItemClickEnabled() {
        return false;
    }
}
