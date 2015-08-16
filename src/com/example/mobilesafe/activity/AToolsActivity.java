package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.SmsUtils;
import com.example.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;

public class AToolsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	public void numberAdressQuery(View v){
		Intent intent = new Intent(this,AdressQueryActivity.class);
		startActivity(intent);
	}
	
	public void BackUpSms(View v){
		
		new Thread(){
			@Override
			public void run() {
				boolean result = SmsUtils.backSms(AToolsActivity.this);
				if(result){
					Looper.prepare();
					ToastUtils.showToast(AToolsActivity.this, "备份成功");
					Looper.loop();
				}else{
					Looper.prepare();
					ToastUtils.showToast(AToolsActivity.this, "备份失败");
					Looper.loop();
				}
				super.run();
			}
		}.start();
		
	}
}
