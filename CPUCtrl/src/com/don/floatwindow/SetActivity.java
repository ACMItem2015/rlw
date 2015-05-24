package com.don.floatwindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rgy.setcpu.R;
import com.don.floatwindow.adapter.MySimpleAdapter;

import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


public class SetActivity extends Activity {

	private ListView mListView;
	private List<Map<String, Object>> data;// 数据源
	private Map<String, Object> item;

	// 模拟数据
	private int image[] = { R.drawable.ic_launcher };
	private String description[] = { "开启悬浮窗" };
	private int length = 1;
	
	ImageButton back;
	
	TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题栏
		setContentView(R.layout.activity_set);

		// 获取控件
		initView();

		// 生成数据源
		initData();

		// 生成简单适配器
		initAdapter();
		
		// 控件操作
		initEvent();
	}

	/**
	 * 控件操作
	 */
	private void initEvent() {
		tv_title.setText("悬浮窗设置");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 生成简单适配器
	 */
	private void initAdapter() {
		// 生成简单适配器
		MySimpleAdapter listAdapter = new MySimpleAdapter(this, data,
				R.layout.listview_item, new String[] { "photo", "description1" },
				new int[] { R.id.photo, R.id.description1 });

		// 设置适配器
		mListView.setAdapter(listAdapter);
	}

	/**
	 * 生成数据源
	 */
	private void initData() {
		data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < length; i++) {
			item = new HashMap<String, Object>();
			item.put("photo", image[i]);
			item.put("description1", description[i]);
			data.add(item);
		}
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.id_listview);
		 //******************* 顶部panel	*******************
		tv_title = (TextView) findViewById(R.id.textview_actionbar);
		back=(ImageButton) findViewById(R.id.imagebutton_actionbar);
		 //******************* 顶部panel	*******************
	}

}