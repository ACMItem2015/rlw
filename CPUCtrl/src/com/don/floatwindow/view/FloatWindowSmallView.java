package com.don.floatwindow.view;

import java.lang.reflect.Field;

import com.don.floatwindow.FloatWindowService;
import com.rgy.setcpu.R;
import com.don.floatwindow.utils.MyWindowManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.LinearLayout;
import android.widget.TextView;

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

	public FloatWindowSmallView(Context context) {
		super(context);
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_small, this);

		initView();

		percentView.setText(FloatWindowService.resultCPU);
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
//			// 如果手指移动悬浮窗低于底面某一高度时
//			if (mParams.y + (viewWidth) / 2 > (windowManager
//					.getDefaultDisplay().getHeight()) - 300) {
//				// 创建火箭动画
//				MyWindowManager.createRocketWindow1(getContext());
//			} else {
//				MyWindowManager.removeRocketWindow1(getContext());
//			}
			break;
		// ********************在这里添加手指拖动悬浮窗时的事件及动画效果****************************************

		// ********************在这里添加手指离开悬浮窗时的事件及动画效果****************************************
		case MotionEvent.ACTION_UP:
			// 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
			if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
				openBigWindow();
			} else {
				// 如果手指离开屏幕时，悬浮窗高于底面某一高度时
//				if (mParams.y + (viewWidth) / 2 < (windowManager
//						.getDefaultDisplay().getHeight()) - 300) {
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
//				} else {// 如果手指离开屏幕时，悬浮窗低于底面某一高度时
//					MyWindowManager.removeRocketWindow1(getContext());
//					MyWindowManager.createRocketWindow2(getContext());
//					MyWindowManager.reCreateSmallWindow(getContext());

//				}

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
}
