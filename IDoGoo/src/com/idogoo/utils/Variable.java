package com.idogoo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.idogoo.app.IDoGooApp;
import com.idogoo.paser.LoginParser;

public class Variable {
	private static Variable mVariable;

	public static Variable getInstance() {
		if (null == mVariable) {
			mVariable = new Variable(IDoGooApp.getContext());
		}
		return mVariable;
	}

	private Variable(Context context) {
		initPersistent(context);
	}

	private static final String PRESISTENT_FILENAME = "DefaultSharedPrefName";
	private SharedPreferences mPersistent;
	private String versionCode;// 程序版本号
	private String access_key;
	private String sessionId;
	private boolean isUserLogin;

	public void initPersistent(Context context) {
		mPersistent = context.getSharedPreferences(PRESISTENT_FILENAME,
				Context.MODE_PRIVATE);
		versionCode = mPersistent.getString(Constant.KEY_VERSION_CODE, "");
		access_key = mPersistent.getString(Constant.KEY_ACCESS_KEY, "");
		sessionId = mPersistent.getString(Constant.KEY_SEESION_ID, "");
		isUserLogin = mPersistent.getBoolean(Constant.KEY_USER_ID, false);
	}

	public boolean isUserGuide() {
		Context context = IDoGooApp.getContext();
		int curCode = IDoGooUtils.getVersionCode(context);
		return !versionCode.equals(String.valueOf(curCode));
	}

	public void setUserGuide() {
		Context context = IDoGooApp.getContext();
		int curCode = IDoGooUtils.getVersionCode(context);
		versionCode = String.valueOf(curCode);
		save(Constant.KEY_VERSION_CODE, versionCode);
	}

	public void setAccessKey(String access_key) {
		this.access_key = access_key;
		save(Constant.KEY_ACCESS_KEY, access_key);
	}
	
	public void setSessionId(String sessionId) {
		save(Constant.KEY_SEESION_ID, sessionId);
	}
	
	public String  getSessionId() {
		return sessionId;
	}

	public String getAccess_key() {
		return access_key;
	}
	
	public void setUserLogin(boolean isUserLogin) {
		this.isUserLogin = isUserLogin;
	}
	
	public boolean getUserLogin() {
		LoginParser parser = new LoginParser();
		return parser.isCacheValid(60 * 60 * 2);
	}
	
	public void setAccessKeyTime(String time) {
		
	}

	private void save(String key, String value) {
		SharedPreferences.Editor editor = mPersistent.edit();
		editor.putString(key, value);
		editor.commit();
	}
}
