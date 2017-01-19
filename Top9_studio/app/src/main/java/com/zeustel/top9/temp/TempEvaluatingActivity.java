package com.zeustel.top9.temp;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zeustel.top9.R;
import com.zeustel.top9.widgets.CustomViewPager;

public class TempEvaluatingActivity extends AppCompatActivity {
    private CustomViewPager mCustomViewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluating_temp);
        mCustomViewPager = (CustomViewPager) findViewById(R.id.tempPager);
        ImageView imageView1 = new ImageView(TempEvaluatingActivity.this);
        imageView1.setImageResource(R.mipmap.evaluating01);
        ImageView imageView2 = new ImageView(TempEvaluatingActivity.this);
        imageView2.setImageResource(R.mipmap.evaluating02);
        ImageView imageView3 = new ImageView(TempEvaluatingActivity.this);
        imageView3.setImageResource(R.mipmap.evaluating03);
        ImageView imageView4 = new ImageView(TempEvaluatingActivity.this);
        imageView4.setImageResource(R.mipmap.evaluating04);
        View[] views = new View[4];
        views[0] = imageView1;
        views[1] = imageView2;
        views[2] = imageView3;
        views[3] = imageView4;
        mCustomViewPager.setAdapter(new ImagePagerAdapter(views));
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private View[] views;
        private LayoutInflater inflater;

        ImagePagerAdapter(View[] views) {
            this.views = views;
            inflater = getLayoutInflater();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views[position]);
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {

            View imageLayout = views[position];
            view.addView(imageLayout);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View container) {
        }
    }
}
