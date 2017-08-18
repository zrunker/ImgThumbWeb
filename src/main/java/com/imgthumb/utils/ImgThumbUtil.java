package com.imgthumb.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.imgthumb.enums.ImageStyleEnum;
import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;

/**
 * thumbnailator图片处理类
 * 
 * @author 邹峰立
 */
public class ImgThumbUtil {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static ImgThumbUtil imgThumbUtil;
	
	public static ImgThumbUtil getInstance() {
		if (imgThumbUtil == null) {
			imgThumbUtil = new ImgThumbUtil();
		}
		return imgThumbUtil;
	}
	
	/**
	 * JDK压缩图片BufferedImage质量
	 * 
	 * @param bufferedImage 原图BufferedImage流
	 * @param quality 输出的图片质量，范围：0.0~1.0，1为最高质量。
	 * @param imgStyle 目标文件类型
	 * @return
	 * @throws IOException
	 */
	public byte[] zoomBufferedImageByQuality(BufferedImage bufferedImage, float quality) throws IOException {
		// 得到指定Format图片的writer
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");// 得到迭代器
		ImageWriter writer = (ImageWriter) iter.next(); // 得到writer

		// 得到指定writer的输出参数设置(ImageWriteParam)
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
		iwp.setCompressionQuality(quality); // 设置压缩质量参数
		iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

		ColorModel colorModel = ColorModel.getRGBdefault();
		// 指定压缩时使用的色彩模式
		iwp.setDestinationType(new ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));

		// 开始打包图片，写入byte[]
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // 取得内存输出流
		IIOImage iIamge = new IIOImage(bufferedImage, null, null);
		// 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput
		// 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput
		writer.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
		writer.write(null, iIamge, iwp);

