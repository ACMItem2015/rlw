package com.wyx.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryChangeReceiver extends BroadcastReceiver {

	private static int level;
	private static int temperature;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
			
			int status = intent.getIntExtra("status", 0);
			int health = intent.getIntExtra("health", 0);
			boolean present = intent.getBooleanExtra("present", false);
			level = intent.getIntExtra("level", 0);
			int scale = intent.getIntExtra("scale", 0);
			int icon_small = intent.getIntExtra("icon-small", 0);
			int plugged = intent.getIntExtra("plugged", 0);
			int voltage = intent.getIntExtra("voltage", 0);
			temperature = intent.getIntExtra("temperature", 0);
			String technology = intent.getStringExtra("technology");

			
			String statusString = "";
			switch (status) {
			case BatteryManager.BATTERY_STATUS_UNKNOWN:
				statusString = "unknown";
				break;
			case BatteryManager.BATTERY_STATUS_CHARGING:
				statusString = "charging";
				break;
			case BatteryManager.BATTERY_STATUS_DISCHARGING:
				statusString = "discharging";
				break;
			case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
				statusString = "not charging";
				break;
			case BatteryManager.BATTERY_STATUS_FULL:
				statusString = "full";
				break;
			}

			String healthString = "";
			switch (health) {
			case BatteryManager.BATTERY_HEALTH_UNKNOWN:
				healthString = "unknown";
				break;
			case BatteryManager.BATTERY_HEALTH_GOOD:
				healthString = "good";
				break;
			case BatteryManager.BATTERY_HEALTH_OVERHEAT:
				healthString = "overheat";
				break;
			case BatteryManager.BATTERY_HEALTH_DEAD:
				healthString = "dead";
				break;
			case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
				healthString = "voltage";
				break;
			case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
				healthString = "unspecified failure";
				break;
			}

			String acString = "";
			switch (plugged) {
			case BatteryManager.BATTERY_PLUGGED_AC:
				acString = "plugged ac";
				break;
			case BatteryManager.BATTERY_PLUGGED_USB:
				acString = "plugged usb";
				break;
			}
		}
	}
	public static int getLevel(){
		//Log.w("LiveWallpaper.Engine", "电池电量："+level);
		return level;
	}

	public static int getTemperature(){
		//Log.w("LiveWallpaper.Engine", "电池温度："+temperature);
		return temperature;
	}
}
