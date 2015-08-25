package com.idogoo.paser;

import org.json.JSONObject;

/**
 * 专家信息接口
 * @author kan212
 *
 */
public class UserInfo {

	private String user_id;
	private String truename;
	private String photo;
	private String phone;
	private String your_city;
	private String email;
	private String occupation;
	private String education;
	private String major;
	private String summary;
	private String topic;
	
	public UserInfo(JSONObject obj) {
		if(null == obj) {
			return;
		}
		this.user_id = obj.optString("user_id");
		this.truename = obj.optString("truename");
		this.photo = obj.optString("photo");
		this.phone = obj.optString("phone");
		this.your_city = obj.optString("your_city");
		this.email = obj.optString("email");
		this.occupation = obj.optString("occupation");
		this.education = obj.optString("education");
		this.major = obj.optString("major");
		this.summary = obj.optString("summary");
		this.topic = obj.optString("topic");
	}

	public String getUser_id() {
		return user_id;
	}

	public String getTruename() {
		return truename;
	}

	public String getPhoto() {
		return photo;
	}

	public String getPhone() {
		return phone;
	}

	public String getYour_city() {
		return your_city;
	}

	public String getEmail() {
		return email;
	}

	public String getOccupation() {
		return occupation;
	}

	public String getEducation() {
		return education;
	}

	public String getMajor() {
		return major;
	}

	public String getSummary() {
		return summary;
	}

	public String getTopic() {
		return topic;
	}
	
	
	
}
