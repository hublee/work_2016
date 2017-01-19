package com.zeustel.cp.views;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zeustel.cp.ZSSDK;
import com.zeustel.cp.ZSStatusCode;
import com.zeustel.cp.bean.HttpCommand;
import com.zeustel.cp.bean.PopView;
import com.zeustel.cp.intf.NextCallBack;
import com.zeustel.cp.net.RequestManager;
import com.zeustel.cp.utils.Constants;
import com.zeustel.cp.utils.SdkUtils;
import com.zeustel.cp.utils.Tools;
import com.zeustel.foxconn.cp_sdk.R;
//import com.zeustel.cp.events.AddViewEvent;

/**
 * Created by Do on 2016/2/23.
 */
public class Exchange extends CommonPopView {
	private TextView totalTextView;// 账户信息
	private EditText countEditText;// 兑换数
	private TextView submitTextView;// 提交
	private int max;// 最大兑换数量

	private TextView rateTextView;// 兑换比例
	RequestManager requestManager;
	HttpCommand command;

	public Exchange(Context context) {
		super(context);
		addView();
		command = new HttpCommand();
		command.setContext(getContext());
		requestManager = new RequestManager();
	}

	private void addView() {
		inflate(getContext(), R.layout.view_exchange, layout);

		totalTextView = (TextView) findViewById(R.id.exchange_total);
		countEditText = (EditText) findViewById(R.id.exchange_count);
		submitTextView = (TextView) findViewById(R.id.exchange_submit);
		rateTextView = (TextView) findViewById(R.id.exchange_tips);

		submitTextView.setOnClickListener(this);

		initData();
	}

	/**
	 * 初始数据
	 */
	private void initData() {
		
		max = SdkUtils.getTotal() / 1;

		totalTextView.setText(max + "");

		rateTextView.setText(SdkUtils.getCoinInfo().getTips());
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		int id = v.getId();
		if (id == R.id.exchange_submit) {
			exchange();
		}
	}

	/**
	 * 兑换
	 */
	private void exchange() {
		if (TextUtils.isEmpty(countEditText.getText().toString())) {
			Tools.tips(getContext(), "请输入兑换积分数！");
			countEditText.requestFocus();
			return;
		}

		if (Integer.parseInt(countEditText.getText().toString()) > max) {
			Tools.tips(getContext(), "您最多可兑换" + max + "积分！");
			countEditText.requestFocus();
			countEditText.setText(max + "");
			return;
		}

		// ZSSDK.getDefault().exchange(ZSSDK.getDefault().getMobile(),new
		// NextCallBack() {
		// @Override
		// public void callBack(int code,String msg) {
		// // TODO Auto-generated method stub
		// if(code==ZSStatusCode.SUCCESS){
		// mHandler.sendEmptyMessage(4);
		// }else{
		// Message message = new Message();
		// message.what = 5;
		// message.obj = msg;
		// mHandler.sendMessage(message);
		// }
		// }
		// });
		submitTextView.setClickable(false);
		hasVer();
	}

	private void notVer() {
		System.out.println("没有 二级密码");
		ZSSDK.getDefault()
				.getExchangeCallBack()
				.callBack(ZSSDK.getDefault().getMobile(),
						Integer.parseInt(countEditText.getText().toString()),
						ZSSDK.getDefault().getAuthCode());
		close();
	}

	private void haveVer() {
		System.out.println("有 二级密码");
		PopView mPopView = (PopView) iView.addView(Constants.EXCHANGE_VER);
		mPopView.setOnPopListener(new OnPopListener() {

			@Override
			public void onPop(boolean allow) {
				if (allow) {
					mHandler.sendEmptyMessage(11);
				} else {
					mHandler.sendEmptyMessage(12);
				}
			}
		});
	}

	private void hasVer() {
		command.hasVer(requestManager, ZSSDK.getDefault().getMobile(),
				new NextCallBack() {

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
												haveVer();
												return;
											}
										}
										
										
										
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
						
								}
								
								notVer();
								
							}
						});

					}
				});
	}

	// @Override
	// public void back() {
	// super.back();
	// iView.addView(Constants.EXCHANGE_VIEWID);
	// close();
	// }

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 4:
				iView.addView(Constants.EXCHANGE_OK_VIEWID);
				close();
				break;
			case 11:
				System.out.println("1111111111111111111");
				ZSSDK.getDefault()
						.getExchangeCallBack()
						.callBack(
								ZSSDK.getDefault().getMobile(),
								Integer.parseInt(countEditText.getText()
										.toString()),
								ZSSDK.getDefault().getAuthCode());
				close();
				break;
			case 12:
				System.out.println("2222222222222222222");
				submitTextView.setClickable(true);
				break;
			case 5:
				Tools.tips(getContext(), msg.obj.toString());
				break;
			}
		}
	};

}
