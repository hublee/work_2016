package com.zeustel.top9.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeustel.top9.R;

import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

public class Grid9WheelAdapter extends AbstractWheelTextAdapter {
    private String[] grid9;
    private Context context;
    private Resources res;

    public Grid9WheelAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1, NO_RESOURCE);
        this.context = context;
        res = context.getResources();
        grid9 = res.getStringArray(R.array.grid9);
        setItemTextResource(android.R.id.text1);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(res.getColor(R.color.red));
        }
        return view;
    }

    @Override
    public int getItemsCount() {
        return grid9.length;
    }

    @Override
    protected CharSequence getItemText(int index) {
        return grid9[index];
    }
}