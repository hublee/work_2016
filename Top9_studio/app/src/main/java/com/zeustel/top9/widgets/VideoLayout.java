package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.LoadProgress;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/19 17:54
 */
public class VideoLayout extends FrameLayout implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, View.OnClickListener {

    private View contentView;

    public VideoLayout(Context context) {
        super(context);
        init();
    }

    public VideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private LayoutInflater inflater = null;
    private VideoView video_view;
    private ImageView video_cover;
    private ImageView video_start;
    private LoadProgress mLoadProgress;
    private boolean isPrepared;
    private boolean needStart;

    private void init() {
        inflater = LayoutInflater.from(getContext());
        mLoadProgress = new LoadProgress(getContext());
        contentView = inflater.inflate(R.layout.widget_video_layout, null);
        video_view = (VideoView) contentView.findViewById(R.id.video_view);
        video_cover = (ImageView) contentView.findViewById(R.id.video_cover);
        video_start = (ImageView) contentView.findViewById(R.id.video_start);
        video_view.setOnErrorListener(this);
        video_view.setOnPreparedListener(this);
        video_view.setOnCompletionListener(this);
        video_view.setClickable(true);
        video_view.setOnClickListener(this);
        video_cover.setOnClickListener(this);
        video_start.setOnClickListener(this);
        addView(contentView, new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//        setBackgroundColor(Color.RED);
    }

    public ImageView getVideo_cover() {
        return video_cover;
    }

    public VideoView getVideo_view() {
        return video_view;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        if (needStart) {
            if (mLoadProgress.isShowing()) {
                mLoadProgress.dismiss();
            }
            start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        video_start.setVisibility(View.VISIBLE);
        video_view.seekTo(0);
    }

    @Override
    public void onClick(View v) {
        if (video_view.equals(v)) {
            video_view.pause();
            video_start.setVisibility(View.VISIBLE);
        } else {
            if (isPrepared) {
                start();
            } else {
                if (!needStart) {
                    needStart = true;
                }
                mLoadProgress.show("准备播放中...");
            }
        }
    }
    private void start(){
        video_view.start();
        video_cover.setVisibility(View.GONE);
        video_start.setVisibility(View.GONE);
        if (mLoadProgress.isShowing()) {
            mLoadProgress.dismiss();
        }
    }
}
