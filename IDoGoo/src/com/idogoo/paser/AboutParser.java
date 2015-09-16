package com.idogoo.paser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.idogoo.utils.Config;

public class AboutParser extends BaseParser {

	List<AboutItem> list = new ArrayList<AboutItem>();
	List<AboutItem> onGoingList = new ArrayList<AboutItem>();
	List<AboutItem> readyList = new ArrayList<AboutItem>();

	@Override
	public void parse(String json) {
		super.parse(json);
		if (SUCCESS == getCode()) {
			parseData(getObj().optJSONArray("data"));
		}
	}
	
	public AboutParser() {
		
	}
	
	public AboutParser(String json) {
		parse(json);
	}

	private void parseData(JSONArray array) {
		if (null == array || array.length() <= 0) {
			setCode(EMPTY);
			return;
		}
		for (int i = 0; i < array.length(); i++) {
			AboutItem item = new AboutItem(array.optJSONObject(i));
			if (null != item) {
				switch (item.getStatus()) {
				case 4:
				case 1:
				case 7:
					readyList.add(item);
					break;
				default:
					onGoingList.add(item);
					break;
				}
			}
		}
	}

	public List<AboutItem> getList() {
		return list;
	}
	
	public List<AboutItem> getOnGoingList() {
		return onGoingList;
	}

	public List<AboutItem> getReadyList() {
		return readyList;
	}
	
}
