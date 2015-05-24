package com.don.floatwindow.view;

import com.rgy.setcpu.R;
import com.don.floatwindow.utils.MyWindowManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;

import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FloatWindowRocketView2 extends RelativeLayout {
	
	/**
	 * 用于更新小悬浮窗的位置和设置桌面动画
	 */
	public WindowManager windowManager;
	
	/**
	 * 记录火箭的宽度
	 */
	public static int viewWidth2;

	/**
	 * 记录火箭的高度
	 */
	public static int viewHeight2;
	
	/**
	 * 火箭窗的参数
	 */
	private WindowManager.LayoutParams rocketParams2;
	
	public View view2;
	public ImageView rocketView2;


	public FloatWindowRocketView2(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_window_rocket2, this);
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_rocket2, this);
		initView();
		animRocketRushUp();
	}

	private void initView() {
		view2 = findViewById(R.id.rocket2);
		viewWidth2 = view2.getLayoutParams().width;
		viewHeight2 = view2.getLayoutParams().height;
		rocketView2 = (ImageView) findViewById(R.id.percentRocket2);

	}
	
	/**
	 * 代码实现火箭起飞动画。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public void animRocketRushUp() {
		Animation animRushUp = new TranslateAnimation(0, 0, 0, -(windowManager
				.getDefaultDisplay().getHeight()));
		animRushUp.setDuration(1000);
		animRushUp.setFillAfter(true);
		rocketView2.startAnimation(animRushUp);
		animRushUp.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				//动画完成后自动销毁
				MyWindowManager.removeRocketWindow2(getContext());
			}
		});
	}

	/**
	 * 将火箭窗的参数传入，用于更新火箭窗的位置。
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams rocketParams) {
		this.rocketParams2 = rocketParams;
	}
}
