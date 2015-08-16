package com.example.mobilesafe.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R.drawable;
import com.example.mobilesafe.bean.AppInfos;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

public class GetAppInfos {

	public static List<AppInfos> getAppInfos(Context context) {
		List<AppInfos> infos = new ArrayList<AppInfos>();

		PackageManager manager = context.getPackageManager();
		List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
		for (PackageInfo packageInfo : installedPackages) {
			AppInfos appInfos = new AppInfos();

			// 获取应用程序的图标
			Drawable loadIcon = packageInfo.applicationInfo.loadIcon(manager);
			appInfos.setApp_icon(loadIcon);
			// 应用的名字
			String loadLabel = (String) packageInfo.applicationInfo
					.loadLabel(manager);
			appInfos.setApp_name(loadLabel);
			// 应用的包名
			String packageName = packageInfo.applicationInfo.packageName;
			appInfos.setApkPackName(packageName);
			// 获取到apk资源的路径
			String sourceDir = packageInfo.applicationInfo.sourceDir;
			File file = new File(sourceDir);
			// apk的大小
			long file_size = file.length();
			appInfos.setApp_size(file_size);

			int flags = packageInfo.applicationInfo.flags;

			if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
				// 系统app
				appInfos.setUserApp(false);
			} else {
				// 用户app
				appInfos.setUserApp(true);
			}

			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
				appInfos.setIsrom(false);
			}else{
				appInfos.setIsrom(true);
			}

			infos.add(appInfos);
		}

		return infos;
	}

}
