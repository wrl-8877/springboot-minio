/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.AttachmentDao;
import com.example.demo.dao.AuthorityDao;
import com.example.demo.dao.MinioRepository;
import com.example.demo.model.Attachment;
import com.example.demo.model.Author;
import com.example.demo.service.FileService;
import com.sinosoft.minio_client_sdk.secrity.exception.SecurityException;
import com.sinosoft.minio_client_sdk.secrity.model.ResponseData;
import com.sinosoft.minio_client_sdk.secrity.model.ResponseHeader;
import com.sinosoft.minio_client_sdk.secrity.util.AES;
import com.sinosoft.minio_client_sdk.secrity.util.SHA;
/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B>接口提供<BR>
 */
@RestController
@RequestMapping("minioapi")
public class MinioRpcController {

	@Autowired
    FileService  fileService;
	
	@Autowired
	MinioRepository  minioRepository;
	
	@Autowired
	private AttachmentDao attachmentDao ;
	
	@Autowired
	private AuthorityDao authorityDao;
	
		
	/**
	 *  
	 * <B>方法名称：</B>uploadUrl<BR>
	 * <B>概要说明：</B>获取文件上传url接口<BR>
	 * @param jsonObject  {"files":[{"bucket":"20190924","fileName":"12345555.txt","id":"1"}]}
	 * @return  {"responseBody":{"uploadUrls":[{"minioUrl":"E:/file/20190926/37fafeee-76d1-44e8-b1b9-65b5b7cb3754.txt","id":"4028d19d6d6b2276016d6b23c4780000"}]},"responseHeader":{"resultCode":"0","resultMessage":"成功"}}
	 * @throws SecurityException 	
	 */	
	@RequestMapping("/file/uploadUrl")
	public String   uploadUrl(@RequestBody JSONObject jsonObject) throws SecurityException{
		//获取请求头部
		JSONObject requestHeaderJson = jsonObject.getJSONObject("requestHeader");
		//获取请求body
		JSONObject requestBodyJson = jsonObject.getJSONObject("requestBody");
		//响应报文
		ResponseData responseData = new ResponseData();
		//响应头部
		ResponseHeader responseHeader = new ResponseHeader();
		//请求签名 ,用于请求报文验证
		String signature = requestHeaderJson.getString("signature");			
		//请求的随机盐值（10000内随机）
	    String nonce = requestHeaderJson.getString("nonce");
	    //分配给第3的唯一标识
	    String appKey = requestHeaderJson.getString("appKey");	   
	    Author auth = authorityDao.getByAppKey(appKey);
	    //请求签名验证
	    //SHA.verifySign256(requestBodyJson + "," + appKey, signature);
	    //获取对请求报文
	    String requestBody = JSON.toJSONString(requestBodyJson);
	    //对请求报文进行解密
	    // requestBody = AES.decrypt128(requestBody, appKey, nonce);
	    requestBodyJson = JSONObject.parseObject(requestBody);
		JSONObject json = new JSONObject();		
		try {
			json = fileService.uploadUrl(appKey,requestBodyJson);
			//responseHeader = new ResponseHeader("0","成功");	
			responseHeader.setResultCode("0");
			responseHeader.setResultMessage("成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block	
			responseHeader.setResultCode("1000000000");
			responseHeader.setResultMessage("失败");
			//responseHeader = new ResponseHeader("1000000000","失败");
			e.printStackTrace();			
		}
		responseHeader.setAppKey(appKey);
		responseHeader.setNonce(nonce);			
		responseHeader.setAppMessageId(requestHeaderJson.getString("appMessageId"));
		responseHeader.setTimestamp(Long.valueOf(requestHeaderJson.getString("timestamp")));		
		//对响应报文进行加密
		String encryption = AES.encrypt128(JSON.toJSONString(json), auth.getDataKey(), nonce);
		//对响应header进行签名
		String sign = SHA.genSign256(responseHeader.getSignString() + "," + encryption + "," + auth.getSignKey());
		responseHeader.setSignature(sign);
		responseData.setResponseHeader(responseHeader);	
		responseData.setResponseBody(json);		
		return  JSON.toJSONString(responseData);		
	}
	
	/**
	 * 
	 * <B>方法名称：</B>submitUploadResult<BR>
	 * <B>概要说明：</B>获取文件上传状态<BR>
	 * @param jsonObject 
	 * @return
	 */
	@RequestMapping("/file/submitUploadResult")
	public JSONObject submitUploadResult(@RequestBody JSONObject jsonObject){
		JSONObject json = new JSONObject();
		//获取请求头部
		JSONObject requestHeaderJson = jsonObject.getJSONObject("requestHeader");
		//获取请求body
		JSONObject requestBodyJson = jsonObject.getJSONObject("requestBody");
		//响应报文
		ResponseData responseData = new ResponseData();
		//响应头部
		ResponseHeader responseHeader = new ResponseHeader();
		//请求签名
		String signature = requestHeaderJson.getString("signature");		
		//请求的随机盐值（10000内随机）
	    String nonce = requestHeaderJson.getString("nonce");
	    //分配给第3的唯一标识
	    String appKey = requestHeaderJson.getString("appKey");
	    //获取fileIds
	    JSONArray array = requestBodyJson.getJSONArray("fileIds");
	    JSONArray jsonarray = new JSONArray();
	    for (int i = 0; i < array.size(); i++) {
	    	JSONObject jsonObj = new JSONObject();
	    	jsonObj.put("fileId", array.getString(i));
	    	//获取信息
	    	Attachment attachment = attachmentDao.getOne(array.getString(i));	    	
	    	//判断文件是否上传成功
	    	boolean flag = minioRepository.getl(appKey, attachment.getFileName());
	    	if(flag){
	    		jsonObj.put("status", "success");
	    	}else{
	    		jsonObj.put("status", "fail");	
	    	}	    	
	    	jsonarray.add(jsonObj);
		}
	    json.put("submitResults", jsonarray);
	    responseHeader.setAppKey(appKey);
		responseHeader.setNonce(nonce);
		responseHeader.setSignature(signature);
		responseHeader.setAppMessageId(requestHeaderJson.getString("appMessageId"));
		responseHeader.setTimestamp(Long.valueOf(requestHeaderJson.getString("timestamp")));
		responseData.setResponseHeader(responseHeader);			
	    responseData.setResponseBody(json);	
		return  JSONObject.parseObject(JSON.toJSONString(responseData));
	}

	/**
	 * 
	 * <B>方法名称：</B>downloadUrl<BR>
	 * <B>概要说明：</B>获取申请文件下载接口<BR>
	 * @param jsonObject {"fileIds":["4028d19d6d6102a8016d61061a9f0001","4028d19d6d6102a8016d610878690002"]}
	 * @return  {"downloadUrls": [{"minioid": "4028d19d6d6102a8016d61061a9f0001","miniourl": "D:/file/20190924/1111.txt"},{"minioid": "4028d19d6d6102a8016d610878690002","miniourl": "D:/file/20190924/1111.txt"}]}
	 * @throws SecurityException 
	 * @throws com.sinosoft.minio_client_sdk.secrity.exception.SecurityException 
	 */	
	@GetMapping("/file/getdownloadurl")
	public JSONObject getdownloadUrl(@RequestBody JSONObject jsonObject) throws SecurityException, com.sinosoft.minio_client_sdk.secrity.exception.SecurityException{
		//获取请求头部
		JSONObject requestHeaderJson = jsonObject.getJSONObject("requestHeader");
		//获取请求body
		JSONObject requestBodyJson = jsonObject.getJSONObject("requestBody");
		//响应报文
		ResponseData responseData = new ResponseData();
		//响应头部
		ResponseHeader responseHeader = new ResponseHeader();
		//请求签名
		String signature = requestHeaderJson.getString("signature");		
		//请求的随机盐值（10000内随机）
	    String nonce = requestHeaderJson.getString("nonce");
	    //分配给第3的唯一标识
	    String appKey = requestHeaderJson.getString("appKey");
	    Author auth = authorityDao.getByAppKey(appKey);
	    //签名验证(请求体+appKey)
	   // SHA.verifySign256(requestBodyJson + "," + appKey, signature);    
		//申请ID
		String  applyId = UUID.randomUUID().toString();
		JSONObject json = new JSONObject();	
		JSONArray array = requestBodyJson.getJSONArray("fileIds");
		JSONArray jsonarray = new JSONArray();
		for (int i = 0; i < array.size(); i++) {
			Attachment attachment = attachmentDao.getOne(array.getString(i));
			attachment.setApplyId(applyId);			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("fileId", attachment.getId());
			String url = null;
		    //获取下载路径
		    url=	minioRepository.downloadUrl(appKey, attachment.getFileName(), 60*60*24);			
			jsonObj.put("downloadurl",url);
			jsonarray.add(jsonObj);
			attachment.setUrl(url);
			attachmentDao.save(attachment);
		}
		json.put("applyId", applyId);
		json.put("downloadUrls", jsonarray);
		responseHeader.setAppKey(appKey);
		responseHeader.setNonce(nonce);		
		responseHeader.setAppMessageId(requestHeaderJson.getString("appMessageId"));
		responseHeader.setTimestamp(Long.valueOf(requestHeaderJson.getString("timestamp")));
		//对响应报文进行加密
		String encryption = AES.encrypt128(JSON.toJSONString(json), auth.getDataKey(), nonce);
		//对响应header进行签名
		String sign = SHA.genSign256(responseHeader.getSignString() + "," +encryption+","+auth.getSignKey());
		responseHeader.setSignature(sign);
		responseData.setResponseHeader(responseHeader);		
		responseData.setResponseBody(json);	   	      		
		return  JSONObject.parseObject(JSON.toJSONString(responseData));		
	}
	
	/**
	 * 
	 * <B>方法名称：</B>downloadUrl<BR>
	 * <B>概要说明：</B>根据申请ApplyId获取下载url<BR>
	 * @param jsonObject  {"applyId":"71751f5f-81e5-4e2d-bcc9-d33da9c8b589"}
	 * @return {"downloadUrls": [{"minioId": "4028d19d6d612cc9016d6131e6280002","minioUrl": "D:/file/20190924/11111.txt"},{"minioId": "4028d19d6d614a8c016d614bd52c0000","minioUrl": "D:/file/20190924/11111.txt"}]}
	 * @throws SecurityException 
	 * @throws com.sinosoft.minio_client_sdk.secrity.exception.SecurityException 
	 */
	@GetMapping("/file/getFileDownloadUrls")
	public JSONObject  downloadUrl(@RequestBody JSONObject jsonObject) throws SecurityException{
		//获取请求头部
		JSONObject requestHeaderJson = jsonObject.getJSONObject("requestHeader");
		//获取请求body
		JSONObject requestBodyJson = jsonObject.getJSONObject("requestBody");
		//响应报文
		ResponseData responseData = new ResponseData();
		//响应头部
		ResponseHeader responseHeader = new ResponseHeader();
		//请求签名
		String signature = requestHeaderJson.getString("signature");		
		//请求的随机盐值（10000内随机）
	    String nonce = requestHeaderJson.getString("nonce");
	    //分配给第3的唯一标识
	    String appKey = requestHeaderJson.getString("appKey");
	    Author auth = authorityDao.getByAppKey(appKey);
	    //签名验证(请求体+appKey)
	    SHA.verifySign256(requestBodyJson + "," + appKey, signature);
		JSONObject json = new JSONObject();
		String applyId = requestBodyJson.getString("applyId");
		//根据申请ID获取信息
		List<Attachment> attList = attachmentDao.getByApplyId(applyId);
		JSONArray jsonarray = new JSONArray();	
		for (Attachment att:attList) {
			JSONObject jsonob = new JSONObject();
			jsonob.put("fileId", att.getId());
			jsonob.put("downloadUrl", att.getUrl());
			jsonarray.add(jsonob);
		}
		json.put("downloadUrls", jsonarray);
		responseHeader.setAppKey(appKey);
		responseHeader.setNonce(nonce);
		responseHeader.setAppMessageId(requestHeaderJson.getString("appMessageId"));
		responseHeader.setTimestamp(Long.valueOf(requestHeaderJson.getString("timestamp")));
		//对响应报文进行加密
		String encryption = AES.encrypt128(JSON.toJSONString(json), auth.getDataKey(), nonce);
		//对响应header进行签名
		String sign = SHA.genSign256(responseHeader.getSignString() + "," + encryption+","+auth.getDataKey());
		responseHeader.setSignature(sign);
		responseData.setResponseHeader(responseHeader);		
		responseData.setResponseBody(encryption);	   	   
		responseData.setResponseHeader(responseHeader);		
		responseData.setResponseBody(encryption);	   	      		
		return  JSONObject.parseObject(JSON.toJSONString(responseData));	
	}

}
