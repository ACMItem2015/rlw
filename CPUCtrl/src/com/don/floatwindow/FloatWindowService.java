package com.don.floatwindow;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.don.floatwindow.utils.MyWindowManager;
import com.rgy.Tools.AppUtils;
import com.rgy.Tools.DeepCpuData;
import com.rgy.Tools.MyConfig;
import com.rgy.Tools.SmallUtils;
import com.rgy.setcpu.AppAllActivity;
import com.rgy.setcpu.CustomService;
import com.rgy.setcpu.MainActivity;
import com.rgy.setcpu.ModelUserActivity;
import com.rgy.setcpu.MyApplication;
import com.rgy.setcpu.R;
import com.rgy.setcpu.SmartyService;

public class FloatWindowService extends Service {

	/**
	 * 用于在线程中创建或移除悬浮窗。
	 */
	private Handler handler = new Handler();

	/**
	 * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
	 */
	private Timer timer;

	/**
	 * 记录小悬浮窗的宽度
	 */
	public static int viewWidth;

	public static String resultCPU;

	MyApplication myApp;

	ImageView moren, jisu, shengdian, yonghu, zidingyi, zhineng;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initView();

		// 开启定时器，每隔1秒刷新一次
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 1000);
		}

		initHideBigWindow();

		return super.onStartCommand(intent, flags, startId);
	}

	// 大悬浮窗初始化隐藏
	public void initHideBigWindow() {
		MyWindowManager.removeBigWindow(getApplicationContext());
	}

	// 获取数据
	public void initView() {
		myApp = (MyApplication) getApplication();
		MyWindowManager.createBigWindow(getApplicationContext());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Service被终止的同时也停止定时器继续运行
		timer.cancel();
		timer = null;
	}

	// 计时器内部类
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
							//
							
						}
						if (MyWindowManager.bigWindow != null) {
							// 添加大悬浮窗事件
							initBigWindowEvent();
						}
					}
				});
			}
		}

	}

	// 添加大悬浮窗事件
	public void initBigWindowEvent() {
		// 极速
		jisu = (ImageView) MyWindowManager.bigWindow.findViewById(R.id.jisu);
		jisu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyWindowManager.removeBigWindow(getApplicationContext());
				new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_PERFORMANCE);
			}
		});

		// 默认
		moren = (ImageView) MyWindowManager.bigWindow.findViewById(R.id.moren);
		moren.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyWindowManager.removeBigWindow(getApplicationContext());
				new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_DEFAULT);
			}
		});

		// 省电
		shengdian = (ImageView) MyWindowManager.bigWindow
				.findViewById(R.id.shengdian);
		shengdian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyWindowManager.removeBigWindow(getApplicationContext());
				new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_POWERSAVE);
			}
		});

		// 用户模式
		yonghu = (ImageView) MyWindowManager.bigWindow
				.findViewById(R.id.yonghu);
		yonghu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyWindowManager.removeBigWindow(getApplicationContext());
				Intent intent = new Intent(FloatWindowService.this,
						ModelUserActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		// 智能模式
		zhineng = (ImageView) MyWindowManager.bigWindow
				.findViewById(R.id.zhineng);
		zhineng.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyWindowManager.removeBigWindow(getApplicationContext());
				//
				String smartySwitch = myApp.getSmartySwitch();
				if (smartySwitch.equals("Stop")) {
					Intent intent = new Intent(FloatWindowService.this,
							SmartyService.class);
					startService(intent);
					Toast.makeText(FloatWindowService.this, "智能模式已启动",
							Toast.LENGTH_SHORT).show();
					myApp.setSmartySwitch("Start");
					// 判断MainActivity是否存在
					if (MainActivity.tv_showmodel != null) {
						MainActivity.tv_showmodel.setText("当前模式:\n" + "智能模式");
					}
					// 如果此时自定义模式还处于开启状态，则关闭它
					String customSwitch = myApp.getCustomSwitch();
					if (customSwitch.equals("Start")) {
						Intent customIntent = new Intent(
								FloatWindowService.this, CustomService.class);
						stopService(customIntent);
						//
						myApp.setCustomSwitch("Stop");
						// btn_custom.setImageResource(R.drawable.shut);
					}
				} else {
					Intent intent = new Intent(FloatWindowService.this,
							SmartyService.class);
					stopService(intent);
					Toast.makeText(FloatWindowService.this, "智能模式已停止",
							Toast.LENGTH_SHORT).show();
					myApp.setSmartySwitch("Stop");
					// btn_smart.setImageResource(R.drawable.shut);
					new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_DEFAULT);
				}
			}
		});

		// 自定义模式
		zidingyi = (ImageView) MyWindowManager.bigWindow
				.findViewById(R.id.zidingyi);
		zidingyi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyWindowManager.removeBigWindow(getApplicationContext());
				Intent intent = new Intent(FloatWindowService.this,
						AppAllActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
	}

	class AsyncTaskSetModel extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			String model = params[0];
			long max = Long.parseLong(myApp.getcpuMaxFreq());
			long min = Long.parseLong(myApp.getcpuMinFreq());
			boolean flag1 = DeepCpuData.setMaxCpuFreq(max);
			boolean flag2 = DeepCpuData.setMinCpuFreq(min);
			boolean flag = false;
			if (model.equals(MyConfig.CPUMODEL_POWERSAVE)) {
				flag = DeepCpuData.setCpuGovernor("powersave");
			} else if (model.equals(MyConfig.CPUMODEL_PERFORMANCE)) {
				flag = DeepCpuData.setCpuGovernor("performance");
			} else if (model.equals(MyConfig.CPUMODEL_DEFAULT)) {
				flag = DeepCpuData.setCpuGovernor("userspace");
			}

			String result = "";
			if (flag && flag1 && flag2) {
				result = "ok" + model;
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("")) {
				Toast.makeText(FloatWindowService.this, "请先获取root权限",
						Toast.LENGTH_SHORT).show();
				return;
			}
			String state = result.substring(0, 2);
			String model = result.substring(2);
			String cpuModel_str = SmallUtils.convertCpuModelName(model);
			if (state.equals("ok")) {
				myApp.setCpuModel(model);
				Toast.makeText(FloatWindowService.this, cpuModel_str + "设置成功",
						Toast.LENGTH_SHORT).show();
				// 判断MainActivity是否存在
				if (MainActivity.tv_showmodel != null) {
					MainActivity.tv_showmodel.setText("当前模式:\n" + cpuModel_str);
				}
				// 关闭 智能模式 和 自定义模式
				if (AppUtils.isServiceWork(FloatWindowService.this,
						MyConfig.SERVICENAME_SMARTY)) {
					Intent intent = new Intent(FloatWindowService.this,
							SmartyService.class);
					stopService(intent);
				} else if (AppUtils.isServiceWork(FloatWindowService.this,
						MyConfig.SERVICENAME_CUSTOM)) {
					Intent intent = new Intent(FloatWindowService.this,
							CustomService.class);
					stopService(intent);
				}
			} else {
				Toast.makeText(FloatWindowService.this, cpuModel_str + "设置失败",
						Toast.LENGTH_SHORT).show();
			}
			// //
			super.onPostExecute(result);
		}
	}

}
