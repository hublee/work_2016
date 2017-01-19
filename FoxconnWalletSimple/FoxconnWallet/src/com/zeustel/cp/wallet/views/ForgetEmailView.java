package com.zeustel.cp.wallet.views;

import android.content.Context;
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
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/30 22:06
 */
public class ForgetEmailView extends WalletFrameLayout implements
		View.OnClickListener {
	// 编辑邮箱
	private EditText emailEdit;
	// 下一步
	private Button next;
	private OnNextListener onNextListener;
	private OnExitListener onExitListener;

	public ForgetEmailView(Context context) {
		super(context);
	}

	@Override
	protected void initView() {
		inflate(getContext(), R.layout.view_forget_email, this);
		emailEdit = (EditText) findViewById(R.id.emailEdit);
		next = (Button) findViewById(R.id.next);
		findViewById(R.id.close).setOnClickListener(this);
		next.setOnClickListener(this);
	}

	public ForgetEmailView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ForgetEmailView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setOnExitListener(OnExitListener onExitListener) {
		this.onExitListener = onExitListener;
	}

	public void setOnNextListener(OnNextListener onNextListener) {
		this.onNextListener = onNextListener;
	}

	/**
	 * Called when a view has been clicked.
	 *
	 * @param v
	 *            The view that was clicked.
	 */
	@Override
	public void onClick(View v) {
		if (next.equals(v)) {
			String email = emailEdit.getText().toString();
			final EditError editError = Tools.checkAccount(email);
			if (editError == EditError.NONE) {
				if (onNextListener != null) {
					onNextListener.onNextClick(this, email);
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
