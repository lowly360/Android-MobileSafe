package com.example.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.bean.BlackNumberInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 黑名单数据库
 * 
 * @author Administrator
 * 
 */
public class BalckNumberDao {
	private BlackNumberOpenHelper helper;

	public BalckNumberDao(Context context) {
		helper = new BlackNumberOpenHelper(context);
	}

	/**
	 * 
	 * @param number
	 *            电话号码
	 * @param mode
	 *            拦截模式
	 */
	public boolean add(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("number", number);
		contentValues.put("mode", mode);

		long rawid = db.insert("blacknumber", null, contentValues);
		db.close();
		if (rawid != -1) {
			return true;
		}
		return false;
	}

	/**
	 * 通过电话号码来删除
	 * 
	 * @param number
	 */
	public boolean delete(String number) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int rawnum = db.delete("blacknumber", "number = ?",
				new String[] { number });
		db.close();
		if (rawnum != 0) {
			return true;
		}
		return false;
	}

	public boolean changeNumberMode(String number, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("mode", mode);
		int update = db.update("blacknumber", contentValues, "number = ?",
				new String[] { number });
		if (update != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 返回该号码的拦截模式
	 * 
	 * @param number
	 */
	public String query(String number) {
		String mode = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[] { "mode" }, "number = ?",
				new String[] { number }, null, null, null);
		
		if(cursor.moveToNext()){
			mode = cursor.getString(0);
		}
		db.close();
		cursor.close();	
		return mode;
	}
	
	public List<BlackNumberInfo> findAll(){
		List<BlackNumberInfo> arrayList = new ArrayList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("blacknumber", new String[]{"number","mode"}, null, null, null, null,null);
		while(cursor.moveToNext()){
			BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
			blackNumberInfo.setNumber(cursor.getString(0));
			blackNumberInfo.setMode(cursor.getString(1));
			arrayList.add(blackNumberInfo);
		}
		cursor.close();
		db.close();	
		return arrayList;		
	}
}
