/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.Assert;
import com.example.demo.common.FileException;
import com.example.demo.dao.AttachmentDao;
import com.example.demo.dao.MinioRepository;
import com.example.demo.model.Attachment;
import com.example.demo.model.FileDto;
import com.google.common.collect.Maps;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import io.minio.MinioClient;
import io.minio.errors.MinioException;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
@Service
public class FileServiceImpl  implements FileService{

	@Resource
	private MinioRepository minioRepository;
	
	@Resource
	private AttachmentDao attachmentDao ;
	
	@Autowired
	private MinioClient client;
	
	@Value("${configs.path}")
	private String path;
	
	private static Map<String, String> contentTypeMap;
	
	private final static String DEFAULT_CONTENT_TYPE = "application/octet-stream";
	
	static {
		contentTypeMap = Maps.newHashMap();
		contentTypeMap.put("doc", "application/msword");
		contentTypeMap.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		contentTypeMap.put("pdf", "application/pdf");
		contentTypeMap.put("xls", "application/vnd.ms-excel");
		contentTypeMap.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		contentTypeMap.put("ppt", "application/vnd.ms-powerpoint");
		contentTypeMap.put("pptx", "pplication/vnd.openxmlformats-officedocument.presentationml.presentation");
		contentTypeMap.put("rar", "application/x-rar-compressed");
		contentTypeMap.put("gif", "image/gif");
		contentTypeMap.put("jpeg","image/jpeg");
		contentTypeMap.put("jpg", "image/jpeg");
		contentTypeMap.put("png", "image/png");
		contentTypeMap.put("txt", "text/plain");
		contentTypeMap.put("zip", "application/x-zip-compressed");
	}
	
	
	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.example.demo.service.FileService#upload(java.lang.String, org.springframework.web.multipart.MultipartFile)
	 */
	@Override
	public String upload(String bucket, MultipartFile file) throws Exception {
		// TODO Auto-generated method stub
		Assert.notBlank(bucket, "bucket不能为空");
		long size = file.getSize();
		System.out.println("size:"+size);
		if(size <= 0){
			throw new FileException("上传的文件不能为空");
		}		
		//判断bucket是否存在
		//若bucket不存在，创建bucket
		//创建bucket
		minioRepository.createBucket(bucket);
		// 组成新的文件名
		String fileName = IdUtil.objectId() + StrUtil.DOT + getExt(file.getOriginalFilename());
		//文件存储地址
		String url = path +"/"+bucket+"/"+ fileName;
		InputStream is = file.getInputStream();
		System.out.println(is);
		System.out.println("ContentType:"+file.getContentType());
		minioRepository.put(bucket, fileName, file.getInputStream(), size, file.getContentType());
		// 文件后缀
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
                .toLowerCase();
		Attachment attachment = new Attachment();
		attachment.setFileName(fileName);
		attachment.setFileSize(size);
		attachment.setFilePath(url);
		attachment.setFileSuffix(fileExt);
		attachment.setUrl(minioRepository.getObjectUrl(bucket, fileName));
		attachmentDao.save(attachment);		
		return fileName;
	}
	
	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.example.demo.service.FileService#upload(com.alibaba.fastjson.JSONObject)
	 */
	@Override
	public JSONObject uploadUrl(String appKey,JSONObject jsonObject) {
		// TODO Auto-generated method stub		
		JSONObject json = new JSONObject();
		JSONArray array = jsonObject.getJSONArray("files");	
		JSONArray jsonarray = new JSONArray();
		for (int i = 0; i < array.size(); i++) {
			String fileName = array.getJSONObject(0).getString("fileName");	
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1)
	                .toLowerCase();
			//第三方id
			String thirdPartId = array.getJSONObject(i).getString("id");
			String type = array.getJSONObject(i).getString("type");
			String length = array.getJSONObject(i).getString("length");
			String strUrl  = "";
			// 组成新的文件名
			fileName = IdUtil.objectId() + StrUtil.DOT +  fileExt;
			//获取上传url
			strUrl=	 minioRepository.uploadUrl(appKey, fileName,60*60*24);			
			//保存信息		
			Attachment attachment = new Attachment();
			attachment.setFileName(fileName);		
			attachment.setFilePath(strUrl);
			attachment.setThirdPartId(thirdPartId);
			attachment.setCreateTime(new Date());
			attachment.setFileType(type);
			attachment.setFileSize(Long.valueOf(length));
			attachmentDao.save(attachment);
			
			JSONObject jsonObj = new JSONObject();
			//返回参数		
			jsonObj.put("fileId", attachment.getId());
			jsonObj.put("uploadurl",strUrl);
			jsonObj.put("id", thirdPartId);
			jsonarray.add(jsonObj);
		}	
		//返回结果集
		json.put("uploadUrls",jsonarray);			
		return json;
	}

	
	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.example.demo.service.FileService#download(java.lang.String, java.lang.String, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void download(String bucket, String fileId, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		Assert.notBlank(bucket, "bucket不能为空");
		Assert.notBlank(fileId, "文件Id不能为空.");		
		InputStream in = minioRepository.get(bucket, fileId);	
		
		// 设置响应头
		String contentType = contentTypeMap.get(getExt(fileId));
        response.setContentType(StrUtil.isNotBlank(contentType) ? contentType : DEFAULT_CONTENT_TYPE);        
        String fileName = encodeFileName(fileId);        
        //下载文件能正常显示中文
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);       
        FastByteArrayOutputStream buffer = IoUtil.read(in);
		IoUtil.write(response.getOutputStream(), false, buffer.toByteArray());
	}
		
	/**
	 * 对中文名称进行编码
	 * @param filename 文件名称
	 * @return
	 */
	private String encodeFileName(String filename){
		return URLUtil.encode(filename);
	}
	
	/**
	 * 得到文件后缀名
	 * @param filename 文件完整名称
	 * @return 文件后缀
	 */
	private String getExt(String filename){
		String extName = StrUtil.subAfter(filename, '.', true);
		return StrUtil.isNotBlank(extName) ? extName : ""; 
	}








	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.example.demo.service.FileService#delete(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject delete(String bucket, String filename) {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();		
		 try {
			minioRepository.delete(bucket, filename);
			json.put("status", "SUCCESS");
		} catch (MinioException e) {
			// TODO Auto-generated catch block
			json.put("status", "FAIL");
			e.printStackTrace();
		}
		 return json;
	}

	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.example.demo.service.FileService#list(java.lang.String)
	 */
	@Override
	public List<Attachment> list(String applyId) {
		// TODO Auto-generated method stub
		
		
		return null;
	}

	/**
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @see com.example.demo.service.FileService#add(com.example.demo.model.FileDto)
	 */
	@Override
	public JSONObject add(FileDto file) {
		String path ="D:\\1\1.gif";
		JSONObject json = new JSONObject();
		MultipartFile files = file.getFile();
		Attachment att = new Attachment();
		att.setCreateTime(new Date());
		att.setFileName(files.getName());
		att.setFilePath(path);
		
		try {
			files.transferTo(new File(path));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		attachmentDao.save(att);
		json.put("id", att.getId());
		return json;
	}

	

	
	
}
