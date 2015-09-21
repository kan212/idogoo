package com.idogoo.paser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class SpeListParser extends BaseParser {

	private List<SpeListItem> list = new ArrayList<SpeListItem>();
	private int num = 0;
	@Override
	public void parse(String json) {
		super.parse(json);
		if (getCode() == BaseParser.SUCCESS) {
			parseSpeList(getObj().optJSONArray("data"));
			num = getObj().optInt("num");
		}
	}
	
	public int getNum() {
		return num;
	}

	private void parseSpeList(JSONArray array) {
		if (null == array || array.length() <= 0) {
			setCode(EMPTY);
			return;
		}
		list = new ArrayList<SpeListItem>();
		for (int i = 0; i < array.length(); i++) {
			SpeListItem item = new SpeListItem(array.optJSONObject(i));
			if (null != item) {
				list.add(item);
			}
		}
	}

	public List<SpeListItem> getList() {
		return list;
	}

}
