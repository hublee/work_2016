package com.zeustel.top9.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.zeustel.top9.R;
import com.zeustel.top9.base.WrapNotFrament;
import com.zeustel.top9.bean.SubUserInfo;
import com.zeustel.top9.result.Result;
import com.zeustel.top9.utils.SimpleResponseHandler;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataUser;

/**
 * 注册
 */
public class RegistFragment extends WrapNotFrament implements RadioGroup.OnCheckedChangeListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private TextInputLayout regist_account;
    private EditText accountEdit;
    private TextInputLayout regist_password;
    private EditText passwordEdit;
    private CheckBox regist_show;
    private RadioGroup regist_group;
    private int gender = SubUserInfo.Gender.GENTLEMEN;
    private Button regist_commit;
    private ProgressDialog registDialog;
    public RegistFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_regist, container, false);
        regist_account = (TextInputLayout) contentView.findViewById(R.id.regist_account);
        accountEdit = regist_account.getEditText();
        regist_password = (TextInputLayout) contentView.findViewById(R.id.regist_password);
        passwordEdit = regist_password.getEditText();
        regist_show = (CheckBox) contentView.findViewById(R.id.regist_show);
        regist_group = (RadioGroup) contentView.findViewById(R.id.regist_group);
        regist_commit = (Button) contentView.findViewById(R.id.regist_commit);
        regist_group.setOnCheckedChangeListener(this);
        regist_show.setOnCheckedChangeListener(this);
        regist_commit.setOnClickListener(this);
        Tools.listenSoftInput(passwordEdit, new Runnable() {
            @Override
            public void run() {
                doRegist();
            }
        });
        Tools.listenSoftInput(accountEdit, EditorInfo.IME_ACTION_NEXT, false, new Runnable() {
            @Override
            public void run() {
                passwordEdit.requestFocus();
            }
        });
        return contentView;
    }

    @Override
    public String getFloatTitle() {
        return null;
    }

    @Override
    public int getBackgroundColor() {
        return 0;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (regist_group.equals(group)) {
            if (R.id.regist_lady == checkedId) {
                gender = SubUserInfo.Gender.LADY;
            } else {
                gender = SubUserInfo.Gender.GENTLEMEN;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @Override
    public void onClick(View v) {
        if (regist_commit.equals(v)) {
            doRegist();
        }
    }
    private void doRegist(){
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (Tools.isEmpty(account)) {
            regist_account.setError(getString(R.string.check_account_not_null));
        } else {
            regist_account.setErrorEnabled(false);
            if (Tools.isEmpty(password)) {
                regist_password.setError(getString(R.string.check_pass_not_null));
            } else {
                regist_password.setErrorEnabled(false);
                regist_commit.setEnabled(false);
                if (registDialog == null) {
                    registDialog = ProgressDialog.show(getActivity(), "", getString(R.string.regist_ing));
                } else {
                    registDialog.show();
                }
                try {
                    DataUser.toRegist(account, password, gender, new SimpleResponseHandler() {
                        @Override
                        public void onCallBack(final int code, final String json) {
                            super.onCallBack(code, json);
                            new Handler(Looper.myLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (registDialog != null) {
                                        registDialog.dismiss();
                                    }
                                    regist_commit.setEnabled(true);
                                    Gson gson = new Gson();
                                    Result result = gson.fromJson(json, Result.class);
                                    if (result != null) {
                                        if (Result.SUCCESS == result.getSuccess()) {
                                            Tools.showToast(getActivity(), result.getMsg());//success
                                            getFragmentManager().beginTransaction().detach(RegistFragment.this).remove(RegistFragment.this).commit();
                                        } else {
                                            Tools.showToast(getActivity(), result.getMsg());
                                        }
                                    } else {
                                        //..数据格式错误
                                    }
                                }
                            });
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
