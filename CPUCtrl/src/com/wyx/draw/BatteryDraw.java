package com.wyx.draw;


import com.wyx.util.BatteryChangeReceiver;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;

/**
 * ���Ƶ�ص��࣬���Ƶ���ѧ��ϵ�е㸴��
 * @author wyx
 *
 */
public class BatteryDraw extends WallpaperDraw{
	
	private int canvasX=370;
	private int canvasY=180;
	/**
	 * ���õ������͵�ظǿ��
	 */
	private float mBatteryXL=10f;//���Ͻ�����
	private float mBatteryYL=0;
	private float mBatteryXR=210f;//���½�x����Ϊ60
	private float mBatteryYR=60f;
	private float mCapWidth=mBatteryXL;
	private float mCapHeight=30f;
	/**
	 * ��ʾ���info
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
		
		levelStr="��ص�����"+level+"%";
		temStr="����¶ȣ�" +temp/10+"��";
		
		c.save();
    	c.translate(canvasX,canvasY);
    	
    	Paint mBatteryPaint=new Paint();
    	Paint mPowerPaint=new Paint();
    	
    	float mPowerWidth;
    	float mStrokeWidth=3f;
    	float mPower;
    	//float mPower=mPowerWidth*(1-bPower/100);
    	/**
    	 * mBatteryPaint������
    	 */
    	mBatteryPaint.setColor(0xffffffff);
    	mBatteryPaint.setStyle(Style.FILL);
    	mBatteryPaint.setStrokeWidth(mStrokeWidth);
    	mBatteryPaint.setAntiAlias(true);//�����ɶ������
    	/**
    	 * mPowerPaint������
    	 */
    	mPowerPaint.setColor(0xff7cfc00);
    	mPowerPaint.setStyle(Style.FILL);
    	mPowerPaint.setStrokeWidth(mStrokeWidth);
    	mBatteryPaint.setAntiAlias(true);//�����ɶ������
    	/**
    	 * new��ؿ��
    	 */
//    	float mBatteryXL=10f;//���Ͻ�����
//    	float mBatteryYL=0;
//    	float mBatteryXR=110f;//���½�x����Ϊ60
//    	float mBatteryYR=60f;
//    	float mCapWidth=mBatteryXL;
//    	float mCapHeight=30f;
    	RectF mBatteryRectF=new RectF(mBatteryXL,mBatteryYL,mBatteryXR,mBatteryYR);
    	/**
    	 * ���õ������
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
    	 * ������С��10����Ϊ��ɫ
    	 */
    	if(level<=10){//�����˸Ļ���
    		mPowerPaint.setColor(0xffff6347);
    	}
    	/**
    	 * �����������
    	 */
    	c.drawRoundRect(mBatteryRectF,2f,2f,mBatteryPaint);//RecFΪʲô��ônew����
    	
    	c.drawText(levelStr, 0, infoY1, mPaint);
    	c.drawText(temStr, 0, infoY2, mPaint);
    	
    	mBatteryPaint.setStyle(Style.FILL);//����ظǵ�ʱ���һ��Style
    	c.drawRoundRect(new RectF(0,(mBatteryYR-mCapHeight)/2,mCapWidth,(mBatteryYR-mCapHeight)/2+mCapHeight),2f,2f,mBatteryPaint);
    	c.drawRect(mPowerRectF,mPowerPaint);
    	c.restore();
	}

}
