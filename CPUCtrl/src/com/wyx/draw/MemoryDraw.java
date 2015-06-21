package com.wyx.draw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

public class MemoryDraw extends WallpaperDraw{
	private Context context;
	private Paint totalPaint=new Paint();
	private Paint usagePaint=new Paint();
	
	private String ramInfo="",storageInfo="",sdInfo="";
	private float ramPercentage=0,storagePercentage=0,sdPercentage=0;
	
	private Timer timer;
	
	public MemoryDraw(Context context,Canvas c,Paint mPaint,Timer timer){
		super(c, mPaint);
		this.context=context;
		//绘制存储总长度的画笔
		totalPaint.setStrokeWidth(10);
		totalPaint.setStyle(Style.FILL);
		totalPaint.setAntiAlias(true);
		totalPaint.setColor(0xffffffff);
		//绘制当前存储长度的画笔
		usagePaint.setStrokeWidth(10);
		usagePaint.setStyle(Style.FILL);
		usagePaint.setAntiAlias(true);
		usagePaint.setColor(0xff87CEEB);
		
		this.timer=timer;
		this.timer.schedule(new TimerTask() {
			@Override
			public void run() {
				new MemoryAsyncTask().execute();
				//Log.w("LiveWallpaper.Engine", "Memory_TimerTask");
			}
		}, 0, 1000);
	}
	
	@Override
	public void draw(){
		
		c.save();
		c.translate(0,600);
		
		c.drawText("RAM",0,20,mPaint);
		c.drawText(ramInfo,400,20,mPaint);
		c.drawLine(0,40,640,40,totalPaint);
		c.drawLine(0,40,(ramPercentage*640f),40,usagePaint);
		
		c.drawText("Storage",0,160,mPaint);
		c.drawText(storageInfo,400,160,mPaint);
		c.drawLine(0,180,640,180,totalPaint);
		c.drawLine(0,180,storagePercentage*640f,180,usagePaint);
		
		c.drawText("SD-Card",0,300,mPaint);
		c.drawText(sdInfo,400,300,mPaint);
		c.drawLine(0,320,640,320,totalPaint);
		c.drawLine(0,320,sdPercentage*640f,320,usagePaint);
		
		c.restore();
	}
	
	//------------------------以下是读取存储数据的方法----------------------------------------
	public String getRamInfo(){
		String usage=Formatter.formatFileSize(context,getRamSize()-getFreeRamSize());
		String total=Formatter.formatFileSize(context,getRamSize());
		return usage+"/"+total;
	}
	
	public float getRamPercentage(){
		return ((getRamSize()-getFreeRamSize())*1f)/(getRamSize()*1f);
	}
	
	public long getFreeRamSize(){//有疑问
		MemoryInfo memoryInfo = new MemoryInfo();  
		ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.getMemoryInfo(memoryInfo) ;  
        long availMemSize = memoryInfo.availMem;
        //Log.w("MemoryDraw", Formatter.formatFileSize(context, availMemSize));
        //return Formatter.formatFileSize(context, availMemSize);
        return availMemSize;
	}

	public long getRamSize(){//有疑问
		String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            br.close();
            fr.close();
            //
            String allMemSizeStr = memoryLine.substring(memoryLine.indexOf(":")+1,memoryLine.indexOf("kB")).trim();
            long allMemSize = Integer.parseInt(allMemSizeStr) * 1024;
            //Log.w("MemoryDraw", Formatter.formatFileSize(context, allMemSize));
            //return Formatter.formatFileSize(context, allMemSize);
            return allMemSize;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return "not data";
        return 0;
	}
	
	public String getStorageInfo(){
		String usage=Formatter.formatFileSize(context,getStorageSize()-getFreeStorageSize());
		String total=Formatter.formatFileSize(context,getStorageSize());
		return usage+"/"+total;
	}
	
	public float getStoragePercentage(){
		return ((getStorageSize()-getFreeStorageSize())*1f)/(getStorageSize()*1f);
	}
	
	public long getFreeStorageSize(){
		File path=Environment.getDataDirectory();
		StatFs mDataFileStats = new StatFs(path.getPath()); 
	    long freeStorage = (long) mDataFileStats.getBlockSize() * mDataFileStats.getAvailableBlocks(); 
	    //Log.w("MemoryDraw", Formatter.formatFileSize(context, freeStorage));
	    return freeStorage;
	    //return Formatter.formatFileSize(context, freeStorage); 
	}
	
	public long getStorageSize(){
		File path=Environment.getDataDirectory();
		StatFs mDataFileStats = new StatFs(path.getPath());
		long totalStorage=(long)mDataFileStats.getBlockSize() * mDataFileStats.getBlockCount();
		//Log.w("MemoryDraw", Formatter.formatFileSize(context, totalStorage));
		return totalStorage;
		//return Formatter.formatFileSize(context, totalStorage); 
	}
	
	public String getSDInfo(){
		String free=Formatter.formatFileSize(context,getSDSize()-getFreeSDSize());
		String total=Formatter.formatFileSize(context,getSDSize());
		return free+"/"+total;
	}
	
	public float getSDPercentage(){
		return ((getSDSize()-getFreeSDSize())*1f)/(getSDSize()*1f);
	}
	
	public long getFreeSDSize(){
		File path = Environment.getExternalStorageDirectory();  
	    // StatFs:检索有关整体上的一个文件系统的空间信息  
	    StatFs stat = new StatFs(path.getPath());  
	    // 一个文件系统的块大小，以字节为单位。  
	    long blockSize = stat.getBlockSize();  
	    // SD卡中可用的块数  
	    long availableBlocks = stat.getAvailableBlocks();
	    //Log.w("MemoryDraw", blockSize*availableBlocks+"");
	    return blockSize*availableBlocks;
	    //return Formatter.formatFileSize(context, blockSize * availableBlocks);
	}
	
	public long getSDSize(){
		File path = Environment.getExternalStorageDirectory();  
	    StatFs stat = new StatFs(path.getPath());  
	    long blockSize = stat.getBlockSize();  
	    long blocks = stat.getBlockCount();
	    return blocks * blockSize;
	    //return Formatter.formatFileSize(context, blockSize * blocks);
	}
	
	//--------------------------------Memory的异步类----------------------------------------------
	class MemoryAsyncTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			ramInfo=getRamInfo();
			ramPercentage=getRamPercentage();
			storageInfo=getStorageInfo();
			storagePercentage=getStoragePercentage();
			sdInfo=getSDInfo();
			sdPercentage=getSDPercentage();
			//Log.w("LiveWallpaper.Engine", "MemoryAsyncTask:"+ramInfo+" "+storageInfo+" "+sdInfo);
			return null;
		}
		
	}

}
