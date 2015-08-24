package com.idogoo.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.idogoo.app.IDoGooApp;

/**
 * TODO
 * @version Apr 7, 20135:38:27 PM
 * 
 */
public abstract class AbsTable {

	// 数据库字段属性
	static final String FIELDTYPE_INTEGERPRIMARYKEY_AUTOINCREMENT = " INTEGER PRIMARY KEY AUTOINCREMENT, ";
	static final String FIELDTYPE_INTEGERPRIMARYKEY = " INTEGER PRIMARY KEY, ";
	static final String FIELDTYPE_INTEGER = " INTEGER, ";
	static final String FIELDTYPE_TEXT = " TEXT, ";
	static final String FIELDTYPE_BLOB = " BLOB, ";

	// 数据库操作
	static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";

	private final static AbsTable[] TABLES = {new UserInfoDB()};

	public abstract String getTableName();

	public abstract String getFieldInfo();

	protected static void onCreate(SQLiteDatabase db) {
		for (AbsTable table : TABLES) {
			db.execSQL(table.getSQLofCreate());
		}
	}

	public String getSQLofCreate() {
		StringBuilder sb = new StringBuilder();
		sb.append(CREATE_TABLE).append(getTableName());
		sb.append('(');
		String fieldinfo = getFieldInfo();
		if (TextUtils.isEmpty(fieldinfo)) {
			throw new RuntimeException(getTableName()
					+ " table must have one field");
		}
		sb.append(getFieldInfo());
		sb.delete(sb.length() - 2, sb.length()).append(')');
		return sb.toString();
	}

	protected static boolean isEmpty(String tableName) {
		boolean isEmpty = false;
		SQLiteDatabase db = IDoGooApp.getDatabaseHelper().getDatabase();
		String sql = "select * from " + tableName;
		Cursor cur = db.rawQuery(sql, null);
		if (null == cur || cur.getCount() == 0) {
			isEmpty = true;
		}
		if (cur != null) {
			cur.close();
		}
		return isEmpty;
	}

}
