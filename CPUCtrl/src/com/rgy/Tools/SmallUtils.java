package com.rgy.Tools;

public class SmallUtils {

	public static String convertCpuModelName(String cpuModel){
		if(cpuModel.equals(MyConfig.CPUMODEL_DEFAULT)){
			cpuModel = "Ĭ��ģʽ";
		}else if(cpuModel.equals(MyConfig.CPUMODEL_PERFORMANCE)){
			cpuModel = "����ģʽ";
		}else if(cpuModel.equals(MyConfig.CPUMODEL_POWERSAVE)){
			cpuModel = "ʡ��ģʽ";
		}else if(cpuModel.equals(MyConfig.CPUMODEL_USER)){
			cpuModel = "�û�ģʽ";
		}
		return cpuModel;
	}
}
