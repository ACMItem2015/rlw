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
	 * ��¼С�������Ŀ��
	 */
	public static int viewWidth;

	/**
	 * ��¼С�������ĸ߶�
	 */
	public static int viewHeight;

	/**
	 * ��¼ϵͳ״̬���ĸ߶�
	 */
	private static int statusBarHeight;

	/**
	 * ���ڸ���С��������λ�ú��������涯��
	 */
	private WindowManager windowManager;

	/**
	 * С�������Ĳ���
	 */
	private WindowManager.LayoutParams mParams;

	/**
	 * ��¼��ǰ��ָλ������Ļ�ϵĺ�����ֵ
	 */
	private float xInScreen;

	/**
	 * ��¼��ǰ��ָλ������Ļ�ϵ�������ֵ
	 */
	private float yInScreen;

	/**
	 * ��¼��ָ����ʱ����Ļ�ϵĺ������ֵ
	 */
	private float xDownInScreen;

	/**
	 * ��¼��ָ����ʱ����Ļ�ϵ��������ֵ
	 */
	private float yDownInScreen;

	/**
	 * ��¼��ָ����ʱ��С��������View�ϵĺ������ֵ
	 */
	private float xInView;

	/**
	 * ��¼��ָ����ʱ��С��������View�ϵ��������ֵ
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
			// ��ָ����ʱ��¼��Ҫ����,�������ֵ����Ҫ��ȥ״̬���߶�
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			break;

		// ********************�����������ָ�϶�������ʱ���¼�������Ч��****************************************
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			// ��ָ�ƶ���ʱ�����С��������λ��
			updateViewPosition();
//			// �����ָ�ƶ����������ڵ���ĳһ�߶�ʱ
//			if (mParams.y + (viewWidth) / 2 > (windowManager
//					.getDefaultDisplay().getHeight()) - 300) {
//				// �����������
//				MyWindowManager.createRocketWindow1(getContext());
//			} else {
//				MyWindowManager.removeRocketWindow1(getContext());
//			}
			break;
		// ********************�����������ָ�϶�������ʱ���¼�������Ч��****************************************

		// ********************�����������ָ�뿪������ʱ���¼�������Ч��****************************************
		case MotionEvent.ACTION_UP:
			// �����ָ�뿪��Ļʱ��xDownInScreen��xInScreen��ȣ���yDownInScreen��yInScreen��ȣ�����Ϊ�����˵����¼���
			if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
				openBigWindow();
			} else {
				// �����ָ�뿪��Ļʱ�����������ڵ���ĳһ�߶�ʱ
//				if (mParams.y + (viewWidth) / 2 < (windowManager
//						.getDefaultDisplay().getHeight()) - 300) {
					// �����ָ�뿪��Ļʱ������������Ļ�Ұ�������ұ�
					if (mParams.x + (viewWidth) / 2 > (windowManager
							.getDefaultDisplay().getWidth()) / 2) {
						mParams.x = windowManager.getDefaultDisplay()
								.getWidth() - viewWidth;
						windowManager.updateViewLayout(this, mParams);
						animJumpRight();
					} else {// �����ָ�뿪��Ļʱ������������Ļ�����������
						mParams.x = 0;
						windowManager.updateViewLayout(this, mParams);
						animJumpLeft();
					}
//				} else {// �����ָ�뿪��Ļʱ�����������ڵ���ĳһ�߶�ʱ
//					MyWindowManager.removeRocketWindow1(getContext());
//					MyWindowManager.createRocketWindow2(getContext());
//					MyWindowManager.reCreateSmallWindow(getContext());

//				}

			}
			// ********************�����������ָ�뿪������ʱ���¼�������Ч��****************************************

			break;
		default:
			break;
		}
		return true;
	}

	// �������ʱ��Ծ����
	private void animJumpLeft() {
		Animation animJumpLeft = AnimationUtils.loadAnimation(getContext(),
				R.anim.anim_translate_jump_left);
		view.startAnimation(animJumpLeft);
	}

	// �����ұ�ʱ��Ծ����
	private void animJumpRight() {
		Animation animJumpRight = AnimationUtils.loadAnimation(getContext(),
				R.anim.anim_translate_jump_right);
		view.startAnimation(animJumpRight);
	}

	/**
	 * ��С�������Ĳ������룬���ڸ���С��������λ�á�
	 * 
	 * @param params
	 *            С�������Ĳ���
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	/**
	 * ����С����������Ļ�е�λ�á�
	 */
	private void updateViewPosition() {
		mParams.x = (int) (xInScreen - xInView);
		mParams.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
	}

	/**
	 * �򿪴���������ͬʱ�ر�С��������
	 */
	private void openBigWindow() {
		MyWindowManager.createBigWindow(getContext());
		MyWindowManager.removeSmallWindow(getContext());
	}

	/**
	 * ���ڻ�ȡ״̬���ĸ߶ȡ�
	 * 
	 * @return ����״̬���߶ȵ�����ֵ��
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
