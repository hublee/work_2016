package com.zeustel.cp.wallet.views;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/7/1 19:39
 */
public class ProgressDialogUtils {
    private ProgressDialog progressDialog;

    public ProgressDialogUtils() {
    }

    public void show(Context context, String msg) {
        progressDialog = ProgressDialog.show(context, "", msg, true, false);
    }

    public void cacel() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        }
    }
}
