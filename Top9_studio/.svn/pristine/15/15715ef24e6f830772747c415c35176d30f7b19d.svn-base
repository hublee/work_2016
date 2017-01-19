package com.zeustel.top9.fm;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.zeustel.top9.utils.Tools;

public class LocalFmService extends Service {
    private LocaBinder mLocaBinder = null;
    private LocalFM localFM;
    private Thread thread;

    @Override
    public void onCreate() {
        super.onCreate();
        localFM = LocalFM.getInstance(getApplicationContext());
        thread = new Thread(localFM);
        mLocaBinder = new LocaBinder();
        Tools.log_i(LocalFmService.class, "onCreate", "");
        thread.start();
    }

    public LocalFmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mLocaBinder;
    }

    private class LocaBinder extends Binder implements ILocalFM {
        @Override
        public void setOnPreparedListener(MediaPlayer.OnPreparedListener onPreparedListener) {
            localFM.setOnPreparedListener(onPreparedListener);
        }

        @Override
        public boolean isPlaying() {
            return localFM.isPlaying();
        }

        @Override
        public void start() {
            localFM.start();
        }

        @Override
        public void pause() {
            localFM.pause();
        }
    }

    @Override
    public void onDestroy() {
        if (localFM != null) {
            localFM.onDestroy();
            localFM = null;
        }
        super.onDestroy();
    }
}
