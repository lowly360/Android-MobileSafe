package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * 手机防盗
 * 
 * @author Administrator
 * 
 */
public class LostFindActivity extends Activity {
	private SharedPreferences mPrefs;
	private String tv_safephone;
	private ImageView ivProtect;
	private TextView tvSafePhone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		mPrefs = getSharedPreferences("config", MODE_PRIVATE);
		
		
		boolean configed = mPrefs.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_lost_find);
			tvSafePhone = (TextView) findViewById(R.id.tv_safephone);
			tv_safephone = mPrefs.getString("phone", "无");
			tvSafePhone.setText(tv_safephone);
			
			ivProtect = (ImageView) findViewById(R.id.iv_protect);
			boolean protect = mPrefs.getBoolean("protect", false);
			if(protect){
				ivProtect.setImageResource(R.drawable.lock);
			}else{
				ivProtect.setImageResource(R.drawable.unlock);
			}
			
			
		}else{
			startActivity(new Intent(this,Setup1Activity.class));
			finish();
		}
	}
	
	public void reEnter(View v){
		startActivity(new Intent(this,Setup1Activity.class));
		finish();
	}
}
