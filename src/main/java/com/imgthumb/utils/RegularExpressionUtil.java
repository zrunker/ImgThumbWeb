package com.imgthumb.utils;

import java.util.regex.Pattern;

/**
 * 常用正则表达是总结 
 * Created by 邹峰立 on 2017/7/13.
 */
public class RegularExpressionUtil {
	/**
	 * 判断文件是否图片格式
	 * 
	 * @param filename 文件名
	 * @return
	 */
	public static boolean isImage(String filename) {
		String regex = ".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.PNG|.png)$";
		return Pattern.matches(regex, filename);
	}
	
	public static void main(String[] args) {
		System.out.println(isImage("F:\\125346.jpg.png.bmp"));
	}
}
