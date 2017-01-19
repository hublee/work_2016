package com.zeustel.cp.wallet.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    public interface OnViewCreatedCallBack{
        void  onViewCreated();
    }

    private OnViewCreatedCallBack onViewCreatedCallBack;
    private Class cls;
    private View contentView;

    public void setOnViewCreatedCallBack(OnViewCreatedCallBack onViewCreatedCallBack) {
        this.onViewCreatedCallBack = onViewCreatedCallBack;
    }

    public BlankFragment() {
    }

    public static BlankFragment newInstance(Class<? extends View> cls) {
        Bundle args = new Bundle();
        BlankFragment fragment = new BlankFragment();
        args.putSerializable("class",cls);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        if (arguments != null) {
            cls = (Class) arguments.getSerializable("class");
            Log.e("TAG", cls.getSimpleName());
            Log.e("TAG", cls.getName());
            
            try {
                final Constructor constructor = cls.getConstructor(Context.class);
                contentView = (View) constructor.newInstance(getActivity());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return contentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (onViewCreatedCallBack != null) {
            onViewCreatedCallBack.onViewCreated();
        }

    }
}
