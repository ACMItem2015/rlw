package com.rgy.setcpu;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rgy.Tools.AppListAdapter;
import com.rgy.Tools.AppUtils;
import com.rgy.Tools.DeepCpuData;
import com.rgy.Tools.MyConfig;
import com.rgy.Tools.SmallUtils;
import com.rgy.entity.AppInfo;
import com.rgy.widget.MyProgressDialog;
import com.rgy.widget.SlidingMenu;

public class AppAllActivity extends Activity {
	
	View view_app_all,view_app_default,view_app_performance,view_app_powersave,view_switch;
	
	TextView tv_switch;
	
	ArrayList<AppInfo> appList;
	
	ListView listView;
	
	ProgressDialog dialog;
	
	MyApplication myApp;
	
	ImageButton back;
	
	TextView tv_title;
	
	SlidingMenu sMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applist_all);
		
		view_app_all = (RelativeLayout)findViewById(R.id.app_all_layout);
		view_app_all.setBackgroundColor(0x11000000);
		
		tv_switch = (TextView)findViewById(R.id.start_stop);
		
		dialog = new MyProgressDialog(this);
//		dialog.show();
		
		//判断CustomService是否正在运行
		myApp = (MyApplication) getApplication();
		String customSwitch = myApp.getCustomSwitch();
		if(customSwitch.equals("Stop")){
			tv_switch.setText("启动自定义");
		}else{
			tv_switch.setText("停用自定义");
		}
		
		//获取左边菜单
		sMenu = (SlidingMenu)findViewById(R.id.id_menu);
		/**
		 * 顶部panel
		 */
		tv_title = (TextView) findViewById(R.id.textview_actionbar);
		tv_title.setText("所有应用");
		back=(ImageButton) findViewById(R.id.imagebutton_actionbar);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sMenu.toggle();
			}
		});
		
		/**
		 * 默认列表
		 */
		view_app_default = (RelativeLayout)findViewById(R.id.app_default_layout);
		view_app_default.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppAllActivity.this,AppDefaultActivity.class);  
				startActivity(intent);
				finish();
			}
		});
		
		/**
		 * 极速列表
		 */
		view_app_performance = (RelativeLayout)findViewById(R.id.app_performance_layout);
		view_app_performance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppAllActivity.this,AppPerformanceActivity.class);  
				startActivity(intent);
				finish();
			}
		});
		
		/**
		 * 省电列表
		 */
		view_app_powersave = (RelativeLayout)findViewById(R.id.app_powersave_layout);
		view_app_powersave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppAllActivity.this,AppPowersaveActivity.class);  
				startActivity(intent);
				finish();
			}
		});
		
		/**
		 * customservice开关
		 */
		view_switch = (RelativeLayout)findViewById(R.id.customservice_switch);
		view_switch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String customSwitch = myApp.getCustomSwitch();
				if(customSwitch.equals("Stop")){
					Intent intent = new Intent(AppAllActivity.this, CustomService.class);
					startService(intent);
					Toast.makeText(AppAllActivity.this, "自定义模式已启动", Toast.LENGTH_SHORT).show();
					MainActivity.tv_showmodel.setText("当前模式:\n"+ "自定义模式");// 修改主界面
					myApp.setCustomSwitch("Start");
					tv_switch.setText("停用自定义");
					// 如果此时智能模式还处于开启状态，则关闭它
					String smartySwitch = myApp.getSmartySwitch();
					if(smartySwitch.equals("Start")){
						Intent smartyIntent = new Intent(AppAllActivity.this, SmartyService.class);
						stopService(smartyIntent);
						//
						myApp.setSmartySwitch("Stop");
					}
				}else{
					Intent intent = new Intent(AppAllActivity.this, CustomService.class);
					stopService(intent);
					Toast.makeText(AppAllActivity.this, "自定义模式已停止", Toast.LENGTH_SHORT).show();
					new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_DEFAULT);// 回到默认模式
					myApp.setCustomSwitch("Stop");
					tv_switch.setText("启动自定义");
				}
			}
		});
		//----------------------------//
		
		listView = (ListView) findViewById(R.id.appinfo_list);
		/**
		 * 点击事件
		 */
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {		
				AppInfo appInfo = appList.get(arg2);
				String packageName = appInfo.getPackageName();
				//
				Intent intent = new Intent(AppAllActivity.this,AppEachInfoActivity.class);
				Bundle data = new Bundle();
				data.putString("packageName", packageName);
				data.putString("model", "undefined");
				intent.putExtras(data);
				startActivity(intent);
				finish();
			}
		});
		
		//
