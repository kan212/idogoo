package com.idogoo.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.idogoo.R;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.BaseParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.IDoGooUtils;

/**
 * 
 * @author kan 用户注册的页面
 */
public class RegisterFragment extends Fragment implements OnClickListener {

	private View mView;
	private EditText nameEdit, pwdEdit, telEdit, smsEdit;
	private Button registerBtn;
	private TextView smsCodeView;
	private int smsTime = 59;
	private SmsReceiver mSmsReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_register, container, false);
		nameEdit = (EditText) mView.findViewById(R.id.register_name_view);
		pwdEdit = (EditText) mView.findViewById(R.id.register_pwd_view);
		telEdit = (EditText) mView.findViewById(R.id.register_tel_view);
		smsEdit = (EditText) mView.findViewById(R.id.register_sms_view);
		registerBtn = (Button) mView.findViewById(R.id.register_btn);
		smsCodeView = (TextView) mView.findViewById(R.id.sms_code_view);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		smsCodeView.setOnClickListener(this);
		nameEdit.setOnFocusChangeListener(mOnFocusChangeListener);
		pwdEdit.setOnFocusChangeListener(mOnFocusChangeListener);
		telEdit.setOnFocusChangeListener(mOnFocusChangeListener);
		smsEdit.setOnFocusChangeListener(mOnFocusChangeListener);
		mSmsReceiver = new SmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		getActivity().registerReceiver(mSmsReceiver, filter);
		initData();
	}

	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			EditText tv = (EditText) v;
			if (null == tv) {
				return;
			}
			String hint;
			if (hasFocus) {
				hint = tv.getHint().toString();
				tv.setTag(hint);
				tv.setText("");
			} else {
				hint = tv.getTag().toString();
				tv.setText(hint);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sms_code_view:
			String phoneNumb = telEdit.getText().toString().trim();
			if (TextUtils.isEmpty(phoneNumb)) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.login_msg1),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (!IDoGooUtils.isMobileNo(phoneNumb)) {
				Toast.makeText(getActivity(),
						getResources().getString(R.string.login_msg2),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (smsCodeView.isClickable()) {
				smsTime = 59;
				handler.sendEmptyMessage(2);
				smsCodeView.setClickable(false);
				smsCodeView.setBackgroundResource(R.drawable.bg_gray_corners);
				smsCodeView.setTextColor(getResources().getColor(R.color.gray));
				getSmsCode(phoneNumb);
			}
			break;
		case R.id.register_btn:
			registerUser();
			break;
		default:
			break;
		}
	}

	/**
	 * 用户注册
	 */
	private void registerUser() {
		BaseParser parser = new BaseParser();
		IDoGooRequest request = new IDoGooRequest(RequestUrl.registerUrl,
				RequestUrl.postRegisterBody(nameEdit.getText().toString()
						.trim(), telEdit.getText().toString().trim(), pwdEdit
						.getText().toString().trim(), smsEdit.getText()
						.toString().trim()), parser,
				new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {

					}
				});
		HttpUtil.addRequest(request, true);
	}

	/**
	 * 获取验证码
	 * 
	 * @param phoneNum
	 */
	private void getSmsCode(String phoneNum) {
		BaseParser parser = new BaseParser();
		IDoGooRequest request = new IDoGooRequest(
				RequestUrl.getSmsCode(phoneNum), parser,
				new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {

					}
				});
		HttpUtil.addRequest(request, true);
	}

	private Handler handler = new Handler(Looper.myLooper()) {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				if (smsTime == 0) {
					smsCodeView.setText("再次发送");
					smsCodeView.setTextColor(getResources().getColor(
							R.color.white));
					smsCodeView.setTextColor(getResources().getColor(
							R.color.sms_code));
					smsCodeView
							.setBackgroundResource(R.drawable.bg_sms_enable_corners);
					smsCodeView.setClickable(true);
				} else {
					smsTime--;
					smsCodeView.setText("发送中 " + smsTime + "s");
					handler.sendEmptyMessageDelayed(2, 1000);
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	private void initData() {
		nameEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				checkExits(1);
			}
		});
		telEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				checkExits(2);
			}
		});
		smsEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				checkExits(3);
			}
		});
	}

	protected void checkExits(int i) {
		BaseParser parser = new BaseParser();
		IDoGooRequest request = null;
		switch (i) {
		case 1:
			request = new IDoGooRequest(RequestUrl.UrlCheckUserExists,
					RequestUrl.checkUserExists(nameEdit.getText().toString()
							.trim()), parser, new OnProtocolTaskListener() {

						@Override
						public void onProgressUpdate(BaseParser parser) {
							initCheckData(parser, 1);
						}
					});
			break;
		case 2:
			request = new IDoGooRequest(RequestUrl.UrlCheckPhoneExists,
					RequestUrl.checkPhoneExists(nameEdit.getText().toString()
							.trim()), parser, new OnProtocolTaskListener() {

						@Override
						public void onProgressUpdate(BaseParser parser) {
							initCheckData(parser, 2);
						}
					});
			break;
		case 3:
			request = new IDoGooRequest(RequestUrl.UrlCheckPhoneCaptcha,
					RequestUrl.checkPhoneCaptcha(telEdit.getText().toString()
							.trim(), smsEdit.getText().toString().trim()),
					parser, new OnProtocolTaskListener() {

						@Override
						public void onProgressUpdate(BaseParser parser) {
							initCheckData(parser, 3);
						}
					});
			break;
		}
		if (null != request) {
			HttpUtil.addRequest(request, true);
		}
	}

	/**
	 * @param parser
	 * @param i
	 */
	protected void initCheckData(BaseParser parser, int i) {
		Toast.makeText(getActivity(), parser.getMsg(), Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().unregisterReceiver(mSmsReceiver);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class SmsReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String content = null;
			Bundle bundle = intent.getExtras();
			if (null != bundle) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] mges = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					// 获取单条短信内容，以pdu格式存,并生成短信对象；
					mges[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				for (SmsMessage mge : mges) {
					content = mge.getMessageBody();
					if (content.contains("爱打勾")) {

					}
				}
			}
		}

	}

}
