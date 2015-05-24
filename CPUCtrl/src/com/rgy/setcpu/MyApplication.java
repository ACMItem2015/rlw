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
	public static String cpuCurFreq = "δ֪";
	
	@Override
	public void onCreate() {
		super.onCreate();
		//---------��ʼ��cpu����ģʽ------------//
		String cpuGovernor = DeepCpuData.getCpuCurGovernor();
		if(cpuGovernor.equals("powersave")){
			cpuModel = MyConfig.CPUMODEL_POWERSAVE;
		}else if(cpuGovernor.equals("performance")){
			cpuModel = MyConfig.CPUMODEL_PERFORMANCE;
		}else{
			cpuModel = MyConfig.CPUMODEL_DEFAULT;
		}
		setCpuModel(cpuModel); // ��ʼ��ȫ�ֱ���
		//---------��ʼ��cpu�����������СƵ��------------//
		cpuMaxFreq = GuardCpuData.getMaxCpuFreq();
		cpuMinFreq = GuardCpuData.getMinCpuFreq();
		//
		//----------------��ʼ��ģʽ�б��ļ�----------------//
		
		FileUtils.makeDir();
		FileUtils.createFile();
		
		//------------------//
		allAppList = AppUtils.getLatestAppList(this);
		ArrayList<String> allAppPackageList = new ArrayList<String>();
		for(int i=0;i<allAppList.size();i++){
			allAppPackageList.add(allAppList.get(i).getPackageName());
		}
		//
		if(allAppList == null){//��������ļ�
			System.out.println("����Ӧ���б�Ϊ��");
			FileUtils.writeListToFile(null, MyConfig.FILENAME_APP_DEFAULT);
			FileUtils.writeListToFile(null, MyConfig.FILENAME_APP_PERFORMANCE);
			FileUtils.writeListToFile(null, MyConfig.FILENAME_APP_POWERSAVE);
		}else{//���������ļ�
			System.out.println("����Ӧ���б�Ϊ��");
			AppUtils.removeUnloadedApp(allAppPackageList, MyConfig.FILENAME_APP_DEFAULT);
			AppUtils.removeUnloadedApp(allAppPackageList, MyConfig.FILENAME_APP_PERFORMANCE);
			AppUtils.removeUnloadedApp(allAppPackageList, MyConfig.FILENAME_APP_POWERSAVE);
			AppUtils.addLoadedAppToDefaultList(allAppPackageList);
		}
		
		//---------------��ʼ���Զ���ģʽ���񿪹�״̬------------------//
		String pkName = this.getPackageName();
		String customService = pkName+MyConfig.SERVICENAME_CUSTOM;
		if(AppUtils.isServiceWork(this, customService)){
			customSwitch = "Start";
		}else{
			customSwitch = "Stop";
		}
		
		//---------------��ʼ������ģʽ���񿪹�״̬------------------//
		String smartyService = pkName+MyConfig.SERVICENAME_SMARTY;
		if(AppUtils.isServiceWork(this, smartyService)){
			smartySwitch = "Start";
		}else{
			smartySwitch = "Stop";
		}
		
		//--------------��ʱ����ȡ��ǰcpuռ���ʼ���ǰƵ��--------------//
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
