package com.zeustel.cp.views;

import android.content.Context;
import android.view.View;

import com.zeustel.cp.bean.PopView;
import com.zeustel.cp.views.DigitalCodeView.OnDigitalCodeListener;
import com.zeustel.foxconn.cp_sdk.R;

public class PopDiagitalCodeView extends PopView implements
		OnDigitalCodeListener {
	private DigitalCodeView mDigitalCodeView;

	public PopDiagitalCodeView(Context context) {
		super(context);

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	protected void initView() {
		inflate(getContext(), R.layout.digital_code_dialog, this);
		mDigitalCodeView = (DigitalCodeView) findViewById(R.id.mDigitalCodeView);
		mDigitalCodeView.setOnDigitalCodeListener(this);
	}

	@Override
	public boolean isNeedMove() {
		return false;
	}

	@Override
	public void onResult(boolean verifySuccess) {
		System.out.println("---------verifySuccess------------------");
		if (popListener != null) {
			popListener.onPop(verifySuccess);
		}
		post(new Runnable() {

			@Override
			public void run() {
				close();
			}
		});
	}

	@Override
	public void onCancel() {
		System.out.println("---------onCancel------------------");
		if (popListener != null) {
			popListener.onPop(false);
		}
		post(new Runnable() {

			@Override
			public void run() {
				close();
			}
		});
	}
}
