/**
 * Copyright 2019 SinoSoft. All Rights Reserved.
 */
package com.example.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.model.FileDto;
import com.example.demo.service.FileService;

import lombok.extern.slf4j.Slf4j;

/**
 * <B>系统名称：</B><BR>
 * <B>模块名称：</B><BR>
 * <B>中文类名：</B><BR>
 * <B>概要说明：</B><BR>
 */
@Slf4j
@RestController
@RequestMapping("att")
public class AttachmentController {

	@Autowired
    FileService  fileService;
	
	@PostMapping()
	public JSONObject  add() throws IOException{ 
		JSONObject jsonObject = new JSONObject();
		return jsonObject;
	} 
}
