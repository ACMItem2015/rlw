package com.wyx.draw;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.rgy.Tools.DeepCpuData;
import com.rgy.setcpu.MyApplication;

/**
 * 绘制CPU频率的类，draw方法还是有点乱
 * @author wyx
 *
 */
public class CpuFreqDraw extends WallpaperDraw{

	private ArrayList<String> freqList;
	
	private String curCpuFreq;
	
	private ArrayList<String> availableFreqList;
	
	private Timer timer;
	
	public CpuFreqDraw(Canvas c,ArrayList<String> freqList,Paint mPaint,Timer timer){
		super(c,mPaint);
		this.freqList=freqList;
		
		if(MyApplication.cpuCurFreq.equals("未知")){
			this.curCpuFreq = "0";
		}else{
			this.curCpuFreq = MyApplication.cpuCurFreq;
		}
		this.availableFreqList=DeepCpuData.getCpuAvailableFreq();
		
		this.timer=timer;
		this.timer.schedule(new TimerTask() {
			@Override
			public void run() {
				//new CpuFreqAsyncTask().execute();
				curCpuFreq = MyApplication.cpuCurFreq;
				//System.out.println("当前频率："+curCpuFreq);
				//Log.w("LiveWallpaper.Engine", "CpuFreq_TimerTask");
			}
		}, 0, 1000);
	}

	@Override
	public void draw() {

		Log.w("LiveWallpaper.Engine", "curCpuFreq:"+curCpuFreq);
		Log.w("LiveWallpaper.Engine", "availableFreqList:"+availableFreqList.size());
		float height=360f;
		float freqH=(height-40)/(availableFreqList.size()*1f-1f);
		
    	Paint linePaint=new Paint();
    	linePaint.setStrokeWidth(3);
    	linePaint.setColor(0xffffffff);
    	linePaint.setAntiAlias(true);//设置平滑
    	
		c.save();
    	String data="";
    	Log.w("LiveWallpaper.Engine", "data = cpuFreqTask.get() before");
		
		data = curCpuFreq;//GuardCpuData.getCurCpuFreq();//cpuFreqTask.getCurCpuFreq();
		
		Log.w("LiveWallpaper.Engine", data);
    	String data1="CPU频率："+data+"KHz";
    	//String data2="GPU频率："+GuardCpuData.getGpuCurFreq()+"KHz";//GPU数据
    	
    	freqList.add(0,data);
    	
    	mPaint.setTextSize(25);
        c.translate(0, 200);//可要可不要mCenterX,mCenterY
        c.drawText(data1,0,0,mPaint);//????????????/
        //c.drawText(data2,0,30,mPaint);
        
        c.drawLine(0,40,0,height,mPaint);//360原来是freqH
      
        for(int i=0;i<freqList.size();i++){//获取可用频率可使用DeepCpuData里的方法
        	
        	float j;
        	int first=Integer.parseInt(availableFreqList.get(0));
        	int last=Integer.parseInt(availableFreqList.get(availableFreqList.size()-1));
        	if(first<last){
        		j=availableFreqList.indexOf(freqList.get(i))*1f;
        	}else{
        		j=availableFreqList.size()*1f-availableFreqList.indexOf(freqList.get(i))-1f;
        	}

        	c.drawLine(i*20,height,i*20+10,height-j*freqH,linePaint);
        	c.drawLine(i*20+10,height-j*freqH,i*20+20,height,linePaint);
        }
        if(freqList.size()==14){
        	freqList.remove(13);
        }
        c.restore();
	}
	
//	class CpuFreqAsyncTask extends AsyncTask<Void,Void,Void> {
//		
//		@Override
//		protected Void doInBackground(Void... params) {
//			curCpuFreq=GuardCpuData.getCurCpuFreq();
//			
//			Log.w("LiveWallpaper.Engine", "CpuFreqAsyncTask:"+curCpuFreq);
//			return null;
//		}
//	}
}
