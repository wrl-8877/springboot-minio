/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.Attachment;
import com.example.demo.model.FileDto;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
public interface FileService {

	/**
	 * 上传文件
	 * @param bucket 文件夹名称
	 * @param file 文件
	 * @return 上传成功的自定义文件名称
	 * @throws Exception
	 */
	public String upload(String bucket, MultipartFile file)throws Exception;
	
	/**
	 * 
	 * <B>方法名称：</B>upload<BR>
	 * <B>概要说明：</B>文件上传<BR>
	 * @param json 文件信息
	 * @return
	 */
	public JSONObject uploadUrl(String appKey,JSONObject jsonObject);

	
	/**
	 * 下载文件
	 * @param bucket 文件夹名称
	 * @param fileId 文件id
	 * @param response Http响应 
	 * @throws Exception 
	 */
	public void download(String bucket, String fileId, HttpServletResponse response) throws Exception;
	
	/**
	 * 
	 * <B>方法名称：</B>delete<BR>
	 * <B>概要说明：</B>删除文件<BR>
	 * @param bucket 
	 * @param filename
	 * @return
	 */
	public JSONObject delete(String bucket,String filename);
	
	/**
	 * 
	 * <B>方法名称：</B>list<BR>
	 * <B>概要说明：</B>获取attachment<BR>
	 * @param applyId
	 * @return
	 */
	List<Attachment>  list(String applyId);
	
	public JSONObject add(FileDto file);
	
	
}
