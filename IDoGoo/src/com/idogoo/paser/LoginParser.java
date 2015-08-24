package com.idogoo.paser;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.idogoo.db.UserInfoDB;

public class LoginParser extends BaseParser {

	private String name;
	private String user_name;
	private String id;
	private String pic;
	private String uType;
	private String role;

	public LoginParser() {

	}

	@Override
	public String getFileName() {
		return "login";
	}

	@Override
	public void parse(String json) {
		super.parse(json);
		if (BaseParser.SUCCESS == getCode()) {
			parseData(getObj().optJSONObject("data"));
		}
	}

	private void parseData(JSONObject obj) {
		if (null == obj) {
			setCode(EMPTY);
			return;
		}
		this.name = obj.optString("name");
		this.user_name = obj.optString("user_name");
		this.id = obj.optString("id");
		this.pic = obj.optString("pic");
		this.uType = obj.optString("uType");
		this.role = obj.optString("role");
	}

	public LoginParser(Cursor cursor) {
		this.id = getString(cursor, UserInfoDB.ID);
		this.name = getString(cursor, UserInfoDB.NAME);
		this.pic = getString(cursor, UserInfoDB.PIC);
		this.role = getString(cursor, UserInfoDB.ROLE);
		this.user_name = getString(cursor, UserInfoDB.USER_NAME);
		this.uType = getString(cursor, UserInfoDB.UTYPE);
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put(UserInfoDB.ID, getId());
		values.put(UserInfoDB.NAME, getName());
		values.put(UserInfoDB.USER_NAME, getUser_name());
		values.put(UserInfoDB.UTYPE, getuType());
		values.put(UserInfoDB.ROLE, getRole());
		values.put(UserInfoDB.PIC, getPic());
		return values;
	}

	private String getString(Cursor cur, String columnName) {
		return cur.getString(cur.getColumnIndex(columnName));
	}

	public String getName() {
		return name;
	}

	public String getUser_name() {
		return user_name;
	}

	public String getId() {
		return id;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPic() {
		return pic;
	}

	public String getuType() {
		return uType;
	}

	public String getRole() {
		return role;
	}

}
