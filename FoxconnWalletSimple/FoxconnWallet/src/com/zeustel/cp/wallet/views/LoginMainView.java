package com.zeustel.cp.wallet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.zeustel.cp.utils.Tools;
import com.zeustel.cp.wallet.interfaces.OnExitListener;
import com.zeustel.cp.wallet.interfaces.OnLoginListener;
import com.zeustel.cp.wallet.interfaces.OnRegisterAccountListener;
import com.zeustel.foxconn.cp_sdk.R;

/**
 * 登录主页
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/30 11:43
 */
public class LoginMainView extends WalletFrameLayout implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private CheckBox checkBox;
    private OnLoginListener onLoginListener;
    private OnRegisterAccountListener onRegisterAccountListener;
    private OnExitListener onExitListener;
    private boolean has;
    private View quickLogin;

    public LoginMainView(Context context) {
        super(context);
    }

    public LoginMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_login_main, this);
        quickLogin = findViewById(R.id.quickLogin);
        quickLogin.setOnClickListener(this);
        findViewById(R.id.close).setOnClickListener(this);
        findViewById(R.id.memberLogin).setOnClickListener(this);
        findViewById(R.id.registerAccount).setOnClickListener(this);
        findViewById(R.id.facebook_login).setOnClickListener(this);
        findViewById(R.id.migme_login).setOnClickListener(this);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(this);
    }
    public void hasHistory(boolean has){
        this.has = has;
        quickLogin.setEnabled(has);
    }
    public LoginMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    public void setOnRegisterAccountListener(OnRegisterAccountListener onRegisterAccountListener) {
        this.onRegisterAccountListener = onRegisterAccountListener;
    }

    public void setOnExitListener(OnExitListener onExitListener) {
        this.onExitListener = onExitListener;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (!checkBox.isChecked()) {
            Toast.makeText(getContext(), "Please confirm the terms of service and privacy policy", Toast.LENGTH_SHORT).show();
            return;
        }
        final int id = v.getId();
        if ((R.id.quickLogin == id)) {
            if (onLoginListener != null) {
                onLoginListener.onLogin(this, OnLoginListener.LoginType.LOGIN_QUICK);
            }

        } else if (R.id.memberLogin == id) {
            if (onLoginListener != null) {
                onLoginListener.onLogin(this, OnLoginListener.LoginType.LOGIN_MEMBER);
            }
        } else if (R.id.registerAccount == id) {
            if (onRegisterAccountListener != null) {
                onRegisterAccountListener.onRegisterAccount(this);
            }
        } else if (R.id.facebook_login == id) {
            if (onLoginListener != null) {
                onLoginListener.onLogin(this, OnLoginListener.LoginType.LOGIN_FACEBOOK);
            }
        } else if (R.id.migme_login == id) {
            //if (onLoginListener != null) {
           //     onLoginListener.onLogin(this, OnLoginListener.LoginType.LOGIN_MIGME);
           // }
            Tools.showToast(getContext(),"This function is not valid!");
        } else if (R.id.close == id) {
            if (onExitListener != null) {
                onExitListener.onExit(this);
            }
        }
    }

    /**
     * Called when the checked state of a compound button has changed.
     *
     * @param buttonView The compound button view whose state has changed.
     * @param isChecked  The new checked state of buttonView.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }
}
