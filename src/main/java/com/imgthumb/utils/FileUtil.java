package com.imgthumb.utils;

import java.io.File;

/**
 * 文件管理类
 * 
 * @author 邹峰立
 */
public class FileUtil {

	/**
	 * 获取文件名
	 * 
	 * @param imagePath 源文件地址
	 * @return
	 */
	public static String getFileName(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return file.getName();
		}
		return "";
	}
}
