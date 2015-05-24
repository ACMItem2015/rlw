package com.rgy.Tools;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

public class GuardSDData {
	
	@SuppressWarnings("deprecation")
	public static long getSDTotalSize(Context context) {  
        File path = Environment.getExternalStorageDirectory();  
        StatFs stat = new StatFs(path.getPath());  
		long blockSize = stat.getBlockSize();  
		long totalBlocks = stat.getBlockCount();  
        //return Formatter.formatFileSize(context, blockSize * totalBlocks);  
		return blockSize * totalBlocks;
    }  
	
	@SuppressWarnings("deprecation")
	public static long getSDAvailableSize(Context context) {  
        File path = Environment.getExternalStorageDirectory();  
        StatFs stat = new StatFs(path.getPath());  
		long blockSize = stat.getBlockSize();  
		long availableBlocks = stat.getAvailableBlocks();  
		return blockSize * availableBlocks;
    }  
	
	@SuppressWarnings("deprecation")
	public static long getDataTotalSize(Context context) {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
		long blockSize = stat.getBlockSize();  
		long totalBlocks = stat.getBlockCount();  
		return blockSize * totalBlocks;
    }  
	
	@SuppressWarnings("deprecation")
	public static long getDataAvailableSize(Context context) {  
        File path = Environment.getDataDirectory();  
        StatFs stat = new StatFs(path.getPath());  
		long blockSize = stat.getBlockSize();  
		long availableBlocks = stat.getAvailableBlocks();  
		return blockSize * availableBlocks;  
    }  
	
	public static String getSDAllSize(Context context){
		long size = getSDTotalSize(context)+getDataTotalSize(context);
		return Formatter.formatFileSize(context, size); 
	}
	
	public static String getSDRestSize(Context context){
		long size = getSDAvailableSize(context)+getDataAvailableSize(context);
		return Formatter.formatFileSize(context, size); 
	}
}
