package com.rgy.setcpu;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.don.floatwindow.FloatWindowService;
import com.don.floatwindow.SetActivity;
import com.rgy.Tools.AppUtils;
import com.rgy.Tools.DeepCpuData;
import com.rgy.Tools.MyConfig;
import com.rgy.Tools.SmallUtils;
import com.rgy.widget.MyProgressDialog;

public class MainActivity extends Activity {
	
	String TAG = "MainActivity";
	
	MyApplication myApp;
	ProgressDialog dialog;
	
	static Button btn_floatwin;//�����������İ�ť
	TextView tv_showfreq;
	public static TextView tv_showmodel;
	ImageButton btn_powersave,btn_performance,btn_defaultModel,btn_userModel;

	ImageButton btn_custom, btn_smart;
	
	Timer timer;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn_floatwin = (Button) findViewById(R.id.button_startfloat);
		
		tv_showfreq = (TextView)findViewById(R.id.textview_freq);
		tv_showmodel = (TextView)findViewById(R.id.textview_mode);
		
		btn_powersave = (ImageButton)findViewById(R.id.button_1);
		btn_defaultModel = (ImageButton)findViewById(R.id.button_2);
		btn_performance = (ImageButton)findViewById(R.id.button_3);
		btn_userModel = (ImageButton)findViewById(R.id.button_4);
		
		btn_smart = (ImageButton)findViewById(R.id.imagebutton_control1);
		btn_custom = (ImageButton)findViewById(R.id.imagebutton_control2);
		
		myApp = (MyApplication) getApplication();
		
		timer = new Timer();
		timer.schedule(new UpdateCpuFreqTask(), 0, 1000);
	    //timer.scheduleAtFixedRate(new UpdateCpuFreqTask(), 0, 1000);
	    
	    dialog = new MyProgressDialog(this);
	    
  		tv_showmodel.setText("��ǰģʽ:\n"+SmallUtils.convertCpuModelName(myApp.getCpuModel()));
  		//�ж�SmartyService�Ƿ���������
  		String smartySwitch = myApp.getSmartySwitch();
  		if(smartySwitch.equals("Stop")){
  			//btn_smart.setImageResource(R.drawable.shut);
  		}else{
  			//btn_smart.setImageResource(R.drawable.start);
  			tv_showmodel.setText("��ǰģʽ:\n"+ "����ģʽ");
  		}
  		//�ж�CustomService�Ƿ���������
  		String customSwitch = myApp.getCustomSwitch();
  		if(customSwitch.equals("Stop")){
  			//btn_custom.setImageResource(R.drawable.shut);
  		}else{
  			//btn_custom.setImageResource(R.drawable.start);
  			tv_showmodel.setText("��ǰģʽ:\n"+ "�Զ���ģʽ");
  		}
		
  		btn_powersave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
				new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_POWERSAVE);
			}
		});
  		
  		btn_performance.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
				new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_PERFORMANCE);
			}
		});
  		
  		btn_defaultModel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
				new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_DEFAULT);
			}
		});
  		
  		btn_userModel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ModelUserActivity.class);
				startActivity(intent);
			}
		});
  		
  		////////////////
  		
  		btn_custom.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AppAllActivity.class);
				startActivity(intent);
				//finish();
			}
		});
		
		/////////////////
		
		btn_smart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String smartySwitch = myApp.getSmartySwitch();
				if(smartySwitch.equals("Stop")){
					Intent intent = new Intent(MainActivity.this, SmartyService.class);
					startService(intent);
					Toast.makeText(MainActivity.this, "����ģʽ������", Toast.LENGTH_SHORT).show();
					myApp.setSmartySwitch("Start");
					//btn_smart.setImageResource(R.drawable.start);
					tv_showmodel.setText("��ǰģʽ:\n"+ "����ģʽ");
					// �����ʱ�Զ���ģʽ�����ڿ���״̬����ر���
					String customSwitch = myApp.getCustomSwitch();
					if(customSwitch.equals("Start")){
						Intent customIntent = new Intent(MainActivity.this, CustomService.class);
						stopService(customIntent);
						//
						myApp.setCustomSwitch("Stop");
						//btn_custom.setImageResource(R.drawable.shut);
					}
				}else{
					Intent intent = new Intent(MainActivity.this, SmartyService.class);
					stopService(intent);
					Toast.makeText(MainActivity.this, "����ģʽ��ֹͣ", Toast.LENGTH_SHORT).show();
					myApp.setSmartySwitch("Stop");
					//btn_smart.setImageResource(R.drawable.shut);
					new AsyncTaskSetModel().execute(MyConfig.CPUMODEL_DEFAULT);
				}
			}
		});
		
		/**
		 * ����������
		 */
		btn_floatwin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,SetActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	@Override
	protected void onDestroy() {
		timer.cancel();
		super.onDestroy();
		System.out.println("MainActivity������");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			// ͨ��id����𱻵���Ĳ˵�ѡ��
			case R.id.exit:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		//------------------------------------------------------------
		case KeyEvent.KEYCODE_BACK://���ؼ�
			finish();
			break;
			
		default:
			break;
		}
		return false;
	}
	
	//����msg
	public static Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
	        switch (msg.what) {
	        case 1:
	        	Bundle data=msg.getData();
	        	int cpuUsage=data.getInt("cpuUsage");
	        	btn_floatwin.setText(cpuUsage+"%");
	            break;
	        }
	    }
	};

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
				Toast.makeText(MainActivity.this, "���Ȼ�ȡrootȨ��", Toast.LENGTH_SHORT).show();
				return;
			}
			String state = result.substring(0, 2);
			String model = result.substring(2);
			String cpuModel_str = SmallUtils.convertCpuModelName(model);
			if(state.equals("ok")){
				myApp.setCpuModel(model);
				Toast.makeText(MainActivity.this, cpuModel_str+"���óɹ�", Toast.LENGTH_SHORT).show();
				Log.i(TAG, cpuModel_str+"���óɹ�");
				tv_showmodel.setText("��ǰģʽ:\n"+ cpuModel_str);
				//�ر� ����ģʽ �� �Զ���ģʽ
				String smartySwitch = myApp.getSmartySwitch();
				String customSwitch = myApp.getCustomSwitch();
				if(smartySwitch.equals("Start")){
					Intent intent = new Intent(MainActivity.this, SmartyService.class);
					stopService(intent);
					myApp.setSmartySwitch("Stop");
				}else if(customSwitch.equals("Start")){
					Intent intent = new Intent(MainActivity.this, CustomService.class);
					stopService(intent);
					myApp.setCustomSwitch("Stop");
				}
			}else{
				Toast.makeText(MainActivity.this, cpuModel_str+"����ʧ��", Toast.LENGTH_SHORT).show();
				Log.i(TAG, cpuModel_str+"����ʧ��");
			}
			////
			super.onPostExecute(result);
			dialog.dismiss();
		}
	}
	
	class UpdateCpuFreqTask extends TimerTask{
		@Override
		public void run() {
			new AsyncTaskUpdateCpuFreq().execute();
		}
	}
	
	class AsyncTaskUpdateCpuFreq extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			String cpuCurFreq = MyApplication.cpuCurFreq;
			return cpuCurFreq;
		}
		
		@Override
		protected void onPostExecute(String result) {
			tv_showfreq.setText("��ǰcpuƵ��:\n"+result+"KHz");
			super.onPostExecute(result);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
