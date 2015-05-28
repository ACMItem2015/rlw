package com.wyx.wallpaper;

import java.util.ArrayList;
import java.util.Timer;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

import com.rgy.setcpu.R;
import com.wyx.draw.BatteryDraw;
import com.wyx.draw.CpuFreqDraw;
import com.wyx.draw.CpuUsageDraw;
import com.wyx.draw.MemoryDraw;
import com.wyx.draw.TimeDraw;
import com.wyx.util.BatteryChangeReceiver;

public class LiveWallpaper extends WallpaperService {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.w("LiveWallpaper.Engine", "WallpaperService onCreate");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.w("LiveWallpaper.Engine", "WallpaperService onDestroy");
	}
	
	@Override
	public Engine onCreateEngine() {
		Log.w("LiveWallpaper.Engine", "onCreateEngine");
		return new LiveWallpaperEngine();
	}
	
	/**
	 * WallpaperService内部的Engine（引擎类）
	 *
	 */
	class LiveWallpaperEngine extends Engine{
		
		private Paint mPaint=new Paint();
		private Canvas c;
		
		private TimeDraw timeDraw;
		private CpuFreqDraw cpuFreqDraw; 
		private BatteryDraw batteryDraw;
		private CpuUsageDraw cpuUsageDraw;
		private MemoryDraw memoryDraw;
		
		private BatteryChangeReceiver mBroadcastReceiver=new BatteryChangeReceiver();
		
		private Timer timer=new Timer();
		
		private ArrayList<String> freqList=new ArrayList<String>();
		private ArrayList<Integer> usageList=new ArrayList<Integer>();
	//-----------以上都是变量------------------------------------------	
		
		public LiveWallpaperEngine(){
			init();
			
		}
		
		void init(){
			
			mPaint.setColor(0xffffffff);
			mPaint.setAntiAlias(true);
			mPaint.setStrokeWidth(2);
			mPaint.setStrokeCap(Paint.Cap.ROUND);
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		}
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {//创建的时候运行
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(true);
			Log.w("LiveWallpaper.Engine", "onCreate");
			
			IntentFilter filter = new IntentFilter();//接听广播，开启
            filter.addAction(Intent.ACTION_BATTERY_CHANGED);
            registerReceiver(mBroadcastReceiver, filter);

			timer.schedule(new TimerTask() {//开定时器刷新，解决跳秒问题！！！
				@Override
				public void run() {
					drawWallpaper();//mHandler.post(mRun);mHandler貌似可以取消了
				}
			},0,1000);
            
		}
		
		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {//创建的时候运行
			super.onSurfaceCreated(holder);
			Log.w("LiveWallpaper.Engine", "onSurfaceCreated");
		}
		
		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,//创建的时候运行
				int width, int height) {
			super.onSurfaceChanged(holder, format, width, height);
			//drawWallpaper();
			Log.w("LiveWallpaper.Engine", "onSurfaceChanged");
		}
		
		@Override
		public void onVisibilityChanged(boolean visible) {
			super.onVisibilityChanged(visible);
			if(visible){
				drawWallpaper();
			}
			Log.w("LiveWallpaper.Engine", "onVisibilityChanged");
		}
		
		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			Log.w("LiveWallpaper.Engine", "onSurfaceDestroyed");
			
		}
		
		@Override
		public void onDestroy() {
			super.onDestroy();
			Log.w("LiveWallpaper.Engine", "onDestroy");
			
			unregisterReceiver(mBroadcastReceiver);//关闭广播
			
			timer.cancel();//关闭定时器
		}
		
		//绘制桌面的方法
		public void drawWallpaper(){
			
			final SurfaceHolder holder = getSurfaceHolder();
			try{
				c=holder.lockCanvas();
				if(c!=null){
					
					Resources res=getResources();
                	Bitmap bmp=BitmapFactory.decodeResource(res,R.drawable.wallpaper4);
                	c.drawBitmap(bmp,-50,-200,mPaint);//坐标需要算一算
					
                	c.translate(40, 80);//
                	Log.w("LiveWallpaper.Engine", "start-----------drawTime");
					drawTime();
					Log.w("LiveWallpaper.Engine", "start-----------drawCpuFreq");
					drawCpuFreq();
					Log.w("LiveWallpaper.Engine", "start-----------drawBattery");
					drawBattery();
					Log.w("LiveWallpaper.Engine", "start-----------drawCpuUsage");
					drawCpuUsage();
					Log.w("LiveWallpaper.Engine", "start-----------drawMemory");
					drawMemory();
				}
			}finally{
				if(c!=null){
					holder.unlockCanvasAndPost(c);
				}
			}
			Log.w("LiveWallpaper.Engine", "drawWallpaper");
		}
		
		//绘制时间
		void drawTime(){
			if(timeDraw==null){
				timeDraw=new TimeDraw(c, mPaint);
				timeDraw.draw();
			}else{
				timeDraw.draw();
			}
        }
        
		//绘制CPU频率
        void drawCpuFreq(){
        	if(cpuFreqDraw==null){
        		cpuFreqDraw=new CpuFreqDraw(c, freqList, mPaint,timer);
        		cpuFreqDraw.draw();
			}else{
				cpuFreqDraw.draw();
			}
        }

        //绘制电池信息
        void drawBattery(){
        	if(batteryDraw==null){
        		batteryDraw=new BatteryDraw(c, mPaint);
        		batteryDraw.draw();
			}else{
				batteryDraw.draw();
			}
        }
        
        //绘制CPU利用率
        void drawCpuUsage(){
        	if(cpuUsageDraw==null){
        		cpuUsageDraw=new CpuUsageDraw(c, usageList, mPaint,timer);
        		cpuUsageDraw.draw();
			}else{
				cpuUsageDraw.draw();
			}
        }
        
        //绘制存储信息
        void drawMemory(){
        	if(memoryDraw==null){
        		memoryDraw=new MemoryDraw(getApplicationContext(),c, mPaint,timer);
        		memoryDraw.draw();
			}else{
				memoryDraw.draw();
			}
        }
    
	}

	
}
