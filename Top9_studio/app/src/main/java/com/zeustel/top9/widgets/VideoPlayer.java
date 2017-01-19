package com.zeustel.top9.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.TransportMediator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.nineoldandroids.view.ViewHelper;
import com.zeustel.top9.R;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;

import java.util.Formatter;
import java.util.Locale;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/19 09:41
 */
public class VideoPlayer extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    public static final String TAG = "CustomVideoView";
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    int width = 0;
    int height = 0;
    private Context context;
    private LayoutInflater inflater;
    private boolean mDragging;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private ImageView media_poster;
    private ImageButton media_play_large;
    private ImageButton media_back;
    private ImageButton media_rewind;
    private ImageButton media_toggle;
    private ImageButton media_fastforward;
    private ImageButton media_fullscreen;
    private TextView media_time_current;
    private SeekBar media_seek;
    private TextView media_time;
    private VideoView media_video;
    private RelativeLayout media_top;
    private LinearLayout media_bottom;
    private Animation fromAlphaAnim, toAlphaAnim;
    private boolean isFullScreen;
    private onVideoPlayerCallBack onVideoPlayerCallBack;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && isShowing() && media_video.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            setEnabled(true);
            show(0);
        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mp) {
            hide();
        }
    };
    private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
        public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
            hide();
            return true;
        }
    };

    public VideoPlayer(Context context) {
        super(context);
        init(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VideoPlayer(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void setOnVideoPlayerCallBack(onVideoPlayerCallBack onVideoPlayerCallBack) {
        this.onVideoPlayerCallBack = onVideoPlayerCallBack;
    }

    private Animation getAlphaAnim(float fromAlpha, float toAlpha) {
        AlphaAnimation animation = new AlphaAnimation(fromAlpha, toAlpha);
        animation.setDuration(200);
        return animation;
    }


    private void init(Context context) {
        this.context = context;
        fromAlphaAnim = getAlphaAnim(0.0f, 1.0f);
        fromAlphaAnim.setAnimationListener(new AnimListener(1.0f));
        toAlphaAnim = getAlphaAnim(1.0f, 0.0f);
        toAlphaAnim.setAnimationListener(new AnimListener(0.0f));
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mRoot = inflater.inflate(R.layout.media_controller_layout, null);
        initComponent(mRoot);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mRoot, params);
    }

    public void setMedia_poster(int media_posterId) {
        media_poster.setImageResource(media_posterId);
    }

    private void initComponent(View mRoot) {
        media_poster = (ImageView) mRoot.findViewById(R.id.media_poster);
        media_play_large = (ImageButton) mRoot.findViewById(R.id.media_play_large);
        media_back = (ImageButton) mRoot.findViewById(R.id.media_back);
        media_rewind = (ImageButton) mRoot.findViewById(R.id.media_rewind);
        media_toggle = (ImageButton) mRoot.findViewById(R.id.media_toggle);
        media_fastforward = (ImageButton) mRoot.findViewById(R.id.media_fastforward);
        media_fullscreen = (ImageButton) mRoot.findViewById(R.id.media_fullscreen);
        media_time_current = (TextView) mRoot.findViewById(R.id.media_time_current);
        media_seek = (SeekBar) mRoot.findViewById(R.id.media_seek);
        media_time = (TextView) mRoot.findViewById(R.id.media_time);
        media_video = (VideoView) mRoot.findViewById(R.id.media_video);
        media_top = (RelativeLayout) mRoot.findViewById(R.id.media_top);
        media_bottom = (LinearLayout) mRoot.findViewById(R.id.media_bottom);
        media_back.setVisibility(View.GONE);
        media_seek.setMax(1000);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        media_toggle.setOnClickListener(this);
        media_fastforward.setOnClickListener(this);
        media_rewind.setOnClickListener(this);
        media_seek.setOnSeekBarChangeListener(this);
        media_play_large.setOnClickListener(this);
        media_back.setOnClickListener(this);
        media_rewind.setOnClickListener(this);
        media_toggle.setOnClickListener(this);
        media_fastforward.setOnClickListener(this);
        media_fullscreen.setOnClickListener(this);
        media_seek.setOnSeekBarChangeListener(this);
        media_video.setOnPreparedListener(onPreparedListener);
        media_video.setOnCompletionListener(mCompletionListener);
        media_video.setOnErrorListener(mErrorListener);
    }

    public void setVideoURI(Uri uri) {
        if (media_video != null)
            media_video.setVideoURI(uri);
    }

    public void setVideoURI(String suffix) {
        if (media_video != null)
            Tools.log_i(VideoPlayer.class, "setVideoURI", "" + Constants.URL_SERVER + suffix);
        media_video.setVideoURI(Uri.parse(Constants.URL_SERVER + suffix));
    }

    public boolean isShowing() {
        Log.i(TAG, "isShowing (line 160): alpha : " + ViewHelper.getAlpha(media_top));
        return 1.0f == ViewHelper.getAlpha(media_top) && 1.0f == ViewHelper.getAlpha(media_bottom);

    }

    public void hide() {
        if (isShowing()) {
            mHandler.removeMessages(SHOW_PROGRESS);
            media_top.startAnimation(toAlphaAnim);
            media_bottom.startAnimation(toAlphaAnim);

        }
    }

    public void show() {
        show(sDefaultTimeout);
    }

    public void show(int timeout) {
        if (!isShowing()) {
            setProgress();
            if (media_toggle != null) {
                media_toggle.requestFocus();
            }
            media_top.startAnimation(fromAlphaAnim);
            media_bottom.startAnimation(fromAlphaAnim);
            disableUnsupportedButtons();
        }
        updatePausePlay();

        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        Message msg = mHandler.obtainMessage(FADE_OUT);
        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    private void updatePausePlay() {
        if (media_toggle == null)
            return;
        if (media_video.isPlaying()) {
            media_toggle.setImageResource(R.mipmap.ic_media_pause);
            media_play_large.setVisibility(View.GONE);
            if (View.VISIBLE == media_poster.getVisibility())
                media_poster.setVisibility(View.GONE);
        } else {
            media_toggle.setImageResource(R.mipmap.ic_media_play);
            media_play_large.setVisibility(View.VISIBLE);
        }
    }

    private long setProgress() {
        if (mDragging) {
            return 0;
        }
        int position = media_video.getCurrentPosition();
        int duration = media_video.getDuration();
        if (media_seek != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                media_seek.setProgress((int) pos);
            }
            int percent = media_video.getBufferPercentage();
            media_seek.setSecondaryProgress(percent * 10);
        }

        if (media_time != null)
            media_time.setText(stringForTime(Math.round(duration)));
        if (media_time_current != null)
            media_time_current.setText(stringForTime(Math.round(position)));

        return position;
    }

    private void doPauseResume() {
        if (media_video != null) {
            if (media_video.isPlaying()) {
                media_video.pause();
            } else {
                media_video.start();
            }
        }
        updatePausePlay();
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (media_toggle != null) {
            media_toggle.setEnabled(enabled);
        }
        if (media_fastforward != null) {
            media_fastforward.setEnabled(enabled);
        }
        if (media_rewind != null) {
            media_rewind.setEnabled(enabled);
        }
        if (media_back != null) {
            media_back.setEnabled(enabled);
        }
        if (media_fullscreen != null) {
            media_fullscreen.setEnabled(enabled);
        }
        if (media_seek != null) {
            media_seek.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    private void disableUnsupportedButtons() {
        int flags = TransportMediator.FLAG_KEY_MEDIA_PLAY | TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE | TransportMediator.FLAG_KEY_MEDIA_STOP;
        if (media_video != null) {
            if (media_video.canPause()) {
                flags |= TransportMediator.FLAG_KEY_MEDIA_PAUSE;
            }
            if (media_video.canSeekBackward()) {
                flags |= TransportMediator.FLAG_KEY_MEDIA_REWIND;
            }
            if (media_video.canSeekForward()) {
                flags |= TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD;
            }
        }
        boolean enabled = isEnabled();
        media_toggle.setEnabled(enabled && (flags & TransportMediator.FLAG_KEY_MEDIA_PAUSE) != 0);
        media_rewind.setEnabled(enabled && (flags & TransportMediator.FLAG_KEY_MEDIA_REWIND) != 0);
        media_fastforward.setEnabled(enabled && (flags & TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD) != 0);
    }

    private void fullscreenListen() {
       /* ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (width == 0 || height == 0) {
            width = layoutParams.width;
            height = layoutParams.height;
        }
        WindowManager.LayoutParams params = ((Activity) context).getWindow().getAttributes();
        if (isFullScreen) {
            ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((Activity) context).getWindow().setAttributes(params);
            ((Activity) context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            layoutParams.width = width;
            layoutParams.height = height;
            isFullScreen = false;
            media_back.setVisibility(View.GONE);
        } else {
            ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            ((Activity) context).getWindow().setAttributes(params);
            ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            isFullScreen = true;
            media_back.setVisibility(View.VISIBLE);
        }
        setLayoutParams(layoutParams);*/
        if (onVideoPlayerCallBack != null) {
            onVideoPlayerCallBack.onFullClick();
        }
    }

    private void fastforwardListen() {
        int pos = media_video.getCurrentPosition();
        pos += 15000; // milliseconds
        media_video.seekTo(pos);
        setProgress();

        show(sDefaultTimeout);
    }

    private void rewindListen() {
        int pos = media_video.getCurrentPosition();
        pos -= 5000; // milliseconds
        media_video.seekTo(pos);
        setProgress();

        show(sDefaultTimeout);
    }

    private void backListen() {
        if (onVideoPlayerCallBack != null) {
            onVideoPlayerCallBack.onBackClick();
        }
    }

    private void toggleListen() {
        doPauseResume();
        show(sDefaultTimeout);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        toggleMediaControlsVisiblity();
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        toggleMediaControlsVisiblity();
        return super.onTrackballEvent(ev);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
        } else {
            toggleMediaControlsVisiblity();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void toggleMediaControlsVisiblity() {
        Log.i(TAG, "toggleMediaControlsVisiblity (line 360): ");
        if (isShowing()) {
            hide();
        } else {
            show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.media_play_large:
            case R.id.media_toggle:
                toggleListen();
                break;
            case R.id.media_back:
                backListen();
                break;
            case R.id.media_rewind:
                rewindListen();
                break;
            case R.id.media_fastforward:
                fastforwardListen();
                break;
            case R.id.media_fullscreen:
                fullscreenListen();
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar bar) {
        show(3600000);

        mDragging = true;

        // By removing these pending progress messages we make sure
        // that a) we won't update the progress while the user adjusts
        // the seekbar and b) once the user is done dragging the thumb
        // we will post one of these messages to the queue again and
        // this ensures that there will be exactly one message queued up.
        mHandler.removeMessages(SHOW_PROGRESS);
    }

    @Override
    public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
        if (!fromuser) {
            // We're not interested in programmatically generated changes to
            // the progress bar's position.
            return;
        }

        int duration = media_video.getDuration();
        long newposition = (duration * progress) / 1000L;
        media_video.seekTo((int) newposition);
        if (media_time_current != null)
            media_time_current.setText(stringForTime((int) newposition));
    }

    @Override
    public void onStopTrackingTouch(SeekBar bar) {
        mDragging = false;
        setProgress();
        updatePausePlay();
        show(sDefaultTimeout);

        // Ensure that progress is properly updated in the future,
        // the call to show() does not guarantee this because it is a
        // no-op if we are already showing.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
    }

    public interface onVideoPlayerCallBack {
        void onBackClick();

        void onFullClick();
    }

    class AnimListener implements Animation.AnimationListener {
        private float toAlpha;

        public AnimListener(float toAlpha) {
            this.toAlpha = toAlpha;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            ViewHelper.setAlpha(media_top, toAlpha);
            ViewHelper.setAlpha(media_bottom, toAlpha);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }
}
