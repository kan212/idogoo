package com.idogoo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.idogoo.R;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.model.AccessKeyModel;
import com.idogoo.paser.AccessKeyParser;
import com.idogoo.paser.BaseParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.Variable;
import com.umeng.message.PushAgent;

public class LoadingActivity extends Activity {

	private IDoGooRequest request;
	private ShowMainUI mShowMain = new ShowMainUI();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		PushAgent.getInstance(this).onAppStart();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		AccessKeyModel.getInstance().requestAccessKey();
		mHandler.postDelayed(mShowMain, 2000);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	private final class ShowMainUI implements Runnable {

		@Override
		public void run() {
			// if(!Variable.getInstance().isUserGuide()) {
			//
			// }else {
			//
			// }
			showMain();
		}
	}

	public void showMain() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		this.finish();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
