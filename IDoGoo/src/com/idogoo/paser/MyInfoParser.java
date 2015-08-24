package com.idogoo.paser;

import org.json.JSONObject;

public class MyInfoParser extends BaseParser {

	private String id;
	private String nick_name;
	private String birthday;
	private String pic;
	private String phone;
	private String city;
	private String summary;
	private String sexy;
	private String role;
	private String comment_id;

	@Override
	public void parse(String json) {
		super.parse(json);
		if (SUCCESS == getCode()) {
			parseData(getObj().optJSONObject("data"));
		}
	}

	private void parseData(JSONObject obj) {
		if (null == obj) {
			setCode(EMPTY);
			return;
		}

		this.id = obj.optString("id");
		this.nick_name = obj.optString("nick_name");
		this.sexy = obj.optString("sexy");
		this.birthday = obj.optString("birthday");
		this.pic = obj.optString("pic");
		this.phone = obj.optString("phone");
		this.city = obj.optString("city");
		this.summary = obj.optString("summary");
		this.role = obj.optString("role");
		this.comment_id = obj.optString("comment_id");

	}

	public String getId() {
		return id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getPic() {
		return pic;
	}

	public String getPhone() {
		return phone;
	}

	public String getCity() {
		return city;
	}

	public String getSummary() {
		return summary;
	}

	public String getSexy() {
		return sexy;
	}

	public String getRole() {
		return role;
	}

	public String getComment_id() {
		return comment_id;
	}

}
