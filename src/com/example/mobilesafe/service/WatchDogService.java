package com.example.mobilesafe.service;

import java.util.List;

import com.example.mobilesafe.activity.EnterPwdActivity;
import com.example.mobilesafe.db.dao.AppLockDao;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.preference.PreferenceManager.OnActivityResultListener;

public class WatchDogService extends Service {

	private ActivityManager activityManager;
	private AppLockDao dao;
	private Thread thread;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// 获取最新的任务栈
		// 获取任务栈顶端activity

		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

		dao = new AppLockDao(this);

		startWatDog();
		receiver = new WatchDogReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(receiver, intentFilter);
		
	}

	class WatchDogReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			PowerManager pm = (PowerManager) context
					.getSystemService(POWER_SERVICE);
			if (pm.isScreenOn()) {
				startWatDog();
			} else {
				flag = false;
			}
		}
	}

	private boolean flag = false;
	private WatchDogReceiver receiver;
	

	private void startWatDog() {
		thread = new Thread() {
			public void run() {
				String tempappString = null;
				boolean finded = false;
				flag = true;
				while (flag) {
					// 获取当前运行的任务栈,返回任务栈list
					List<RunningTaskInfo> tasks = activityManager
							.getRunningTasks(10);
					// 获取最新开启的任务栈
					RunningTaskInfo taskInfo = tasks.get(0);
					// 获取栈顶的activity
					String packagename = taskInfo.topActivity.getPackageName();
					
					SystemClock.sleep(30);
					
					
					if (dao.find(packagename)) {
						finded = true;
						tempappString = packagename; // 把包名暂存起来,用于判断
						boolean delete = dao.delete(tempappString);
						
						// 服务中跳转,一定要设置flags
						
						Intent intent = new Intent(WatchDogService.this,
								EnterPwdActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
						intent.putExtra("packageName", packagename);
						
						startActivity(intent);
						
						
					} else if (packagename.equals("com.example.mobilesafe")) {
						// 在输入密码的时候不作处理
						
						
						finded = true;
					} else if (finded && packagename.equals(tempappString)) {
						// 输入密码正确的话,会进入到这里
					
			
						finded = true;
					}else if (packagename.equals("com.huawei.android.launcher")) {
						System.out.println("---- huawei ");
					}
					
					else {
						// 这里代表打开了别的应用
						if (finded) {
							// 把暂存的包名加回到数据库中
							dao.add(tempappString);
							tempappString = null;
							finded = false;
						}
					}
				}
			};
		};
		thread.start();
	}

	@Override
	public void onDestroy() {
		flag = false;
		unregisterReceiver(receiver);
		super.onDestroy();

	}
	
	

}
