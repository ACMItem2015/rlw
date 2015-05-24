package com.rgy.setcpu;
/**
 * 用户模式下 频率选择列表
 */
import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rgy.Tools.DeepCpuData;
import com.rgy.Tools.SmallUtils;
import com.rgy.widget.MyProgressDialog;

public class ModelUserActivity extends ListActivity {
	
	final String TAG = "UserModelActivity";  
	
	ProgressDialog dialog;
	
	MyApplication myApp;
	
	ArrayList<String> items,showItems;
	
	ImageButton back;
	
	TextView tv_title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usermodel);
		items = DeepCpuData.getCpuAvailableFreq();
		showItems = new ArrayList<String>();
		for(int i=0;i<items.size();i++){
			showItems.add(items.get(i)+" KHz");
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item_1, showItems);
		setListAdapter(adapter);
		myApp = (MyApplication) getApplication();
		dialog = new MyProgressDialog(this);
		//------------------------------//
		/**
		 * 顶部panel
		 */
		tv_title = (TextView) findViewById(R.id.textview_actionbar);
		tv_title.setText("选择频率");
		back=(ImageButton) findViewById(R.id.imagebutton_actionbar);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String freq_str = items.get(position);
		dialog.show();
		new AsyncTaskSetCpuFreq().execute(freq_str);
	}
	
	class AsyncTaskSetCpuFreq extends AsyncTask<String, Integer, String>{
		@Override
		protected String doInBackground(String... params) {
			String freq_str = params[0];
			long freq = Long.parseLong(freq_str);
			boolean flag_min = DeepCpuData.setMinCpuFreq(freq);
			boolean flag_max = DeepCpuData.setMaxCpuFreq(freq);
			
			String result = "";
			if(flag_min&flag_max){
				result = "ok"+ freq_str;
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			String state = result.substring(0, 2);
			String freq_str = result.substring(2);
			if(state.equals("ok")){
				String cpuModel = SmallUtils.convertCpuModelName("用户模式");
				myApp.setCpuModel(cpuModel);
				Toast.makeText(ModelUserActivity.this, "已设置当前频率为："+freq_str+" KHz", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ModelUserActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}else {
				Toast.makeText(ModelUserActivity.this, "cpu频率设置失败！",Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
			dialog.dismiss();
		}
	}

}
