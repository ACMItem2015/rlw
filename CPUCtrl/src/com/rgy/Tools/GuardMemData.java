package com.rgy.Tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.text.format.Formatter;

public class GuardMemData {
	/**
	 * ���أ�allMemStr/usedMemStr/availMemStr��
	 * @param context
	 * @return
	 */
	public static String getMemData(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            br.close();
            fr.close();
            //
            String allMemSizeStr = memoryLine.substring(memoryLine.indexOf(":")+1,memoryLine.indexOf("kB")).trim();
            long allMemSize = Integer.parseInt(allMemSizeStr) * 1000;
            
            //���MemoryInfo����  
            MemoryInfo memoryInfo = new MemoryInfo();  
            //���ϵͳ�����ڴ棬������MemoryInfo������  
            ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            mActivityManager.getMemoryInfo(memoryInfo) ;  
            long availMemSize = memoryInfo.availMem;
            
            long usedMemSize = allMemSize - availMemSize;
            
            //�ַ�����ת��  
            String availMemStr = Formatter.formatFileSize(context, availMemSize);  
            String allMemStr = Formatter.formatFileSize(context, allMemSize);
            String usedMemStr = Formatter.formatFileSize(context, usedMemSize);
            
            return allMemStr+"/"+usedMemStr+"/"+availMemStr;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "not data";
    }
	
	public static String getMemAllSize(Context context){
		String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            br.close();
            fr.close();
            //
            String allMemSizeStr = memoryLine.substring(memoryLine.indexOf(":")+1,memoryLine.indexOf("kB")).trim();
            long allMemSize = Integer.parseInt(allMemSizeStr) * 1000;
            return Formatter.formatFileSize(context, allMemSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "not data";
	}
	
	public static String getMemRestSize(Context context){
		MemoryInfo memoryInfo = new MemoryInfo();  
		ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.getMemoryInfo(memoryInfo) ;  
        long availMemSize = memoryInfo.availMem;
        return Formatter.formatFileSize(context, availMemSize);
	}
}
