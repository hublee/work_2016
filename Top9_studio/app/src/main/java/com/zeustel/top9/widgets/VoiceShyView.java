package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/11/6 14:34
 */
public class VoiceShyView extends FrameLayout implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, View.OnClickListener {
    private int[] stateRes = {R.mipmap.ic_media_play, R.mipmap.ic_media_pause};//播放按键图片资源
    private MediaPlayer mediaPlayer;//声音播放
    private boolean autoPlay = false;//是否自动播放
    private boolean isAlive;//是否活动的
    private ListenerThread listenerThread;//监听活动线程
    private ImageButton shy_control;
    private ImageView shy_state;
    private TextView shy_time;
    private Scroller scroller;
    private boolean isPrepared;

    public VoiceShyView(Context context) {
        super(context);
        init();
    }

    public VoiceShyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public VoiceShyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VoiceShyView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnCompletionListener(this);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.shy_view, null);
        shy_control = (ImageButton) contentView.findViewById(R.id.shy_control);
        shy_state = (ImageView) contentView.findViewById(R.id.shy_state);
        shy_time = (TextView) contentView.findViewById(R.id.shy_time);
        shy_control.setOnClickListener(this);
        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(contentView, params);
        isAlive = true;
        listenerThread = new ListenerThread();
        listenerThread.start();
        setOnClickListener(this);
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    public void start(final String url) {
        Tools.log_i(VoiceShyView.class, "start", "url : " + url);
        try {
            if (stateRes != null) {
                shy_control.setImageResource(stateRes[0]);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            if (stateRes != null) {
                shy_control.setImageResource(stateRes[1]);
            }
        }
        isPrepared = false;
        Tools.pool.submit(new PlayTask(getContext().getApplicationContext(), mediaPlayer, url));

        //            shy_time.setText(String.valueOf(mediaPlayer.getDuration() / 1000));
    }

    private static final class PlayTask implements Runnable {
        private final WeakReference<MediaPlayer> mediaPlayerRef;
        private final WeakReference<Context> contextRef;
        private String url;

        public PlayTask(Context context, MediaPlayer mMediaPlayer, String url) {
            this.url = url;
            mediaPlayerRef = new WeakReference<MediaPlayer>(mMediaPlayer);
            contextRef = new WeakReference<Context>(context);
        }

        @Override
        public void run() {
            final MediaPlayer mediaPlayer = mediaPlayerRef.get();
            final Context context = contextRef.get();
            if (context != null && mediaPlayer != null) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(url));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepareAsync();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Tools.log_i(VoiceShyView.class, "onPrepared", "");
        isPrepared = true;
        if (isAutoPlay()) {
            try {
                mediaPlayer.start();
                if (stateRes != null) {
                    shy_control.setImageResource(stateRes[1]);
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
                if (stateRes != null) {
                    shy_control.setImageResource(stateRes[0]);
                }
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Tools.log_i(VoiceShyView.class, "onError", "");
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Tools.log_i(VoiceShyView.class, "onCompletion", "");
    }

    public boolean isOpened() {
        return scroller.getFinalX() == 0;
    }

    @Override
    public void onClick(View v) {
        liberality();
        if (shy_control.equals(v)) {
            Tools.log_i(VoiceShyView.class, "onClick", "liberality.............");
            if (isPrepared) {
                if (mediaPlayer.isPlaying()) {
                    try {
                        mediaPlayer.pause();
                        if (stateRes != null) {
                            shy_control.setImageResource(stateRes[0]);
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        if (stateRes != null) {
                            shy_control.setImageResource(stateRes[1]);
                        }
                    }

                } else {
                    try {
                        mediaPlayer.start();
                        if (stateRes != null) {
                            shy_control.setImageResource(stateRes[1]);
                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                        if (stateRes != null) {
                            shy_control.setImageResource(stateRes[0]);
                        }
                    }

                }
            } else {
                Tools.showToast(getContext(), "正在准备语音");
            }
        }
    }

    private void shy() {
        smoothScrollTo(-80, 0);
    }

    private void liberality() {
        smoothScrollTo(0, 0);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

    public void onDestroy() {
        Tools.log_i(VoiceShyView.class,"onDestroy","");
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        isPrepared = false;
        isAlive = false;
        removeAllViews();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            int currX = scroller.getCurrX();
            int currY = scroller.getCurrY();
            scrollTo(currX, currY);
            postInvalidate();
        } else {

        }
    }

    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - scroller.getFinalX();
        int dy = fy - scroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy) {
        //设置mScroller的滚动偏移量
        scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), dx, dy, 1000);
        invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
    }

    private class ListenerThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isAlive) {
                try {
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        if (isOpened()) {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (getHandler() != null) {
                                getHandler().post(new Runnable() {
                                    @Override
                                    public void run() {
                                        shy();
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mediaPlayer = null;
                }
            }
        }
    }
}

