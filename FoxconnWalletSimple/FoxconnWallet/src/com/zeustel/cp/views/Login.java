package com.zeustel.cp.views;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zeustel.cp.bean.Users;
import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by Do on 2016/2/23.
 */
public class Login extends Closeable{
    private EditText userNameEditText;
    private EditText userPassEditText;
    private Button submitButton;

    public Login(Context context){
        super(context);
        addView();
    }

    private void addView() {
        inflate(getContext(), R.layout.view_login, layout);

        userNameEditText = (EditText)findViewById(R.id.login_username);
        userPassEditText = (EditText)findViewById(R.id.login_userpass);
        submitButton = (Button)findViewById(R.id.login_submit);

        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        if(id == R.id.login_submit){
            login();
        }
    }

    private void login(){
        if(TextUtils.isEmpty(userNameEditText.getText().toString())){
            Tools.tips(getContext(), "请输入手机号");
            userNameEditText.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(userPassEditText.getText().toString())){
            Tools.tips(getContext(), "请输入密码");
            userPassEditText.requestFocus();
            return;
        }

        try {
            Tools.SaveUserInfo(new Users(userNameEditText.getText().toString(),Tools.md5Encrypt(userPassEditText.getText().toString()),new Random().nextInt()+"",Long.parseLong("12345")));
            Tools.tips(getContext(),"登录成功！");
            close();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

    }

}
