package com.zeustel.cp.wallet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeustel.cp.bean.AccountInfo;
import com.zeustel.cp.wallet.interfaces.OnExitListener;
import com.zeustel.cp.wallet.interfaces.OnGameStartListener;
import com.zeustel.cp.wallet.interfaces.OnLoginListener;
import com.zeustel.cp.wallet.interfaces.OnRegisterAccountListener;
import com.zeustel.foxconn.cp_sdk.R;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/30 22:06
 */
public class AccountBoundView extends WalletFrameLayout implements View.OnClickListener {
    //第三方图标
    private ImageView accountIcon;
    //账号
    private TextView accountName;
    //会员登录
    private Button memberLogin;
    //创建账号
    private Button registerAccount;
    //开始游戏
    private Button gameStart;
    private OnExitListener onExitListener;
    private OnGameStartListener onGameStartListener;
    private OnLoginListener onLoginListener;
    private OnRegisterAccountListener onRegisterAccountListener;
    private AccountInfo accountInfo;
    private boolean isBind;


    /**
     * 设置账号
     * @param accountInfo
     */
    public void setAccountInfo(AccountInfo accountInfo){
    	this.accountInfo = accountInfo;
    	if (this.accountInfo != null) {
    		 accountIcon.setImageResource(this.accountInfo.getAccountIcon());
            final String email = accountInfo.getEmail();
            if (email != null && !email.isEmpty() && !email.trim().equals("null")) {
                accountName.setText(email);
            } else {
                accountName.setText(this.accountInfo.getAccount());
            }
		}
    }
    public void setBind(boolean isBind){
        this.isBind = isBind;
        updateBindState();
    }

    private void updateBindState(){
        memberLogin.setEnabled(!this.isBind);
        registerAccount.setEnabled(!this.isBind);
    }

    public AccountBoundView(Context context) {
        super(context);
    }

    public AccountBoundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AccountBoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_account_bound, this);
        accountIcon = (ImageView) findViewById(R.id.accountIcon);
        accountName = (TextView) findViewById(R.id.accountName);
        memberLogin = (Button) findViewById(R.id.memberLogin);
        registerAccount = (Button) findViewById(R.id.registerAccount);
        gameStart = (Button) findViewById(R.id.gameStart);
        findViewById(R.id.close).setOnClickListener(this);
        memberLogin.setOnClickListener(this);
        registerAccount.setOnClickListener(this);
        gameStart.setOnClickListener(this);

        updateBindState();
    }

    public void setOnExitListener(OnExitListener onExitListener) {
        this.onExitListener = onExitListener;
    }

    public void setOnGameStartListener(OnGameStartListener onGameStartListener) {
        this.onGameStartListener = onGameStartListener;
    }

    public void setOnLoginListener(OnLoginListener onLoginListener) {
        this.onLoginListener = onLoginListener;
    }

    public void setOnRegisterAccountListener(OnRegisterAccountListener onRegisterAccountListener) {
        this.onRegisterAccountListener = onRegisterAccountListener;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (memberLogin.equals(v)) {
            if (onLoginListener != null) {
                onLoginListener.onLogin(this, OnLoginListener.LoginType.LOGIN_MEMBER,accountInfo);
            }

        } else if (registerAccount.equals(v)) {
            if (onRegisterAccountListener != null) {
                onRegisterAccountListener.onRegisterAccount(this,accountInfo);
            }
        } else if (gameStart.equals(v)) {
            if (onGameStartListener != null) {
                onGameStartListener.onGameStartClick(this,accountInfo);
            }

        } else if (R.id.close == v.getId()) {
            if (onExitListener != null) {
                onExitListener.onExit(this);
            }
        }
    }
}
