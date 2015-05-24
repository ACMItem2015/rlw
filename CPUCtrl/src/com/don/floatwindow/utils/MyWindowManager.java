package com.don.floatwindow.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.rgy.setcpu.R;
import com.don.floatwindow.view.FloatWindowBigView;
import com.don.floatwindow.view.FloatWindowRocketView1;
import com.don.floatwindow.view.FloatWindowRocketView2;
import com.don.floatwindow.view.FloatWindowSmallView;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import android.widget.TextView;

public class MyWindowManager {

	/**
	 * �����View1��ʵ��
	 */
	public static FloatWindowRocketView1 rocketWindow1;

	/**
	 * �����View2��ʵ��
	 */
	public static FloatWindowRocketView2 rocketWindow2;

	/**
	 * С������View��ʵ��
	 */
	public static FloatWindowSmallView smallWindow;

	/**
	 * ��������View��ʵ��
	 */
	public static FloatWindowBigView bigWindow;

	/**
	 * С������View�Ĳ���
	 */
	private static LayoutParams smallWindowParams;

	/**
	 * ��������View�Ĳ���
	 */
	private static LayoutParams bigWindowParams;

	/**
	 * �����View1�Ĳ���
	 */
	private static LayoutParams rocketWindowParams1;

	/**
	 * �����View2�Ĳ���
	 */
	private static LayoutParams rocketWindowParams2;

	/**
	 * ���ڿ�������Ļ����ӻ��Ƴ�������
	 */
	private static WindowManager mWindowManager;

	/**
	 * ���ڻ�ȡ�ֻ������ڴ�
	 */
	private static ActivityManager mActivityManager;

	/**
	 * ����һ�������1��
	 */
	public static void createRocketWindow1(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();

		if (rocketWindow1 == null) {
			rocketWindow1 = new FloatWindowRocketView1(context);
			if (rocketWindowParams1 == null) {
				rocketWindowParams1 = new LayoutParams();
				rocketWindowParams1.type = LayoutParams.TYPE_PHONE;
				rocketWindowParams1.format = PixelFormat.RGBA_8888;
				rocketWindowParams1.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				rocketWindowParams1.gravity = Gravity.LEFT | Gravity.TOP;
				rocketWindowParams1.width = FloatWindowRocketView1.viewWidth1;
				rocketWindowParams1.height = FloatWindowRocketView1.viewHeight1;
				rocketWindowParams1.x = screenWidth / 2
						- rocketWindowParams1.width / 2;
				rocketWindowParams1.y = screenHeight
						- rocketWindowParams1.height;
			}
			rocketWindow1.setParams(rocketWindowParams1);
			windowManager.addView(rocketWindow1, rocketWindowParams1);
		}
	}

	/**
	 * �������1����Ļ���Ƴ���
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
	 */
	public static void removeRocketWindow1(Context context) {
		if (rocketWindow1 != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(rocketWindow1);
			rocketWindow1 = null;
		}
	}

	/**
	 * ����һ�������2��
	 */
	public static void createRocketWindow2(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();

		if (rocketWindow2 == null) {
			rocketWindow2 = new FloatWindowRocketView2(context);
			if (rocketWindowParams2 == null) {
				rocketWindowParams2 = new LayoutParams();
				rocketWindowParams2.type = LayoutParams.TYPE_PHONE;
				rocketWindowParams2.format = PixelFormat.RGBA_8888;
				rocketWindowParams2.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				rocketWindowParams2.gravity = Gravity.LEFT | Gravity.TOP;
				rocketWindowParams2.width = FloatWindowRocketView2.viewWidth2;
				rocketWindowParams2.height = FloatWindowRocketView2.viewHeight2;
				rocketWindowParams2.x = screenWidth / 2
						- rocketWindowParams2.width / 2;
				rocketWindowParams2.y = screenHeight
						- rocketWindowParams2.height;
			}
			rocketWindow2.setParams(rocketWindowParams2);
			windowManager.addView(rocketWindow2, rocketWindowParams2);
		}
	}

	/**
	 * �������2����Ļ���Ƴ���
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
	 */
	public static void removeRocketWindow2(Context context) {
		if (rocketWindow2 != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(rocketWindow2);
			rocketWindow2 = null;
		}
	}

