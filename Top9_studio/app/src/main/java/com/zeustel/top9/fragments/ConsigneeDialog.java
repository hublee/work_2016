package com.zeustel.top9.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.zeustel.top9.R;
import com.zeustel.top9.utils.Tools;
import com.zeustel.top9.widgets.TextInputHorizontalLayout;

/**
 * 填写收货人对话框
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/9/16 16:51
 */
public class ConsigneeDialog extends DialogFragment implements DialogInterface.OnClickListener {
    private TextInputHorizontalLayout consigee_name;
    private TextInputHorizontalLayout consigee_address;
    private TextInputHorizontalLayout consigee_tel;
    private OnConsigneeListener listener;

    public void setListener(OnConsigneeListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View contentView = inflater.inflate(R.layout.dialog_fragment_consignee, null);
        consigee_name = (TextInputHorizontalLayout) contentView.findViewById(R.id.consigee_name);
        consigee_address = (TextInputHorizontalLayout) contentView.findViewById(R.id.consigee_address);
        consigee_tel = (TextInputHorizontalLayout) contentView.findViewById(R.id.consigee_tel);
        builder.setView(contentView).setPositiveButton(R.string.conversion, this).setNegativeButton(R.string.cancel, this);
        setCancelable(false);
        Tools.listenSoftInput(consigee_tel.getEditText(), new Runnable() {
            @Override
            public void run() {
                positiveClick();
            }
        });
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (DialogInterface.BUTTON_POSITIVE == which) {
            positiveClick();
        } else if (DialogInterface.BUTTON_NEGATIVE == which) {
            dismiss();
        }
    }

    private void positiveClick() {
        String name = consigee_name.getEditText().getText().toString();
        String address = consigee_address.getEditText().getText().toString();
        String tel = consigee_tel.getEditText().getText().toString();
        dismiss();

        if (Tools.isEmpty(name)) {
            Tools.showToast(getActivity(), R.string.consigee_name_empty);
            return;
        }
        if (Tools.isEmpty(address)) {
            Tools.showToast(getActivity(), R.string.consigee_address_empty);
            return;
        }
        if (Tools.isEmpty(tel)) {
            Tools.showToast(getActivity(), R.string.consigee_tel_empty);
            return;
        }
        if (listener != null) {
            listener.onConsigneeListener(name, address, tel);
        }
        Tools.hideSoftInput(getContext(), consigee_tel.getEditText().getWindowToken());
    }

    /**
     * 回调
     */
    public interface OnConsigneeListener {
        void onConsigneeListener(String name, String address, String tel);
    }
}
