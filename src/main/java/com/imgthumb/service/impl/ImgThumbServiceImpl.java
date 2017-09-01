package com.imgthumb.service.impl;

import org.springframework.stereotype.Service;

import com.imgthumb.dto.ResultData;
import com.imgthumb.entity.ImageInfoEntity;
import com.imgthumb.enums.ImageStyleEnum;
import com.imgthumb.service.ImgThumbService;
import com.imgthumb.utils.ConstanceUtil;
import com.imgthumb.utils.FileUtil;
import com.imgthumb.utils.ImgThumbUtil;
import com.imgthumb.utils.RegularExpressionUtil;
import com.imgthumb.utils.StringUtil;

/**
 * 图片压缩-接口实现类
 * 
 * @author 邹峰立
 */
@Service("imgThumbService")
public class ImgThumbServiceImpl implements ImgThumbService {
	
	// 获取处理后的图片信息
	private ImageInfoEntity formatImageInfo(String imagePath, String outputPath) {
		String imgStyle = StringUtil.getFileSuffix(imagePath);
		if (StringUtil.isEmpty(outputPath)) {
			outputPath = imagePath;
		} else {
			outputPath = StringUtil.getFilePathNoSuffix(outputPath);
			outputPath = outputPath + "." + imgStyle;
		}
		ImageInfoEntity data = new ImageInfoEntity(
				FileUtil.getFileName(outputPath), 
				outputPath, 
				imgStyle
			);
		return data;
	}

	/**
	 * 图片尺寸不变，压缩图片文件大小
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param quality 输出的图片质量，范围：0.0~1.0，1为最高质量。
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	@Override
	public ResultData<ImageInfoEntity> compressImageByQuality(String imagePath,
			float quality, String outputPath) {
		String resultMsg = null;
		int resultCode = -1;
		ImageInfoEntity data = null;
		if (StringUtil.isEmpty(imagePath)
				|| quality < 0
				|| quality > 1
				|| !RegularExpressionUtil.isImage(imagePath)) {
			resultCode = ConstanceUtil.RESULT_INPUT_ERROR_CODE;
			resultMsg = ConstanceUtil.RESULT_INPUT_ERROR_MSG;
		} else {
			// 处理图片
			boolean bool = ImgThumbUtil.getInstance().compressImageByQuality(imagePath, quality, outputPath);
			if (bool) {
				// 获取处理后的图片信息
				data = formatImageInfo(imagePath, outputPath);
				// 成功
				resultCode = ConstanceUtil.RESULT_SUCCESS_CODE;
				resultMsg = ConstanceUtil.RESULT_SUCCESS_MSG;
			} else {
				// 失败
				resultCode = ConstanceUtil.RESULT_FAILED_CODE;
				resultMsg = ConstanceUtil.RESULT_FAILED_MSG;
			}
		}
		return new ResultData<>(resultCode, resultMsg, data);
	}

	/**
	 * 图片尺寸不变，压缩图片文件大小（质量），输出jpg。
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param quality 输出的图片质量，范围：0.0~1.0，1为最高质量。
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	@Override
	public ResultData<ImageInfoEntity> compressImageToJpgByQuality(
			String imagePath, float quality, String outputPath) {
		String resultMsg = null;
		int resultCode = -1;
		ImageInfoEntity data = null;
		if (StringUtil.isEmpty(imagePath)
				|| quality < 0
				|| quality > 1
				|| !RegularExpressionUtil.isImage(imagePath)) {
			resultCode = ConstanceUtil.RESULT_INPUT_ERROR_CODE;
			resultMsg = ConstanceUtil.RESULT_INPUT_ERROR_MSG;
		} else {
			// 处理图片
			boolean bool = ImgThumbUtil.getInstance().compressImageToJpgByQuality(imagePath, quality, outputPath);
			if (bool) {
				// 获取处理后的图片信息
				imagePath = StringUtil.getFilePathNoSuffix(imagePath) + ImageStyleEnum.stateInfoOf("JPG");
				data = formatImageInfo(imagePath, outputPath);
				// 成功
				resultCode = ConstanceUtil.RESULT_SUCCESS_CODE;
				resultMsg = ConstanceUtil.RESULT_SUCCESS_MSG;
			} else {
				// 失败
				resultCode = ConstanceUtil.RESULT_FAILED_CODE;
				resultMsg = ConstanceUtil.RESULT_FAILED_MSG;
			}
		}
		return new ResultData<>(resultCode, resultMsg, data);
	}

}
