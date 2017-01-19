package com.zeustel.top9.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/14 11:15
 */
public class LoadProgress {
    private ProgressDialog loadProgress;
    private Context context;

    public LoadProgress(Context context) {
        this.context = context;
        this.context = context;
    }

    public void show(String text) {
        if (text != null && context != null) {
            if (loadProgress == null) {
                loadProgress = ProgressDialog.show(context, "", text);
            } else {
                if (!loadProgress.isShowing()) {
                    loadProgress.setMessage(text);
                    loadProgress.show();
                }
            }
        }
    }

    public void show(int resId) {
        show(context.getString(resId));
    }

    public void dismiss() {
        if (loadProgress != null && loadProgress.isShowing()) {
            loadProgress.dismiss();
        }
    }

    public boolean isShowing() {
        if (loadProgress != null) {
            return loadProgress.isShowing();
        }
        return false;
    }
}
