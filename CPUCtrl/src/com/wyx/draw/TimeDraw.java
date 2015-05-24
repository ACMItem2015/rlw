package com.wyx.draw;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.graphics.Canvas;
import android.graphics.Paint;

public class TimeDraw extends WallpaperDraw{
	
	private Date date;//=new Date(System.currentTimeMillis()); 
	
	private SimpleDateFormat data= new SimpleDateFormat("yyyy/MM/dd");
	private SimpleDateFormat time = new SimpleDateFormat("hh:mm");
	private SimpleDateFormat second= new SimpleDateFormat("ss");
	
//	private String dataStr="";
//	private String timeStr="";
//	private String secondStr="";
	
	public TimeDraw(Canvas c, Paint mPaint) {
		super(c, mPaint);
	}

	@Override
	public void draw() {
		
		c.save();
    	
    	Date date = new Date(System.currentTimeMillis());  

        String dataStr = data.format(date); 
        String timeStr = time.format(date); 
        String secondStr = second.format(date); 
        //将时间画到画布上
        mPaint.setTextSize(30);
        c.drawText(dataStr,0,0,mPaint);
        mPaint.setTextSize(100);
        c.translate(0,90);
        c.drawText(timeStr,0,0,mPaint);
        mPaint.setTextSize(60);
        c.translate(260,0);
        c.drawText(secondStr,0,0,mPaint);
        mPaint.setTextSize(30);
        c.drawText("s",70,0,mPaint);
        
        c.restore();
	}
}
