package com.wyx.draw;


import com.wyx.util.BatteryChangeReceiver;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;

/**
 * 绘制电池的类，绘制的数学关系有点复杂
 * @author wyx
 *
 */
public class BatteryDraw extends WallpaperDraw{
	
	private int canvasX=370;
	private int canvasY=180;
	/**
	 * 设置电池坐标和电池盖宽高
	 */
	private float mBatteryXL=10f;//左上角坐标
	private float mBatteryYL=0;
	private float mBatteryXR=210f;//右下角x坐标为60
	private float mBatteryYR=60f;
	private float mCapWidth=mBatteryXL;
	private float mCapHeight=30f;
	/**
	 * 显示电池info
	 */
	private String levelStr;
	private String temStr;
	private float infoY1;
	private float infoY2;
	
	private int level;
	private int temp;
	
	public BatteryDraw(Canvas c,Paint mPaint){
		super(c,mPaint);
		
		infoY1=mBatteryYR+30;
		infoY2=infoY1+30;
		
	}

	@Override
	public void draw() {
		
		level=BatteryChangeReceiver.getLevel();
		temp=BatteryChangeReceiver.getTemperature();
		
		levelStr="电池电量："+level+"%";
		temStr="电池温度：" +temp/10+"℃";
		
		c.save();
    	c.translate(canvasX,canvasY);
    	
    	Paint mBatteryPaint=new Paint();
    	Paint mPowerPaint=new Paint();
    	
    	float mPowerWidth;
    	float mStrokeWidth=3f;
    	float mPower;
    	//float mPower=mPowerWidth*(1-bPower/100);
    	/**
    	 * mBatteryPaint的设置
    	 */
    	mBatteryPaint.setColor(0xffffffff);
    	mBatteryPaint.setStyle(Style.FILL);
    	mBatteryPaint.setStrokeWidth(mStrokeWidth);
    	mBatteryPaint.setAntiAlias(true);//这个是啥？？？
    	/**
    	 * mPowerPaint的设置
    	 */
    	mPowerPaint.setColor(0xff7cfc00);
    	mPowerPaint.setStyle(Style.FILL);
    	mPowerPaint.setStrokeWidth(mStrokeWidth);
    	mBatteryPaint.setAntiAlias(true);//这个是啥？？？
    	/**
    	 * new电池框架
    	 */
//    	float mBatteryXL=10f;//左上角坐标
//    	float mBatteryYL=0;
//    	float mBatteryXR=110f;//右下角x坐标为60
//    	float mBatteryYR=60f;
//    	float mCapWidth=mBatteryXL;
//    	float mCapHeight=30f;
    	RectF mBatteryRectF=new RectF(mBatteryXL,mBatteryYL,mBatteryXR,mBatteryYR);
    	/**
    	 * 设置电量宽高
    	 */
    	float mPowerXL=mBatteryXL+mStrokeWidth;
    	float mPowerYL=mStrokeWidth;
    	float mPowerXR=mBatteryXR-mStrokeWidth;
    	float mPowerYR=mBatteryYR-mStrokeWidth;
    	mPowerWidth=mPowerXR-mPowerXL;//
    	mPower=mPowerWidth*(1-level/100f);//
    	mPowerXL+=mPower;//
    	RectF mPowerRectF=new RectF(mPowerXL,mPowerYL,mPowerXR,mPowerYR);
    	
    	/**
    	 * 当电量小于10，变为红色
    	 */
    	if(level<=10){//别忘了改回来
    		mPowerPaint.setColor(0xffff6347);
    	}
    	/**
    	 * 绘制整个电池
    	 */
    	c.drawRoundRect(mBatteryRectF,2f,2f,mBatteryPaint);//RecF为什么这么new？？
    	
    	c.drawText(levelStr, 0, infoY1, mPaint);
    	c.drawText(temStr, 0, infoY2, mPaint);
    	
    	mBatteryPaint.setStyle(Style.FILL);//画电池盖的时候改一下Style
    	c.drawRoundRect(new RectF(0,(mBatteryYR-mCapHeight)/2,mCapWidth,(mBatteryYR-mCapHeight)/2+mCapHeight),2f,2f,mBatteryPaint);
    	c.drawRect(mPowerRectF,mPowerPaint);
    	c.restore();
	}

}
