package com.idogoo.paser;

import org.json.JSONObject;

public class TopicParser extends BaseParser {

	public String topic_id;
	public String user_id;
	public String specialist_id;
	public int type;
	public String title;
	public String evaluate_score;
	public String price;
	public int period;
	public String summary;
	public int if_stop;
	public int if_show;
	public int if_delete;
	
	
	private String id;
	private String photo;
	private String truename;
	
	public TopicParser() {
		
	}

	@Override
	public void parse(String json) {
		super.parse(json);
		if (SUCCESS == getCode()) {
			parseData(getObj().optJSONObject("data"));
		}
	}
	
	public TopicParser parseData(JSONObject obj) {
		if (null == obj) {
			return null;
		}
		this.topic_id = obj.optString("topic_id");
		this.id = obj.optString("id");
		this.user_id = obj.optString("user_id");
		this.specialist_id = obj.optString("specialist_id");
		this.type = obj.optInt("type");
		this.title = obj.optString("title");
		this.evaluate_score = obj.optString("evaluate_score");
		this.price = obj.optString("price");
		this.period = obj.optInt("period");
		this.summary = obj.optString("summary");
		this.if_stop = obj.optInt("if_stop");
		this.if_show = obj.optInt("if_show");
		this.if_delete = obj.optInt("if_delete");
		this.photo = obj.optString("photo");
		this.truename = obj.optString("truename");
		return this;
	}

	public TopicParser(JSONObject obj) {
		if (null == obj) {
			return;
		}
		this.id = obj.optString("id");
		this.topic_id = obj.optString("topic_id");
		this.user_id = obj.optString("user_id");
		this.specialist_id = obj.optString("specialist_id");
		this.type = obj.optInt("type");
		this.title = obj.optString("title");
		this.evaluate_score = obj.optString("evaluate_score");
		this.price = obj.optString("price");
		this.period = obj.optInt("period");
		this.summary = obj.optString("summary");
		this.if_stop = obj.optInt("if_stop");
		this.if_show = obj.optInt("if_show");
		this.if_delete = obj.optInt("if_delete");
	}
	
	public String getTruename() {
		return truename;
	}
	
	public String getPhoto() {
		return photo;
	}
	
	public String getId() {
		return id;
	}

	public String getUser_id() {
		return user_id;
	}

	public String getTopic_id() {
		return topic_id;
	}

	public String getSpecialist_id() {
		return specialist_id;
	}

	public int getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public String getEvaluate_score() {
		return evaluate_score;
	}

	public String getPrice() {
		return price;
	}

	public int getPeriod() {
		return period;
	}

	public String getSummary() {
		return summary;
	}

	public int getIf_stop() {
		return if_stop;
	}

	public int getIf_show() {
		return if_show;
	}

	public int getIf_delete() {
		return if_delete;
	}

}
