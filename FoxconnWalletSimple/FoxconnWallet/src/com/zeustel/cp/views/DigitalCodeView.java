package com.zeustel.cp.views;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zeustel.cp.ZSSDK;
import com.zeustel.cp.ZSStatusCode;
import com.zeustel.cp.bean.HttpCommand;
import com.zeustel.cp.intf.NextCallBack;
import com.zeustel.cp.net.RequestManager;
import com.zeustel.foxconn.cp_sdk.R;

/**
 * 数字密码
 *
 * @author NiuLei
 * @email niulei@zeustel.com
 * @date 2016/5/27 09:38
 */
public class DigitalCodeView extends FrameLayout implements
		View.OnClickListener {
	private static final int MAX_COUNT = 6;// 六位密码
	private static final int KEYBORD_ROW = 4;// 4行
	private static final int KEYBORD_COLUMN = 3;// 3列
	// 返回
	private ImageView bacl;
	// 输入框
	private GridLayout edit_group;
	// 键盘
	private GridLayout keybord;
	// 忘记密码
	private TextView forget;
	// 错误提醒
	private TextView note;
	private RelativeLayout progress;
	// 编辑索引
	private int editIndex;
	// 上次操作是否为添加
	private boolean afterAdd = true;
	// private ProgressDialog dialog;
	private boolean isShow;

	private OnDigitalCodeListener listener;

	public void setOnDigitalCodeListener(OnDigitalCodeListener listener) {
		this.listener = listener;
	}

	public interface OnDigitalCodeListener {
		void onResult(boolean verifySuccess);

		void onCancel();
	}

	public DigitalCodeView(Context context) {
		super(context);
		init();
	}

	public DigitalCodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DigitalCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		View contentView = inflate(getContext(), R.layout.digitai_code_view,
				null);
		bacl = (ImageView) contentView.findViewById(R.id.back);
		progress = (RelativeLayout) contentView.findViewById(R.id.progress);
		edit_group = (GridLayout) contentView.findViewById(R.id.edit_group);
		forget = (TextView) contentView.findViewById(R.id.forget);
		note = (TextView) contentView.findViewById(R.id.note);
		keybord = (GridLayout) contentView.findViewById(R.id.keybord);
		bacl.setOnClickListener(this);
		forget.setOnClickListener(this);
		prepare();
		addView(contentView, new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	private void showProgress() {
		progress.setVisibility(View.VISIBLE);
		isShow = true;
		// if (dialog == null) {
		// dialog = ProgressDialog.show(getContext(), "", "验证中...", true,
		// false);
		// } else {
		// dialog.show();
		// }
	}

	private void dismissProgress() {
		progress.setVisibility(View.GONE);
		isShow = false;
		// if (dialog != null && dialog.isShowing()) {
		// dialog.dismiss();
		// }
	}

	private void commit() {
		showProgress();
		HttpCommand command = new HttpCommand();
		command.setContext(getContext());
		RequestManager requestManager = new RequestManager();
		StringBuilder build = new StringBuilder();
		for (int i = 0; i < edit_group.getChildCount(); i++) {
			TextView child = (TextView) edit_group.getChildAt(i);
			build.append(child.getText().toString());
		}
		command.verNumPwd(requestManager, ZSSDK.getDefault().getMobile(),
				build.toString(), new NextCallBack() {

					@Override
					public void callBack(final int code, final String msg) {
						System.out.println("msg : " + msg);
						post(new Runnable() {
							public void run() {
								if (code == ZSStatusCode.SUCCESS) {
									try {
										JSONObject json = new JSONObject(msg);
										if (json.has("result")) {
											int result = json.getInt("result");
											if (1 == result) {// 成功
												if (listener != null) {
													listener.onResult(true);
													success();
												}
												return;
											}
										}
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								if (listener != null) {
//									listener.onResult(false);
									failed();
								}
							}
						});

					}
				});
	}

	private void success() {
		dismissProgress();
		// 关闭页面
	}

	private void failed() {
		editIndex = 0;
		final int childCount = edit_group.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final TextView child = (TextView) edit_group.getChildAt(i);
			child.setText("");
			afterAdd = true;
		}
		dismissProgress();
		note.setVisibility(View.VISIBLE);
	}

	/**
	 * 准备编辑框 和键盘
	 */
	private void prepare() {
		for (int i = 0; i < MAX_COUNT; i++) {
			TextView textView = new TextView(getContext());
			textView.setInputType(EditorInfo.TYPE_CLASS_NUMBER
					| EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
			textView.setGravity(Gravity.CENTER);
			textView.setEnabled(false);
			textView.setMinEms(1);
			textView.setTextSize(30);
			// 设置等宽
			GridLayout.Spec columnSpec = GridLayout.spec(i, 1.0f);
			GridLayout.Spec rowSpec = GridLayout.spec(0, 1.0f);
			GridLayout.LayoutParams params = new GridLayout.LayoutParams(
					rowSpec, columnSpec);
			edit_group.addView(textView, params);
		}

		for (int row = 0; row < KEYBORD_ROW; row++) {
			for (int col = 0; col < KEYBORD_COLUMN; col++) {
				TextView textView = new TextView(getContext());
				textView.setGravity(Gravity.CENTER);
				textView.setOnClickListener(this);
				textView.setTextSize(20);
				String text = "";
				if (row == KEYBORD_ROW - 1) {// 最后一行
					if (col == 0) {// 左下角
						textView.setText("");
					} else if (col == KEYBORD_COLUMN - 1) {// 右下角
						text = "x";
					} else {
						text = "0";
					}
				} else {
					// 设置数字
					text = String.valueOf((col + 1) + (row * KEYBORD_COLUMN));
				}
				textView.setText(text);
				textView.setTag(text);
				// 平分宽度 高度
				GridLayout.Spec columnSpec = GridLayout.spec(col, 1.0f);
				GridLayout.Spec rowSpec = GridLayout.spec(row, 1.0f);
				GridLayout.LayoutParams params = new GridLayout.LayoutParams(
						rowSpec, columnSpec);
				keybord.addView(textView, params);

			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!isShow) {
			System.out.println("onTouchEvent");
			return true;
		}
		System.out.println("onTouchEvent--------------------");
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(bacl)) {
			if (listener != null) {
				listener.onCancel();
			}
		} else if (v.equals(forget)) {

		} else {
			if (note.getVisibility() != View.INVISIBLE) {
				note.setVisibility(View.INVISIBLE);
			}
			if (v.getParent() != null && v.getParent().equals(keybord)
					&& v.getTag() != null) {
				final String text = v.getTag().toString();
				if (text.equals("x")) {// delete
					if (editIndex >= 0) {// 最小限制
						if (editIndex >= MAX_COUNT) {
							editIndex = MAX_COUNT - 1;// 修正
						}
						final TextView child = (TextView) edit_group
								.getChildAt(editIndex);
						if (child != null) {
							child.setText("");
							editIndex--;
							afterAdd = false;
						}
					}
				} else if (text.trim().length() == 0) {
					// 忽略
				} else {
					if (editIndex < MAX_COUNT) {// 最大限制
						if (editIndex < 0) {
							editIndex = 0;// 修正删除后
						} else {
							if (!afterAdd) {
								editIndex++;
							}
						}

						final TextView child = (TextView) edit_group
								.getChildAt(editIndex);
						if (child != null) {
							child.setText(text);
							editIndex++;
							afterAdd = true;
							if (editIndex == MAX_COUNT) {// 输入完成
								commit();
							}
						}
					}
				}
			}
		}
	}
}
