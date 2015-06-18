package com.rgy.Tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GuardCpuData {
	/**
	 * 返回cpu使用率（字符串）
	 * @return
	 */
	public static String getCpuUsage(){
		String result = "";
		try {
			Process p = Runtime.getRuntime().exec("top -n 1");
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((result = br.readLine()) != null) {
				if (result.trim().length() < 1) {
					continue;
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		int sub = 0;
		String array[] = result.split(",");
		for(int i=0;i<array.length;i++){
			array[i] = array[i].trim();
			String item = array[i];
			array[i]=item.substring(item.indexOf(" ")+1,item.indexOf("%"));
			sub+=Integer.parseInt(array[i]);
		}
		sub = sub < 100 ? sub : 100;
		return sub+"%";
	}
	
	/**
	 * 返回cpu最大频率
	 * @return
	 */
	public static String getMaxCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}
	
	/**
	 * 返回cpu最小频率
	 * @return
	 */
	public static String getMinCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}
	
	/**
	 * 返回cpu当前频率
	 * @return
	 */
	public static String getCurCpuFreq() {
		String result = "";
		ProcessBuilder cmd;
		try {
			String[] args = { "/system/bin/cat",
					"/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[24];
			while (in.read(re) != -1) {
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			result = "N/A";
		}
		return result.trim();
	}
	
	/**
	 * 返回cpu名字
	 * @return
	 */
	public static String getCpuName() {
		try {
			FileReader fr = new FileReader("/proc/cpuinfo");
			BufferedReader br = new BufferedReader(fr);
			String text = br.readLine();
			br.close();
			String[] array = text.split(":\\s+", 2);
			for (int i = 0; i < array.length; i++) {
				
			}
			return array[1];
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
