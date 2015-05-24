package com.rgy.entity;

import android.graphics.drawable.Drawable;

public class AppInfo {
	
	private String appName;
	private String packageName;
	private String versionName;
	private int versionCode;
	private Drawable appIcon;
	
	public AppInfo(){
		
	}
	
	//
	public void setAppName(String appName){
		this.appName = appName;
	}
	
	public String getAppName(){
		return appName;
	}
	//
	public void setPackageName(String packageName){
		this.packageName = packageName;
	}
	
	public String getPackageName(){
		return packageName;
	}
	//
	public void setVersionName(String versionName){
		this.versionName = versionName;
	}
	
	public String getVersionName(){
		return versionName;
	}
	//
	public void setVersionCode(int versionCode){
		this.versionCode = versionCode;
	}
	
	public int getVersionCode(){
		return versionCode;
	}
	//
	public void setAppIcon(Drawable appIcon){
		this.appIcon = appIcon;
	}
	
	public Drawable getAppIcon(){
		return appIcon;
	}
	
}













