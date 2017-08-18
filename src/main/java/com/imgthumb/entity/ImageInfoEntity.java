package com.imgthumb.entity;

public class ImageInfoEntity {
	private String imgName;
	private String imgPath;
	private String imgStyle;

	public ImageInfoEntity() {
		super();
	}

	public ImageInfoEntity(String imgName, String imgPath, String imgStyle) {
		super();
		this.imgName = imgName;
		this.imgPath = imgPath;
		this.imgStyle = imgStyle;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getImgStyle() {
		return imgStyle;
	}

	public void setImgStyle(String imgStyle) {
		this.imgStyle = imgStyle;
	}

	@Override
	public String toString() {
		return "ImageInfoEntity [imgName=" + imgName + ", imgPath=" + imgPath
				+ ", imgStyle=" + imgStyle + "]";
	}

}
