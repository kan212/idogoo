package com.idogoo.paser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 评论的解析
 * @author kan212
 *
 */
public class CommentParser extends BaseParser {

	private List<CommentItem> mList = new ArrayList<CommentItem>();

	@Override
	public void parse(String json) {
		super.parse(json);
		if (getCode() == BaseParser.SUCCESS) {
			parseData(getObj().optJSONArray("data"));
		}
	}

	private void parseData(JSONArray array) {
		if (null == array) {
			return;
		}
		mList = new ArrayList<CommentParser.CommentItem>();
		for (int i = 0; i < array.length(); i++) {
			CommentItem item = new CommentItem(array.optJSONObject(i));
			if (null != item) {
				mList.add(item);
			}
		}
	}

	public List<CommentItem> getList() {
		return mList;
	}

	public class CommentItem {

		private int resId;
		private int sourceId;
		private String url;
		private String content;
		private String uid;
		private int score;
		private String audit;
		private int expertId;
		private int receiver;
		private String tags;
		private int id;
		private long addTime;
		private String status;
		private User mUser;
		private String title;

		public CommentItem(JSONObject obj) {

			if (null == obj) {
				return;
			}
			this.resId = obj.optInt("resId");
			this.sourceId = obj.optInt("sourceId");
			this.url = obj.optString("url");
			this.content = obj.optString("content");
			this.uid = obj.optString("uid");
			this.score = obj.optInt("score");
			this.audit = obj.optString("audit");
			this.expertId = obj.optInt("expertId");
			this.receiver = obj.optInt("receiver");
			this.tags = obj.optString("tags");
			this.id = obj.optInt("id");
			this.addTime = obj.optLong("addTime");
			this.status = obj.optString("status");
			this.title = obj.optString("title");
			
			if (obj.has("user")) {
				mUser = new User(obj.optJSONObject("user"));
			}
		}
		
		public String getTitle() {
			return title;
		}

		public int getResId() {
			return resId;
		}

		public int getSourceId() {
			return sourceId;
		}

		public String getUrl() {
			return url;
		}

		public String getContent() {
			return content;
		}

		public String getUid() {
			return uid;
		}

		public int getScore() {
			return score;
		}

		public String getAudit() {
			return audit;
		}

		public int getExpertId() {
			return expertId;
		}

		public int getReceiver() {
			return receiver;
		}

		public String getTags() {
			return tags;
		}

		public int getId() {
			return id;
		}

		public long getAddTime() {
			return addTime;
		}

		public String getStatus() {
			return status;
		}

		public User getUser() {
			return mUser;
		}

	}

	public class User {

		private String uid;
		private String name;
		private String picUrl;
		private String role;

		public User(JSONObject obj) {
			if (null == obj) {
				return;
			}

			this.uid = obj.optString("uid");
			this.name = obj.optString("name");
			this.picUrl = obj.optString("picUrl");
			this.role = obj.optString("role");
		}

		public String getUid() {
			return uid;
		}

		public String getName() {
			return name;
		}

		public String getPicUrl() {
			return picUrl;
		}

		public String getRole() {
			return role;
		}

	}

}
