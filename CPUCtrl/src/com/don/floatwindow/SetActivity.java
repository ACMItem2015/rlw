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
	private List<Map<String, Object>> data;// ����Դ
	private Map<String, Object> item;

	// ģ������
	private int image[] = { R.drawable.ic_launcher };
	private String description[] = { "����������" };
	private int length = 1;
	
	ImageButton back;
	
	TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ���ر�����
		setContentView(R.layout.activity_set);

		// ��ȡ�ؼ�
		initView();

		// ��������Դ
		initData();

		// ���ɼ�������
		initAdapter();
		
		// �ؼ�����
		initEvent();
	}

	/**
	 * �ؼ�����
	 */
	private void initEvent() {
		tv_title.setText("����������");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * ���ɼ�������
	 */
	private void initAdapter() {
		// ���ɼ�������
		MySimpleAdapter listAdapter = new MySimpleAdapter(this, data,
				R.layout.listview_item, new String[] { "photo", "description1" },
				new int[] { R.id.photo, R.id.description1 });

		// ����������
		mListView.setAdapter(listAdapter);
	}

	/**
	 * ��������Դ
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
		 //******************* ����panel	*******************
		tv_title = (TextView) findViewById(R.id.textview_actionbar);
		back=(ImageButton) findViewById(R.id.imagebutton_actionbar);
		 //******************* ����panel	*******************
	}

}