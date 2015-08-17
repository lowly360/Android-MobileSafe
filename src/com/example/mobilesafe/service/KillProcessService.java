package com.example.mobilesafe.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Time;

public class KillProcessService extends Service {

	private Timer timer;
	private TimerTask task;
	private LockScreenReceiver receiver;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		receiver = new LockScreenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);

		timer = new Timer();

		task = new TimerTask() {

			@Override
			public void run() {
				
				
			}
		};

		// 定时调度
		timer.schedule(task, 1000, 1000);

	}

	class LockScreenReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ActivityManager service = (ActivityManager) context
					.getSystemService(context.ACTIVITY_SERVICE);
			
			List<RunningAppProcessInfo> runningAppProcesses = service.getRunningAppProcesses();
			
			for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
				service.killBackgroundProcesses(runningAppProcessInfo.processName);
			}

		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(receiver);
		receiver = null;
		timer.cancel();
	}

}
