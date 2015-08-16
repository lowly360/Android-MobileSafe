package com.example.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 服务状态检测
 * @author Lowly
 *
 */
public class ServiceStatusUtils {
	/**
	 * 检测服务是否在运行
	 * @return
	 */
	public static boolean isServiceRuning(Context ctx,String serviceName){
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();
			
			if(className.equals(serviceName)){
				return true;
			}
		}
		
		return false;
	}
}
