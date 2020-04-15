/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.test;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.common.HttpUtil;

import io.minio.MinioClient;
import io.minio.errors.MinioException;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
public class Test {
		
    @Autowired
	static
	MinioClient  minioClient; 
	public static void main(String[] args) throws Exception {
		
		String url ="https://play.min.io";
		 String accessKey = "Q3AM3UQ867SPQQA43P2F";
		 String secretAccessKey = "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG";
		
		/* String url ="http://127.0.0.1:9000";
		 String accessKey = "P6SL3H5RQQGFYC2M2TP3";
		 String secretAccessKey = "6Hq0qYhbIgONMt5fTx7tNYOEra6xfG6iENJuhT6T";*/
		 // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象		 
		/* try {*/
		    String bucketname = "test";
			String objectname = "test.docx";	
			MinioClient minioClient = new MinioClient(url, accessKey, secretAccessKey);	
			System.out.println(minioClient.bucketExists(bucketname));
			try {
				  String urll = minioClient.presignedPutObject(bucketname, objectname, 60*60*24);				 
				  String path = "F:/1.docx";
				  HttpUtil.uploadFile(urll, path);
				} catch (MinioException e) {
				  System.out.println("Error occurred: " + e);
				}			
	}
	
	public static String getUrl(String url){
        String[] urlArray = url.split("[?]",1000);	
        String urll = "";
		if(urlArray.length > 0){
			urll = urlArray[0];			
		}
        return urll;
	}
}
