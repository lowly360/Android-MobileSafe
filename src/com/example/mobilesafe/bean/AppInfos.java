package com.example.mobilesafe.bean;


import android.graphics.drawable.Drawable;

public class AppInfos {
	private Drawable app_icon;
	private String app_name;
	private boolean isrom;
	private long app_size;
	private boolean userApp;
	private String apkPackName;
	
	
	
	public AppInfos() {
		super();
		
	}
	@Override
	public String toString() {
		return "AppInfos [app_icon=" + app_icon + ", app_name=" + app_name
				+ ", isrom=" + isrom + ", app_size=" + app_size + ", userApp="
				+ userApp + ", apkPackName=" + apkPackName + "]";
	}
	public String getApkPackName() {
		return apkPackName;
	}
	public void setApkPackName(String apkPackName) {
		this.apkPackName = apkPackName;
	}
	public boolean isUserApp() {
		return userApp;
	}
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	
	public Drawable getApp_icon() {
		return app_icon;
	}
	public void setApp_icon(Drawable app_icon) {
		this.app_icon = app_icon;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public boolean getIsrom() {
		return isrom;
	}
	public void setIsrom(boolean isrom) {
		this.isrom = isrom;
	}
	public long getApp_size() {
		return app_size;
	}
	public void setApp_size(long app_size) {
		this.app_size = app_size;
	}
	
	
}
