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
			// �����ָ�ƶ����������ڵ���ĳһ�߶�ʱ
			if (mParams.y + (viewWidth) / 2 > (windowManager
					.getDefaultDisplay().getHeight()) - 300) {
				// �����������
				MyWindowManager.createRocketWindow1(getContext());
			} else {
				MyWindowManager.removeRocketWindow1(getContext());
			}
			break;
		// ********************�����������ָ�϶�������ʱ���¼�������Ч��****************************************

		// ********************�����������ָ�뿪������ʱ���¼�������Ч��****************************************
		case MotionEvent.ACTION_UP:
			// �����ָ�뿪��Ļʱ��xDownInScreen��xInScreen��ȣ���yDownInScreen��yInScreen��ȣ�����Ϊ�����˵����¼���
			if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
				openBigWindow();
			} else {
				// �����ָ�뿪��Ļʱ�����������ڵ���ĳһ�߶�ʱ
				if (mParams.y + (viewWidth) / 2 < (windowManager
						.getDefaultDisplay().getHeight()) - 300) {
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
				} else {// �����ָ�뿪��Ļʱ�����������ڵ���ĳһ�߶�ʱ
					MyWindowManager.removeRocketWindow1(getContext());
					MyWindowManager.createRocketWindow2(getContext());
					MyWindowManager.reCreateSmallWindow(getContext());

					//�˴����С����¼�!!(RGY) ����ģʽ
					new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_PERFORMANCE);
				}

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
				Toast.makeText(context, "���Ȼ�ȡrootȨ��",Toast.LENGTH_SHORT).show();
				return;
			}
			String state = result.substring(0, 2);
			String model = result.substring(2);
			String cpuModel_str = SmallUtils.convertCpuModelName(model);
			if (state.equals("ok")) {
				myApp.setCpuModel(model);
				Toast.makeText(context, cpuModel_str + "���óɹ�",
						Toast.LENGTH_SHORT).show();
				Log.i(TAG, cpuModel_str+"���óɹ�");
				// �ж�MainActivity�Ƿ����
				if (MainActivity.tv_showmodel != null) {
					MainActivity.tv_showmodel.setText("��ǰģʽ:\n" + cpuModel_str);
				}
				// �ر� ����ģʽ �� �Զ���ģʽ
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
				Toast.makeText(context, cpuModel_str + "����ʧ��",Toast.LENGTH_SHORT).show();
				Log.i(TAG, cpuModel_str+"����ʧ��");
			}
			// //
			super.onPostExecute(result);
		}
	}
	
}
