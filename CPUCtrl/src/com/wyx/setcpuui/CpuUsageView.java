package com.wyx.setcpuui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.rgy.setcpu.MainActivity;
import com.rgy.setcpu.MyApplication;

public class CpuUsageView extends View {

	private Paint mPaint1=new Paint();
	private Paint mPaint2=new Paint();
	
	private ArrayList<Integer> list=new ArrayList<Integer>();

	private float measureWidth;
	private float measureHeigth;
	
//	private final Handler mHandler=new Handler();
	private final Runnable mRun=new Runnable() {
		@Override
		public void run() {
			invalidate();
//			Log.w("Wallpaper","invalidate()");
		}
	};
	
	public CpuUsageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initDraw();
	}

	public CpuUsageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDraw();
	}

	public CpuUsageView(Context context) {
		super(context);
		initDraw();
	}
	
	private void initDraw(){
		mPaint1.setStyle(Style.FILL);
		mPaint1.setStrokeWidth(15f);
		mPaint1.setColor(0xffffffff);
		mPaint1.setAntiAlias(true);
		
		mPaint2.setStyle(Style.FILL);
		mPaint2.setStrokeWidth(15f);
		mPaint2.setColor(0xffffffff);
		mPaint2.setAlpha(80);
		mPaint2.setAntiAlias(true);
		
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
//		Log.w("Wallpaper","removeCallbacks(mRun)");
		removeCallbacks(mRun);
	}
	
	/**
	 * 在画布中画画的方法(详细步骤)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		//super.onDraw(canvas);
		//获取CPU利用率
		String str_cpuUsage = MyApplication.cpuCurUsage;
		int cpuUsage = Integer.parseInt(str_cpuUsage.substring(0, str_cpuUsage.length()-1));
//		System.out.println("cpu占用率："+cpuUsage);
		int result=cpuUsage/10;
		
		//使用Handler给MainActivity发送msg
		Message msg=new Message();
		msg.what=1;
		Bundle data=new Bundle();
		data.putInt("cpuUsage", cpuUsage);
		msg.setData(data);
		MainActivity.mHandler.sendMessage(msg);
		
		//将历史记录保存在list中，绘图时需要
		list.add(0,result);

//		Log.w("Wallpaper",result+"");
		
//		canvas.drawLine(0,measureHeigth,measureWidth,measureHeigth,mPaint1);
		//开始绘图
		canvas.save();
		canvas.translate((measureWidth-(50f*12f+13f*10f))/2f,(measureHeigth-435f)/2f);//居中
		//Log.w("Wallpaper",measureWidth+"");
		//Log.w("Wallpaper",measureHeigth+"");
		
		for(int i=0;i<list.size();i++){
			for(int j=0;j<list.get(i);j++){
				canvas.drawLine(i*60+10,200-j*20,i*60+60,200-j*20,mPaint1);
				canvas.drawLine(i*60+10,220+j*20,i*60+60,220+j*20,mPaint2);
			}
		}
		if(list.size()==12){
			list.remove(11);
		}
		
		canvas.restore();

		postDelayed(mRun, 1000);
		
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int)measureWidth, (int)measureHeigth);
    }

}
