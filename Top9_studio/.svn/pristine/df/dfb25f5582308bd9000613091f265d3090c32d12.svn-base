package com.zeustel.top9.widgets;

/**
 * 视频控制器
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/17 14:41
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.TransportMediator;
import android.support.v4.media.TransportPerformer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zeustel.top9.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Formatter;
import java.util.Locale;

public class CustomController extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private TransportMediator transportMediator;
    private Context mContext;
    private View mAnchor;
    private View mRoot;
    private WindowManager mWindowManager;
    private Window mWindow;
    private View mDecor;
    private WindowManager.LayoutParams mDecorLayoutParams;
    private boolean mShowing;
    private boolean mDragging;
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private ImageButton media_rewind;
    private ImageButton media_toggle;
    private ImageButton media_fastforward;
    private ImageButton media_fullscreen;
    private TextView media_time_current;
    private SeekBar media_seek;
    private TextView media_time;


    public CustomController(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomController(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public CustomController(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mRoot = this;
        mContext = context;
        initFloatingWindowLayout();
        initFloatingWindow();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mRoot != null)
            initControllerView();

    }

    private void initFloatingWindow() {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        try {
            Class obj = Class.forName("com.android.internal.policy.PolicyManager");
            Method m = obj.getMethod("makeNewWindow", Context.class);
            mWindow = (Window) m.invoke(obj, getContext());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        mWindow.setWindowManager(mWindowManager, null, null);
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mDecor = mWindow.getDecorView();
        mDecor.setOnTouchListener(mTouchListener);
        mWindow.setContentView(this);
        mWindow.setBackgroundDrawableResource(android.R.color.transparent);

        // While the media controller is up, the volume control keys should
        // affect the media stream type
        mWindow.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setFocusable(true);
        setFocusableInTouchMode(true);
        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        requestFocus();
    }

    // Allocate and initialize the static parts of mDecorLayoutParams. Must
    // also call updateFloatingWindowLayout() to fill in the dynamic parts
    // (y and width) before mDecorLayoutParams can be used.
    private void initFloatingWindowLayout() {
        mDecorLayoutParams = new WindowManager.LayoutParams();
        WindowManager.LayoutParams p = mDecorLayoutParams;
        p.gravity = Gravity.TOP | Gravity.LEFT;
        p.height = LayoutParams.WRAP_CONTENT;
        p.x = 0;
        p.format = PixelFormat.TRANSLUCENT;
        p.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        p.flags |= WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH;
        p.token = null;
        p.windowAnimations = 0; // android.R.style.DropDownAnimationDown;
    }

    // Update the dynamic parts of mDecorLayoutParams
    // Must be called with mAnchor != NULL.
    private void updateFloatingWindowLayout() {
        int[] anchorPos = new int[2];
        mAnchor.getLocationOnScreen(anchorPos);

        // we need to know the size of the controller so we can properly position it
        // within its space
        mDecor.measure(MeasureSpec.makeMeasureSpec(mAnchor.getWidth(), MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(mAnchor.getHeight(), MeasureSpec.AT_MOST));

        WindowManager.LayoutParams p = mDecorLayoutParams;
        p.width = mAnchor.getWidth();
        p.x = anchorPos[0] + (mAnchor.getWidth() - p.width) / 2;
        p.y = anchorPos[1] + mAnchor.getHeight() - mDecor.getMeasuredHeight();
    }

    // This is called whenever mAnchor's layout bound changes
    private OnLayoutChangeListener mLayoutChangeListener = new OnLayoutChangeListener() {
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            updateFloatingWindowLayout();
            if (mShowing) {
                mWindowManager.updateViewLayout(mDecor, mDecorLayoutParams);
            }
        }
    };

    private OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (mShowing) {
                    hide();
                }
            }
            return false;
        }
    };


    /**
     * Set the view that acts as the anchor for the control view.
     * This can for example be a VideoView, or your Activity's main view.
     * When VideoView calls this method, it will use the VideoView's parent
     * as the anchor.
     *
     * @param view The view to which to anchor the controller when it is visible.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setAnchorView(View view) {

        View anchorView = view.getParent() instanceof View ? (View) view.getParent() : view;

        if (mAnchor != null) {
            mAnchor.removeOnLayoutChangeListener(mLayoutChangeListener);
        }
        mAnchor = anchorView;
        if (mAnchor != null) {
            mAnchor.addOnLayoutChangeListener(mLayoutChangeListener);
        }
        LayoutParams frameParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        removeAllViews();
        View v = makeControllerView();
        addView(v, frameParams);
    }

    /**
     * Create the view that holds the widgets that control playback.
     * Derived classes can override this to create their own.
     *
     * @return The controller view.
     * @hide This doesn't work as advertised
     */
    protected View makeControllerView() {
        LayoutInflater inflate = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflate.inflate(R.layout.media_controller_layout, null);
        initControllerView();
        return mRoot;
    }

    private void initControllerView() {
        //        media_poster = (ImageView) mRoot.findViewById(R.id.media_poster);
        //        media_play_large = (ImageButton) mRoot.findViewById(R.id.media_play_large);
        //        media_back = (ImageButton) mRoot.findViewById(R.id.media_back);
        media_rewind = (ImageButton) mRoot.findViewById(R.id.media_rewind);
        media_toggle = (ImageButton) mRoot.findViewById(R.id.media_toggle);
        media_fastforward = (ImageButton) mRoot.findViewById(R.id.media_fastforward);
        media_fullscreen = (ImageButton) mRoot.findViewById(R.id.media_fullscreen);
        media_time_current = (TextView) mRoot.findViewById(R.id.media_time_current);
        media_seek = (SeekBar) mRoot.findViewById(R.id.media_seek);
        media_time = (TextView) mRoot.findViewById(R.id.media_time);


        if (media_toggle != null) {
            media_toggle.requestFocus();
            media_toggle.setOnClickListener(this);
        }

        if (media_fastforward != null) {
            media_fastforward.setOnClickListener(this);
        }

        if (media_rewind != null) {
            media_rewind.setOnClickListener(this);
        }
        if (media_seek != null) {
            media_seek.setOnSeekBarChangeListener(this);
            media_seek.setMax(1000);
        }
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        //        media_play_large.setOnClickListener(this);
        //        media_back.setOnClickListener(this);
        media_rewind.setOnClickListener(this);
        media_toggle.setOnClickListener(this);
        media_fastforward.setOnClickListener(this);
        media_fullscreen.setOnClickListener(this);
        media_seek.setOnSeekBarChangeListener(this);
    }

    public void setTransportMediator(TransportMediator transportMediator) {
        this.transportMediator = transportMediator;
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 3 seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked.
     * This requires the control interface to be a MediaPlayerControlExt
     */
    private void disableUnsupportedButtons() {
        int flags = transportMediator.getTransportControlFlags();
        boolean enabled = isEnabled();
        media_toggle.setEnabled(enabled && (flags & TransportMediator.FLAG_KEY_MEDIA_PAUSE) != 0);
        media_rewind.setEnabled(enabled && (flags & TransportMediator.FLAG_KEY_MEDIA_REWIND) != 0);
        media_fastforward.setEnabled(enabled && (flags & TransportMediator.FLAG_KEY_MEDIA_FAST_FORWARD) != 0);
    }

    /**
     * Show the controller on screen. It will go away
     * automatically after 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show
     *                the controller until hide() is called.
     */
    public void show(int timeout) {
        if (!mShowing && mAnchor != null) {
            setProgress();
            if (media_toggle != null) {
                media_toggle.requestFocus();
            }
            disableUnsupportedButtons();
            updateFloatingWindowLayout();
            mWindowManager.addView(mDecor, mDecorLayoutParams);
            mShowing = true;
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

    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mAnchor == null)
            return;

        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                mWindowManager.removeView(mDecor);
            } catch (IllegalArgumentException ex) {
                Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }

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
                    if (!mDragging && mShowing && transportMediator.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

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

    private long setProgress() {
        if (transportMediator == null || mDragging) {
            return 0;
        }
        long position = transportMediator.getCurrentPosition();
        long duration = transportMediator.getDuration();
        if (media_seek != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                media_seek.setProgress((int) pos);
            }
            int percent = transportMediator.getBufferPercentage();
            media_seek.setSecondaryProgress(percent * 10);
        }

        if (media_time != null)
            media_time.setText(stringForTime(Math.round(duration)));
        if (media_time_current != null)
            media_time_current.setText(stringForTime(Math.round(position)));

        return position;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                show(0); // show until hide is called
                break;
            case MotionEvent.ACTION_UP:
                show(sDefaultTimeout); // start timeout
                break;
            case MotionEvent.ACTION_CANCEL:
                hide();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0 && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show(sDefaultTimeout);
                if (media_toggle != null) {
                    media_toggle.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !transportMediator.isPlaying()) {
                transportMediator.startPlaying();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && transportMediator.isPlaying()) {
                transportMediator.pausePlaying();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            return true;
        }

        show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }

    private void updatePausePlay() {
        if (mRoot == null || media_toggle == null)
            return;

        if (transportMediator.isPlaying()) {
            media_toggle.setImageResource(R.mipmap.ic_media_pause);
            //            media_play_large.setVisibility(View.GONE);
        } else {
            media_toggle.setImageResource(R.mipmap.ic_media_play);
            //            media_play_large.setVisibility(View.VISIBLE);
        }
    }

    private void doPauseResume() {
        if (transportMediator != null) {
            if (transportMediator.isPlaying()) {
                transportMediator.pausePlaying();
            } else {
                if (transportMediator.getRemoteControlClient() != null) {
                    transportMediator.startPlaying();
                } else {
                    if (reflectPerformer(transportMediator) != null) {
                        reflectPerformer(transportMediator).onStart();
                    }
                }
            }
        } updatePausePlay();
    }

    /**
     *  java.lang.NullPointerException
     at android.support.v4.media.TransportMediatorJellybeanMR2.startPlaying(TransportMediatorJellybeanMR2.java:141)
     at android.support.v4.media.TransportMediator.startPlaying(TransportMediator.java:259)
     at com.zeustel.top9.widgets.CustomController.doPauseResume(CustomController.java:501)
     at com.zeustel.top9.widgets.CustomController.toggleListen(CustomController.java:580)
     at com.zeustel.top9.widgets.CustomController.onClick(CustomController.java:536)
     at android.view.View.performClick(View.java:4240)
     at android.view.View$PerformClick.run(View.java:17721)
     at android.os.Handler.handleCallback(Handler.java:730)
     at android.os.Handler.dispatchMessage(Handler.java:92)
     at android.os.Looper.loop(Looper.java:137)
     at android.app.ActivityThread.main(ActivityThread.java:5136)
     at java.lang.reflect.Method.invokeNative(Native Method)
     at java.lang.reflect.Method.invoke(Method.java:525)
     at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:737)
     at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:553)
     at dalvik.system.NativeStart.main(Native Method)
     */
    private TransportPerformer performer;

    private TransportPerformer reflectPerformer(TransportMediator mediator) {
        if (performer != null) {
            return performer;
        }
        if (mediator != null) {
            Class<TransportMediator> cls = (Class<TransportMediator>) mediator.getClass();
            if (cls != null) {
                try {
                    Field mCallbacks = cls.getDeclaredField("mCallbacks");
                    if (mCallbacks != null) {
                        mCallbacks.setAccessible(true);
                        performer = (TransportPerformer) mCallbacks.get(mediator);
                        return performer;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
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
        //        if (media_back != null) {
        //            media_back.setEnabled(enabled);
        //        }
        if (media_fullscreen != null) {
            media_fullscreen.setEnabled(enabled);
        }
        if (media_seek != null) {
            media_seek.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //            case R.id.media_play_large:
            case R.id.media_toggle:
                toggleListen();
                break;
            //            case R.id.media_back:
            //                backListen();
            //                break;
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

    private void fullscreenListen() {

    }

    private void fastforwardListen() {
        long pos = transportMediator.getCurrentPosition();
        pos += 15000; // milliseconds
        transportMediator.seekTo(pos);
        setProgress();

        show(sDefaultTimeout);
    }

    private void rewindListen() {
        long pos = transportMediator.getCurrentPosition();
        pos -= 5000; // milliseconds
        transportMediator.seekTo(pos);
        setProgress();

        show(sDefaultTimeout);
    }

    private void backListen() {

    }

    private void toggleListen() {
        doPauseResume();
        show(sDefaultTimeout);
    }

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

    public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
        if (!fromuser) {
            // We're not interested in programmatically generated changes to
            // the progress bar's position.
            return;
        }

        long duration = transportMediator.getDuration();
        long newposition = (duration * progress) / 1000L;
        transportMediator.seekTo((int) newposition);
        if (media_time_current != null)
            media_time_current.setText(stringForTime((int) newposition));
    }

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
}
