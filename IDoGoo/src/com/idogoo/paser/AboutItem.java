package com.idogoo.paser;

import java.io.Serializable;

import org.json.JSONObject;

public class AboutItem extends BaseParser implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String user_id;
	private String specialist_id;
	private String topic_id;
	private String topic_title;
	private String price;
	private long date;
	private String time_zone;
	private String location_by_user;
	private int comment_id;
	private int evaluate_id;
	private int evaluate_score;
	private int if_delete;
	private int if_paied;
	private int if_accepted;
	private int if_appeal;
	private int if_refund;
	private int if_success;
	private int if_complete;
	private String completeTimestamp;
	private String createTimestamp;
	private int status;
	private String status_cn;
	private String specialist_name;
	private String self_url;
	private String pic;
	
	private String specialist_pic;
	private String user_pic;

	private JSONObject mJson;
	
	public AboutItem() {

	}
	
	public AboutItem(JSONObject obj) {
		parseData(obj);
	}


	@Override
	public void parse(String json) {
		super.parse(json);
		if (SUCCESS == getCode()) {
			parseData(getObj().optJSONObject("data"));
		}
	}

	public AboutItem parseData(JSONObject obj) {
		if (null == obj) {
			return null;
		}
		this.mJson = obj;
		
		this.id = obj.optString("id");
		this.date = obj.optLong("date");
		this.location_by_user = obj.optString("location_by_user");
		this.time_zone = obj.optString("time_zone");
		this.price = obj.optString("price");
		this.topic_title = obj.optString("topic_title");
		this.user_id = obj.optString("user_id");
		this.topic_id = obj.optString("topic_id");
		this.specialist_id = obj.optString("specialist_id");
		this.comment_id = obj.optInt("comment_id");
		this.evaluate_id = obj.optInt("evaluate_id");
		this.evaluate_score = obj.optInt("evaluate_score");
		this.if_delete = obj.optInt("if_delete");
		this.if_paied = obj.optInt("if_paied");
		this.if_accepted = obj.optInt("if_accepted");
		this.if_appeal = obj.optInt("if_appeal");
		this.if_refund = obj.optInt("if_refund");
		this.if_success = obj.optInt("if_success");
		this.if_complete = obj.optInt("if_complete");
		this.completeTimestamp = obj.optString("completeTimestamp");
		this.createTimestamp = obj.optString("createTimestamp");
		this.status = obj.optInt("status");
		this.status_cn = obj.optString("status_cn");
		this.specialist_name = obj.optString("specialist_name");
		this.self_url = obj.optString("self_url");
		this.pic = obj.optString("pic");
		this.specialist_pic = obj.optString("specialist_pic");
		this.user_pic = obj.optString("user_pic");
		return this;
	}
	
	public String getSpecialist_pic() {
		return specialist_pic;
	}
	
	public String getUser_pic() {
		return user_pic;
	}

	public String getId() {
		return id;
	}

	public String getUser_id() {
		return user_id;
	}

	public String getSpecialist_id() {
		return specialist_id;
	}

	public String getTopic_id() {
		return topic_id;
	}

	public String getTopic_title() {
		return topic_title;
	}

	public String getPrice() {
		return price;
	}

	public long getDate() {
		return date;
	}

	public String getTime_zone() {
		return time_zone;
	}

	public String getLocation_by_user() {
		return location_by_user;
	}

	public int getComment_id() {
		return comment_id;
	}

	public int getEvaluate_id() {
		return evaluate_id;
	}

	public int getEvaluate_score() {
		return evaluate_score;
	}

	public int getIf_delete() {
		return if_delete;
	}

	public int getIf_paied() {
		return if_paied;
	}

	public int getIf_accepted() {
		return if_accepted;
	}

	public int getIf_appeal() {
		return if_appeal;
	}

	public int getIf_refund() {
		return if_refund;
	}

	public int getIf_success() {
		return if_success;
	}

	public int getIf_complete() {
		return if_complete;
	}

	public String getCompleteTimestamp() {
		return completeTimestamp;
	}

	public String getCreateTimestamp() {
		return createTimestamp;
	}

	public int getStatus() {
		return status;
	}

	public String getStatus_cn() {
		return status_cn;
	}

	public String getSpecialist_name() {
		return specialist_name;
	}

	public String getSelf_url() {
		return self_url;
	}

	public String getPic() {
		return pic;
	}

	@Override
	public String toString() {
		return mJson.toString();
	}
	
}