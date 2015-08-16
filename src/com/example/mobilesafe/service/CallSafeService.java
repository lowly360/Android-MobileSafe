package com.example.mobilesafe.service;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.example.mobilesafe.db.dao.BalckNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class CallSafeService extends Service {
	private BalckNumberDao dao;
	private CallSafeRecevier callSafeRecevier;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new BalckNumberDao(this);
		callSafeRecevier = new CallSafeRecevier();
		IntentFilter filter = new IntentFilter(
				"android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(callSafeRecevier, filter);

		// 来电拦截
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		MyPhoneListener mylistener = new MyPhoneListener();
		tm.listen(mylistener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class MyPhoneListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String mode = dao.query(incomingNumber);
				if (mode == null) {
					break;
				}
				if (mode.equals("1") || mode.equals("2")) {
					Uri uri = Uri.parse("content://call_log/calls");
					getContentResolver()
							.registerContentObserver(
									uri,
									true,
									new MyContentObserver(new Handler(),
											incomingNumber));
					endCall();
				}
				break;

			default:
				break;
			}
		}
	}

	private class MyContentObserver extends ContentObserver {
		private String incomingNumber;

		public MyContentObserver(Handler handler, String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			getContentResolver().unregisterContentObserver(this);
			deleteCallLog(incomingNumber);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (callSafeRecevier != null) {
			unregisterReceiver(callSafeRecevier);
		}
	}

	/**
	 * 删掉电话号码
	 * 
	 * @param incomingNumber
	 */
	public void deleteCallLog(String incomingNumber) {
		Uri uri = Uri.parse("content://call_log/calls");
		getContentResolver().delete(uri, "number = ?",
				new String[] { incomingNumber });
	}

	/**
	 * 挂断电话
	 */
	public void endCall() {
		try {
			Class<?> class1 = getClassLoader().loadClass(
					"android.os.ServiceManager");
			Method method = class1
					.getDeclaredMethod("getService", String.class);
			IBinder invoke = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
			ITelephony iTelephony = ITelephony.Stub.asInterface(invoke);
			iTelephony.endCall();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CallSafeRecevier extends BroadcastReceiver {

		public CallSafeRecevier() {
			super();
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] objects = (Object[]) intent.getExtras().get("pdus");
			for (Object object : objects) {
				// 超出的话,会分为多条短信发送,所以是一个数组,因为我们的短信指令很短,所以for循环只执行一次
				SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
				String originatingAddress = message.getOriginatingAddress();// 短信来源号码
				// String messageBody = message.getMessageBody();// 短信内容
				// 通过短信的电话号码查询拦截的模式

				System.out.println("---->" + originatingAddress);
				String mode = dao.query(originatingAddress);
				System.out.println("---->" + mode);
				if (mode == null) {
					return;
				}
				if (mode.equals("1")) {
					abortBroadcast();
				} else if (mode.equals("3")) {
					abortBroadcast();
				}
			}
		}
	}
}
