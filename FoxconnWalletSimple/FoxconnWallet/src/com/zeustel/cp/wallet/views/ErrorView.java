package com.zeustel.cp.wallet.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.zeustel.foxconn.cp_sdk.R;

/**
 * ...
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/6/30 22:06
 */
public class ErrorView extends WalletFrameLayout implements View.OnClickListener {
    //错误信息
    private TextView errorMsg;
    //重试
    private TextView retry;
    //退出
    private TextView exit;

    public ErrorView(Context context) {
        super(context); initView();
    }
    @Override
    protected void initView() {
        inflate(getContext(), R.layout.view_error, this);
        errorMsg = (TextView) findViewById(R.id.errorMsg);
        retry = (TextView) findViewById(R.id.retry);
        exit = (TextView) findViewById(R.id.exit);
        retry.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void updateMsg(String msg){
        errorMsg.setText(msg);
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (retry.equals(v)) {

        } else if (exit.equals(v)) {

        }
    }
}
