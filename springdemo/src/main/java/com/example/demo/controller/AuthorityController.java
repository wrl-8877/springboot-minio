/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.AuthorityDao;
import com.example.demo.model.Author;
import com.example.demo.service.AuthorityService;

import lombok.extern.slf4j.Slf4j;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>

 */

@Slf4j
@RestController
@RequestMapping("auth")
public class AuthorityController {
	
	@Autowired
	private AuthorityDao authorityDao;
	
	@Autowired
	private AuthorityService authorityService;
	/**
	 * 
	 * <B>方法名称：</B><BR>
	 * <B>概要说明：</B><BR>
	 * @return
	 */
	@PostMapping("/add")
	public JSONObject  add(JSONObject jsonObject){ 
		JSONObject  json = new JSONObject();
		Author auth = JSONObject.toJavaObject(jsonObject, Author.class);
		boolean flag = authorityService.getByAppKey(auth.getAppKey());
		if(flag){
			try {
				Author author = authorityDao.save(auth);
				json.put("content", author);
				json.put("status", "success");
			} catch (Exception e) {
				// TODO: handle exception
				json.put("status", "fail");
			}	
		}else{
			json.put("message", "内容已存在,请重新添加！");
		}		
		return json;
	} 
	

}
