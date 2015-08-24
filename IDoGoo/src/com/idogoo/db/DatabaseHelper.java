package com.idogoo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author shuanzi
 * @version Apr 7, 20135:20:13 PM
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper helper;
	private SQLiteDatabase db;
	private final static String DateBase_Name = "IDoGoo.db";
	/**
	 * @author kan212 <br>
	 *         2.1是5 ， 2.2 是6 ， 2.3 是7 2.4是8; 2.5是9 ;2.7是10; 3.0为11; 3.1为12 <br>
	 *         下一版增加到11(或者更换数据库)
	 */
	private static int DateBase_Version = 12;

	public DatabaseHelper(Context context) {

		super(context, DateBase_Name, null, DateBase_Version);
	}

	public static DatabaseHelper getInstance(Context context) {
		if (helper == null) {
			helper = new DatabaseHelper(context);
		}
		return helper;
	}

	public SQLiteDatabase getDatabase() {
		db = helper.getWritableDatabase();
		return db;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		AbsTable.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + "mainhot");
		db.execSQL("DROP TABLE IF EXISTS " + "match_status");
		db.execSQL("DROP TABLE IF EXISTS " + "usersinfo");
		db.execSQL("DROP TABLE IF EXISTS " + "matchs");
		this.onCreate(db);
	}
}
