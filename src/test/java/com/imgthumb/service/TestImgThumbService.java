package com.imgthumb.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ImgThumbService进行单元测试
 * 
 * @author 邹峰立
 */
// 配置spring和junit整合，junit启动时加载spring IOC容器 包依赖spring-test,junit
@RunWith(SpringJUnit4ClassRunner.class)
// 告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-service.xml" })
public class TestImgThumbService {

	@Autowired
	private ImgThumbService imgThumbService;
	
	@Test
	public void compressImageByQuality() throws Exception {
		imgThumbService.compressImageByQuality("F:\\b.png", 0.25f, "F:\\c");
	}
}
