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
	 * ���ڸ���С��������λ�ú��������涯��
	 */
	public WindowManager windowManager;
	
	/**
	 * ��¼����Ŀ��
	 */
	public static int viewWidth2;

	/**
	 * ��¼����ĸ߶�
	 */
	public static int viewHeight2;
	
	/**
	 * ������Ĳ���
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
	 * ����ʵ�ֻ����ɶ�����
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
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
				//������ɺ��Զ�����
				MyWindowManager.removeRocketWindow2(getContext());
			}
		});
	}

	/**
	 * ��������Ĳ������룬���ڸ��»������λ�á�
	 * 
	 * @param params
	 *            С�������Ĳ���
	 */
	public void setParams(WindowManager.LayoutParams rocketParams) {
		this.rocketParams2 = rocketParams;
	}
}
