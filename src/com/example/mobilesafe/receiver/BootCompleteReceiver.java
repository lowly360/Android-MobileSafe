package com.example.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
/**
 * 监听手机开机的广播
 * @author Administrator
 *
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		boolean protect = sp.getBoolean("protect", false);
		if(protect){
			String sim = sp.getString("sim", null);
			
			if(!TextUtils.isEmpty(sim)){
				//获取当前的sim卡
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				String currentSim = tm.getSimSerialNumber();
				
				if(sim.equals(currentSim)){
					
				}else{
					String phone = sp.getString("phone", "");
					
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phone, null, "sim card changed!", null, null);
				}
				
			}
		}
		
		
	}

}
