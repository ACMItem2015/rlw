package com.don.floatwindow.adapter;

import java.util.List;
import java.util.Map;

import com.don.floatwindow.FloatWindowService;
import com.rgy.setcpu.R;
import com.don.floatwindow.utils.MyWindowManager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MySimpleAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> data;// 数据源
	private int resource;// 布局文件对应的ID
	private String[] from;// 索引
	private int[] to;// 对应组件ID
	private LayoutInflater inflater;// 用于动态载入界面的inflater

	private ImageView option;

	public MySimpleAdapter(Context context, List<Map<String, Object>> data,
			int resource, String[] from, int[] to) {
		this.context = context;
		this.data = data;
		this.resource = resource;
		this.from = from;
		this.to = to;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(resource, null);
		// 为ImageView和TextView控件设置显示效果
		for (int i = 0; i < to.length; i++) {
			if (convertView.findViewById(to[i]) instanceof ImageView) {
//				ImageView imageView = (ImageView) convertView
//						.findViewById(to[i]);
//				// 获得数据源中，对应该列的数据项
				Map<String, Object> item = data.get(position);
//				// 设置imageView为对应的图像
//				imageView.setBackgroundResource((Integer) item.get(from[i]));
			} else if (convertView.findViewById(to[i]) instanceof TextView) {
				TextView textView = (TextView) convertView.findViewById(to[i]);
				// 获得数据源中，对应该列的数据项
				Map<String, Object> item = data.get(position);
				// 设置textView为对应的文字
				textView.setText((String) item.get(from[i]));
			}
		}

		// 为button绑定单击事件
		option = (ImageView) convertView.findViewById(R.id.confirm);
		if (MyWindowManager.smallWindow != null) {
			option.setImageResource(R.drawable.start);
		} else {
			option.setImageResource(R.drawable.shut);
		}

		final int p = position;

		option.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (p) {
				/**
				 * 开启悬浮窗选项
				 * 
				 * 有悬浮窗显示返回true，没有的话返回false。
				 */
				case 0:
					if (MyWindowManager.smallWindow != null) {
						option.setImageResource(R.drawable.shut);
						// 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
						MyWindowManager.removeBigWindow(context);
						MyWindowManager.removeSmallWindow(context);
						MyWindowManager.removeRocketWindow1(context);
						MyWindowManager.removeRocketWindow2(context);
						Intent intent = new Intent(context,
								FloatWindowService.class);
						context.stopService(intent);
					} else {
						option.setImageResource(R.drawable.start);
						Intent intent = new Intent(context,
								FloatWindowService.class);
						context.startService(intent);
					}

					break;

				default:
					break;
				}
			}

		});

		return convertView;
	}

}
