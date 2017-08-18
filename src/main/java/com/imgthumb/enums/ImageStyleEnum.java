package com.imgthumb.enums;

/**
 * 图片类型-枚举
 * 
 * @author 邹峰立
 */
public enum ImageStyleEnum {
	PNG("PNG", "png"), 
	JPEG("JPEG", "jpeg"), 
	GIF("GIF", "gif"), 
	JPG("JPG", "jpg");

	private String state;
	private String stateInfo;

	private ImageStyleEnum(String state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public String getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static ImageStyleEnum stateOf(String state) {
		for (ImageStyleEnum imageStyleEnum : values()) {
			if (imageStyleEnum.getState().equals(state)) {
				return imageStyleEnum;
			}
		}
		return null;
	}
	
	public static String stateInfoOf(String state){
		for (ImageStyleEnum imageStyleEnum : values()) {
			if (imageStyleEnum.getState().equals(state)) {
				return imageStyleEnum.getStateInfo();
			}
		}
		return null;
	}

}
