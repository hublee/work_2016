package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;


/**
 * Created by zzz40500 on 15/4/6.
 */
public class LoadingView extends FrameLayout implements View.OnClickListener {

    private static final int ANIMATION_DURATION = 500;

    private static float mDistance = 200;

    private ShapeLoadingView mShapeLoadingView;

    private ImageView mIndicationIm;

    private TextView mLoadTextView;
    private int mTextAppearance;

    private String mLoadText;

    private RelativeLayout load_ing_layout;
    private LinearLayout load_failed_layout;

    private AnimatorSet upThrowSet;
    private AnimatorSet freeFallSet;
    private OnFailedClickListener onFailedClickListener;

    /**
     * 点击加载失败回调
     */
    public interface OnFailedClickListener {
        public void onFailedClick();
    }

    public void addOnFailedClickListener(OnFailedClickListener onFailedClickListener) {
        this.onFailedClickListener = onFailedClickListener;
    }

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);
        setBackgroundColor(Color.WHITE);
        setVisibility(View.VISIBLE);
    }

    private void loading() {
        load_ing_layout.setVisibility(View.VISIBLE);
        load_failed_layout.setVisibility(View.GONE);
        setVisibility(View.VISIBLE);
    }

    public void loadFailed() {
        load_ing_layout.setVisibility(View.GONE);
        load_failed_layout.setVisibility(View.VISIBLE);
    }

    public void loadSuccess() {
        load_ing_layout.setVisibility(View.GONE);
        load_failed_layout.setVisibility(View.GONE);
        setVisibility(View.GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

    public void onDestroy() {
        if (upThrowSet != null) {
            upThrowSet.cancel();
            upThrowSet = null;
        }
        if (freeFallSet != null) {
            freeFallSet.cancel();
            freeFallSet = null;
        }
        onFailedClickListener = null;
        removeAllViews();
    }

    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        mLoadText = typedArray.getString(R.styleable.LoadingView_loadingText);
        mTextAppearance = typedArray.getResourceId(R.styleable.LoadingView_loadingTextAppearance,
                -1);

        typedArray.recycle();
    }


    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        View view = LayoutInflater.from(getContext()).inflate(R.layout.load_view, null);

        mDistance = dip2px(54f);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        layoutParams.gravity = Gravity.CENTER;

        load_ing_layout = (RelativeLayout) view.findViewById(R.id.load_ing_layout);
        load_failed_layout = (LinearLayout) view.findViewById(R.id.load_failed_layout);
        mShapeLoadingView = (ShapeLoadingView) view.findViewById(R.id.shapeLoadingView);
        mIndicationIm = (ImageView) view.findViewById(R.id.indication);
        mLoadTextView = (TextView) view.findViewById(R.id.promptTV);
        load_failed_layout.setOnClickListener(this);
        if (mTextAppearance != -1) {
            mLoadTextView.setTextAppearance(getContext(), mTextAppearance);
        }
        setLoadingText(mLoadText);

        addView(view, layoutParams);

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                freeFall();
            }
        }, 900);

    }

    public void setLoadingText(CharSequence loadingText) {


        if (TextUtils.isEmpty(loadingText)) {
            mLoadTextView.setVisibility(GONE);
        } else {
            mLoadTextView.setVisibility(VISIBLE);
        }
        mLoadTextView.setText(loadingText);
    }

    /**
     * 上抛
     */
    public void upThrow() {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShapeLoadingView, "translationY",
                mDistance, 0);
        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 0.2f, 1);


        ObjectAnimator objectAnimator1 = null;
        switch (mShapeLoadingView.getShape()) {
            case SHAPE_RECT:


                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, -120);

                break;
            case SHAPE_CIRCLE:
                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);

                break;
            case SHAPE_TRIANGLE:

                objectAnimator1 = ObjectAnimator.ofFloat(mShapeLoadingView, "rotation", 0, 180);

                break;
        }


        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator1.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new DecelerateInterpolator(factor));
        objectAnimator1.setInterpolator(new DecelerateInterpolator(factor));
        upThrowSet = new AnimatorSet();
        upThrowSet.setDuration(ANIMATION_DURATION);
        upThrowSet.playTogether(objectAnimator, objectAnimator1, scaleIndication);

        upThrowSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                freeFall();


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        upThrowSet.start();
    }

    public float factor = 1.2f;

    /**
     * 下落
     */
    public void freeFall() {

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mShapeLoadingView, "translationY",
                0, mDistance);
        ObjectAnimator scaleIndication = ObjectAnimator.ofFloat(mIndicationIm, "scaleX", 1, 0.2f);


        objectAnimator.setDuration(ANIMATION_DURATION);
        objectAnimator.setInterpolator(new AccelerateInterpolator(factor));
        freeFallSet = new AnimatorSet();
        freeFallSet.setDuration(ANIMATION_DURATION);
        freeFallSet.playTogether(objectAnimator, scaleIndication);
        freeFallSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {


                mShapeLoadingView.changeShape();
                upThrow();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        freeFallSet.start();
    }

    @Override
    public void onClick(View v) {
        Tools.log_i(LoadingView.class,"onClick","load_failed_layout ....");
        loading();
        if (onFailedClickListener != null) {
            onFailedClickListener.onFailedClick();
        }
    }
}
