package com.zeustel.top9.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/22 15:37
 */
@Deprecated
public class ReflectPagerAdapter extends PagerAdapter {

    public static class PagerInfo {
        private Class<? extends Fragment> cls;
        private Bundle args;
        private String tag;

        public PagerInfo(Bundle args, Class<? extends Fragment> cls, String tag) {
            this.args = args;
            this.cls = cls;
            this.tag = tag;
        }
    }

    private Context context;
    private FragmentManager fm;
    private FragmentTransaction mCurTransaction;
    private List<PagerInfo> data;
    private Fragment mCurrentPrimaryItem;

    public ReflectPagerAdapter(Context context, FragmentManager fm,List<PagerInfo> data) {
        this.context = context;
        this.fm = fm;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = fm.beginTransaction();
        }
        PagerInfo mPagerInfo = data.get(position);
        Fragment fragment = Fragment.instantiate(context, mPagerInfo.cls.getName(), mPagerInfo.args);
        mCurTransaction.add(container.getId(), fragment, mPagerInfo.tag == null? mPagerInfo.cls.getName() : mPagerInfo.tag );
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = fm.beginTransaction();
        }
        mCurTransaction.detach((Fragment) object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            fm.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment) object).getView() == view;
    }
}
