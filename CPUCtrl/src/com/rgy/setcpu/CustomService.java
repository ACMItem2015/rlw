package com.rgy.setcpu;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

import com.rgy.Tools.AppUtils;
import com.rgy.Tools.DeepCpuData;
import com.rgy.Tools.FileUtils;
import com.rgy.Tools.MyConfig;
import com.rgy.Tools.SmallUtils;

public class CustomService extends Service {
	
	MyApplication myApp;
	Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		myApp = (MyApplication) getApplication();
		
		timer = new Timer();
		timer.schedule(new CheckAppTask(), 0, 1000);//���ӳ�Ч��
	    //timer.scheduleAtFixedDealy(new CheckAppTask(), 0, 1000);
		System.out.println("�Զ�����ط���������");
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override  
    public void onDestroy() {
		timer.cancel();
		System.out.println("�Զ�����ط�����ֹͣ");
        super.onDestroy();  
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
			if(flag&flag1&flag2){
				result = "ok"+model;
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result.equals("")){
				Toast.makeText(CustomService.this, "���Ȼ�ȡrootȨ��", Toast.LENGTH_SHORT).show();
				stopSelf();
				return;
			}
			String state = result.substring(0, 2);
			String model = result.substring(2);
			String cpuModel_str = SmallUtils.convertCpuModelName(model);
			if(state.equals("ok")){
				myApp.setCpuModel(model);
				//�����ΪĬ��ģʽ���򵯳���ʾ��䣻���򲻵���
				if(!model.equals(MyConfig.CPUMODEL_DEFAULT)){
					Toast.makeText(CustomService.this, cpuModel_str+"�����ɹ�", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(CustomService.this, cpuModel_str+"����ʧ��", Toast.LENGTH_SHORT).show();
			}
			////
			super.onPostExecute(result);
		}
	}
	
	/**
	 * CheckAppTask��ʱ��
	 * @author Administrator
	 *
	 */
	class CheckAppTask extends TimerTask{
		@Override
		public void run() {
			new AsyncTaskCheckApp().execute();
		}
	}
	
	/**
	 * ���topapp������һ���б���������Ӧģʽ
	 * @author Administrator
	 *
	 */
	class AsyncTaskCheckApp extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			
			ArrayList<String> performanceList = FileUtils.readListFromFile(MyConfig.FILENAME_APP_PERFORMANCE);
			ArrayList<String> powersaveList = FileUtils.readListFromFile(MyConfig.FILENAME_APP_POWERSAVE);

			String packageName = AppUtils.getCurrentPk(CustomService.this);
			
			if(performanceList!=null&&performanceList.contains(packageName)){
				if(!myApp.getCpuModel().equals(MyConfig.CPUMODEL_PERFORMANCE)){
					new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_PERFORMANCE);
				}
			}else if(powersaveList!=null&&powersaveList.contains(packageName)){
				if(!myApp.getCpuModel().equals(MyConfig.CPUMODEL_POWERSAVE)){
					new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_POWERSAVE);
				}
			}else{//��ǰӦ�ö��������������б��У�������Ĭ��ģʽ
				if(!myApp.getCpuModel().equals(MyConfig.CPUMODEL_DEFAULT)){
					new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_DEFAULT);
				}
			}
			
			return null;
		}
	}
	
}
