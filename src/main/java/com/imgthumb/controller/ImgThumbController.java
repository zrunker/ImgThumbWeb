package com.imgthumb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imgthumb.dto.ResultData;
import com.imgthumb.entity.ImageInfoEntity;
import com.imgthumb.service.ImgThumbService;

@Controller
@RequestMapping("/image")
public class ImgThumbController {
	
	@Autowired
	private ImgThumbService imgThumbService;
	
	/**
	 * 图片尺寸不变，压缩图片文件大小
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param quality 输出的图片质量，范围：0.0~1.0，1为最高质量。
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	@RequestMapping(value = "/quality/compress")
	@ResponseBody
	public ResultData<ImageInfoEntity> compressImageByQuality(
			@RequestParam(name = "imagePath", required = true) String imagePath,
			@RequestParam(name = "quality", required = true) float quality,
			@RequestParam(name = "outputPath", required = false) String outputPath) {
		
		// 图片尺寸不变，压缩图片文件大小，png文件无法压缩
		ResultData<ImageInfoEntity> result = imgThumbService.compressImageByQuality(
				imagePath, 
				quality, 
				outputPath
			);
		return result;
	}
}
