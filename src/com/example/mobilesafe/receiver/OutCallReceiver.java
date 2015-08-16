package com.example.mobilesafe.receiver;

import com.example.mobilesafe.db.dao.AddressDao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 监听去电的接收者
 * @author Lowly
 *  android.permission.PROCESS_OUTGOING_CALLS
 */
public class OutCallReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {		// TODO Auto-generated method stub
		String number = getResultData(); //获取去电号码
		
		String address = AddressDao.getAddress(number);
		
	}

}
