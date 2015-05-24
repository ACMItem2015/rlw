package com.rgy.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {
	
	public MyTextView(Context context){
        this(context,null);
    }
    
    public MyTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        
        setFocusable(true);
        setFocusableInTouchMode(true);
        
        setSingleLine();
        setMarqueeRepeatLimit(-1);
    }
    
    public MyTextView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        
        setFocusable(true);
        setFocusableInTouchMode(true);
        
        setSingleLine();
        setMarqueeRepeatLimit(-1);
    }


    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect){
        if (focused){
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }
    
    @Override
    public void onWindowFocusChanged(boolean focused){
        if (focused){
            super.onWindowFocusChanged(focused);
        }
    }
    
    @Override
    public boolean isFocused(){
        return true;
    }
}
