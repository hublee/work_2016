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
import com.zeustel.cp.wallet.interfaces.OnGameStartListener;
import com.zeustel.foxconn.cp_sdk.R;

/**
 * 登录主页
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/30 11:43
 */
public class LoginNormalView extends WalletFrameLayout implements View.OnClickListener {
    //邮箱
    private EditText editEmail;
    //密码
    private EditText editPassword;
    //开始游戏
    private Button gameStart;
    private OnGameStartListener onGameStartListener;
    private OnExitListener onExitListener;
    private OnLoginNormalListener onLoginNormalListener;

    public LoginNormalView(Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_login_normal, this);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editPassword.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editPassword.setTypeface(Typeface.SANS_SERIF);
        findViewById(R.id.forgetLayout).setOnClickListener(this);;
        gameStart = (Button) findViewById(R.id.gameStart);
        findViewById(R.id.close).setOnClickListener(this);
        gameStart.setOnClickListener(this);
    }

    public LoginNormalView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginNormalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 回调
     * @author pxz011
     *
     */
    public interface OnLoginNormalListener{
    	void onForgetClick();
    }


    public void setOnExitListener(OnExitListener onExitListener) {
        this.onExitListener = onExitListener;
    }

    public void setOnGameStartListener(OnGameStartListener onGameStartListener) {
        this.onGameStartListener = onGameStartListener;
    }
    public void setOnLoginNormalListener(OnLoginNormalListener onLoginNormalListener){
    	this.onLoginNormalListener = onLoginNormalListener;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if ((R.id.gameStart == id)) {
        	String email = editEmail.getText().toString();
        	String pwd = editPassword.getText().toString();

            final EditError editError = Tools.allowLogin(email, pwd);
            if (editError == EditError.NONE) {
                if (onGameStartListener != null) {
                    onGameStartListener.onGameStartClick(this,email,pwd);
                }
            } else {
                editError.alert(getContext());
            }

        } else if (R.id.close == id) {
            if (onExitListener != null) {
                onExitListener.onExit(this);
            }
            
        }else if(R.id.forgetLayout == id){
        	if (onLoginNormalListener != null) {
        		onLoginNormalListener.onForgetClick();
			}
        }
    }
}
