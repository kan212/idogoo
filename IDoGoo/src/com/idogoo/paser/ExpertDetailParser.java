package com.idogoo.paser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 专家详情解析
 * @author kan212
 *
 */
public class ExpertDetailParser extends BaseParser {

	private UserInfo mUserInfo;
	private List<Tags> mTagList = new ArrayList<Tags>();
	private int meet_num;
	private Scores mScores;
	private List<TopicParser> mTopicList = new ArrayList<TopicParser>();

	@Override
	public void parse(String json) {
		super.parse(json);
		if (getCode() == BaseParser.SUCCESS) {
			parseData(getObj().optJSONObject("data"));
		}
	}

	private void parseData(JSONObject obj) {
		if (null == obj) {
			return;
		}
		if (obj.has("info")) {
			mUserInfo = new UserInfo(obj.optJSONObject("info"));
		}
		if (obj.has("tags")) {
			parseTagData(obj.optJSONArray("tags"));
		}
		this.meet_num = obj.optInt("meet_num");
		if (obj.has("scores")) {
			mScores = new Scores(obj.optJSONObject("scores"));
		}

		if (obj.has("topic")) {
			parseTopicList(obj.optJSONArray("topic"));
		}
	}

	private void parseTopicList(JSONArray array) {
		if (null == array) {
			return;
		}
		mTopicList = new ArrayList<TopicParser>();
		for (int i = 0; i < array.length(); i++) {
			TopicParser item = new TopicParser(array.optJSONObject(i));
			if (null != item) {
				mTopicList.add(item);
			}
		}
	}

	private void parseTagData(JSONArray array) {
		if (null == array) {
			return;
		}
		mTagList = new ArrayList<Tags>();
		for (int i = 0; i < array.length(); i++) {
			Tags tags = new Tags(array.optJSONObject(i));
			if (null != tags) {
				mTagList.add(tags);
			}
		}
	}

	public List<TopicParser> getTopicList() {
		return mTopicList;
	}

	public Scores getScores() {
		return mScores;
	}

	public List<Tags> getTagList() {
		return mTagList;
	}

	public UserInfo getUserInfo() {
		return mUserInfo;
	}

	public int getMeet_num() {
		return meet_num;
	}

	public class Scores {

		private String expertId;
		private String likeTimes;
		private String score;
		private String times;
		private int rank;
		private String id;
		private long addTime;
		private String status;

		public Scores(JSONObject obj) {
			if (null == obj) {
				return;
			}
			this.expertId = obj.optString("expertId");
			this.likeTimes = obj.optString("likeTimes");
			this.times = obj.optString("times");
			this.score = obj.optString("score");
			this.rank = obj.optInt("rank");
			this.id = obj.optString("id");
			this.addTime = obj.optLong("addTime");
			this.status = obj.optString("status");
		}

		public String getExpertId() {
			return expertId;
		}

		public String getLikeTimes() {
			return likeTimes;
		}

		public String getScore() {
			return score;
		}

		public String getTimes() {
			return times;
		}

		public int getRank() {
			return rank;
		}

		public String getId() {
			return id;
		}

		public long getAddTime() {
			return addTime;
		}

		public String getStatus() {
			return status;
		}

	}

}
