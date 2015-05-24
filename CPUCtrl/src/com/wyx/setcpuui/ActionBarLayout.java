package com.wyx.setcpuui;

import com.rgy.setcpu.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ActionBarLayout extends LinearLayout {

	public ActionBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.top,this);
		
	}
	
}
