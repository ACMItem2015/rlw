package com.rgy.setcpu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rgy.Tools.AppUtils;
import com.rgy.Tools.MyConfig;
import com.rgy.Tools.SmallUtils;
import com.rgy.entity.AppInfo;
import com.rgy.widget.MyImageView;

public class AppEachInfoActivity extends Activity {
	
	ImageView imageView;
	
	TextView tv_appname,tv_packagename,tv_versionname,tv_model,tv_tip;
	
	MyImageView btn_defaultModel,btn_performance,btn_powersave,btn_goback;
	
	AppInfo appInfo = null;
	
	String packageName = "";
	String model = "";
	
	ImageButton back;
	
	TextView tv_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_each_appinfo);
		
		imageView = (ImageView)findViewById(R.id.app_icon);
		tv_appname = (TextView)findViewById(R.id.app_name);
		tv_packagename = (TextView)findViewById(R.id.package_name);
		tv_versionname = (TextView)findViewById(R.id.version_name);
		tv_model = (TextView)findViewById(R.id.model);
		tv_tip = (TextView)findViewById(R.id.tip);
		
		btn_defaultModel = (MyImageView)findViewById(R.id.default_model);
		btn_performance = (MyImageView)findViewById(R.id.performance_model);
		btn_powersave = (MyImageView)findViewById(R.id.powersave_model);
		btn_goback = (MyImageView)findViewById(R.id.go_back);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		packageName = bundle.getString("packageName");
		model = bundle.getString("model");
		
		if(model.equals(MyConfig.CPUMODEL_DEFAULT)){
			btn_defaultModel.setVisibility(View.GONE);
		}else if(model.equals(MyConfig.CPUMODEL_PERFORMANCE)){
			btn_performance.setVisibility(View.GONE);
		}else if(model.equals(MyConfig.CPUMODEL_POWERSAVE)){
			btn_powersave.setVisibility(View.GONE);
		}else if(model.equals("undefined")){
			tv_model.setVisibility(View.GONE);
			tv_tip.setVisibility(View.GONE);
			btn_defaultModel.setVisibility(View.INVISIBLE);
			btn_performance.setVisibility(View.INVISIBLE);
			btn_powersave.setVisibility(View.INVISIBLE);
		}
		
		//---------------------------//
		
		ArrayList<AppInfo> appList = AppUtils.getLatestAppList(this);
		for(int i=0;i<appList.size();i++){
			if(packageName.equals(appList.get(i).getPackageName())){
				appInfo = appList.get(i);
				break;
			}
		}
		
		if(appInfo == null){
			Toast.makeText(this, "该应用刚刚被卸载！", Toast.LENGTH_SHORT).show();
		}else{
			String appName = appInfo.getAppName();
			String appPackageName = appInfo.getPackageName();
			String versionName = appInfo.getVersionName();
			Drawable appIcon = appInfo.getAppIcon();
			String cpuModel = SmallUtils.convertCpuModelName(model);
			
			imageView.setImageDrawable(appIcon);
			tv_appname.setText(appName);
			tv_packagename.setText(appPackageName);
			tv_versionname.setText("版本号："+versionName);
			tv_model.setText("cpu模式："+cpuModel);
		}
		
		//---------------------------//
		
		/**
		 * 顶部panel
		 */
		tv_title = (TextView) findViewById(R.id.textview_actionbar);
		tv_title.setText("应用详情");
		back=(ImageButton) findViewById(R.id.imagebutton_actionbar);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		btn_defaultModel.setOnClickIntent(new MyImageView.OnViewClickListener() {
			@Override
			public void onViewClick(MyImageView view) {
				if(model.equals(MyConfig.CPUMODEL_PERFORMANCE)){
					AppUtils.addAppToListFile(MyConfig.FILENAME_APP_DEFAULT, packageName);        
					AppUtils.removeAppFromListFile(MyConfig.FILENAME_APP_PERFORMANCE, packageName);
				}else if(model.equals(MyConfig.CPUMODEL_POWERSAVE)){
					AppUtils.addAppToListFile(MyConfig.FILENAME_APP_DEFAULT, packageName);        
					AppUtils.removeAppFromListFile(MyConfig.FILENAME_APP_POWERSAVE, packageName);
				}
				//
				Intent intent = new Intent(AppEachInfoActivity.this,AppDefaultActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		btn_performance.setOnClickIntent(new MyImageView.OnViewClickListener() {
			@Override
			public void onViewClick(MyImageView view) {
				if(model.equals(MyConfig.CPUMODEL_DEFAULT)){
					AppUtils.addAppToListFile(MyConfig.FILENAME_APP_PERFORMANCE, packageName);        
					AppUtils.removeAppFromListFile(MyConfig.FILENAME_APP_DEFAULT, packageName);
				}else if(model.equals(MyConfig.CPUMODEL_POWERSAVE)){
					AppUtils.addAppToListFile(MyConfig.FILENAME_APP_PERFORMANCE, packageName);        
					AppUtils.removeAppFromListFile(MyConfig.FILENAME_APP_POWERSAVE, packageName);
				}
				//
				Intent intent = new Intent(AppEachInfoActivity.this,AppPerformanceActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		btn_powersave.setOnClickIntent(new MyImageView.OnViewClickListener() {
			@Override
			public void onViewClick(MyImageView view) {
				if(model.equals(MyConfig.CPUMODEL_DEFAULT)){
					AppUtils.addAppToListFile(MyConfig.FILENAME_APP_POWERSAVE, packageName);        
					AppUtils.removeAppFromListFile(MyConfig.FILENAME_APP_DEFAULT, packageName);
				}else if(model.equals(MyConfig.CPUMODEL_PERFORMANCE)){
					AppUtils.addAppToListFile(MyConfig.FILENAME_APP_POWERSAVE, packageName);        
					AppUtils.removeAppFromListFile(MyConfig.FILENAME_APP_PERFORMANCE, packageName);
				}
				//
				Intent intent = new Intent(AppEachInfoActivity.this,AppPowersaveActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		btn_goback.setOnClickIntent(new MyImageView.OnViewClickListener() {
			@Override
			public void onViewClick(MyImageView view) {
//				Intent intent = new Intent(AppEachInfoActivity.this,AppAllActivity.class);
//				if(model.equals(MyConfig.CPUMODEL_DEFAULT)){
//					intent = new Intent(AppEachInfoActivity.this,AppDefaultActivity.class);
//				}else if(model.equals(MyConfig.CPUMODEL_PERFORMANCE)){
//					intent = new Intent(AppEachInfoActivity.this,AppPerformanceActivity.class);
//				}else if(model.equals(MyConfig.CPUMODEL_POWERSAVE)){
//					intent = new Intent(AppEachInfoActivity.this,AppPowersaveActivity.class);
//				}else if(model.equals("undefined")){
//					intent = new Intent(AppEachInfoActivity.this,AppAllActivity.class);
//				}
//				startActivity(intent);
				finish();
			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		//------------------------------------------------------------
		case KeyEvent.KEYCODE_BACK://返回键
			finish();
			break;
			
		default:
			break;
		}
		return false;
	}
	
}









