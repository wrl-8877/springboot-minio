/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
@Data
@Entity
@Table(name = "ATTACHMENT")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Attachment {

	@Id
	@GeneratedValue(generator = "jpa-uuid")
	private String id ;
	/**
	 * 文件名
	 */
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
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 文件类型
	 */
	private String fileType;
}
