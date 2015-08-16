package com.example.mobilesafe.service;

import com.example.mobilesafe.R;
import com.example.mobilesafe.db.dao.AddressDao;
import com.example.mobilesafe.receiver.OutCallReceiver;
import com.example.mobilesafe.utils.ToastUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallReceiver receiver;
	private WindowManager mWM;
	private View view;
	private SharedPreferences mPref;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);

		receiver = new OutCallReceiver();
		registerReceiver(receiver, filter);// 动态注册广播

	}

	class MyListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				String address = AddressDao.getAddress(incomingNumber); // 等到来电号码的归属地
				showToast(address);
				// ToastUtils.showToast(AddressService.this, address);

				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if (mWM != null && view != null) {
					mWM.removeView(view);
					view = null;
				}
				break;
			default:
				break;
			}
		}
	}

	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData(); // 获取去电号码

			String address = AddressDao.getAddress(number);
			// ToastUtils.showToast(AddressService.this, address);
			showToast(address);
		}

	}

	private void showToast(String text) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		params.gravity = Gravity.LEFT + Gravity.TOP;
		params.setTitle("Toast");
		
		int dragX = mPref.getInt("drag_x", 0);
		int dragY = mPref.getInt("drag_y", 0);
		
		params.x = dragX;
		params.y = dragY;
		
		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };

		int style = mPref.getInt("address_style", 0);

		view = View.inflate(this, R.layout.toast_address, null);
		
		view.setBackgroundResource(bgs[style]);//根据存储的样式更新背景
		
		TextView tvnumber = (TextView) view.findViewById(R.id.tv_address);

		tvnumber.setText(text);

		mWM.addView(view, params);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		unregisterReceiver(receiver);
	}

}
