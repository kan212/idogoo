package com.idogoo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.idogoo.app.IDoGooApp;

public class UserInfoDB extends AbsTable{

	public static final String TABLE_NAME = "userInfo";
	
	public static final String NAME = "name";
	public static final String USER_NAME = "user_name";
	public static final String ID = "id";
	public static final String PIC = "pic";
	public static final String UTYPE = "uType";
	public static final String ROLE = "role";
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}

	@Override
	public String getFieldInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append(ID).append(FIELDTYPE_TEXT);
		sb.append(NAME).append(FIELDTYPE_TEXT);
		sb.append(USER_NAME).append(FIELDTYPE_TEXT);
		sb.append(PIC).append(FIELDTYPE_TEXT);
		sb.append(UTYPE).append(FIELDTYPE_TEXT);
		sb.append(ROLE).append(FIELDTYPE_TEXT);
		return sb.toString();
	}
	
	public static boolean isEmpty() {
		return AbsTable.isEmpty(TABLE_NAME);
	}

	public static long insert(SQLiteDatabase db, ContentValues values) {
		return db.insert(TABLE_NAME, null, values);
	}

	public static long detele(SQLiteDatabase db) {
		return db.delete(TABLE_NAME, null, null);
	}
	
	public static Cursor getAllData() {
		SQLiteDatabase db = IDoGooApp.getDatabaseHelper().getDatabase();
		String sql = "select * from " + TABLE_NAME;
		return db.rawQuery(sql, null);
	}

}