	/**
	 * ����һ��С����������ʼλ��Ϊ��Ļ���Ҳ��м�λ�á�
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
	 */
	public static void createSmallWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (smallWindow == null) {
			smallWindow = new FloatWindowSmallView(context);
			if (smallWindowParams == null) {
				smallWindowParams = new LayoutParams();
				smallWindowParams.type = LayoutParams.TYPE_PHONE;
				smallWindowParams.format = PixelFormat.RGBA_8888;
				smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				smallWindowParams.width = FloatWindowSmallView.viewWidth;
				smallWindowParams.height = FloatWindowSmallView.viewHeight;
				smallWindowParams.x = screenWidth;
				smallWindowParams.y = screenHeight / 2;
			}

			smallWindow.setParams(smallWindowParams);
			windowManager.addView(smallWindow, smallWindowParams);
		}
	}

	/**
	 * ����С������λ�á�
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
	 */
	public static void reCreateSmallWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		smallWindowParams.x = screenWidth;
		smallWindowParams.y = screenHeight / 2;
		windowManager.updateViewLayout(smallWindow, smallWindowParams);
	}

	/**
	 * ��С����������Ļ���Ƴ���
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
	 */
	public static void removeSmallWindow(Context context) {
		if (smallWindow != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(smallWindow);
			smallWindow = null;
		}
	}

	/**
	 * ����һ������������λ��Ϊ��Ļ���м䡣
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
	 */
	public static void createBigWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (bigWindow == null) {
			bigWindow = new FloatWindowBigView(context);
			if (bigWindowParams == null) {
				bigWindowParams = new LayoutParams();
				bigWindowParams.x = screenWidth / 2
						- FloatWindowBigView.viewWidth / 2;
				bigWindowParams.y = screenHeight / 2
						- FloatWindowBigView.viewHeight / 2;
				bigWindowParams.type = LayoutParams.TYPE_PHONE;
				bigWindowParams.format = PixelFormat.RGBA_8888;
				bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				bigWindowParams.width = FloatWindowBigView.viewWidth;
				bigWindowParams.height = FloatWindowBigView.viewHeight;
			}
			windowManager.addView(bigWindow, bigWindowParams);
		}
	}

	/**
	 * ��������������Ļ���Ƴ���
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
	 */
	public static void removeBigWindow(Context context) {
		if (bigWindow != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(bigWindow);
			bigWindow = null;
		}
	}

	/**
	 * ����С��������TextView�ϵ����ݣ���ʾ�ڴ�ʹ�õİٷֱȡ�
	 * 
	 * @param context
	 *            �ɴ���Ӧ�ó��������ġ�
	 */
	public static void updateUsedPercent(Context context) {
		if (smallWindow != null) {
			TextView percentView = (TextView) smallWindow
					.findViewById(R.id.percent);
			percentView.setText(getUsedPercentValue(context));
		}
	}

	/**
	 * �Ƿ���������(����С�������ʹ�������)��ʾ����Ļ�ϡ�
	 * 
	 * @return ����������ʾ�������Ϸ���true��û�еĻ�����false��
	 */
	public static boolean isWindowShowing() {
		return smallWindow != null || bigWindow != null;
	}

	/**
	 * ���WindowManager��δ�������򴴽�һ���µ�WindowManager���ء����򷵻ص�ǰ�Ѵ�����WindowManager��
	 * 
	 * @param context
	 *            ����ΪӦ�ó����Context.
	 * @return WindowManager��ʵ�������ڿ�������Ļ����ӻ��Ƴ���������
	 */
	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}

	/**
	 * ���ActivityManager��δ�������򴴽�һ���µ�ActivityManager���ء����򷵻ص�ǰ�Ѵ�����ActivityManager��
	 * 
	 * @param context
	 *            �ɴ���Ӧ�ó��������ġ�
	 * @return ActivityManager��ʵ�������ڻ�ȡ�ֻ������ڴ档
	 */
	private static ActivityManager getActivityManager(Context context) {
		if (mActivityManager == null) {
			mActivityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}

	/**
	 * ������ʹ���ڴ�İٷֱȣ������ء�
	 * 
	 * @param context
	 *            �ɴ���Ӧ�ó��������ġ�
	 * @return ��ʹ���ڴ�İٷֱȣ����ַ�����ʽ���ء�
	 */
	public static String getUsedPercentValue(Context context) {
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr, 2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine
					.indexOf("MemTotal:"));
			br.close();
			long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll(
					"\\D+", ""));
			long availableSize = getAvailableMemory(context) / 1024;
			int percent = (int) ((totalMemorySize - availableSize)
					/ (float) totalMemorySize * 100);
			return percent + "%";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "������";
	}

	/**
	 * ��ȡ��ǰ�����ڴ棬�����������ֽ�Ϊ��λ��
	 * 
	 * @param context
	 *            �ɴ���Ӧ�ó��������ġ�
	 * @return ��ǰ�����ڴ档
	 */
	private static long getAvailableMemory(Context context) {
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		getActivityManager(context).getMemoryInfo(mi);
		return mi.availMem;
	}

}
