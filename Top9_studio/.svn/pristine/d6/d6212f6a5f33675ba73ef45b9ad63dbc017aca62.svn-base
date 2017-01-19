package com.zeustel.top9.fm;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;


/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/16 15:33
 */
public final class LocalFM implements Runnable, /*ILocalFM,*/ MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer mediaPlayer;
    private Context context = null;
    private boolean isPlaying;
    private boolean isPrepared;
    private static LocalFM mSimulationFM = null;
    private MediaPlayer.OnPreparedListener onPreparedListener;


    private LocalFM(Context context) {
        this.context = context;
    }

    public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    /**
     * 懒汉式 单例模式
     * 特点 实例的延迟加载
     * 由于 多线程并发访问会出现安全问题 要加同步锁
     * 同步代码块和 同步关键字都行 <低效>
     * 用双层判断 可以解决 低效 问题
     */
    public static LocalFM getInstance(Context context) {
        if (mSimulationFM == null) {
            synchronized (LocalFM.class) {
                if (mSimulationFM == null)
                    mSimulationFM = new LocalFM(context);
            }
        }
        return mSimulationFM;
    }

    public boolean isPlaying() {
        if (!isPrepared) {
            return false;
        }
        return isPlaying = mediaPlayer.isPlaying();
    }

    public void start() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    @Override
    public void run() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
        //        mediaPlayer = MediaPlayer.create(this.context, R.raw.fm);
        mediaPlayer = new MediaPlayer();
//        try {
//            mediaPlayer.setDataSource(this.context, Uri.parse("android.resource://" + context.getPackageName() +
//                    "/" + R.raw.fm));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //        mediaPlayer.prepareAsync();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        /**
         * 重播
         */
        if (mp != null) {
            mp.seekTo(0);
            mp.start();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        if (onPreparedListener != null) {
            onPreparedListener.onPrepared(mp);
        }
    }

    public void onDestroy() {
        if (mediaPlayer != null) {
            if (isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
