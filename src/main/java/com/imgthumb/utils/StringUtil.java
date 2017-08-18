package com.imgthumb.utils;


/**
 * 字符串管理类
 * 
 * @author 邹峰立
 */
public class StringUtil {

	// 判断字符串为空
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
			return true;
		}
		return false;
	}
	
	// 获取文件后缀
	public static String getFileSuffix(String fileName) {
		String suffix = "";
		if (!StringUtil.isEmpty(fileName) && fileName.contains(".")) {
			suffix = fileName.substring(fileName.lastIndexOf('.') + 1); 
		}
		return suffix;
	}
	
	// 获取文件名，不包含后缀
	public static String getFileName(String fileName) {
		if (!StringUtil.isEmpty(fileName) && fileName.contains(".")) {
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
		}
		return fileName;
	}

}
