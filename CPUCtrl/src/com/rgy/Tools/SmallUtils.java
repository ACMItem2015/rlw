package com.rgy.Tools;

public class SmallUtils {

	public static String convertCpuModelName(String cpuModel){
		if(cpuModel.equals(MyConfig.CPUMODEL_DEFAULT)){
			cpuModel = "默认模式";
		}else if(cpuModel.equals(MyConfig.CPUMODEL_PERFORMANCE)){
			cpuModel = "极速模式";
		}else if(cpuModel.equals(MyConfig.CPUMODEL_POWERSAVE)){
			cpuModel = "省电模式";
		}else if(cpuModel.equals(MyConfig.CPUMODEL_USER)){
			cpuModel = "用户模式";
		}
		return cpuModel;
	}
}
