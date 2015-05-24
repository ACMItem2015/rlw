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
	 * �������߳��д������Ƴ���������
	 */
	private Handler handler = new Handler();

	/**
	 * ��ʱ������ʱ���м�⵱ǰӦ�ô��������Ƴ���������
	 */
	private Timer timer;

	public static String resultCPU;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// ������ʱ����ÿ��1��ˢ��һ��
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 1000);
		}

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Service����ֹ��ͬʱҲֹͣ��ʱ����������
		timer.cancel();
		timer = null;
	}

	class RefreshTask extends TimerTask {

		/**
		 * ���κ�app�����е�������
		 */
		@Override
		public void run() {
			// û����������ʾ���򴴽���������
			if (!MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager
								.createSmallWindow(getApplicationContext());
					}
				});
			}

			// ����������ʾ��������ڴ����ݡ�
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
