package com.example.mobilesafe.utils;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;

public class SmsUtils {
	public static boolean backSms(Context context) {
		System.out.println("----------------------------");

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {


			try {

				File file = new File(Environment.getExternalStorageDirectory(),
						"backup.xml");
				FileOutputStream os = new FileOutputStream(file);
				// 得到序列化器
				// 在android中所有有关xml的解析都是pull解析
				XmlSerializer serializer = Xml.newSerializer();
				//
				serializer.setOutput(os, "utf-8");
				// true 表示文件独立
				serializer.startDocument("utf-8", true);

				serializer.startTag(null, "smss");
				

				ContentResolver contentResolver = context.getContentResolver();

				Uri uri = Uri.parse("content://sms/");
				// type =1 接受短信 type =2 发送短信
				Cursor cursor = contentResolver.query(uri, new String[] {
						"address", "date", "type", "body" }, null, null, null);

				while (cursor.moveToNext()) {

					serializer.startTag(null, "sms");

					serializer.startTag(null, "address");
					serializer.text(cursor.getString(0));
					serializer.endTag(null, "address");

					serializer.startTag(null, "date");
					serializer.text(cursor.getString(1));
					serializer.endTag(null, "date");

					serializer.startTag(null, "type");
					serializer.text(cursor.getString(2));
					serializer.endTag(null, "type");

					serializer.startTag(null, "body");
					serializer.text(cursor.getString(3));
					serializer.endTag(null, "body");

					serializer.endTag(null, "sms");

				}

				serializer.endTag(null, "smss");

				serializer.endDocument();
				
				cursor.close();
				
				os.flush();
				
				os.close();
				
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}
}
