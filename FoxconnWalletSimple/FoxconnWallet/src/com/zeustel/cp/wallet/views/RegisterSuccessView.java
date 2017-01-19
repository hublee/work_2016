package com.zeustel.cp.wallet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zeustel.cp.wallet.interfaces.IAccountUpdate;
import com.zeustel.cp.wallet.interfaces.OnExitListener;
import com.zeustel.cp.wallet.interfaces.OnGameStartListener;
import com.zeustel.foxconn.cp_sdk.R;

/**
 * 登录主页
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/30 11:43
 */
public class RegisterSuccessView extends WalletFrameLayout implements View.OnClickListener,IAccountUpdate{
    //账号
    private TextView emailAccount;
    //下一步
    private Button gameStart;

    public RegisterSuccessView(Context context) {
        super(context);
    }

    public RegisterSuccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterSuccessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static String email;
    private static String pwd;

    public static void setAccountInfo(String emailString,String pwdSting){
        email = emailString;
        pwd = pwdSting;
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_register_success, this);
        emailAccount = (TextView) findViewById(R.id.emailAccount);
        gameStart = (Button) findViewById(R.id.gameStart);
        findViewById(R.id.close).setOnClickListener(this);
        gameStart.setOnClickListener(this);
    }

    private OnExitListener onExitListener;
    private OnGameStartListener onGameStartListener;

    public void setOnExitListener(OnExitListener onExitListener) {
        this.onExitListener = onExitListener;
    }

    public void setOnGameStartListener(OnGameStartListener onGameStartListener) {
        this.onGameStartListener = onGameStartListener;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (gameStart.equals(v)) {
            if (onGameStartListener != null) {
                onGameStartListener.onGameStartClick(this,email,pwd);
            }
        } else if (R.id.close == v.getId()) {
            if (onExitListener != null) {
                onExitListener.onExit(this);
            }
        }
    }

	@Override
	public void updateAccount(String account) {
		emailAccount.setText(account);
	}
}
