package com.idogoo.paser;

import org.json.JSONObject;

public class AccessKeyParser extends BaseParser{

	private String access_key;
	
	@Override
	public void parse(String json) {
		super.parse(json);
		if(getCode() == BaseParser.SUCCESS) {
			parseAccessKey(getObj().optJSONObject("data"));
		}
	}

	private void parseAccessKey(JSONObject obj) {
		if(null == obj) {
			setCode(EMPTY);
			return;
		}
		access_key = obj.optString("access_key");
	}
	
	public String getAccess_key() {
		return access_key;
	}
	
}
