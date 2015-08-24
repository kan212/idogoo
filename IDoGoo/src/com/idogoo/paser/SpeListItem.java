package com.idogoo.paser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SpeListItem {
	
	private UserInfo mUserInfo;
	private List<TopicParser> tipList = new ArrayList<TopicParser>();

	public SpeListItem(JSONObject obj) {
		if (null == obj) {
			return;
		}
		if (obj.has("user_info")) {
			mUserInfo = new UserInfo(obj.optJSONObject("user_info"));
		}

		if (obj.has("type")) {
			parseUserType(obj.optJSONArray("type"));
		}
	}

	private void parseUserType(JSONArray array) {
		if (null == array || array.length() <= 0) {
			return;
		}
		tipList = new ArrayList<TopicParser>();
		for (int i = 0; i < array.length(); i++) {
			TopicParser tips = new TopicParser(array.optJSONObject(i));
			if (null != tips) {
				tipList.add(tips);
			}
		}
	}

	public List<TopicParser> getTipList() {
		return tipList;
	}

	public boolean hasTipList() {
		return (tipList == null || tipList.isEmpty()) ? false : true;
	}
	
	public UserInfo getUserInfo() {
		return mUserInfo;
	}

}
