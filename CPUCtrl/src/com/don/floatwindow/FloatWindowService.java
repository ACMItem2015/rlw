package com.don.floatwindow;

import java.util.Timer;
import java.util.TimerTask;

import com.rgy.setcpu.MyApplication;
import com.rgy.setcpu.R;
import com.don.floatwindow.utils.MyWindowManager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.TextView;

public class FloatWindowService extends Service {

	/**
	 * 用于在线程中创建或移除悬浮窗。
	 */
	private Handler handler = new Handler();

	/**
	 * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
	 */
	private Timer timer;

	public static String resultCPU;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 开启定时器，每隔1秒刷新一次
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 1000);
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Service被终止的同时也停止定时器继续运行
		timer.cancel();
		timer = null;
	}

	class RefreshTask extends TimerTask {

		/**
		 * 在任何app上运行的悬浮窗
		 */
		@Override
		public void run() {
			// 没有悬浮窗显示，则创建悬浮窗。
			if (!MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager
								.createSmallWindow(getApplicationContext());
					}
				});
			}

			// 有悬浮窗显示，则更新内存数据。
			else if (MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						resultCPU = MyApplication.cpuCurUsage;
						if (MyWindowManager.smallWindow != null) {
							TextView percentView = (TextView) MyWindowManager.smallWindow
									.findViewById(R.id.percent);
							percentView.setText(resultCPU);
						}
					}
				});
			}
		}

	}

}
