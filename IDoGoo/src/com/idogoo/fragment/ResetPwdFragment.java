package com.idogoo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.idogoo.R;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.BaseParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;

public class ResetPwdFragment extends Fragment implements OnClickListener {

	private View mView;
	private EditText currentPwd, resetPwd, confilmPwd;
	private Button submitBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_reset_pwd, container, false);
		currentPwd = (EditText) mView.findViewById(R.id.edit_current_pwd);
		resetPwd = (EditText) mView.findViewById(R.id.edit_new_pwd);
		confilmPwd = (EditText) mView.findViewById(R.id.edit_confilm_pwd);
		submitBtn = (Button) mView.findViewById(R.id.btn_submit);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		submitBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			resetPwd(resetPwd.getText().toString().trim());
			break;
		}
	}

	/**
	 * 重置密码
	 * @param pwd
	 */
	private void resetPwd(String pwd) {
		BaseParser parser = new BaseParser();
		IDoGooRequest request = null;
		request = new IDoGooRequest(RequestUrl.UrlResetPwd,
				RequestUrl.postResetPwd(pwd, "", ""), parser,
				new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {
						// TODO 登录文字说明 失败为：1、参数信息有误 2、验证信息超时，请重新获取验证码
						// 3、请使用注册时填写的手机号码 4、密码重置失败 5、校验数据错误
					}
				});
		HttpUtil.addRequest(request, true);
	}

}
