package com.zeustel.cp.wallet.views;

import android.content.Context;
import android.graphics.Typeface;
import android.text.InputType;
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
 * 登录主页
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/30 11:43
 */
public class RegisterNormalView extends WalletFrameLayout implements View.OnClickListener {
    //邮箱
    private EditText editEmail;
    //密码
    private EditText editPassword;
    //再次
    private EditText editAgain;
    //下一步
    private Button next;

    public RegisterNormalView(Context context) {
        super(context);
    }

    public RegisterNormalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterNormalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_register_normal, this);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editPassword.setTypeface(Typeface.SANS_SERIF);
        editAgain = (EditText) findViewById(R.id.editAgain

        );
        editAgain.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editAgain.setTypeface(Typeface.SANS_SERIF);
        next = (Button) findViewById(R.id.next);
        findViewById(R.id.close).setOnClickListener(this);
        next.setOnClickListener(this);
    }

    private OnNextListener onNextListener;
    private OnExitListener onExitListener;

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
        	String email = editEmail.getText().toString();
        	String pwd = editPassword.getText().toString();
        	String again = editAgain.getText().toString();
            RegisterSuccessView.setAccountInfo(email,pwd);
            final EditError editError = Tools.allowRegister(email, pwd,again);
            if (editError == EditError.NONE) {
                if (onNextListener != null) {
                    onNextListener.onNextClick(this,email,pwd);
                }
            } else {
                editError.alert(getContext());
            }

        } else if (R.id.close == v.getId()) {
            if (onExitListener != null) {
                onExitListener.onExit(this);
            }
        }
    }
}
