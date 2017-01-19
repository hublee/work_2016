package com.zeustel.top9.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;

/**
 * 货币不足对话框
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/16 15:22
 */
public class LackMoneyDialog extends DialogFragment {

    private TextView lack_money_tips;
    private String[] topBtips = null;
    private String[] topBnums = null;
    private String suffix = null;
    private StringBuffer buffer = null;

    @Override
    public void onDestroy() {
        super.onDestroy();
        topBtips = null;
        topBnums = null;
        suffix = null;
        buffer = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        topBtips = getResources().getStringArray(R.array.topBtips);
        topBnums = getResources().getStringArray(R.array.topBnums);
        buffer = new StringBuffer();
        suffix = getString(R.string.space) + ":" + getString(R.string.space) + getString(R.string.top9_b) + getString(R.string.space) + getString(R.string.space);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Tools.log_i(LackMoneyDialog.class, "onCreateDialog", "");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_fragment_lack_money, null);
        lack_money_tips = (TextView) contentView.findViewById(R.id.lack_money_tips);
        contentView.findViewById(R.id.lack_money_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        String tips;
        String num;
        for (int i = 0; i < topBtips.length; i++) {
            tips = topBtips[i];
            num = topBnums[i];
            buffer.append(getString(R.string.html_font, "#484D57"/*color*/, tips + suffix));
            buffer.append(getString(R.string.html_font, "#FD4C3C"/*color*/, num));
            if (topBtips.length - 1 != i) {
                buffer.append("<br>");/*html换行*/
            }
        }
        tips = null;
        num = null;
        lack_money_tips.setText(Html.fromHtml(buffer.toString()));
        builder.setView(contentView);
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Tools.log_i(LackMoneyDialog.class, "onCreateView", "");
        //去掉标题栏
        //        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
