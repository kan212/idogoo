package com.idogoo.fragment;

import com.idogoo.R;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.BaseParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.IDoGooUtils;
import com.idogoo.utils.JumpUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 找回密码
 * 
 * @author kan212
 *
 */
public class RetPwdFragment extends Fragment implements OnClickListener {

	private View mView;
	private EditText ret_phone_view, register_sms_view;
	private TextView smsCodeView;
	private int smsTime = 59;
	private Button btn_next;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_retpwd, container, false);
		register_sms_view = (EditText) mView
				.findViewById(R.id.register_sms_view);
		ret_phone_view = (EditText) mView.findViewById(R.id.ret_phone_view);
		smsCodeView = (TextView) mView.findViewById(R.id.sms_code_view);
		btn_next = (Button) mView.findViewById(R.id.btn_next);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		btn_next.setOnClickListener(this);
		register_sms_view.setOnClickListener(this);
		smsCodeView.setOnClickListener(this);
		ret_phone_view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sms_code_view:
			String phoneNumb = ret_phone_view.getText().toString().trim();
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
		case R.id.register_sms_view:

			break;
		case R.id.ret_phone_view:

			break;
		case R.id.btn_next:
			JumpUtils.startLogin(getActivity());
			break;
		}
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
