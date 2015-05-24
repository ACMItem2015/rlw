package com.rgy.Tools;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import com.rgy.entity.AppInfo;

public class AppUtils {

	/**
	 * 获取当前最新的appInfo列表
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<AppInfo> getLatestAppList(Context context) {
		ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
		List<PackageInfo> packages = context.getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			// 非系统应用和应用本身
			boolean flag1 = (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0;
			boolean flag2 = !packageInfo.packageName.equals("com.rgy.setcpu");
			if (flag1 && flag2) {

				String appName = packageInfo.applicationInfo.loadLabel(
						context.getPackageManager()).toString();
				String packageName = packageInfo.packageName;
				String versionName = packageInfo.versionName;
				int versionCode = packageInfo.versionCode;
				Drawable appIcon = packageInfo.applicationInfo.loadIcon(context
						.getPackageManager());

				AppInfo appInfo = new AppInfo();
				appInfo.setAppName(appName);
				appInfo.setPackageName(packageName);
				appInfo.setVersionName(versionName);
				appInfo.setVersionCode(versionCode);
				appInfo.setAppIcon(appIcon);

				appList.add(appInfo);
			}
		}
		return appList;
	}

	/**
	 * 移除已经卸载的应用
	 * 
	 * @param appPackageList
	 */
	public static void removeUnloadedApp(ArrayList<String> allAppPackageList, String fileName) {
		ArrayList<String> appPackageList = FileUtils.readListFromFile(fileName);
		if (appPackageList == null) {
			return;
		}
		for (int i = 0; i < appPackageList.size(); i++) {
			String package_name = appPackageList.get(i);
			// 如果最新总列表中不包含此项，则将之从默认列表中删除，以达到更新的目的
			if (!allAppPackageList.contains(package_name)) {
				appPackageList.remove(i);
			}
		}
		FileUtils.writeListToFile(appPackageList, fileName);
	}

	/**
	 * 在默认列表中添加新增的应用
	 * 
	 * @param context
	 */
	public static void addLoadedAppToDefaultList(ArrayList<String> allAppPackageList) {
		
		ArrayList<String> appDefaultList = FileUtils.readListFromFile(
				MyConfig.FILENAME_APP_DEFAULT);
		ArrayList<String> appPerformanceList = FileUtils.readListFromFile(
				MyConfig.FILENAME_APP_PERFORMANCE);
		ArrayList<String> appPowersaveList = FileUtils.readListFromFile(
				MyConfig.FILENAME_APP_POWERSAVE);
		
		if(appDefaultList==null||appPerformanceList==null||appPowersaveList==null){
			return;
		}

		ArrayList<String> allList = new ArrayList<String>();
		allList.addAll(appDefaultList);
		allList.addAll(appPerformanceList);
		allList.addAll(appPowersaveList);

		allAppPackageList.removeAll(allList);
		ArrayList<String> newAppList = allAppPackageList;
		System.out.println("新增的应用个数---》"+newAppList.size());
		if(newAppList!=null){
			appDefaultList.addAll(newAppList);
			FileUtils.writeListToFile(appDefaultList, MyConfig.FILENAME_APP_DEFAULT);
		}
	}

	/**
	 * 到对应文件中添加应用
	 * 
	 * @param context
	 * @param fileName
	 * @param packageName
	 */
	public static void addAppToListFile(String fileName,String packageName) {
		ArrayList<String> appPackageList = FileUtils.readListFromFile(fileName);
		if (appPackageList == null) {
			appPackageList = new ArrayList<String>();
		}
		appPackageList.add(packageName);
		System.out.println(packageName + "已经加入" + fileName);
		FileUtils.writeListToFile(appPackageList, fileName);
	}

	/**
	 * 从对应文件中删除应用
	 * 
	 * @param context
	 * @param fileName
	 * @param packageName
	 */
	public static void removeAppFromListFile(String fileName,String packageName) {
		ArrayList<String> appPackageList = FileUtils.readListFromFile(
				fileName);
		if (appPackageList != null) {
			appPackageList.remove(packageName);
			System.out.println(packageName + "已经从" + fileName + "移除!");
		}
		FileUtils.writeListToFile(appPackageList, fileName);
	}

	/**
	 * 获取当前所运行app的包名
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentPk(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService("activity");
		String packageName = am.getRunningTasks(1).get(0).topActivity
				.getPackageName();
		return packageName;
	}

	/**
	 * 判断某个服务是否正在工作
	 * 
	 * @param mContext
	 * @param serviceName
	 * @return
	 */
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(40);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}

}
