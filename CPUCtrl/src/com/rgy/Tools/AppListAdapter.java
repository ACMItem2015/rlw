package com.rgy.Tools;

import java.util.ArrayList;

import com.rgy.entity.AppInfo;
import com.rgy.setcpu.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppListAdapter extends BaseAdapter {
	
	Context context=null;
	ArrayList<AppInfo> list=null;
	
	public AppListAdapter(Context context,ArrayList<AppInfo> list) {
		super();
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		AppInfo appInfo = list.get(position);
		Drawable appIcon = appInfo.getAppIcon();
		String appName = appInfo.getAppName();
		String packageName = appInfo.getPackageName();
		
		//
		convertView = LayoutInflater.from(context).inflate(R.layout.list_item_app, null);
		
		ImageView imageView_icon = (ImageView)convertView.findViewById(R.id.app_icon);
		TextView textView_appName = (TextView)convertView.findViewById(R.id.app_name);
		TextView textView_packageName = (TextView)convertView.findViewById(R.id.package_name);
		
		imageView_icon.setImageDrawable(appIcon);

		textView_appName.setText(appName);
		textView_packageName.setText(packageName);
		
		return convertView;
	}
	
}







