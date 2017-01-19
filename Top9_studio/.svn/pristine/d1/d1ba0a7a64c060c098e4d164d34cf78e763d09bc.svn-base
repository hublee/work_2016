package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.bean.AmountNote;
import com.zeustel.top9.bean.Note;
import com.zeustel.top9.utils.Tools;

import java.util.List;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/25 15:52
 */
public class AdapterChoice<T extends Note> extends BaseAdapter {
    private static final int DURATION_MILLIS = 300;
    private List<T> data;
    private Context context;
    private LayoutInflater inflater;
    private int start;
    private int end;
    private T item;
    private String title;
    private long time;
    private int index;
    private int lineTime;
    private int offset;
    private int reverse;

    public AdapterChoice(List<T> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public T getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    public void anim(int start, int end) {
        this.start = start;
        this.end = end;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (context == null) {
            context = parent.getContext().getApplicationContext();
        }
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        ViewHolder mViewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_choice, null);
            mViewHolder = new ViewHolder();
            mViewHolder.set(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        item = getItem(position);
        if (item != null) {
            title = item.getTitle();
            time = item.getTime();
            mViewHolder.choice_describe.setText(title);
            mViewHolder.choice_time.setText(Tools.formatHHmm.format(time));
            if (item instanceof AmountNote) {
                mViewHolder.choice_count.setVisibility(View.VISIBLE);
                mViewHolder.choice_count.setText(String.valueOf(((AmountNote)item).getAmount()));
            } else {
                mViewHolder.choice_count.setVisibility(View.INVISIBLE);
            }
        }
        if (end - start > 0) {
            index = position - start;
            lineTime = index * DURATION_MILLIS;
            reverse = end - position;
            offset = DURATION_MILLIS + (reverse == 0 ? DURATION_MILLIS : DURATION_MILLIS / reverse);

            Animation sacleIn = AnimationUtils.loadAnimation(context, R.anim.scale_in_vertical);
            Animation radioIn = AnimationUtils.loadAnimation(context, R.anim.anim_sacle_in);
            radioIn.setInterpolator(new OvershootInterpolator());
            Animation layoutIn = AnimationUtils.loadAnimation(context, R.anim.anim_list_item_set_in);
            sacleIn.setDuration(DURATION_MILLIS);
            radioIn.setDuration(DURATION_MILLIS);
            layoutIn.setDuration(DURATION_MILLIS);

            sacleIn.setStartOffset(lineTime);
            Tools.log_i(AdapterChoice.class, "getView", "position " + position + " , offset : " + offset);
            radioIn.setStartOffset(lineTime + offset);
            layoutIn.setStartOffset(lineTime + offset);

            mViewHolder.choice_line.clearAnimation();
            mViewHolder.choice_radio.clearAnimation();
            mViewHolder.choice_content.clearAnimation();
            mViewHolder.choice_line.startAnimation(sacleIn);
            mViewHolder.choice_radio.startAnimation(radioIn);
            mViewHolder.choice_content.startAnimation(layoutIn);
            if (end == position) {
                end = start = index = 0;
            }
        }
        return convertView;
    }

    private static final class ViewHolder implements CompoundButton.OnCheckedChangeListener {
        private ImageView choice_line;
        private RadioButton choice_radio;
        private TextView choice_describe;
        private TextView choice_time;
        private RelativeLayout choice_content;
        private TextView choice_count;

        public ViewHolder() {

        }

        public void set(View convertView) {
            choice_line = (ImageView) convertView.findViewById(R.id.choice_line);
            choice_radio = (RadioButton) convertView.findViewById(R.id.choice_radio);
            choice_describe = (TextView) convertView.findViewById(R.id.choice_describe);
            choice_time = (TextView) convertView.findViewById(R.id.choice_time);
            choice_count = (TextView) convertView.findViewById(R.id.choice_count);
            choice_content = (RelativeLayout) convertView.findViewById(R.id.choice_content);
            choice_radio.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            choice_describe.setSelected(isChecked);
            choice_time.setSelected(isChecked);
        }
    }

}