		// 获取压缩后的btye
		byte[] tempByte = byteArrayOutputStream.toByteArray();
		return tempByte;
	}
	
	/**
	 * GIF压缩质量，尺寸不变
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param imgStyle 目标文件类型
	 * @param quality 输出的图片质量，范围：0.0~1.0，1为最高质量。
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 * @throws IOException
	 */
	public void zoomGifByQuality(String imagePath, String imgStyle, float quality, String outputPath) throws IOException {
		// 防止图片后缀与图片本身类型不一致的情况
		outputPath = outputPath + "." + imgStyle;
		// GIF需要特殊处理
		GifDecoder decoder = new GifDecoder();
		int status = decoder.read(imagePath);
		if (status != GifDecoder.STATUS_OK) {
			throw new IOException("read image " + imagePath + " error!");
		}
		// LZW算法压缩，拆分一帧一帧的压缩之后合成
		AnimatedGifEncoder encoder = new AnimatedGifEncoder();
		encoder.start(outputPath);// 设置合成位置
		encoder.setRepeat(decoder.getLoopCount());// 设置GIF重复次数
		int frameCount = decoder.getFrameCount();// 获取GIF有多少个frame
		for (int i = 0; i < frameCount; i++) {
			encoder.setDelay(decoder.getDelay(i));// 设置GIF延迟时间
			BufferedImage bufferedImage = decoder.getFrame(i);
			// 利用java SDK压缩BufferedImage
			byte[] tempByte = zoomBufferedImageByQuality(bufferedImage, quality);
			ByteArrayInputStream in = new ByteArrayInputStream(tempByte);
			BufferedImage zoomImage = ImageIO.read(in);
			encoder.addFrame(zoomImage);// 合成
		}
		encoder.finish();
		File outFile = new File(outputPath);
		BufferedImage image = ImageIO.read(outFile);
		ImageIO.write(image, outFile.getName(), outFile);
	}
	
	/**
	 * GIF压缩尺寸大小
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param imgStyle 目标文件类型
	 * @param width 目标文件宽
	 * @param height 目标文件高
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 * @throws IOException
	 */
	public void zoomGifBySize(String imagePath, String imgStyle, int width, int height, String outputPath) throws IOException {
		// 防止图片后缀与图片本身类型不一致的情况
		outputPath = outputPath + "." + imgStyle;

		// GIF需要特殊处理
		GifDecoder decoder = new GifDecoder();
		int status = decoder.read(imagePath);
		if (status != GifDecoder.STATUS_OK) {
			throw new IOException("read image " + imagePath + " error!");
		}
		// LZW算法压缩，拆分一帧一帧的压缩之后合成
		AnimatedGifEncoder encoder = new AnimatedGifEncoder();
		encoder.start(outputPath);
		encoder.setRepeat(decoder.getLoopCount());
		for (int i = 0; i < decoder.getFrameCount(); i++) {
			encoder.setDelay(decoder.getDelay(i));// 设置播放延迟时间
			BufferedImage bufferedImage = decoder.getFrame(i);// 获取每帧BufferedImage流
			BufferedImage zoomImage = new BufferedImage(width, height, bufferedImage.getType());
			Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			Graphics gc = zoomImage.getGraphics();
			gc.setColor(Color.WHITE);
			gc.drawImage(image, 0, 0, null);
			encoder.addFrame(zoomImage);
		}
		encoder.finish();
		File outFile = new File(outputPath);
		BufferedImage image = ImageIO.read(outFile);
		ImageIO.write(image, outFile.getName(), outFile);
	}
	
	/**
	 * 获取原图的宽
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @return
	 */
	public int getImageWidth(String imagePath) {
		int width = 0;
		try {
			BufferedImage image = ImageIO.read(new File(imagePath));
			width = image.getWidth();
		} catch (IOException e) {
			 logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return width;
	}
	
	/**
	 * 获取原图的高
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @return
	 */
	public int getImageHeight(String imagePath) {
		int height = 0;
		try {
			BufferedImage image = ImageIO.read(new File(imagePath));
			height = image.getHeight();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return height;
	}
	
	/**
	 * 输出到OutputStream
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param width 原图片宽
	 * @param height 原图片高
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 * @return
	 */
	public OutputStream imagePathToOutputStream(String imagePath, int width, int height, String outputPath) {
		OutputStream os = null;
		try {
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath);
			// toOutputStream(流对象) 
			os = new FileOutputStream(outputPath);
			Thumbnails.of(imagePath).size(width, height).toOutputStream(os);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return os;
	}
	
	/**
	 * 输出到BufferedImage
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param width 原图片宽
	 * @param height 原图片高
	 * @param imageStyle 目标文件类型
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 * @return
	 */
	public BufferedImage imagePathToBufferedImage(String imagePath, int width, int height, ImageStyleEnum imageStyle, String outputPath) {
		BufferedImage thumbnail = null;
		try {
			// 获取文件需要转换的格式
			String imgStyle = "jpg";
			if (imageStyle != null) {
				imgStyle = imageStyle.getStateInfo();
			}
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath) + "." + imageStyle;
			// toBufferedImage
			thumbnail = Thumbnails.of(imagePath).size(width, height).asBufferedImage();
			ImageIO.write(thumbnail, imgStyle, new File(outputPath));  
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return thumbnail;
	}
	
	/**
	 * 图片尺寸不变，修改图片文件类型
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param imageStyle 目标文件类型
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	public void modifyImgStyle(String imagePath, ImageStyleEnum imageStyle, String outputPath) {
		try {
			// 获取文件需要转换的格式
			String imgStyle = "jpg";
			if (imageStyle != null) {
				imgStyle = imageStyle.getStateInfo();
			}
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath);
			// 进行格式转换
			// outputFormat：输出的图片格式。注意使用该方法后toFile()方法不要再含有文件类型的后缀了，否则会生成 imagename.jpg.jpg 的图片。
			Thumbnails.of(imagePath).scale(1f).outputFormat(imgStyle).toFile(outputPath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 图片尺寸不变，修改图片文件类型
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param width 原图片宽
	 * @param height 原图片高
	 * @param imageStyle 目标文件类型
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	public void modifyImgStyle2(String imagePath, int width, int height, ImageStyleEnum imageStyle, String outputPath) {
		try {
			// 获取文件需要转换的格式
			String imgStyle = "jpg";
			if (imageStyle != null) {
				imgStyle = imageStyle.getStateInfo();
			}
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath);
			// 进行格式转换
			Thumbnails.of(imagePath).size(width, height).outputFormat(imgStyle).toFile(outputPath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 图片尺寸不变，压缩图片文件大小。
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param quality 输出的图片质量，范围：0.0~1.0，1为最高质量。
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	public boolean compressImageByQuality(String imagePath, float quality, String outputPath) {
		boolean bool = false;
		try {
			// 获取文件后缀
			String imgStyle = StringUtil.getFileSuffix(imagePath);
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath);
			// 压缩文件，Thumbnails对png,gif之类的不可压缩图片处理并不好
			if ("png".equals(imgStyle.toLowerCase())) {// png压缩
				File file = new File(imagePath);
				BufferedImage bufferedImage = ImageIO.read(file);// 获取BufferedImage流
				quality = quality * 2.2 > 1 ? 1 : quality * 2.2f;// 设置压缩质量参数，png大约等于2.2*jpeg
				byte[] tempByte = zoomBufferedImageByQuality(bufferedImage, quality);// JDK压缩图片质量
				// 创建输出文件
				File outFile = new File(outputPath + "." + imgStyle);
				FileOutputStream fos = new FileOutputStream(outFile);
				fos.write(tempByte);
				fos.close();
				if (!outFile.exists()) {
					return false;
				}
			} else if ("gif".equals(imgStyle.toLowerCase())) {// gif压缩
				quality = quality / 8;// 重新计算压缩质量值，这里只是取一个大约值，具体计算规则不清楚
				zoomGifByQuality(imagePath, imgStyle, quality, outputPath);
			} else {// jpg.jpeg
				Thumbnails.of(imagePath).scale(1f).outputQuality(quality).outputFormat(imgStyle).toFile(outputPath);
			}
			bool = true;
		 } catch (IOException e) {
			 logger.error(e.getMessage(), e);
			e.printStackTrace();
		 }
		 return bool;
	}
	
	/**
	 * 压缩至指定图片尺寸（例如：宽400 高300），不保持图片比例
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param width 原图片宽
	 * @param height 原图片高
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	public void compressImageNoKeepRatioBySize(String imagePath, int width, int height, String outputPath) {
		try {
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath);
			// 压缩文件
			Thumbnails.of(imagePath).forceSize(width, height).toFile(outputPath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}  
	}
	
	/**
	 * 压缩至指定图片尺寸（例如：宽400 高300），保持图片比例
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param width 原图片宽
	 * @param height 原图片高
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	public void compressImageKeepRatioBySize(String imagePath, int width, int height, String outputPath) {
		try {
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath);
			// 压缩文件
			Thumbnails.of(imagePath).size(width, height).keepAspectRatio(true).toFile(outputPath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}  
	}
	
	/**
	 * 压缩至原图片的百分之多少
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param scale 缩放比例
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	public boolean compressImageByRatio(String imagePath, double scale, String outputPath) {
		boolean bool = false;
		try {
			BufferedImage image = ImageIO.read(new File(imagePath));
			if (image != null) {
				// 获取文件后缀
				String imgStyle = StringUtil.getFileSuffix(imagePath);
				// 设置输出路径（不带后缀）
				if (StringUtil.isEmpty(outputPath)) 
					outputPath = imagePath;
				outputPath = StringUtil.getFileName(outputPath) + "." + imgStyle;
				// 压缩文件
				Thumbnails.of(image).scale(scale).toFile(outputPath);
				bool = true;
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} 
		return bool;
	}
	
	/**
	 * 中心裁剪至指定图片尺寸（例如：宽400  高300），保持图片不变形，多余部分裁剪掉
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param width 裁剪区域宽
	 * @param height 裁剪区域高
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	public void cropImageByCenter(String imagePath, int width, int height, String outputPath) {
		try {
			// 获取原图的宽和高
			BufferedImage image = ImageIO.read(new File(imagePath));
			Builder<BufferedImage> builder = null;
			int imageWidth = image.getWidth();
			int imageHeitht = image.getHeight();
			// 计算比例，沿中心裁剪
			if ((float) height / width != (float) imageWidth / imageHeitht) {
				if (imageWidth > imageHeitht) {
					image = Thumbnails.of(imagePath).height(height).asBufferedImage();
				} else {
					image = Thumbnails.of(imagePath).width(width).asBufferedImage();
				}
				builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, width, height).size(width, height);
			} else {
				builder = Thumbnails.of(image).size(width, height);
			}
			// 获取文件后缀
			String imgStyle = StringUtil.getFileSuffix(imagePath);
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath);
			builder.outputFormat(imgStyle).toFile(outputPath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 裁剪
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param width 裁剪区域宽
	 * @param height 裁剪区域高
	 * @param position 裁剪位置
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	public void cropImage(String imagePath, int width, int height, Position position, String outputPath) {
		try {
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath);
			// 裁剪
			Thumbnails.of(imagePath)  
				.sourceRegion(Positions.CENTER, width, height)
			    .size(width, height)  
			    .keepAspectRatio(false)  
			    .toFile(outputPath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 旋转
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param angle 旋转角度
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	public void rotateImage(String imagePath, double angle, String outputPath) {
		try {
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath);
			// 旋转
			Thumbnails.of(imagePath).scale(1f).rotate(angle).toFile(outputPath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 水印
	 * 
	 * @param imagePath 原图片路径地址，如：F:\\a.png
	 * @param width 原图片宽
	 * @param height 原图片高
	 * @param watermarkImgPath 水印图片路径地址，如：F:\\b.png
	 * @param opacity 水印图片透明度  0~1f
	 * @param outputPath 输出文件路径（不带后缀），如：F:\\b，默认与原图片路径相同，为空时将会替代原文件
	 */
	public void watermarkImage(String imagePath, int width, int height, String watermarkImgPath, float opacity, String outputPath) {
		try {
			// 设置输出路径（不带后缀）
			if (StringUtil.isEmpty(outputPath)) 
				outputPath = imagePath;
			outputPath = StringUtil.getFileName(outputPath);
			// 水印
			Thumbnails.of(imagePath)
				.size(width, height)
				.watermark(Positions.CENTER, ImageIO.read(new File(watermarkImgPath)), opacity)
				.outputQuality(1f)
				.toFile(outputPath);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		modifyImgStyle("F:\\b.png", ImageStyleEnum.JPEG, "F:\\b");
//		modifyImgStyle("F:\\b.png", ImageStyle.PNG, null);
//		modifyImgStyle2("F:\\b.png", 1920, 400, ImageStyle.PNG, null);
//		compressImageByQuality("F:\\b.png", 0.25f, "F:\\c");
//		compressImageNoKeepRatioBySize("F:\\b.png", 400, 400, "F:\\d");
//		compressImageKeepRatioBySize("F:\\b.png", 400, 400, "F:\\e");
//		compressImageByRatio("F:\\b.png", 0.5, "F:\\bd");
//		cropImage("F:\\c.png", 500, 200, null);
//		rotateImage("F:\\bd.png", -90, null);
//		watermarkImage("F:\\b.png", 1920, 400, "F:\\e.png", 0.5f, "F:\\bc");
//		cropImage("F:\\b.png", 400, 400, Positions.CENTER, "F:\\cc");
	}

}
