package com.don.floatwindow;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开机自动启动悬浮窗服务
 * 
 * @author Don
 * 
 */
public class AutomaticStartup extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		intent = new Intent(context, FloatWindowService.class);
		context.startService(intent);
	}

}
