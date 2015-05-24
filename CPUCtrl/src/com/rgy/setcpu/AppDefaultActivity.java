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
import com.rgy.Tools.FileUtils;
import com.rgy.Tools.MyConfig;
import com.rgy.entity.AppInfo;
import com.rgy.widget.MyProgressDialog;
import com.rgy.widget.SlidingMenu;

public class AppDefaultActivity extends Activity {
	
	View view_app_all,view_app_default,view_app_performance,view_app_powersave,view_switch;
	
	TextView tv_switch;
	
	ArrayList<AppInfo> appList = new ArrayList<AppInfo>();
	
	ListView listView;
	
	ProgressDialog dialog;
	
	MyApplication myApp;
	
	ImageButton back;
	
	TextView tv_title;
	
	SlidingMenu sMenu;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_applist_default);
		
		view_app_default = (RelativeLayout)findViewById(R.id.app_default_layout);
		view_app_default.setBackgroundColor(0x11000000);
		
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
		tv_title.setText("默认列表");
		back=(ImageButton) findViewById(R.id.imagebutton_actionbar);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sMenu.toggle();
			}
		});

		/**
		 * 所有应用
		 */
		view_app_all = (RelativeLayout)findViewById(R.id.app_all_layout);
		view_app_all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppDefaultActivity.this,AppAllActivity.class);  
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
				Intent intent = new Intent(AppDefaultActivity.this,AppPerformanceActivity.class);  
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
				Intent intent = new Intent(AppDefaultActivity.this,AppPowersaveActivity.class);  
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
					Intent intent = new Intent(AppDefaultActivity.this, CustomService.class);
					startService(intent);
					Toast.makeText(AppDefaultActivity.this, "自定义模式已启动", Toast.LENGTH_SHORT).show();
					myApp.setCustomSwitch("Start");
					tv_switch.setText("停用自定义");
					// 如果此时智能模式还处于开启状态，则关闭它
					String smartySwitch = myApp.getSmartySwitch();
					if(smartySwitch.equals("Start")){
						Intent smartyIntent = new Intent(AppDefaultActivity.this, SmartyService.class);
						stopService(smartyIntent);
						//
						myApp.setSmartySwitch("Stop");
					}
				}else{
					Intent intent = new Intent(AppDefaultActivity.this, CustomService.class);
					stopService(intent);
					Toast.makeText(AppDefaultActivity.this, "自定义模式已停止", Toast.LENGTH_SHORT).show();
					myApp.setCustomSwitch("Stop");
					tv_switch.setText("启动自定义");
				}
			}
		});
		
		//-------------------//
		
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
				Intent intent = new Intent(AppDefaultActivity.this,AppEachInfoActivity.class);
				Bundle data = new Bundle();
				data.putString("packageName", packageName);
				data.putString("model", MyConfig.CPUMODEL_DEFAULT);
				intent.putExtras(data);
				startActivity(intent);
				//finish();
			}
		});
		
		//-------------------//
//		new AsyncTaskGetData().execute();
		ArrayList<AppInfo> allAppList = getAppList();
		if(allAppList == null){
			Toast.makeText(AppDefaultActivity.this, "您还未安装任何第三方应用", Toast.LENGTH_SHORT).show();
			return;
		}
		AppListAdapter appListAdapter = new AppListAdapter(AppDefaultActivity.this, allAppList);
		listView.setAdapter(appListAdapter);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		//------------------------------------------------------------
		case KeyEvent.KEYCODE_BACK://返回键
			finish();
			Intent intent = new Intent(AppDefaultActivity.this, MainActivity.class);
			startActivity(intent);
			break;
			
		default:
			break;
		}
		return false;
	}
	
	class AsyncTaskGetData extends AsyncTask<String, Integer, ArrayList<AppInfo>>{
		@Override
		protected ArrayList<AppInfo> doInBackground(String... params) {
			ArrayList<String> packageName = FileUtils.readListFromFile(MyConfig.FILENAME_APP_DEFAULT);
			ArrayList<AppInfo> allAppList = AppUtils.getLatestAppList(AppDefaultActivity.this);
			if(allAppList == null){
				return null;
			}
			if(packageName == null||packageName.isEmpty()){
				System.out.println("默认列表为空");
				ArrayList<String> allAppPackage = new ArrayList<String>();
				for(int i=0;i<allAppList.size();i++){
					allAppPackage.add(allAppList.get(i).getPackageName());
				}
				FileUtils.writeListToFile(allAppPackage, MyConfig.FILENAME_APP_DEFAULT);
				appList = allAppList;
			}else{
				System.out.println("默认列表不为空");
				ArrayList<String> allAppPackage = new ArrayList<String>();
				for(int i=0;i<allAppList.size();i++){
					allAppPackage.add(allAppList.get(i).getPackageName());
				}
				for(int i=0;i<packageName.size();i++){
					String package_name = packageName.get(i);
					if(allAppPackage.contains(package_name)){
						int index = allAppPackage.indexOf(package_name);
						AppInfo appInfo = allAppList.get(index);
						appList.add(appInfo);
					}
				}
			}	
			return appList;
		}
		
		@Override
		protected void onPostExecute(ArrayList<AppInfo> appList) {
			if(appList == null){
				Toast.makeText(AppDefaultActivity.this, "您还未安装任何第三方应用", Toast.LENGTH_SHORT).show();
				return;
			}
			//注册适配器
			AppListAdapter appListAdapter = new AppListAdapter(AppDefaultActivity.this, appList);
			listView.setAdapter(appListAdapter);
			dialog.dismiss();
		}
	}
	
	public ArrayList<AppInfo> getAppList(){
		ArrayList<String> packageName = FileUtils.readListFromFile(MyConfig.FILENAME_APP_DEFAULT);
		ArrayList<AppInfo> allAppList = AppUtils.getLatestAppList(AppDefaultActivity.this);
		if(allAppList == null){
			return null;
		}
		if(packageName == null||packageName.isEmpty()){
			System.out.println("默认列表为空");
			ArrayList<String> allAppPackage = new ArrayList<String>();
			for(int i=0;i<allAppList.size();i++){
				allAppPackage.add(allAppList.get(i).getPackageName());
			}
			FileUtils.writeListToFile(allAppPackage, MyConfig.FILENAME_APP_DEFAULT);
			appList = allAppList;
		}else{
			System.out.println("默认列表不为空");
			ArrayList<String> allAppPackage = new ArrayList<String>();
			for(int i=0;i<allAppList.size();i++){
				allAppPackage.add(allAppList.get(i).getPackageName());
			}
			for(int i=0;i<packageName.size();i++){
				String package_name = packageName.get(i);
				if(allAppPackage.contains(package_name)){
					int index = allAppPackage.indexOf(package_name);
					AppInfo appInfo = allAppList.get(index);
					appList.add(appInfo);
				}
			}
		}	
		return appList;
	}
	
}











