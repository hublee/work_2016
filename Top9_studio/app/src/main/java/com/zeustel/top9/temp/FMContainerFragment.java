package com.zeustel.top9.temp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import com.zeustel.top9.R;
import com.zeustel.top9.base.WrapOneFragment;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.Rotate3dAnimation;

/**
 *
 */
public class FMContainerFragment extends WrapOneFragment implements TempSkipFragment.OnSkipListener {

    private TempFMPropertyFragment fm;
    private TempGameFragment game;
    private View contentView;
    private float centerX;
    private float centerY;
    private static final float mDepthZ = 0f;
    private static final int mDuration = 500;

    public FMContainerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tools.log_i(FMContainerFragment.class, "onCreate", "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Tools.log_i(FMContainerFragment.class, "onDestroyView", "");
    }

    @Override
    public void onSkip(Fragment fragment) {
        if (fm.equals(fragment)) {
            mStartAnimView = fm.getView();

        } else if (game.equals(fragment)) {
            mStartAnimView = game.getView();
        }
        applyRotation(mStartAnimView, 0, -90);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_fmcontainer, container, false);
        fm = new TempFMPropertyFragment();
        game = new TempGameFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fmContainer, game).add(R.id.fmContainer, fm).commit();
//        fm.setOnSkipListener(this);
        game.setOnSkipListener(this);
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
        centerX = fm.getView().getWidth() / 2;
        centerY = fm.getView().getHeight() / 2;
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
            if (contentView != null) {
                contentView.post(new SwapViews());
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private View mStartAnimView;

    private final class SwapViews implements Runnable {
        @Override
        public void run() {
            game.getView().setVisibility(View.VISIBLE);
            fm.getView().setVisibility(View.VISIBLE);
            if (fm.getView() == mStartAnimView) {
                mStartAnimView = game.getView();
            } else {
                mStartAnimView = fm.getView();
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
