package com.rgy.setcpu;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Application;

import com.rgy.Tools.AppUtils;
import com.rgy.Tools.DeepCpuData;
import com.rgy.Tools.FileUtils;
import com.rgy.Tools.GuardCpuData;
import com.rgy.Tools.MyConfig;
import com.rgy.entity.AppInfo;

public class MyApplication extends Application {

	private String cpuModel;
	
	private String cpuMaxFreq,cpuMinFreq;
	
	private ArrayList<AppInfo> allAppList;
	
	private String customSwitch, smartySwitch;
	
	public static String cpuCurUsage = "15%";
	public static String cpuCurFreq = "未知";
	
	@Override
	public void onCreate() {
		super.onCreate();
		//---------初始化cpu调控模式------------//
		String cpuGovernor = DeepCpuData.getCpuCurGovernor();
		if(cpuGovernor.equals("powersave")){
			cpuModel = MyConfig.CPUMODEL_POWERSAVE;
		}else if(cpuGovernor.equals("performance")){
			cpuModel = MyConfig.CPUMODEL_PERFORMANCE;
		}else{
			cpuModel = MyConfig.CPUMODEL_DEFAULT;
		}
		setCpuModel(cpuModel); // 初始化全局变量
		//---------初始化cpu固有最大与最小频率------------//
		cpuMaxFreq = GuardCpuData.getMaxCpuFreq();
		cpuMinFreq = GuardCpuData.getMinCpuFreq();
		//
		//----------------初始化模式列表文件----------------//
		
		FileUtils.makeDir();
		FileUtils.createFile();
		
		//------------------//
		allAppList = AppUtils.getLatestAppList(this);
		ArrayList<String> allAppPackageList = new ArrayList<String>();
		for(int i=0;i<allAppList.size();i++){
			allAppPackageList.add(allAppList.get(i).getPackageName());
		}
		//
		if(allAppList == null){//清空三个文件
			System.out.println("最新应用列表为空");
			FileUtils.writeListToFile(null, MyConfig.FILENAME_APP_DEFAULT);
			FileUtils.writeListToFile(null, MyConfig.FILENAME_APP_PERFORMANCE);
			FileUtils.writeListToFile(null, MyConfig.FILENAME_APP_POWERSAVE);
		}else{//更新三个文件
			System.out.println("最新应用列表不为空");
			AppUtils.removeUnloadedApp(allAppPackageList, MyConfig.FILENAME_APP_DEFAULT);
			AppUtils.removeUnloadedApp(allAppPackageList, MyConfig.FILENAME_APP_PERFORMANCE);
			AppUtils.removeUnloadedApp(allAppPackageList, MyConfig.FILENAME_APP_POWERSAVE);
			AppUtils.addLoadedAppToDefaultList(allAppPackageList);
		}
		
		//---------------初始化自定义模式服务开关状态------------------//
		String pkName = this.getPackageName();
		String customService = pkName+MyConfig.SERVICENAME_CUSTOM;
		if(AppUtils.isServiceWork(this, customService)){
			customSwitch = "Start";
		}else{
			customSwitch = "Stop";
		}
		
		//---------------初始化智能模式服务开关状态------------------//
		String smartyService = pkName+MyConfig.SERVICENAME_SMARTY;
		if(AppUtils.isServiceWork(this, smartyService)){
			smartySwitch = "Start";
		}else{
			smartySwitch = "Stop";
		}
		
		//--------------定时器获取当前cpu占用率及当前频率--------------//
		Timer timer = new Timer();
		timer.schedule(new UpdateCpuUsageTask(), 0, 1000);
		timer.schedule(new UpdateCpuFreqTask(), 0, 1000);
	}
	
	public void setCpuModel(String cpuModel){
		this.cpuModel = cpuModel;
	}

	public String getCpuModel() {
		return cpuModel;
	}

	public String getcpuMaxFreq() {
		return cpuMaxFreq;
	}

	public String getcpuMinFreq() {
		return cpuMinFreq;
	}
	
	public void setCustomSwitch(String customSwitch){
		this.customSwitch = customSwitch;
	}
	
	public String getCustomSwitch(){
		return customSwitch;
	}
	
	public void setSmartySwitch(String smartySwitch){
		this.smartySwitch = smartySwitch;
	}
	
	public String getSmartySwitch(){
		return smartySwitch;
	}
	
	///////
	
	class UpdateCpuUsageTask extends TimerTask{
		@Override
		public void run() {
			cpuCurUsage = GuardCpuData.getCpuUsage();
		}
	}
	
	class UpdateCpuFreqTask extends TimerTask{
		@Override
		public void run() {
			cpuCurFreq = GuardCpuData.getCurCpuFreq();
		}
	}
	
}
