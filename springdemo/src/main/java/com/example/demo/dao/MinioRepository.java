/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.dao;

import java.io.InputStream;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.hutool.core.util.StrUtil;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import lombok.SneakyThrows;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
@Repository
public class MinioRepository {

	@Resource
	private MinioClient minioClient;
	
	/**
	 * 
	 * <B>方法名称：</B>put<BR>
	 * <B>概要说明：</B>上传文件<BR>
	 * @param bucket bucket名称
	 * @param fileName 文件名称
	 * @param stream 文件流
	 * @param size  文件大小
	 * @param contentType  文件类型
	 * @throws MinioException
	 */
	public void put(String bucket, String fileName, InputStream stream, long size, String contentType) throws MinioException{
		try {
			minioClient.putObject(bucket, fileName, stream, size, contentType);
		}catch (Exception e) {
			throw new MinioException(StrUtil.format("上传文件失败， 失败原因：{}", e.getMessage()));
		}
	}
	
	/**
	 * 
	 * <B>方法名称：</B>get<BR>
	 * <B>概要说明：</B>获取文件<BR>
	 * @param bucketName bucket名称
	 * @param fileName 文件名称
	 * @return
	 * @throws MinioException
	 */
	public InputStream get(String bucketName, String fileName) throws MinioException{
		try {
			return minioClient.getObject(bucketName, fileName);	
		}catch (Exception e) {
			throw new MinioException(StrUtil.format("获取文件失败， 失败原因：{}", e.getMessage()));
		}
	}
	@SneakyThrows
	public boolean getl(String bucketName, String fileName){
		boolean flag = false;
		try {
			minioClient.getObject(bucketName, fileName);
			flag = true; 
		} catch (Exception e) {
			// TODO: handle exception
			flag = false; 
		}		
		return flag;
	}
	
    /**
     * 
     * <B>方法名称：</B>delete<BR>
     * <B>概要说明：</B>删除文件<BR>
     * @param bucketName bucket名称
     * @param fileName  文件名称
     * @throws MinioException
     */
	public void delete(String bucketName, String fileName) throws MinioException{
		try {
			minioClient.removeObject(bucketName, fileName);
		}catch (Exception e) {
			throw new MinioException(StrUtil.format("删除文件失败， 失败原因：{}", e.getMessage()));
		}
	}
	
	/**
	 * 
	 * <B>方法名称：</B>checkBucket<BR>
	 * <B>概要说明：</B>判断bucket是否存在<BR>
	 * @param bucket
	 * @return
	 * @throws MinioException
	 */
	public boolean checkBucket(String bucket) throws MinioException{
		try {
			return minioClient.bucketExists(bucket);
		}catch (Exception e) {
			throw new MinioException(StrUtil.format("检查bucket失败， 失败原因：{}", e.getMessage()));
		}
	}
	
  
   /**
    * 
    * <B>方法名称：</B>createBucket<BR>
    * <B>概要说明：</B>创建bucket<BR>
    * @param bucketName  bucket名称
    */
   @SneakyThrows
   public void createBucket(String bucketName) {
      if (!minioClient.bucketExists(bucketName)) {
    	  minioClient.makeBucket(bucketName);
      }
   }
   
   /**
    * 
    * <B>方法名称：</B>getObjectUrl<BR>
    * <B>概要说明：</B>获取文件url<BR>
    * @param bucketName  bucket名称
    * @param fileName  文件名称
    * @return
    */
   @SneakyThrows
   public String getObjectUrl(String bucketName,String fileName){
	   //minioClient.getPresignedObjectUrl(method, bucketName, objectName, expires, reqParams)
	   return minioClient.getObjectUrl(bucketName, fileName);
   }
   
   /**
    * 
    * <B>方法名称：</B>uploadUrl<BR>
    * <B>概要说明：</B>获取上传路径<BR>
    * @param bucketName
    * @param objectName
    * @param expires
    * @return
    */
   @SneakyThrows
   public String uploadUrl(String bucketName, String objectName, Integer expires){
	   return minioClient.presignedPutObject(bucketName, objectName, expires);
   }
   
   /**
    * 
    * <B>方法名称：</B>downloadUrl<BR>
    * <B>概要说明：</B>获取下载路径<BR>
    * @param bucketName
    * @param objectName
    * @param expires
    * @return
    */
   @SneakyThrows
   public String downloadUrl(String bucketName, String objectName, Integer expires){
	   return minioClient.presignedGetObject(bucketName, objectName, expires);
   }
   /**
    * 
    * <B>方法名称：</B>copyFile<BR>
    * <B>概要说明：</B>copy到指定桶<BR>
    * @param bucketName 源桶
    * @param objectName  源文件
    * @param destBucketName 新桶
    * @param destObjectName  新文件
    */
   @SneakyThrows
   public void copyFile(String bucketName, String objectName, String destBucketName, String destObjectName){
	   minioClient.copyObject(bucketName,objectName, destBucketName, destObjectName);                                                	  	     
   }
}
