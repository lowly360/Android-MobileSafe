package com.example.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ContactActivity extends Activity {
	private ListView lvList;
	private ArrayList<HashMap<String, String>> readContact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_contacts);
		
		lvList = (ListView) findViewById(R.id.lv_list);

		readContact = readcontacts();
		//readcontacts();
		lvList.setAdapter(new SimpleAdapter(this, readContact,
				R.layout.contact_list_item, new String[] { "name", "phone" },
				new int[]{R.id.tv_name,R.id.tv_phone}));
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = readContact.get(position).get("phone");
				Intent intent = new Intent();
				intent.putExtra("phone", phone);
				setResult(Activity.RESULT_OK,intent);//将数据放在intent中，返回给上一个页面
				finish();
				
			}
		});
		
	}
	
	private ArrayList<HashMap<String, String>> readcontacts() {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		Uri rawContactsUri = Uri
				.parse("content://com.android.contacts/raw_contacts");

		Uri dataUri = Uri.parse("content://com.android.contacts/data");

		Cursor rawContactsCursor = getContentResolver().query(rawContactsUri,
				new String[] { "contact_id" }, null, null, null);

		if (rawContactsCursor != null) {
			while (rawContactsCursor.moveToNext()) {
				String contactId = rawContactsCursor.getString(0);

				Cursor dataCursor = getContentResolver().query(dataUri,
						new String[] { "data1", "mimetype" }, "contact_id = ?",
						new String[] { contactId }, null);
				System.out.println("----->"+contactId);
	
				if (dataCursor != null) {
					HashMap<String, String> map = new HashMap<String, String>();
					while (dataCursor.moveToNext()) {
						String data1 = dataCursor.getString(0);
						String mimetype = dataCursor.getString(1);
						if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
							map.put("phone", data1);
						} else if ("vnd.android.cursor.item/name"
								.equals(mimetype)) {
							map.put("name", data1);
						}
					}
					list.add(map);
					dataCursor.close();
				}

			}
			rawContactsCursor.close();
		}
		return list;
	}
}
