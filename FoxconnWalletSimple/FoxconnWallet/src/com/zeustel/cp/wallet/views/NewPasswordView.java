package com.zeustel.cp.wallet.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zeustel.cp.utils.EditError;
import com.zeustel.cp.utils.Tools;
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
public class NewPasswordView extends WalletFrameLayout implements View.OnClickListener {
    //验证码
    private EditText editVer;
    //密码
    private EditText editPassword;
    //下一步
    private Button next;
    private String email;
    private OnNextListener onNextListener;
    private OnExitListener onExitListener;

    public NewPasswordView(Context context) {
        super(context);
    }
    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_forget_new_pwd, this);
        editVer = (EditText) findViewById(R.id.editVer);
        editPassword = (EditText) findViewById(R.id.editPassword);
        next = (Button) findViewById(R.id.next);
        findViewById(R.id.next).setOnClickListener(this);
        next.setOnClickListener(this);
    }
    public void setEmail(String email){
    	this.email = email;
    }

    public NewPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewPasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
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
        	if (TextUtils.isEmpty(email)) {
				return;
			}
        	String pwd = editPassword.getText().toString();
        	String ver = editVer.getText().toString();
        	if (TextUtils.isEmpty(ver)) {
                EditError.VER_EMPTY.alert(getContext());
				return;
			}
            final EditError editError = Tools.checkPwd(pwd);
            if (editError != EditError.NONE) {
                editError.alert(getContext());
                return;
            }
            if (onNextListener != null) {
                onNextListener.onNextClick(this,email,pwd,ver);
            }
        } else if (R.id.close == v.getId()) {
            if (onExitListener != null) {
                onExitListener.onExit(this);
            }
        }
    }
}
