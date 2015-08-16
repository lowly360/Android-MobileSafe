package com.example.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.mobilesafe.R;
import com.example.mobilesafe.utils.ToastUtils;
import com.example.mobilesafe.view.SettingItemView;


public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView sivSim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		sivSim = (SettingItemView) findViewById(R.id.siv_sim);
		
		String sim = mPref.getString("sim", null);
		
		if(!TextUtils.isEmpty(sim)){
			sivSim.setChecked(true);
		}else{
			sivSim.setChecked(false);
		}
		
		sivSim.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(sivSim.isChecked()){
					sivSim.setChecked(false);
					mPref.edit().remove("sim").commit();
				}else{
					sivSim.setChecked(true);
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();
					mPref.edit().putString("sim", simSerialNumber).commit();			
				}
			}
		});
		
	}
	
	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_previous_in,
				R.anim.tran_previous_out);
	}

	@Override
	public void showNextPage() {
		//
		String sim = mPref.getString("sim", null);
		if(TextUtils.isEmpty(sim)){
			ToastUtils.showToast(this,"必须绑定SIM卡！");		
			return;
		}
		
		
		startActivity(new Intent(this, Setup3Activity.class));
		finish();
		overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
	}

}
