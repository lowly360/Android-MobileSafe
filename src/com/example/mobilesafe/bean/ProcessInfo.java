package com.example.mobilesafe.bean;

import android.graphics.drawable.Drawable;

public class ProcessInfo {
	
	private Drawable icon;
	private String packageName;
	private String name;
	private long size;
	private boolean isUers;
	private boolean ischeck;
	
	
	
	public ProcessInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ProcessInfo(Drawable icon, String packageName, String name,
			long size, boolean isUers, boolean ischeck) {
		super();
		this.icon = icon;
		this.packageName = packageName;
		this.name = name;
		this.size = size;
		this.isUers = isUers;
		this.ischeck = ischeck;
	}
	public final Drawable getIcon() {
		return icon;
	}
	public final void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public final String getPackageName() {
		return packageName;
	}
	public final void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final long getSize() {
		return size;
	}
	public final void setSize(long size) {
		this.size = size;
	}
	public final boolean isUers() {
		return isUers;
	}
	public final void setUers(boolean isUers) {
		this.isUers = isUers;
	}
	public final boolean isIscheck() {
		return ischeck;
	}
	public final void setIscheck(boolean ischeck) {
		this.ischeck = ischeck;
	}

	
	
}
