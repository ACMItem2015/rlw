package com.wyx.draw;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class WallpaperDraw {
	
	protected Canvas c;
	protected Paint mPaint;
	public WallpaperDraw(Canvas c,Paint mPaint){
		this.c=c;
		this.mPaint=mPaint;
	}
	
	public abstract void draw();

}