//		new AsyncTaskGetData().execute();
		ArrayList<AppInfo> allAppList = AppUtils.getLatestAppList(AppAllActivity.this);
		appList = allAppList;
		AppListAdapter appListAdapter = new AppListAdapter(AppAllActivity.this, allAppList);
		listView.setAdapter(appListAdapter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("AppAllActivity被销毁");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		//------------------------------------------------------------
		case KeyEvent.KEYCODE_BACK://返回键
//			Intent intent = new Intent(AppAllActivity.this, MainActivity.class);
//			startActivity(intent);
			finish();
			break;
			
		default:
			break;
		}
		return false;
	}
	
	class AsyncTaskGetData extends AsyncTask<String, Integer, ArrayList<AppInfo>>{
		@Override
		protected ArrayList<AppInfo> doInBackground(String... params) {
			ArrayList<AppInfo> allAppList = AppUtils.getLatestAppList(AppAllActivity.this);
			if(allAppList == null||allAppList.isEmpty()){
				return null;
			}
			appList = allAppList;
			return allAppList;
		}
		
		@Override
		protected void onPostExecute(ArrayList<AppInfo> appList) {
			if(appList == null){
				Toast.makeText(AppAllActivity.this, "您还未安装任何第三方应用", Toast.LENGTH_SHORT).show();
				return;
			}
			AppListAdapter appListAdapter = new AppListAdapter(AppAllActivity.this, appList);
			listView.setAdapter(appListAdapter);
			dialog.dismiss();
		}
	}
	
	class AsyncTaskSetModel extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			String model = params[0];
			long max = Long.parseLong(myApp.getcpuMaxFreq());
			long min = Long.parseLong(myApp.getcpuMinFreq());
			boolean flag1 = DeepCpuData.setMaxCpuFreq(max);
			boolean flag2 = DeepCpuData.setMinCpuFreq(min);
			boolean flag = false;
			if(model.equals(MyConfig.CPUMODEL_POWERSAVE)){
				flag = DeepCpuData.setCpuGovernor("powersave");
			}else if(model.equals(MyConfig.CPUMODEL_PERFORMANCE)){
				flag = DeepCpuData.setCpuGovernor("performance");
			}else if(model.equals(MyConfig.CPUMODEL_DEFAULT)){
				flag = DeepCpuData.setCpuGovernor("userspace");
			}
			
			String result = "";
			if(flag&&flag1&&flag2){
				result = "ok"+model;
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result.equals("")){
				Toast.makeText(AppAllActivity.this, "请先获取root权限", Toast.LENGTH_SHORT).show();
				return;
			}
			String state = result.substring(0, 2);
			String model = result.substring(2);
			String cpuModel_str = SmallUtils.convertCpuModelName(model);
			if(state.equals("ok")){
				myApp.setCpuModel(model);
				MainActivity.tv_showmodel.setText("当前模式:\n"+ "默认模式");// 修改主界面
				Toast.makeText(AppAllActivity.this, cpuModel_str+"设置成功", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(AppAllActivity.this, cpuModel_str+"设置失败", Toast.LENGTH_SHORT).show();
			}
			////
			super.onPostExecute(result);
		}
	}

}
