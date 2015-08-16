package com.example.mobilesafe.logic;

import java.util.ArrayList;
import java.util.List;



import com.example.mobilesafe.R;
import com.example.mobilesafe.bean.ProcessInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class GetProcessInfo {
	public static int GetProessNumber(Context context) {
		ActivityManager systemService = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> processes = systemService
				.getRunningAppProcesses();

		return processes.size();
	}

	@SuppressWarnings("null")
	public static long GetFreeMemory(Context context) {

		ActivityManager systemService = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		systemService.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	public static List<ProcessInfo> GetProcessList(Context context) {
		List<ProcessInfo> processlist = new ArrayList<ProcessInfo>();

		PackageManager packageManager = context.getPackageManager();

		ActivityManager systemService = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = systemService
				.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {

			String packageName = runningAppProcessInfo.processName;

			ProcessInfo processinfo = new ProcessInfo();

			PackageInfo packageInfo;
			processinfo.setPackageName(packageName);
			try {
				packageInfo = packageManager.getPackageInfo(packageName, 0);
				Drawable icon = packageInfo.applicationInfo
						.loadIcon(packageManager);
				String name = (String) packageInfo.applicationInfo
						.loadLabel(packageManager);
				processinfo.setIcon(icon);
				processinfo.setName(name);

				int flags = packageInfo.applicationInfo.flags;

				if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
					// 系统app
					processinfo.setUers(false);
				} else {
					// 用户app
					processinfo.setUers(true);
				}

			} catch (Exception e) {				
				e.printStackTrace();
				processinfo.setName(packageName);
				processinfo.setIcon(context.getResources().getDrawable(
						R.drawable.ic_launcher));
				
			}

			int pid = runningAppProcessInfo.pid;
			int[] pids = new int[] { pid };
			android.os.Debug.MemoryInfo[] info = systemService
					.getProcessMemoryInfo(pids);
			long size = info[0].dalvikPrivateDirty*1024;
			processinfo.setSize(size);
			processinfo.setIscheck(false);

			processlist.add(processinfo);
		}

		return processlist;
	}

	@SuppressWarnings("null")
	public static long GetTotalMemory(Context context) {

		ActivityManager systemService = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		systemService.getMemoryInfo(outInfo);
		return outInfo.totalMem;
	}
	
	public static void KillProcess(Context context,List<ProcessInfo> list){
		ActivityManager systemService = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ProcessInfo processInfo : list) {
			if(processInfo.isIscheck()){
				systemService.killBackgroundProcesses(processInfo.getPackageName());
			}			
		};	
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
