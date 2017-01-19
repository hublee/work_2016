package com.zeustel.top9.fm;

import android.media.MediaPlayer;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/10/16 15:33
 */
public interface ILocalFM {
    void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener);
    boolean isPlaying();
    void start();
    void pause();
}
