package com.example.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirusDao {
	private final static String PATH = "data/data/com.example.mobilesafe/files/antivirus.db";
	private static SQLiteDatabase db;

	
	public static String isAntivirusDao(String md5) {
		//获得病毒数据库
				db = SQLiteDatabase.openDatabase(PATH, null,
						SQLiteDatabase.OPEN_READONLY);
		String desc = null;		
		Cursor cursor = db.rawQuery("select desc from datable where md5 = ?", new String[]{md5});
		if(cursor.moveToNext()){
			desc = cursor.getString(0);
		}
		return desc;
	}
	
	
	
}
