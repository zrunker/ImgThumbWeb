package com.imgthumb.service;

import com.imgthumb.dto.ResultData;
import com.imgthumb.entity.ImageInfoEntity;

/**
 * 图片压缩-接口
 * 
 * @author 邹峰立
 */
public interface ImgThumbService {
	
	/**
	 * 图片尺寸不变，压缩图片文件大小
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param quality 输出的图片质量，范围：0.0~1.0，1为最高质量。
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	ResultData<ImageInfoEntity> compressImageByQuality(String imagePath, float quality, String outputPath);
	
	/**
	 * 图片尺寸不变，压缩图片文件大小（质量），输出jpg。
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param quality 输出的图片质量，范围：0.0~1.0，1为最高质量。
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	ResultData<ImageInfoEntity> compressImageToJpgByQuality(String imagePath, float quality, String outputPath);
}
