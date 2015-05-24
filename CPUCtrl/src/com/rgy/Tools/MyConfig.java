package com.rgy.Tools;

public class MyConfig {
	
	public static String dir = FileUtils.getSDPath()+"/acon/";   
	
	public static final String FILENAME_APP_DEFAULT = dir+"app_default.rgy";
	public static final String FILENAME_APP_PERFORMANCE = dir+"app_performance.rgy";
	public static final String FILENAME_APP_POWERSAVE = dir+"app_powersave.rgy";
	
	public static final String CPUMODEL_DEFAULT = "default_model";
	public static final String CPUMODEL_PERFORMANCE = "performance_model";
	public static final String CPUMODEL_POWERSAVE = "powersave_model";
	public static final String CPUMODEL_USER = "user_model";
	
	public static final String SERVICENAME_CUSTOM = ".CustomService";
	public static final String SERVICENAME_SMARTY = ".SmartyService";
	
}
