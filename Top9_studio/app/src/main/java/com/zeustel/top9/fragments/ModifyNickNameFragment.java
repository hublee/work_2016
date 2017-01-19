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
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.LoadProgress;
import com.zeustel.top9.utils.SimpleResponseHandler;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.utils.operate.DataUser;

/**
 * 修改昵称
 */
public class ModifyNickNameFragment extends Fragment implements View.OnClickListener {


    private TextInputLayout modify_nick;
    private ImageButton bottom_back;
    private Button bottom_done;
    private LoadProgress loadProgress;
    public ModifyNickNameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_modify_nick_name, container, false);
        modify_nick = (TextInputLayout) contentView.findViewById(R.id.modify_nick);
        View bottomLayout = contentView.findViewById(R.id.bottom_layout);
        bottom_back = (ImageButton) bottomLayout.findViewById(R.id.bottom_back);
        bottom_done = (Button) bottomLayout.findViewById(R.id.bottom_done);
        loadProgress = new LoadProgress(getActivity());
        bottom_back.setOnClickListener(this);
        bottom_done.setOnClickListener(this);
        modify_nick.setHint(Constants.USER.getNickName());
        Tools.listenSoftInput(modify_nick.getEditText(), new Runnable() {
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
        Tools.hideSoftInput(getActivity(), modify_nick.getEditText().getWindowToken());
        final String text = modify_nick.getEditText().getText().toString();
        if (Tools.isEmpty(text)) {
            modify_nick.setError(getString(R.string.modify_nick_empty));
            return;
        }
        if (Constants.USER.getNickName().equals(text)) {
            modify_nick.setError(getString(R.string.modify_nick_none));
            return;
        }
        loadProgress.show(R.string.modify_ing);
        DataUser.updateInfo(text, new SimpleResponseHandler() {
            @Override
            public void onCallBack(int code, String json) {
                super.onCallBack(code, json);
                loadProgress.dismiss();
                Gson gson = new Gson();
                Result result = gson.fromJson(json, Result.class);
                if (result != null) {
                    Tools.showToast(getActivity(), result.getMsg());
                    //                    Tools.showToast(getActivity(),getString(R.string.modify_done));
                    if (Result.SUCCESS == result.getSuccess()) {
                        Constants.USER.setNickName(text);
                        getFragmentManager().popBackStack();
                        //更新本地数据库
                    }
                }
            }
        });
    }
}
