/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.model;

import lombok.Data;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
@Data
public class AttachVo {

	private String fileName;
	/**
	 * 上传路径
	 */
	private String filePath;
	/**
	 * 文件大小
	 */
	private long fileSize;
	/**
	 * 文件后缀
	 */
	private String fileSuffix;
	/**
	 * 下载url
	 */
	private String url;
	/**
	 * 申请ID
	 */
	private String applyId;
	/**
	 * 第三方ID
	 */
	private String  thirdPartId;
	
	/**
	 * 文件类型
	 */
	private String fileType;
}
