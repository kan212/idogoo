package com.idogoo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.idogoo.R;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.BaseParser;
import com.idogoo.paser.LoginParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.JumpUtils;
import com.idogoo.utils.Variable;

public class LoginFragment extends Fragment implements OnClickListener {

	private View mView;
	private TextView userNameView, passwordView, registerView, resetView;
	private Button loginBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_login, container, false);
		userNameView = (TextView) mView.findViewById(R.id.login_name_view);
		passwordView = (TextView) mView.findViewById(R.id.login_sms_view);
		loginBtn = (Button) mView.findViewById(R.id.login_btn);
		registerView = (TextView) mView.findViewById(R.id.tv_register);
		resetView = (TextView) mView.findViewById(R.id.tv_reset_pwd);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loginBtn.setOnClickListener(this);
		registerView.setOnClickListener(this);
		resetView.setOnClickListener(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			String userName = userNameView.getText().toString().trim();
			String passWard = passwordView.getText().toString().trim();
			logIn(userName, passWard);
			break;
		case R.id.tv_register:
			JumpUtils.startRegister(getActivity());
			break;
		case R.id.tv_reset_pwd:
			JumpUtils.startRestPwd(getActivity());
			break;
		}
	}

	private void logIn(String userName, String passWard) {
		LoginParser loginParser = new LoginParser();
		IDoGooRequest request = new IDoGooRequest(RequestUrl.loginUrl,
				RequestUrl.getLoginBody(userName, passWard), loginParser,
				new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {
						initData((LoginParser) parser);
					}
				});
		request.setLogin(true);
		HttpUtil.addRequest(request, true);
	}

	protected void initData(LoginParser parser) {
		if (BaseParser.SUCCESS == parser.getCode()) {
//			SQLiteDatabase db = IDoGooApp.getDatabaseHelper().getDatabase();
//			db.beginTransaction();
//			UserInfoDB.insert(db, parser.getContentValues());
//			db.endTransaction();
			Variable.getInstance().setUserLogin(true);
			getActivity().finish();
		} else {
			Toast.makeText(getActivity(), parser.getMsg(), Toast.LENGTH_SHORT)
					.show();
		}

	}

}
