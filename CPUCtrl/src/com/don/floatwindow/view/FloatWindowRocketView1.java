package com.don.floatwindow.view;

import com.rgy.setcpu.R;
import com.don.floatwindow.utils.MyWindowManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FloatWindowRocketView1 extends RelativeLayout {
	
	/**
	 * 用于更新小悬浮窗的位置和设置桌面动画
	 */
	public WindowManager windowManager;
	
	/**
	 * 记录火箭的宽度
	 */
	public static int viewWidth1;

	/**
	 * 记录火箭的高度
	 */
	public static int viewHeight1;
	
	/**
	 * 火箭窗的参数
	 */
	private WindowManager.LayoutParams rocketParams1;
	
	public View view1;
	public ImageView rocketView1;


	public FloatWindowRocketView1(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_window_rocket1, this);
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_rocket1, this);
		initView();
		initEvent();

	}

	/**
	 * 防止小火箭因锁屏等突发事件卡死
	 */
	private void initEvent() {
		view1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyWindowManager.removeRocketWindow1(getContext());
				MyWindowManager.createRocketWindow2(getContext());
				MyWindowManager.reCreateSmallWindow(getContext());
			}
		});
	}

	private void initView() {
		view1 = findViewById(R.id.rocket1);
		viewWidth1 = view1.getLayoutParams().width;
		viewHeight1 = view1.getLayoutParams().height;
		rocketView1 = (ImageView) findViewById(R.id.percentRocket1);

	}
	
	/**
	 * 将火箭窗的参数传入，用于更新火箭窗的位置。
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams rocketParams) {
		this.rocketParams1 = rocketParams;
	}

}
