package com.idogoo.paser;

import org.json.JSONObject;

public class VolidTimeParser extends BaseParser{

	private long volidTime;
	
	@Override
	public void parse(String json) {
		super.parse(json);
		if(getCode() == BaseParser.SUCCESS) {
			parseData(getObj().optJSONObject("data"));
		}
	}
	
	
	private void parseData(JSONObject obj) {
		if(null == obj) {
			return;
		}
		this.volidTime = obj.optLong("ttl");
	}


	public long getVolidTime() {
		return volidTime;
	}

}
