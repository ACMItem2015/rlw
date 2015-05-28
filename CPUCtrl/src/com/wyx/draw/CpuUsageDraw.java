package com.wyx.draw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.rgy.setcpu.MyApplication;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.Log;
/**
 * ����CPUʹ���ʵ���
 * @author wyx
 *
 */
public class CpuUsageDraw extends WallpaperDraw{
	
	private int cpuUsage;
	private ArrayList<Integer> usageList;
	private Timer timer;
	
	public CpuUsageDraw(Canvas c,ArrayList<Integer> usageList,Paint mPaint,Timer timer){
		super(c, mPaint);
		this.usageList=usageList;//���result
		
		this.timer=timer;
		this.timer.schedule(new TimerTask() {
			@Override
			public void run() {
				//new CpuUsageAsyncTask1().execute();
				String str_cpuUsage = MyApplication.cpuCurUsage;
				cpuUsage = Integer.parseInt(str_cpuUsage.substring(0, str_cpuUsage.length()-1));
				//Log.w("LiveWallpaper.Engine", "CpuUsage_TimerTask");
			}
		}, 0,1000);
	}
	
	@Override
	public void draw(){

		int result=cpuUsage/10;
		
		if(result<=1){//���С��10��������Ϊ�յ����
			result=1;
		}

		usageList.add(0,result);//CPUʹ���ʵ���ʷ����
		
		//����ÿһ��ʹ���ʵĻ���
		Paint linePaint=new Paint();
		linePaint.setStyle(Style.FILL);
		linePaint.setStrokeWidth(15f);
		linePaint.setColor(0xffffffff);
		//�������淽��Ļ���
		Paint recfPaint=new Paint();
		recfPaint.setStyle(Style.STROKE);
		recfPaint.setStrokeWidth(2f);
		recfPaint.setColor(0xffffffff);
		
		c.save();
		c.translate(360,350);//c.translate(20,510);
		String data3="CPUռ���ʣ�"+cpuUsage+"%";//�첽��ȡʹ����
		c.drawText(data3,0,0,mPaint);
		c.drawRoundRect(new RectF(0,10f,60f,210f),3f,3f,recfPaint);
		
		for(int i=0;i<result;i++){
			c.drawLine(0,200-i*20,60,200-i*20,linePaint);
		}
		
		c.translate(70,0);
		c.drawRoundRect(new RectF(0,10,210,210),3f,3f,recfPaint);
		for(int i=1;i<usageList.size();i++){
			for(int j=0;j<usageList.get(i);j++){
				c.drawLine((i-1)*40+10,200-j*20,(i-1)*40+40,200-j*20,linePaint);
			}
		}
		if(usageList.size()==6){
			usageList.remove(5);
		}
		c.restore();

	}
	
	class CpuUsageAsyncTask1 extends AsyncTask<Void,Void,Void> {
		/**
		 * ������ʵ��ֱ��ʹ��GuardCpuData��ķ���
		 */
		@Override
		protected Void doInBackground(Void... params) {//ִ�й���
			String result = "";
			int sub;
			
				try {
					Process p = Runtime.getRuntime().exec("top -n 1 -d 0");
					BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
					while ((result = br.readLine()) != null) {
						if (result.trim().length() < 1) {
							continue;
						} else {
							break;
						}
					}
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				//Log.w("LiveWallpaper.Engine", "CPUʹ�����ַ���"+result);
				//
				sub=0;
				String array[] = result.split(",");
				for(int i=0;i<array.length;i++){
					array[i] = array[i].trim();
					String item = array[i];
					array[i]=item.substring(item.indexOf(" ")+1,item.indexOf("%"));
					sub+=Integer.parseInt(array[i]);
				}
				if(sub > 100){
					sub = 100;
				}
				cpuUsage=sub;
				Log.w("LiveWallpaper.Engine", "CPUʹ�����ַ���"+cpuUsage);
			return null;
		}
	}

}
