package com.idogoo.paser;

import java.io.File;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.text.TextUtils;

import com.idogoo.app.IDoGooApp;
import com.idogoo.utils.FileUtil;

public class BaseParser {

	public static final int SUCCESS = 0;

	public static final int ERROR = -1;

	public static final int EMPTY = -2;

	private int code = ERROR;

	private String msg;

	private HttpUriRequest mHttpUriRequest;

	private JSONObject obj;
	
	private boolean isUseCache;
	private  String fileName;

	public BaseParser() {
	}
	
	public BaseParser(String json) {
		parse(json);
	}
	
	public void parse(String json) {
		tryParse(json);
		if (!isSave()) {
			return;
		}
		if (SUCCESS == getCode() && !isUseCache) {
			FileUtil.saveFile(IDoGooApp.getContext(), getFileName(),
					json.getBytes());
		}
	}

	private void tryParse(String json) {
		if (TextUtils.isEmpty(json)) {
			setCode(ERROR);
			return;
		}
		try {
			// Android 4.0及以上都已经在内部类中处理,Android 2.2至Android 2.3.3未作处理
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
				if (json != null && json.startsWith("\ufeff")) {
					json = json.substring(1);
				}
			}
			obj = new JSONObject(json);
			if (obj.has("status")) {
				parseStatus(obj.optJSONObject("status"));
			}
		} catch (JSONException e) {
			setCode(ERROR);
			e.printStackTrace();
			return;
		}
	}
	
	public String readCache() {
		if (!isSave()) {
			return null;
		}
		isUseCache = true;
		byte[] buffer = FileUtil
				.readFile(IDoGooApp.getContext(), getFileName());
		if (null != buffer) {
			return new String(buffer);
		}
		return null;
	}
	
	public void resetCache(String json) {
		FileUtil.saveFile(IDoGooApp.getContext(), getFileName(),
				json.getBytes());
	}
	
	/**
	 * isCacheValid:缓存是否有效. <br/>
	 * 
	 * @author hushuan
	 * @param validTime
	 *            有效时长，单位秒，0：不做时间限制
	 * @return
	 */
	public boolean isCacheValid(long validTime) {
		if (!isSave()) {
			return false;
		}
		File file = new File(IDoGooApp.getContext().getFilesDir(),
				getFileName());
		if (!file.exists()) {
			return false;
		}
		if (validTime > 0) {
			long durationTime = System.currentTimeMillis()
					- file.lastModified();
			durationTime /= 1000;
			if (durationTime > validTime) {
				return false;
			}
		}
		byte[] buffer = FileUtil
				.readFile(IDoGooApp.getContext(), getFileName());
		return null != buffer;
	}
	
	public void clearFile() {
		FileUtil.deleteFile(new File(IDoGooApp.getContext().getFilesDir(), getFileName()));
	}
	

	private void parseStatus(JSONObject obj) {
		this.code = obj.optInt("code");
		this.msg = obj.optString("msg");
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public void setHttpUriRequest(HttpUriRequest mHttpUriRequest) {
		this.mHttpUriRequest = mHttpUriRequest;
	}

	public HttpUriRequest getHttpUriRequest() {
		return mHttpUriRequest;
	}
	
	public boolean isSave() {
		return !TextUtils.isEmpty(getFileName());
	}

	public boolean isUseCache() {
		return isUseCache;
	}

	public void setUseCache(boolean isUseCache) {
		this.isUseCache = isUseCache;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public JSONObject getObj() {
		return obj;
	}

}
