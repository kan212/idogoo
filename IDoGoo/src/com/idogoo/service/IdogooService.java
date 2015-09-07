package com.idogoo.service;

import org.android.agoo.client.BaseConstants;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.idogoo.utils.IDoGooUtils;
import com.umeng.message.UTrack;
import com.umeng.message.UmengBaseIntentService;
import com.umeng.message.entity.UMessage;

public class IdogooService extends UmengBaseIntentService {

	private static final String TAG = IdogooService.class.getName();

	@Override
	protected void onError(Context arg0, String arg1) {
		super.onError(arg0, arg1);
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		super.onMessage(context, intent);
		try {
			// 可以通过MESSAGE_BODY取得消息体
			String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
			UMessage msg = new UMessage(new JSONObject(message));
			IDoGooUtils.showNotification(context,
					Integer.parseInt(msg.message_id), msg.title, msg.ticker,
					getIntent(context, msg));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
	}

	private Intent getIntent(Context context, UMessage msg) {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		super.onRegistered(arg0, arg1);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		super.onUnregistered(arg0, arg1);
	}

	@Override
	protected boolean shouldProcessMessage(Context arg0, Intent arg1) {
		return super.shouldProcessMessage(arg0, arg1);
	}

}
