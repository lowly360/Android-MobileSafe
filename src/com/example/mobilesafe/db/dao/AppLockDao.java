package com.example.mobilesafe.db.dao;

import java.security.PublicKey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppLockDao {
	private AppLockOpenHelper helper;

	public AppLockDao(Context context){
		helper = new AppLockOpenHelper(context);	
	}
	
	
	public boolean add(String packageName){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packagename", packageName);
		db.insert("info", null, values);	
		db.close();
		return true;	
	}
	
	public boolean delete(String packageName) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("info", "packagename = ?", new String[]{packageName});
		
		db.close();
		return true;	
	}
	
	public boolean find(String packagename) {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query( "info	", null	, "packagename = ?", new String[]{packagename}, null, null, null);
		if(cursor.moveToNext()){
			cursor.close();
			db.close();
			return true;
		}else {
			cursor.close();
			db.close();
			return false;
		}
	
	}

}
