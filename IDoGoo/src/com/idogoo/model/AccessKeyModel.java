package com.idogoo.model;

import java.io.File;

import android.text.TextUtils;

import com.idogoo.app.IDoGooApp;
import com.idogoo.inter.OnProtocolTaskListener;
import com.idogoo.paser.AccessKeyParser;
import com.idogoo.paser.BaseParser;
import com.idogoo.paser.VolidTimeParser;
import com.idogoo.request.HttpUtil;
import com.idogoo.request.IDoGooRequest;
import com.idogoo.request.RequestUrl;
import com.idogoo.utils.FileUtil;
import com.idogoo.utils.Variable;

public class AccessKeyModel {

	public static AccessKeyModel mAccessKeyModel;
	private IDoGooRequest request;
	private String timeFileName = "volidTime";

	public static AccessKeyModel getInstance() {
		if (null == mAccessKeyModel) {
			mAccessKeyModel = new AccessKeyModel();
		}
		return mAccessKeyModel;
	}

	public void requestAccessKey() {
		AccessKeyParser parser = new AccessKeyParser();
		request = new IDoGooRequest(RequestUrl.accessKeyUrl,
				RequestUrl.getClientTicket(), parser,
				new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {
						if (parser.getCode() == BaseParser.SUCCESS) {
							requesetAccesskeyTime(((AccessKeyParser) parser)
									.getAccess_key());
							Variable.getInstance().setAccessKey(
									((AccessKeyParser) parser).getAccess_key());
						}
					}
				});

		HttpUtil.addRequest(request);
	}

	public void requesetAccesskeyTime(String accessKey) {
		VolidTimeParser parser = new VolidTimeParser();
		request = new IDoGooRequest(RequestUrl.ttlAccessKeyUrl,
				RequestUrl.getVolidTime(accessKey), parser,
				new OnProtocolTaskListener() {

					@Override
					public void onProgressUpdate(BaseParser parser) {
						if (parser.getCode() == BaseParser.SUCCESS) {
							FileUtil.saveFile(IDoGooApp.getContext(),
									timeFileName, parser.getObj().toString()
											.getBytes());
						}
					}
				});

		HttpUtil.addRequest(request);
	}

	public boolean isAccessKeyVolid() {
		if (TextUtils.isEmpty(Variable.getInstance().getAccess_key())) {
			return false;
		}
		File file = new File(IDoGooApp.getContext().getFilesDir(), timeFileName);
		if (!file.exists()) {
			return false;
		}

		byte[] buffer = FileUtil.readFile(IDoGooApp.getContext(), timeFileName);
		if (null != buffer) {
			VolidTimeParser parser = new VolidTimeParser();
			parser.parse(new String(buffer));

			long durationTime = (System.currentTimeMillis() - file
					.lastModified()) / 1000;
			if (durationTime > parser.getVolidTime()) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

}
