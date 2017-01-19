package com.zeustel.cp.wallet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.zeustel.cp.wallet.interfaces.OnExitListener;
import com.zeustel.cp.wallet.interfaces.OnNextListener;
import com.zeustel.foxconn.cp_sdk.R;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/30 22:06
 */
public class RegisterAffirmView extends WalletFrameLayout implements View.OnClickListener {
    private CheckBox privacyCheck;
    private CheckBox termsCheckbox;
    //开始游戏
    private Button next;
    private String email;
    private String pwd;

    public RegisterAffirmView(Context context) {
        super(context);
    }

    public RegisterAffirmView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterAffirmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_register_affirm, this);
        privacyCheck = (CheckBox) findViewById(R.id.privacyCheck);
        termsCheckbox = (CheckBox) findViewById(R.id.termsCheckbox);
        next = (Button) findViewById(R.id.next);
        findViewById(R.id.close).setOnClickListener(this);
        next.setOnClickListener(this);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    private OnExitListener onExitListener;
    private OnNextListener onNextListener;

    public void setOnExitListener(OnExitListener onExitListener) {
        this.onExitListener = onExitListener;
    }

    public void setOnNextListener(OnNextListener onNextListener) {
        this.onNextListener = onNextListener;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (next.equals(v)) {
            if (privacyCheck.isChecked() && termsCheckbox.isChecked()) {
                if (onNextListener != null) {
                    onNextListener.onNextClick(this, email, pwd);
                }
            }

        } else if (R.id.close == v.getId()) {
            if (onExitListener != null) {
                onExitListener.onExit(this);
            }
        }
    }
}
