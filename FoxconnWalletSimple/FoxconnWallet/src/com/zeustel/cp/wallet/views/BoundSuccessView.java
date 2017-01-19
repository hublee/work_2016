package com.zeustel.cp.wallet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeustel.cp.bean.AccountInfo;
import com.zeustel.cp.wallet.interfaces.IOtherAccountUpdate;
import com.zeustel.cp.wallet.interfaces.OnExitListener;
import com.zeustel.cp.wallet.interfaces.OnGameStartListener;
import com.zeustel.foxconn.cp_sdk.R;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/30 22:06
 */
public class BoundSuccessView extends WalletFrameLayout implements View.OnClickListener, IOtherAccountUpdate {
    //第三方图标
    private ImageView accountIcon;
    //账号
    private TextView accountName;
    //会员登录
    private Button memberAccount;
    //开始游戏
    private Button gameStart;
    private AccountInfo accountInfo;
    private AccountInfo memberAccountInfo;

    public BoundSuccessView(Context context) {
        super(context);
    }

    public BoundSuccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoundSuccessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_bound_success, this);
        accountIcon = (ImageView) findViewById(R.id.accountIcon);
        accountName = (TextView) findViewById(R.id.accountName);
        memberAccount = (Button) findViewById(R.id.memberAccount);
        gameStart = (Button) findViewById(R.id.gameStart);
        findViewById(R.id.close).setOnClickListener(this);
        gameStart.setOnClickListener(this);
    }

    private OnGameStartListener onGameStartListener;
    private OnExitListener onExitListener;

    public void setOnExitListener(OnExitListener onExitListener) {
        this.onExitListener = onExitListener;
    }

    public void setOnGameStartListener(OnGameStartListener onGameStartListener) {
        this.onGameStartListener = onGameStartListener;
    }

    public void setAccountInfo(AccountInfo accountInfo) {
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

    public void setMemberAccountInfo(AccountInfo memberAccountInfo) {
        this.memberAccountInfo = memberAccountInfo;
        if (this.memberAccountInfo != null) {
            final String email = memberAccountInfo.getEmail();
            if (email != null && !email.isEmpty() && !email.trim().equals("null")) {
                memberAccount.setText(email);
            } else {
                memberAccount.setText(this.memberAccountInfo.getAccount());
            }
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (R.id.close == v.getId()) {
            if (onExitListener != null) {
                onExitListener.onExit(this);
            }
        } else if (gameStart.equals(v)) {
            if (onGameStartListener != null) {
                onGameStartListener.onGameStartClick(this,accountInfo);
            }
        }
    }

    @Override
    public void updateAccount(int icon, String account) {
        accountIcon.setImageResource(icon);
        accountName.setText(account);
    }
}
