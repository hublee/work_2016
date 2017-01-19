package com.zeustel.top9.widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zeustel.top9.LoginActivity;
import com.zeustel.top9.R;
import com.zeustel.top9.utils.Constants;
import com.zeustel.top9.utils.Tools;

/**
 * 输入
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2015/7/23 14:08
 */
public class InputView extends CardView implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private View contentView;
    private TextInputLayout input_edit_layout;
    private EditText editText;
    private ImageView input_emoticon;
    private Button input_send;
    private ProgressDialog loginDialog;
    private OnInputView onInputView;


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        inflater = null;
        if (input_send != null) {
            input_send.setOnClickListener(null);
        }
        onInputView = null;
        removeAllViews();
    }

    public InputView(Context context) {
        super(context);
        init(context);
    }

    public InputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public EditText getEditText() {
        return editText;
    }

    public void setOnInputView(OnInputView onInputView) {
        this.onInputView = onInputView;
    }

    private void init(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(R.layout.input_view_layout, null);
        input_edit_layout = (TextInputLayout) contentView.findViewById(R.id.input_edit_layout);
        input_emoticon = (ImageView) contentView.findViewById(R.id.input_emoticon);
        input_send = (Button) contentView.findViewById(R.id.input_send);
        editText = input_edit_layout.getEditText();
        addView(contentView);
        input_send.setOnClickListener(this);
        Tools.listenSoftInput(editText, new Runnable() {
            @Override
            public void run() {
                publish();
            }
        });
    }

    public void setBg(int color) {
        contentView.setBackgroundColor(color);
    }

    public void done() {
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        if (input_send.equals(v)) {
            publish();
        }
    }

    private void publish() {
        String content = editText.getText().toString();
        if (Tools.isEmpty(content)) {
            Tools.showToast(getContext(), R.string.input_empty);
        } else {
            if (Constants.USER.isOnline()) {
                Tools.hideSoftInput(getContext(), editText.getWindowToken());
                if (onInputView != null) {
                    onInputView.onInputView(content);
                }
                if (loginDialog == null) {
                    loginDialog = ProgressDialog.show(context, "", getResources().getString(R.string.input_send_ing));
                } else {
                    loginDialog.show();
                }
                editText.setText("");
            } else {
                Intent intent = new Intent(getContext().getApplicationContext(), LoginActivity.class);
                getContext().startActivity(intent);
            }
        }
    }

    public interface OnInputView {
        void onInputView(String content);
    }
}
