package com.example.mobilesafe.activity;

import com.example.mobilesafe.R;
import com.example.mobilesafe.service.KillProcessService;
import com.example.mobilesafe.utils.ServiceStatusUtils;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ProcessMangerSetting extends Activity {

	private CheckBox cb_process_cheak;
	private CheckBox cb_ProcessSetting_kill;

	private SharedPreferences mPre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
	}

	private void initUI() {
		setContentView(R.layout.activity_process_setting);
		mPre = getSharedPreferences("config", MODE_PRIVATE);

		boolean showsys = mPre.getBoolean("showsys", false);
		boolean iskillprocess = mPre.getBoolean("iskillprocess", false);
		cb_process_cheak = (CheckBox) findViewById(R.id.cb_ProcessSetting);

		cb_ProcessSetting_kill = (CheckBox) findViewById(R.id.cb_ProcessSetting_kill);

		cb_process_cheak.setChecked(showsys);

		cb_process_cheak
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						mPre.edit().putBoolean("showsys", isChecked).commit();
					}
				});

		

		cb_ProcessSetting_kill
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							mPre.edit().putBoolean("iskillprocess", isChecked)
									.commit();
							if (ServiceStatusUtils
									.isServiceRuning(ProcessMangerSetting.this,
											"com.example.mobilesafe.service.KillProcessService")) {

							} else {
								startService(new Intent(
										ProcessMangerSetting.this,
										KillProcessService.class));
							}

						} else {
							mPre.edit().putBoolean("iskillprocess", isChecked)
									.commit();
							if (ServiceStatusUtils
									.isServiceRuning(ProcessMangerSetting.this,
											"com.example.mobilesafe.service.KillProcessService")) {
								stopService(new Intent(
										ProcessMangerSetting.this,
										KillProcessService.class));
							}
						}
					}
				});
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (ServiceStatusUtils
				.isServiceRuning(ProcessMangerSetting.this,
						"com.example.mobilesafe.service.KillProcessService")) {
			cb_ProcessSetting_kill.setChecked(true);
		}else {
			cb_ProcessSetting_kill.setChecked(false);
		}
	
	}
}
