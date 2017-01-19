package com.zeustel.top9;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Html;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zeustel.top9.assist.share.LoginBaseActivity;
import com.zeustel.top9.fragments.RegistFragment;
import com.zeustel.top9.utils.Tools;

import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录页面
 */
public class LoginActivity extends LoginBaseActivity implements View.OnClickListener {
    //账号布局
    private TextInputLayout login_account;
    //账号
    private AutoCompleteTextView accountEdit;
    //密码布局
    private TextInputLayout login_password;
    //密码
    private EditText passwordEdit;
    //登录按键
    private Button login_commit;
    //注册
    private TextView login_regist;
    //qq登录
    private ImageButton login_qq;
    //微信登录
    private ImageButton login_wechat;

    private RegistFragment mRegistFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_account = (TextInputLayout) findViewById(R.id.login_account);
        accountEdit = (AutoCompleteTextView) login_account.getEditText();
        login_password = (TextInputLayout) findViewById(R.id.login_password);
        passwordEdit = login_password.getEditText();
        login_commit = (Button) findViewById(R.id.login_commit);
        login_regist = (TextView) findViewById(R.id.login_regist);
        login_qq = (ImageButton) findViewById(R.id.login_qq);
        login_wechat = (ImageButton) findViewById(R.id.login_wechat);
        login_regist.setText(Html.fromHtml(getString(R.string.html_font, "#484D57"/*color*/, getString(R.string.login_no_account)) +
                getString(R.string.space) +
                getString(R.string.html_font, "#FD4C3C"/*color*/, getString(R.string.login_regist_now))));
        login_commit.setOnClickListener(this);
        login_regist.setOnClickListener(this);
        login_qq.setOnClickListener(this);
        login_wechat.setOnClickListener(this);
        Tools.listenSoftInput(passwordEdit, new Runnable() {
            @Override
            public void run() {
                doLogin();
            }
        });
        mRegistFragment = new RegistFragment();
    }

    @Override
    public void onBackPressed() {
        if (mRegistFragment.isAdded()) {
            if (!mRegistFragment.isDetached()) {
                getSupportFragmentManager().beginTransaction().detach(mRegistFragment).remove(mRegistFragment).commit();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_commit:
                doLogin();
                break;
            case R.id.login_regist:
                getSupportFragmentManager().beginTransaction().add(R.id.login, mRegistFragment).commit();
                break;
            case R.id.login_qq:
                login(new QQ(getApplicationContext()));
                break;
            case R.id.login_wechat:
                login(new Wechat(getApplicationContext()));
                break;
        }
    }

    private void doLogin() {
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (Tools.isEmpty(account)) {
            login_account.setError(getString(R.string.check_account_not_null));
        } else {
            login_account.setErrorEnabled(false);
            if (Tools.isEmpty(password)) {
                login_password.setError(getString(R.string.check_pass_not_null));
            } else {
                login_password.setErrorEnabled(false);
                login_commit.setEnabled(false);
                try {
                    login(account, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void endLogin(boolean success) {
        login_commit.setEnabled(true);
        if (success) {
            finish();
        }
    }
}
