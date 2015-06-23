package com.don.floatwindow.view;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.don.floatwindow.FloatWindowService;
import com.don.floatwindow.utils.MyWindowManager;
import com.rgy.Tools.DeepCpuData;
import com.rgy.Tools.MyConfig;
import com.rgy.Tools.SmallUtils;
import com.rgy.setcpu.CustomService;
import com.rgy.setcpu.MainActivity;
import com.rgy.setcpu.MyApplication;
import com.rgy.setcpu.R;
import com.rgy.setcpu.SmartyService;

public class FloatWindowSmallView extends LinearLayout {

	/**
	 * 记录小悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录小悬浮窗的高度
	 */
	public static int viewHeight;

	/**
	 * 记录系统状态栏的高度
	 */
	private static int statusBarHeight;

	/**
	 * 用于更新小悬浮窗的位置和设置桌面动画
	 */
	private WindowManager windowManager;

	/**
	 * 小悬浮窗的参数
	 */
	private WindowManager.LayoutParams mParams;

	/**
	 * 记录当前手指位置在屏幕上的横坐标值
	 */
	private float xInScreen;

	/**
	 * 记录当前手指位置在屏幕上的纵坐标值
	 */
	private float yInScreen;

	/**
	 * 记录手指按下时在屏幕上的横坐标的值
	 */
	private float xDownInScreen;

	/**
	 * 记录手指按下时在屏幕上的纵坐标的值
	 */
	private float yDownInScreen;

	/**
	 * 记录手指按下时在小悬浮窗的View上的横坐标的值
	 */
	private float xInView;

	/**
	 * 记录手指按下时在小悬浮窗的View上的纵坐标的值
	 */
	private float yInView;
	private View view;
	private TextView percentView;
	
	//rgy
	
	MyApplication myApp;
	
	String TAG = "FloatWindowSmallView";
	
	Context context;

	public FloatWindowSmallView(Context context) {
		super(context);
		this.context = context;
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);

		initView();
		percentView.setText(FloatWindowService.resultCPU);
		//
		myApp = (MyApplication) this.context.getApplicationContext();
	}

	private void initView() {
		view = findViewById(R.id.small_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		percentView = (TextView) findViewById(R.id.percent);

	}

	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			break;

		// ********************在这里添加手指拖动悬浮窗时的事件及动画效果****************************************
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			// 手指移动的时候更新小悬浮窗的位置
			updateViewPosition();
			// 如果手指移动悬浮窗低于底面某一高度时
			if (mParams.y + (viewWidth) / 2 > (windowManager
					.getDefaultDisplay().getHeight()) - 300) {
				// 创建火箭动画
				MyWindowManager.createRocketWindow1(getContext());
			} else {
				MyWindowManager.removeRocketWindow1(getContext());
			}
			break;
		// ********************在这里添加手指拖动悬浮窗时的事件及动画效果****************************************

		// ********************在这里添加手指离开悬浮窗时的事件及动画效果****************************************
		case MotionEvent.ACTION_UP:
			// 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
			if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
				openBigWindow();
			} else {
				// 如果手指离开屏幕时，悬浮窗高于底面某一高度时
				if (mParams.y + (viewWidth) / 2 < (windowManager
						.getDefaultDisplay().getHeight()) - 300) {
					// 如果手指离开屏幕时，悬浮窗在屏幕右半边则贴右壁
					if (mParams.x + (viewWidth) / 2 > (windowManager
							.getDefaultDisplay().getWidth()) / 2) {
						mParams.x = windowManager.getDefaultDisplay()
								.getWidth() - viewWidth;
						windowManager.updateViewLayout(this, mParams);
						animJumpRight();
					} else {// 如果手指离开屏幕时，悬浮窗在屏幕左半边则贴左壁
						mParams.x = 0;
						windowManager.updateViewLayout(this, mParams);
						animJumpLeft();
					}
				} else {// 如果手指离开屏幕时，悬浮窗低于底面某一高度时
					MyWindowManager.removeRocketWindow1(getContext());
					MyWindowManager.createRocketWindow2(getContext());
					MyWindowManager.reCreateSmallWindow(getContext());

					//此处填加小火箭事件!!(RGY) 极速模式
					new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_PERFORMANCE);
				}

			}
			// ********************在这里添加手指离开悬浮窗时的事件及动画效果****************************************

			break;
		default:
			break;
		}
		return true;
	}

	// 当贴左壁时跳跃动画
	private void animJumpLeft() {
		Animation animJumpLeft = AnimationUtils.loadAnimation(getContext(),
				R.anim.anim_translate_jump_left);
		view.startAnimation(animJumpLeft);
	}

	// 当贴右壁时跳跃动画
	private void animJumpRight() {
		Animation animJumpRight = AnimationUtils.loadAnimation(getContext(),
				R.anim.anim_translate_jump_right);
		view.startAnimation(animJumpRight);
	}

	/**
	 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	/**
	 * 更新小悬浮窗在屏幕中的位置。
	 */
	private void updateViewPosition() {
		mParams.x = (int) (xInScreen - xInView);
		mParams.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
	}

	/**
	 * 打开大悬浮窗，同时关闭小悬浮窗。
	 */
	private void openBigWindow() {
		MyWindowManager.createBigWindow(getContext());
		MyWindowManager.removeSmallWindow(getContext());
	}

	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
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
				Toast.makeText(context, "请先获取root权限",Toast.LENGTH_SHORT).show();
				return;
			}
			String state = result.substring(0, 2);
			String model = result.substring(2);
			String cpuModel_str = SmallUtils.convertCpuModelName(model);
			if (state.equals("ok")) {
				myApp.setCpuModel(model);
				Toast.makeText(context, cpuModel_str + "设置成功",
						Toast.LENGTH_SHORT).show();
				Log.i(TAG, cpuModel_str+"设置成功");
				// 判断MainActivity是否存在
				if (MainActivity.tv_showmodel != null) {
					MainActivity.tv_showmodel.setText("当前模式:\n" + cpuModel_str);
				}
				// 关闭 智能模式 和 自定义模式
				String smartySwitch = myApp.getSmartySwitch();
				String customSwitch = myApp.getCustomSwitch();
				if (smartySwitch.equals("Start")) {
					Intent intent = new Intent(context,SmartyService.class);
					context.stopService(intent);
					myApp.setSmartySwitch("Stop");
				} else if (customSwitch.equals("Start")) {
					Intent intent = new Intent(context,CustomService.class);
					context.stopService(intent);
					myApp.setCustomSwitch("Stop");
				}
			} else {
				Toast.makeText(context, cpuModel_str + "设置失败",Toast.LENGTH_SHORT).show();
				Log.i(TAG, cpuModel_str+"设置失败");
			}
			// //
			super.onPostExecute(result);
		}
	}
	
}
