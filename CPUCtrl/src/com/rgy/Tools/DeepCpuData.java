package com.rgy.Tools;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public class DeepCpuData {
	private final static String TAG = "SetCPU";  
    private final static String cpuFreqPath = "/sys/devices/system/cpu/cpu0/cpufreq";  
    
    /** 
     * 获得当前cpu调控模式 
     */  
    public static String getCpuCurGovernor() {   
        try {  
            DataInputStream is = null;  
            Process process = Runtime.getRuntime().exec("cat " + cpuFreqPath + "/scaling_governor");  
            is = new DataInputStream(process.getInputStream());  
            @SuppressWarnings("deprecation")
			String line = is.readLine();  
            return line;
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;
    }  
    
    /** 
     * 设置cpu调控模式
     * @param governor 
     * @return 
     */  
    public static boolean setCpuGovernor(String governor) {  
        DataOutputStream os = null;   
        String command = "echo " + governor + " > " + cpuFreqPath + "/scaling_governor";  
//        Log.i(TAG, "command: " + command);  
        try {  
            Process process = Runtime.getRuntime().exec("su");  
            os = new DataOutputStream(process.getOutputStream());  
            os.writeBytes(command + "\n");  
            os.writeBytes("exit\n");  
            os.flush();  
            process.waitFor();   
        } catch (IOException e) {  
            Log.i(TAG, "writeCpuGovernor: write CPU Governor(" + governor + ") failed!");  
            return false;  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
            return false;  
        }  
        return true;  
    }  
    
    /**
     * 设置cpu最小频率
     * @param freq
     * @return
     */
    public static boolean setMinCpuFreq(long freq) {  
        DataOutputStream os = null;   
        String command = "echo " + freq + " > " + cpuFreqPath + "/scaling_min_freq";  
//        Log.i(TAG, "command: " + command);  
        try {  
            Process process = Runtime.getRuntime().exec("su");  
            os = new DataOutputStream(process.getOutputStream());  
            os.writeBytes(command + "\n");  
            os.writeBytes("exit\n");  
            os.flush();  
            process.waitFor();  
//            Log.i(TAG, "exit value = " + process.exitValue());  
        } catch (IOException e) {  
            Log.i(TAG, "新的cpu频率设置失败！");  
            return false;  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
            return false;  
        }  
        return true;  
    }  
    
    /**
     * 设置cpu最大频率
     * @param freq
     * @return
     */
    public static boolean setMaxCpuFreq(long freq) {  
        DataOutputStream os = null;   
        String command = "echo " + freq + " > " + cpuFreqPath + "/scaling_max_freq";  
//        Log.i(TAG, "command: " + command);  
        try {  
            Process process = Runtime.getRuntime().exec("su");  
            os = new DataOutputStream(process.getOutputStream());  
            os.writeBytes(command + "\n");  
            os.writeBytes("exit\n");  
            os.flush();  
            process.waitFor();  
//            Log.i(TAG, "exit value = " + process.exitValue());  
        } catch (IOException e) {  
            Log.i(TAG, "新的cpu频率设置失败！");  
            return false;  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
            return false;  
        }  
        return true;  
    }  

    /** 
     * 获得cpu所有调控模式集合
     * @return 
     */  
    public static ArrayList<String> getCpuGovernors() {  
    	ArrayList<String> governors = new ArrayList<String>();  
        DataInputStream is = null;  
        try {  
            Process process = Runtime.getRuntime().exec("cat " + cpuFreqPath + "/scaling_available_governors");  
            is = new DataInputStream(process.getInputStream());  
            @SuppressWarnings("deprecation")
			String line = is.readLine();  

            String[] strs = line.split(" ");  
            for (int i = 0; i < strs.length; i++){
            	governors.add(strs[i]);  
            }
        } catch (IOException e) {  
            Log.i(TAG, "readCpuGovernors: read CPU Governors failed!");  
        }  
        return governors;  
    }  
    
    /**
     * 获取cpu可用频率集合
     * @return
     */
    public static ArrayList<String> getCpuAvailableFreq(){
    	ArrayList<String> availableFreq = new ArrayList<String>();  
        DataInputStream is = null;  
        try {  
            Process process = Runtime.getRuntime().exec("cat " + cpuFreqPath + "/scaling_available_frequencies");  
            is = new DataInputStream(process.getInputStream());  
            @SuppressWarnings("deprecation")
			String line = is.readLine();  

            String[] strs = line.split(" ");  
            for (int i = 0; i < strs.length; i++){
            	availableFreq.add(strs[i]);  
            }
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return availableFreq; 
    }
    
    
}
