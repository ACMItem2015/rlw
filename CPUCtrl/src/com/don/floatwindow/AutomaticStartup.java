package com.don.floatwindow;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * �����Զ���������������
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
