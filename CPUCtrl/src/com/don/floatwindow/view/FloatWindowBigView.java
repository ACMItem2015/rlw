package com.don.floatwindow.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.don.floatwindow.utils.MyWindowManager;
import com.rgy.Tools.AppUtils;
import com.rgy.Tools.DeepCpuData;
import com.rgy.Tools.MyConfig;
import com.rgy.Tools.SmallUtils;
import com.rgy.setcpu.CustomService;
import com.rgy.setcpu.MyApplication;
import com.rgy.setcpu.R;
import com.rgy.setcpu.SmartyService;
import com.rgy.widget.MyProgressDialog;

public class FloatWindowBigView extends LinearLayout {

	/**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录大悬浮窗的高度
	 */
	public static int viewHeight;

	private View view;
	private ImageView moren, jisu, shengdian, yonghu, zidingyi, zhineng;

	ProgressDialog dialog;
	MyApplication myApp;

	Context context;

	public FloatWindowBigView(final Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.float_window_big, this);

		this.context = context;

		initView();
		recordDatas();

		// hiddenButtons(context);
		animScale(context);
		initEvent(context);
	}

	// 控件动画效果
	public final void animScale(final Context context) {
		Animation animScaleShengDian = new ScaleAnimation(0f, 1f, 0f, 1f, 50f,
				50f);
		animScaleShengDian.setFillAfter(false);
		animScaleShengDian.setDuration(700);

		Animation animScaleMoRen = new ScaleAnimation(0f, 1f, 0f, 1f, 50f, 50f);
		animScaleMoRen.setFillAfter(false);
		animScaleMoRen.setDuration(500);

		Animation animScaleZhiNeng = new ScaleAnimation(0f, 1f, 0f, 1f, 50f,
				50f);
		animScaleZhiNeng.setFillAfter(false);
		animScaleZhiNeng.setDuration(300);

		Animation animScaleZiDingYi = new ScaleAnimation(0f, 1f, 0f, 1f, 50f,
				50f);
		animScaleZiDingYi.setFillAfter(false);
		animScaleZiDingYi.setDuration(900);

		Animation animScaleYongHu = new ScaleAnimation(0f, 1f, 0f, 1f, 50f, 50f);
		animScaleYongHu.setFillAfter(false);
		animScaleYongHu.setDuration(1100);

		Animation animScaleJiSu = new ScaleAnimation(0f, 1f, 0f, 1f, 50f, 50f);
		animScaleJiSu.setFillAfter(false);
		animScaleJiSu.setDuration(1300);

		jisu.startAnimation(animScaleJiSu);
		moren.startAnimation(animScaleMoRen);
		shengdian.startAnimation(animScaleShengDian);
		zidingyi.startAnimation(animScaleZiDingYi);
		zhineng.startAnimation(animScaleZhiNeng);
		yonghu.startAnimation(animScaleYongHu);

		// Animation animGoUp = AnimationUtils.loadAnimation(context,
		// R.anim.anim_translate_go_up);
		// Animation animGoRightBottom = AnimationUtils.loadAnimation(context,
		// R.anim.anim_translate_go_rightbottom);
		// Animation animGoLeftBottom = AnimationUtils.loadAnimation(context,
		// R.anim.anim_translate_go_leftbottom);

	}

	// 记录数据
	private void recordDatas() {
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
	}

	private void initEvent(final Context context) {

		// 点击默认模式的时候切换默认模式，移除大悬浮窗，创建小悬浮窗
		// moren.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// MyWindowManager.removeBigWindow(context);
		// try {
		// DeepCpuData.setCpuGovernor("userspace");
		// Toast.makeText(context, "默认模式设置成功", Toast.LENGTH_SHORT)
		// .show();
		// // dialog.show();
		// // new
		// // AsyncTaskSetModel().execute(MyConfig.CPUMODEL_DEFAULT);
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		// }
		// });

		// 点击极速模式的时候切换极速模式，移除大悬浮窗，创建小悬浮窗
		// jisu.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// MyWindowManager.removeBigWindow(context);
		// try {
		// DeepCpuData.setCpuGovernor("performance");
		// Toast.makeText(context, "极速模式设置成功", Toast.LENGTH_SHORT)
		// .show();
		// // dialog.show();
		// // new
		// // AsyncTaskSetModel().execute(MyConfig.CPUMODEL_PERFORMANCE);
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		// }
		// });

		// 点击省电模式的时候切换省电模式，移除大悬浮窗，创建小悬浮窗
		// shengdian.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// MyWindowManager.removeBigWindow(context);
		// try {
		// DeepCpuData.setCpuGovernor("powersave");
		// Toast.makeText(context, "省电模式设置成功", Toast.LENGTH_SHORT)
		// .show();
		// // dialog.show();
		// // new
		// // AsyncTaskSetModel().execute(MyConfig.CPUMODEL_POWERSAVE);
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		// }
		// });

		// 点击自定义模式的时候切换省电模式，移除大悬浮窗，创建小悬浮窗
		// zidingyi.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// MyWindowManager.removeBigWindow(context);
		// try {
		// DeepCpuData.setCpuGovernor("powersave");
		// Toast.makeText(context, "自定义模式设置成功", Toast.LENGTH_SHORT)
		// .show();
		// // dialog.show();
		// // new
		// // AsyncTaskSetModel().execute(MyConfig.CPUMODEL_POWERSAVE);
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		// }
		// });

		// 点击用户模式的时候切换省电模式，移除大悬浮窗，创建小悬浮窗
		// yonghu.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// MyWindowManager.removeBigWindow(context);
		// try {
		// DeepCpuData.setCpuGovernor("powersave");
		// Toast.makeText(context, "用户模式设置成功", Toast.LENGTH_SHORT)
		// .show();
		// // dialog.show();
		// // new
		// // AsyncTaskSetModel().execute(MyConfig.CPUMODEL_POWERSAVE);
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		// }
		// });

		// 点击智能模式的时候切换省电模式，移除大悬浮窗，创建小悬浮窗
		// zhineng.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// MyWindowManager.removeBigWindow(context);
		// try {
		// DeepCpuData.setCpuGovernor("powersave");
		// Toast.makeText(context, "智能模式设置成功", Toast.LENGTH_SHORT)
		// .show();
		// // dialog.show();
		// // new
		// // AsyncTaskSetModel().execute(MyConfig.CPUMODEL_POWERSAVE);
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		// }
		// });

		/**
		 * 防止该界面因突发事件卡死
		 */
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyWindowManager.removeBigWindow(getContext());
				MyWindowManager.createSmallWindow(getContext());
			}
		});
	}

	private void initView() {
		view = findViewById(R.id.big_window_layout);
		moren = (ImageView) findViewById(R.id.moren);
		jisu = (ImageView) findViewById(R.id.jisu);
		shengdian = (ImageView) findViewById(R.id.shengdian);
		zidingyi = (ImageView) findViewById(R.id.zidingyi);
		zhineng = (ImageView) findViewById(R.id.zhineng);
		yonghu = (ImageView) findViewById(R.id.yonghu);
		dialog = new MyProgressDialog(context);
	}

	// /**
	// * 异步任务设置cpu模式
	// *
	// * @author Administrator
	// *
	// */
	// class AsyncTaskSetModel extends AsyncTask<String, Integer, String> {
	// @Override
	// protected String doInBackground(String... params) {
	// String model = params[0];
	// long max = Long.parseLong(myApp.getcpuMaxFreq());
	// long min = Long.parseLong(myApp.getcpuMinFreq());
	// boolean flag1 = DeepCpuData.setMaxCpuFreq(max);
	// boolean flag2 = DeepCpuData.setMinCpuFreq(min);
	// boolean flag = false;
	// if (model.equals(MyConfig.CPUMODEL_POWERSAVE)) {
	// flag = DeepCpuData.setCpuGovernor("powersave");
	// } else if (model.equals(MyConfig.CPUMODEL_PERFORMANCE)) {
	// flag = DeepCpuData.setCpuGovernor("performance");
	// } else if (model.equals(MyConfig.CPUMODEL_DEFAULT)) {
	// flag = DeepCpuData.setCpuGovernor("userspace");
	// }
	//
	// String result = "";
	// if (flag && flag1 && flag2) {
	// result = "ok" + model;
	// }
	// return result;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// if (result.equals("")) {
	// Toast.makeText(context, "请先获取root权限", Toast.LENGTH_SHORT)
	// .show();
	// return;
	// }
	// Toast.makeText(context, "异步任务执行了", Toast.LENGTH_SHORT).show();
	// String state = result.substring(0, 2);
	// String model = result.substring(2);
	// String cpuModel_str = SmallUtils.convertCpuModelName(model);
	// if (state.equals("ok")) {
	// myApp.setCpuModel(model);
	// Toast.makeText(context, cpuModel_str + "设置成功",
	// Toast.LENGTH_SHORT).show();
	// // 关闭 智能模式 和 自定义模式
	// if (AppUtils
	// .isServiceWork(context, MyConfig.SERVICENAME_SMARTY)) {
	// Intent intent = new Intent(context, SmartyService.class);
	// context.stopService(intent);
	// } else if (AppUtils.isServiceWork(context,
	// MyConfig.SERVICENAME_CUSTOM)) {
	// Intent intent = new Intent(context, CustomService.class);
	// context.stopService(intent);
	// }
	// } else {
	// Toast.makeText(context, cpuModel_str + "设置失败",
	// Toast.LENGTH_SHORT).show();
	// }
	// // //
	// super.onPostExecute(result);
	// dialog.dismiss();
	// }
	// }

}