package com.example.mobilesafe.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	private final static String PATH = "data/data/com.example.mobilesafe/files/address.db";

	public static String getAddress(String number) {
		String address = "未知号码";
		SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);
		// 手机号码特点 1(3,4,5,6,7,8,)(9位数字)
		// 正则表达式
		// ^1[3-8]\d{9}$

		if (number.matches("^1[3-8]\\d{9}$")) {// 匹配手机号码
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[] { number.substring(0, 7) });

			if (cursor.moveToNext()) {
				address = cursor.getString(0);
			}
			cursor.close();
		} else {
			switch (number.length()) {
			case 3:
				address = "报警电话";
				break;
			case 4:
				address = "模拟器号码";
				break;
			case 5:
				address = "客服号码";
				break;
			case 7:
			case 8:
				address = "本地固定号码";
				break;
			default:
				if (number.startsWith("0") && number.length() > 10) {// 有可能是长途电话
					Cursor cursor = database.rawQuery("select location from data2 where area = ?",
							new String[] { number.substring(1, 4) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					}else{
						cursor.close();
						cursor = database.rawQuery("select location from data2 where area = ?",
								new String[] { number.substring(1, 3) });
						if (cursor.moveToNext()) {
							address = cursor.getString(0);
						}
						cursor.close();
					}
				}
				break;
			}

		}
		database.close();
		return address;
	}
}
