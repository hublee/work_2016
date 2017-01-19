package com.zeustel.top9.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.zeustel.top9.R;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.LoadProgress;
import com.zeustel.top9.utils.SimpleResponseHandler;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataUser;

import java.security.NoSuchAlgorithmException;

/**
 * 修改密码
 */
public class ModifyPwdFragment extends Fragment implements View.OnClickListener {


    private TextInputLayout modify_pwd;
    private TextInputLayout modify_pwd_again;
    private TextInputLayout modify_pwd_new;
    private ImageButton bottom_back;
    private Button bottom_done;
    private LoadProgress loadProgress;

    public ModifyPwdFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_modify_pwd, container, false);
        modify_pwd = (TextInputLayout) contentView.findViewById(R.id.modify_pwd);
        modify_pwd_again = (TextInputLayout) contentView.findViewById(R.id.modify_pwd_again);
        modify_pwd_new = (TextInputLayout) contentView.findViewById(R.id.modify_pwd_new);
        View bottomLayout = contentView.findViewById(R.id.bottom_layout);
        bottom_back = (ImageButton) bottomLayout.findViewById(R.id.bottom_back);
        bottom_done = (Button) bottomLayout.findViewById(R.id.bottom_done);
        loadProgress = new LoadProgress(getActivity());
        bottom_back.setOnClickListener(this);
        bottom_done.setOnClickListener(this);
        Tools.listenSoftInput(modify_pwd_new.getEditText(), new Runnable() {
            @Override
            public void run() {
                done();
            }
        });
        return contentView;
    }


    @Override
    public void onClick(View v) {
        if (bottom_done.equals(v)) {
            done();
        } else if (bottom_back.equals(v)) {
            getFragmentManager().popBackStack();
        }
    }

    private void done() {
        Tools.hideSoftInput(getActivity(), modify_pwd_new.getEditText().getWindowToken());
        final String pwd = modify_pwd.getEditText().getText().toString();
        final String pwdAgain = modify_pwd_again.getEditText().getText().toString();
        final String pwdNew = modify_pwd_new.getEditText().getText().toString();
        if (Tools.isEmpty(pwd)) {
            modify_pwd.setError(getString(R.string.modify_pwd_empty));
            return;
        }
        if (Tools.isEmpty(pwdAgain)) {
            modify_pwd_again.setError(getString(R.string.modify_pwd_empty));
            return;
        }
        if (!pwd.equals(pwdAgain)) {
            modify_pwd_again.setError(getString(R.string.modify_pwd_diff));
            return;
        }
        if (Tools.isEmpty(pwdNew)) {
            modify_pwd_new.setError(getString(R.string.modify_pwd_empty));
            return;
        }
        loadProgress.show(R.string.modify_ing);
        try {
            DataUser.updateInfo(pwdAgain, pwdNew, new SimpleResponseHandler() {
                @Override
                public void onCallBack(int code, String json) {
                    super.onCallBack(code, json);
                    loadProgress.dismiss();
                    Gson gson = new Gson();
                    Result result = gson.fromJson(json, Result.class);
                    if (result != null) {
                        Tools.showToast(getActivity(), result.getMsg());
                        if (Result.SUCCESS == result.getSuccess()) {
                            getFragmentManager().popBackStack();
                        }
                    }
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
