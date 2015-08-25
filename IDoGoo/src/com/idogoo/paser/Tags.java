package com.idogoo.paser;

import org.json.JSONObject;

public class Tags {
	private String tag;
	private int num;
	
	public Tags(String tag) {
		this.tag = tag;
	}

	public Tags(JSONObject obj) {
		if (null == obj) {
			return;
		}
		this.tag = obj.optString("tag");
		this.num = obj.optInt("num");
	}

	public String getTag() {
		return tag;
	}

	public int getNum() {
		return num;
	}
}