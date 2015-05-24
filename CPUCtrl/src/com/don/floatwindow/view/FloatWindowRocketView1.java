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
	 * ���ڸ���С��������λ�ú��������涯��
	 */
	public WindowManager windowManager;
	
	/**
	 * ��¼����Ŀ��
	 */
	public static int viewWidth1;

	/**
	 * ��¼����ĸ߶�
	 */
	public static int viewHeight1;
	
	/**
	 * ������Ĳ���
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
	 * ��ֹС�����������ͻ���¼�����
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
	 * ��������Ĳ������룬���ڸ��»������λ�á�
	 * 
	 * @param params
	 *            С�������Ĳ���
	 */
	public void setParams(WindowManager.LayoutParams rocketParams) {
		this.rocketParams1 = rocketParams;
	}

}
