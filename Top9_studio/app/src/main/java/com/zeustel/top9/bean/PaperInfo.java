package com.zeustel.top9.bean;

import android.support.v4.app.Fragment;
import android.widget.RadioButton;

/**
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/8/7 14:09
 */
@Deprecated
public class PaperInfo {
    public Fragment fragment;
    public RadioButton tab;

    public PaperInfo(Fragment fragment, RadioButton tab) {
        this.fragment = fragment;
        this.tab = tab;
    }
}
