package com.zeustel.top9;

import android.app.Application;

import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.UncaughtHandler;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/22 17:31
 */
public class TApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!Constants.DEBUG) {
            UncaughtHandler.getInstance(getApplicationContext());
        }
        Tools.initImageLoaderSDK(getApplicationContext());
    }
}
