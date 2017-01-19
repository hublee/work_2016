package com.zeustel.top9.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/5 17:06
 */
public class OverallListAdapter<T> extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<T> data;
    private int layoutId;
    private Class<? extends OverallRecyclerHolder> cls;
    private Constructor<?> vh;

    public OverallListAdapter(List<T> data, int layoutId, Class<? extends OverallRecyclerHolder> cls) throws NoSuchMethodException {
        this.data = data;
        this.layoutId = layoutId;
        this.cls = cls;
        if (cls != null) {
            try {
                vh = cls.getConstructor(Context.class, View.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                throw e;
            }
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (context == null)
            context = parent.getContext();
        if (inflater == null)
            inflater = LayoutInflater.from(context);
        OverallRecyclerHolder mRecyclerHolderHelper = null;
        if (convertView == null) {
            if (vh != null) {
                try {
                    convertView = inflater.inflate(layoutId, null);
                    mRecyclerHolderHelper = (OverallRecyclerHolder) vh.newInstance(context, convertView);
                    convertView.setTag(mRecyclerHolderHelper);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } else {
            mRecyclerHolderHelper = (OverallRecyclerHolder) convertView.getTag();
        }
        mRecyclerHolderHelper.set(this, position);
        return convertView;
    }
}
