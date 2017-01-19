package com.zeustel.top9.temp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zeustel.top9.R;
import com.zeustel.top9.base.WrapOneFragment;
import com.zeustel.top9.widgets.Rotate3dAnimation;

/**
 *
 */
public class FMContainerTestFragment extends WrapOneFragment {
    private View contentView;
    private float centerX;
    private float centerY;
    private static final float mDepthZ = 0f;
    private static final int mDuration = 500;
    private ImageView img1;
    private ImageView img2;
    private FrameLayout test;

    public FMContainerTestFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_fmcontainer_test, container, false);
        test = (FrameLayout) contentView.findViewById(R.id.test);
        img1 = (ImageView) contentView.findViewById(R.id.img1);
        img2 = (ImageView) contentView.findViewById(R.id.img2);
        contentView.findViewById(R.id.anim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStartAnimView == null) {
                    mStartAnimView = img1;
                }
                applyRotation(mStartAnimView, 0, -90);
            }
        });
        return contentView;
    }

    @Override
    public String getFloatTitle() {
        return getString(R.string.title_fm_detail);
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    private void applyRotation(View animView, float startAngle, float toAngle) {
        centerX = test.getWidth() / 2;
        centerY = test.getHeight() / 2;
        Rotate3dAnimation rotation = new Rotate3dAnimation(startAngle, toAngle, centerX, centerY, mDepthZ, false);
        rotation.setDuration(mDuration);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView());
        animView.startAnimation(rotation);
    }

    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            contentView.post(new SwapViews());
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private View mStartAnimView;

    private final class SwapViews implements Runnable {
        @Override
        public void run() {
            img2.setVisibility(View.GONE);
            img1.setVisibility(View.GONE);
            if (img1 == mStartAnimView) {
                mStartAnimView = img2;
            } else {
                mStartAnimView = img1;
            }
            mStartAnimView.setVisibility(View.VISIBLE);
            mStartAnimView.requestFocus();

            Rotate3dAnimation rotation = new Rotate3dAnimation(90, 0, centerX, centerY, mDepthZ, false);
            rotation.setDuration(mDuration);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            mStartAnimView.startAnimation(rotation);
        }
    }
}
