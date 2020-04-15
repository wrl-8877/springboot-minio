/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.common.R;
import com.example.demo.dao.MinioRepository;
import com.example.demo.service.FileService;

import io.swagger.annotations.ApiOperation;



/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>

 */
@RestController
@RequestMapping("file")
public class MinioFileController {

	@Resource
	FileService fileService;
	
	@Autowired
	MinioRepository minioRepository;
	
	/**
	 * 上传文件
	 * @param bucket 文件夹名称
	 * @param file  文件
	 * @return 自定的文件名
	 * @throws Exception
	 */
	 @ApiOperation(value = "附件上传", notes = "附件上传接口")	    	    
  	 @PostMapping("upload")	 
	 public R upload(String bucket, MultipartFile file) throws Exception{
		return R.ok().data(fileService.upload(bucket, file));
	 }
	
	
	/**
	 * 下载文件
	 * @param bucket 文件夹名称
	 * @param fileId 文件id
	 * @param response Http响应对象
	 * @throws Exception
	 */
	@ApiOperation(value = "附件下载", notes = "附件下载接口")
	@RequestMapping("download/{bucket}/{fileId:.+}")
	public void download(@PathVariable String bucket, @PathVariable String fileId,
			                HttpServletResponse response) throws Exception{
		fileService.download(bucket, fileId, response);
	}
	
	@GetMapping
	public String getFileUrl( String bucket,String fileName){		
		return minioRepository.getObjectUrl(bucket,fileName);
	}
}
